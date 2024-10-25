package org.dows.modules.base.service.sys.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.crud.ModifyEnum;
import org.dows.core.exception.RadeException;
import org.dows.core.security.RadeSecurityUtil;
import org.dows.modules.base.entity.sys.BaseSysRoleDepartmentEntity;
import org.dows.modules.base.entity.sys.BaseSysRoleEntity;
import org.dows.modules.base.entity.sys.BaseSysRoleMenuEntity;
import org.dows.modules.base.mapper.sys.BaseSysRoleDepartmentMapper;
import org.dows.modules.base.mapper.sys.BaseSysRoleMapper;
import org.dows.modules.base.mapper.sys.BaseSysRoleMenuMapper;
import org.dows.modules.base.service.sys.BaseSysPermsService;
import org.dows.modules.base.service.sys.BaseSysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色
 */
@RequiredArgsConstructor
@Service
public class BaseSysRoleServiceImpl extends BaseServiceImpl<BaseSysRoleMapper, BaseSysRoleEntity>
        implements BaseSysRoleService {

    final private BaseSysRoleMapper baseSysRoleMapper;

    final private BaseSysRoleMenuMapper baseSysRoleMenuMapper;

    final private BaseSysRoleDepartmentMapper baseSysRoleDepartmentMapper;

    final private BaseSysPermsService baseSysPermsService;

    @Override
    public Object add(JSONObject requestParams, BaseSysRoleEntity entity) {
        BaseSysRoleEntity checkLabel = getOne(QueryWrapper.create().eq(BaseSysRoleEntity::getLabel, entity.getLabel()));
        if (checkLabel != null) {
            throw new RadeException("标识已存在");
        }
        entity.setUserId((RadeSecurityUtil.getAdminUserInfo(requestParams).getLong("userId")));
        return super.add(requestParams, entity);
    }

    @Override
    public Object info(Long id) {
        BaseSysRoleEntity roleEntity = getById(id);
        Long[] menuIdList = new Long[0];
        Long[] departmentIdList = new Long[0];
        if (roleEntity != null) {
            List<BaseSysRoleMenuEntity> list = baseSysRoleMenuMapper
                    .selectListByQuery(QueryWrapper.create().eq(BaseSysRoleMenuEntity::getRoleId, id, !id.equals(1L)));
            menuIdList = list.stream().map(BaseSysRoleMenuEntity::getMenuId).toArray(Long[]::new);

            List<BaseSysRoleDepartmentEntity> departmentEntities = baseSysRoleDepartmentMapper.selectListByQuery(
                    QueryWrapper.create().eq(BaseSysRoleDepartmentEntity::getRoleId, id, !id.equals(1L)));

            departmentIdList = departmentEntities.stream().map(BaseSysRoleDepartmentEntity::getDepartmentId)
                    .toArray(Long[]::new);
        }
        return Dict.parse(roleEntity).set("menuIdList", menuIdList).set("departmentIdList", departmentIdList);
    }

    @Override
    public void modifyAfter(JSONObject requestParams, BaseSysRoleEntity baseSysRoleEntity, ModifyEnum type) {
        if (type == ModifyEnum.DELETE) {
            Long[] ids = requestParams.get("ids", Long[].class);
            for (Long id : ids) {
                baseSysPermsService.refreshPermsByRoleId(id);
            }
        } else {
            baseSysPermsService.updatePerms(baseSysRoleEntity.getId(), requestParams.get("menuIdList", Long[].class),
                    requestParams.get("departmentIdList", Long[].class));
        }
    }

    @Override
    public Object list(JSONObject requestParams, QueryWrapper queryWrapper) {
        return baseSysRoleMapper.selectListByQuery(queryWrapper.ne(BaseSysRoleEntity::getId, 1L).and(qw -> {
            JSONObject object = RadeSecurityUtil.getAdminUserInfo(requestParams);
            qw.eq(BaseSysRoleEntity::getUserId, object.get("userId")).or(w -> {
                w.in(BaseSysRoleEntity::getId,
                        (Object) object.get("roleIds", Long[].class));
            });
        }, !RadeSecurityUtil.getAdminUsername().equals("admin")));
    }
}