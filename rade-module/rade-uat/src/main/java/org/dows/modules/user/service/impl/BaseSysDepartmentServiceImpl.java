package org.dows.modules.user.service.impl;

import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import lombok.RequiredArgsConstructor;
import org.dows.core.rbac.RbacProvider;
import org.dows.core.security.SecurityProvider;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.modules.user.entity.BaseSysDepartmentEntity;
import org.dows.modules.user.entity.BaseSysUserEntity;
import org.dows.modules.user.mapper.BaseSysDepartmentMapper;
import org.dows.modules.user.mapper.BaseSysUserMapper;
import org.dows.modules.user.service.BaseSysDepartmentService;
import org.dows.core.uat.UserProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统部门
 */
@RequiredArgsConstructor
@Service
public class BaseSysDepartmentServiceImpl extends
        BaseServiceImpl<BaseSysDepartmentMapper, BaseSysDepartmentEntity>
        implements BaseSysDepartmentService {

    final private BaseSysUserMapper baseSysUserMapper;
    final private RbacProvider rbacProvider;
    final private SecurityProvider securityProvider;
    final private UserProvider userProvider;

    @Override
    public void order(List<BaseSysDepartmentEntity> list) {
        list.forEach(baseSysDepartmentEntity -> {
            UpdateChain.of(BaseSysDepartmentEntity.class)
                    .set(BaseSysDepartmentEntity::getOrderNum, baseSysDepartmentEntity.getOrderNum())
                    .set(BaseSysDepartmentEntity::getParentId, baseSysDepartmentEntity.getParentId())
                    .eq(BaseSysDepartmentEntity::getId, baseSysDepartmentEntity.getId()).update();
        });
    }

    @Override
    public List<BaseSysDepartmentEntity> list(JSONObject requestParams, QueryWrapper queryWrapper) {
        String username = securityProvider.getAdminUsername();
        Long[] loginDepartmentIds = userProvider.loginDepartmentIds();
        if (loginDepartmentIds != null && loginDepartmentIds.length == 0) {
            return new ArrayList<>();
        }
        List<BaseSysDepartmentEntity> list = this.list(
                QueryWrapper.create()
                        .in(BaseSysDepartmentEntity::getId, loginDepartmentIds, !username.equals("admin"))
                        .orderBy(BaseSysDepartmentEntity::getOrderNum, false));
        list.forEach(e -> {
            List<BaseSysDepartmentEntity> parentDepartment = list.stream()
                    .filter(sysDepartmentEntity -> e.getParentId() != null
                            && e.getParentId().equals(sysDepartmentEntity.getId()))
                    .toList();
            if (!parentDepartment.isEmpty()) {
                e.setParentName(parentDepartment.get(0).getName());
            }
        });
        return list;
    }

    @Override
    public boolean delete(JSONObject requestParams, Long... ids) {
        super.delete(ids);
        // 是否删除对应用户 否则移动到顶层部门
        if (requestParams.getBool("deleteUser")) {
            QueryWrapper in = QueryWrapper.create().in(BaseSysUserEntity::getDepartmentId, (Object) ids);
            return baseSysUserMapper.deleteByQuery(in) > 0;
        } else {
            QueryWrapper aNull = QueryWrapper.create().isNull(BaseSysDepartmentEntity::getParentId);
            BaseSysDepartmentEntity topDepartment = getOne(aNull);
            if (topDepartment != null) {
                UpdateChain.of(BaseSysUserEntity.class)
                        .set(BaseSysUserEntity::getDepartmentId, topDepartment.getId())
                        .in(BaseSysUserEntity::getDepartmentId, (Object) ids).update();
            }
        }
        return false;
    }
}