package org.dows.modules.recycle.service.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.crud.service.MapperProviderService;
import org.dows.modules.recycle.entity.RecycleDataEntity;
import org.dows.modules.recycle.mapper.RecycleDataMapper;
import org.dows.modules.recycle.service.RecycleDataService;
import org.dows.uat.UserApi;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据回收站
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecycleDataServiceImpl extends BaseServiceImpl<RecycleDataMapper, RecycleDataEntity>
        implements RecycleDataService {

    //final private BaseSysUserService baseSysUserService;

    final private UserApi userApi;
    final private MapperProviderService mapperProviderService;

    @Override
    public Object page(JSONObject requestParams, Page<RecycleDataEntity> page, QueryWrapper queryWrapper) {
        String keyWord = requestParams.getStr("keyWord");
        if (ObjUtil.isNotEmpty(keyWord)) {
            List<Long> list = userApi.selectUserIdByKeywordWithLike(keyWord);
            /*List<Long> list = baseSysUserService
                    .list(queryWrapper
                            .select(BaseSysUserEntity::getId)
                            .like(BaseSysUserEntity::getName, keyWord))
                    .stream()
                    .map(BaseSysUserEntity::getId)
                    .toList();*/
            queryWrapper.like(RecycleDataEntity::getUrl, keyWord).or(w -> {
                w.in(RecycleDataEntity::getUserId, list, ObjUtil.isNotEmpty(list));
            });
        }
        Page<RecycleDataEntity> iPage = page(page, queryWrapper);
        List<RecycleDataEntity> records = iPage.getRecords();
        List<Long> list = records.stream().map(RecycleDataEntity::getUserId)
                .filter(ObjUtil::isNotEmpty).toList();

        if (ObjUtil.isNotEmpty(list)) {
            Map<Long, String> map = userApi.selectUserNameByUserId(list);
           /* Map<Long, String> map = baseSysUserService
                    .list(QueryWrapper.create()
                            .select(BaseSysUserEntity::getId, BaseSysUserEntity::getName)
                            .in(BaseSysUserEntity::getId, list))
                    .stream()
                    .collect(Collectors.toMap(BaseSysUserEntity::getId, BaseSysUserEntity::getName));*/
            records.forEach(o -> {
                if (map.containsKey(o.getUserId())) {
                    o.setUserName(map.get(o.getUserId()));
                }
            });
        }
        return iPage;
    }

    @Override
    public Boolean restore(List<Long> ids) {
        if (ObjUtil.isEmpty(ids)) {
            return false;
        }
        List<RecycleDataEntity> list = list(
                QueryWrapper.create().in(RecycleDataEntity::getId, ids));
        list.forEach(o -> {
            // 处理恢复数据
            boolean flag = handlerRestore(o);
            if (flag) {
                // 删除回收站记录
                o.removeById();
            }
        });
        return true;
    }

    /**
     * 处理数据恢复
     */
    private boolean handlerRestore(RecycleDataEntity recycleDataEntity) {
        RecycleDataEntity.EntityInfo entityInfo = recycleDataEntity.getEntityInfo();
        try {
            Class<?> entityClass = ClassUtil.loadClass(entityInfo.getEntityClassName());
            List<Object> records = recycleDataEntity.getData();
            BaseMapper<?> baseMapper = mapperProviderService.getMapperByEntityClass(
                    entityClass);
            // 插入数据
            List insertList = new ArrayList<>();
            for (Object record : records) {
                Object entity = JSONUtil.toBean(JSONUtil.parseObj(record), entityClass);
                Method getIdMethod = entityClass.getMethod("getId");
                Object id = getIdMethod.invoke(entity);
                if (baseMapper.selectOneById((Long) id) == null) {
                    insertList.add(entity);
                }
            }
            baseMapper.insertBatch(insertList);
            return true;
        } catch (Exception e) {
            log.error("恢复数据失败", e);
        }
        return false;
    }
}