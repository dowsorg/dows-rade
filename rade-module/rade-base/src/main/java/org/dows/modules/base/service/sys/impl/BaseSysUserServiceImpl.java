package org.dows.modules.base.service.sys.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.tangzc.autotable.core.constants.DatabaseDialect;
import lombok.RequiredArgsConstructor;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.crud.ModifyEnum;
import org.dows.core.cache.RadeCache;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.util.DatabaseDialectUtils;
import org.dows.core.security.RadeSecurityUtil;
import org.dows.modules.base.entity.sys.BaseSysDepartmentEntity;
import org.dows.modules.base.entity.sys.BaseSysUserEntity;
import org.dows.modules.base.mapper.sys.BaseSysDepartmentMapper;
import org.dows.modules.base.mapper.sys.BaseSysUserMapper;
import org.dows.modules.base.service.sys.BaseSysPermsService;
import org.dows.modules.base.service.sys.BaseSysUserService;
import org.springframework.stereotype.Service;

import static com.mybatisflex.core.query.QueryMethods.groupConcat;
import static org.dows.modules.base.entity.sys.table.BaseSysDepartmentEntityTableDef.BASE_SYS_DEPARTMENT_ENTITY;
import static org.dows.modules.base.entity.sys.table.BaseSysRoleEntityTableDef.BASE_SYS_ROLE_ENTITY;
import static org.dows.modules.base.entity.sys.table.BaseSysUserEntityTableDef.BASE_SYS_USER_ENTITY;
import static org.dows.modules.base.entity.sys.table.BaseSysUserRoleEntityTableDef.BASE_SYS_USER_ROLE_ENTITY;

/**
 * 系统用户
 */
@Service
@RequiredArgsConstructor
public class BaseSysUserServiceImpl extends BaseServiceImpl<BaseSysUserMapper, BaseSysUserEntity>
    implements BaseSysUserService {

    final private RadeCache radeCache;

    final private BaseSysPermsService baseSysPermsService;

    final private BaseSysDepartmentMapper baseSysDepartmentMapper;

    @Override
    public Object page(JSONObject requestParams, Page<BaseSysUserEntity> page, QueryWrapper qw) {
        String keyWord = requestParams.getStr("keyWord");
        Integer status = requestParams.getInt("status");
        Long[] departmentIds = requestParams.get("departmentIds", Long[].class);
        JSONObject tokenInfo = RadeSecurityUtil.getAdminUserInfo(requestParams);
        // 用户的部门权限
        Long[] permsDepartmentArr = radeCache.get("admin:department:" + tokenInfo.get("userId"),
            Long[].class);
        String databaseDialect = DatabaseDialectUtils.getDatabaseDialect();
        if (databaseDialect.equals(DatabaseDialect.PostgreSQL)) {
            // 兼容postgresql
            qw.select("base_sys_user.id","base_sys_user.create_time","base_sys_user.department_id",
                "base_sys_user.email","base_sys_user.head_img","base_sys_user.name","base_sys_user.nick_name",
                "base_sys_user.phone","base_sys_user.remark","base_sys_user.status",
                "base_sys_user.update_time","base_sys_user.username",
                "string_agg(base_sys_role.name, ', ') AS roleName",
                "base_sys_department.name AS departmentName"
            );
        } else {
            qw.select(BASE_SYS_USER_ENTITY.ALL_COLUMNS,
                groupConcat(BASE_SYS_ROLE_ENTITY.NAME).as("roleName"),
                BASE_SYS_DEPARTMENT_ENTITY.NAME.as("departmentName")
            );
        }

        qw.from(BASE_SYS_USER_ENTITY).leftJoin(BASE_SYS_USER_ROLE_ENTITY)
            .on(BASE_SYS_USER_ENTITY.ID.eq(BASE_SYS_USER_ROLE_ENTITY.USER_ID))
            .leftJoin(BASE_SYS_ROLE_ENTITY)
            .on(BASE_SYS_USER_ROLE_ENTITY.ROLE_ID.eq(BASE_SYS_ROLE_ENTITY.ID))
            .leftJoin(BASE_SYS_DEPARTMENT_ENTITY)
            .on(BASE_SYS_USER_ENTITY.DEPARTMENT_ID.eq(BASE_SYS_DEPARTMENT_ENTITY.ID));

        // 不显示admin用户
        qw.and(BASE_SYS_USER_ENTITY.USERNAME.ne("admin"));
        // 筛选部门
        qw.and(BASE_SYS_USER_ENTITY.DEPARTMENT_ID.in(departmentIds,
            ArrayUtil.isNotEmpty(departmentIds)));
        // 筛选状态
        qw.and(BASE_SYS_USER_ENTITY.STATUS.eq(status, status != null));
        // 搜索关键字
        if (StrUtil.isNotEmpty(keyWord)) {
            qw.and(BASE_SYS_USER_ENTITY.NAME.like(keyWord)
                .or(BASE_SYS_USER_ENTITY.USERNAME.like(keyWord)));
        }
        // 过滤部门权限
        qw.and(BASE_SYS_USER_ENTITY.DEPARTMENT_ID.in(
            permsDepartmentArr == null || permsDepartmentArr.length == 0 ? new Long[]{null}
                : permsDepartmentArr,
                !RadeSecurityUtil.getAdminUsername().equals("admin")));
        if (databaseDialect.equals(DatabaseDialect.PostgreSQL)) {
            // 兼容postgresql
            qw.groupBy("base_sys_user.id","base_sys_user.create_time","base_sys_user.department_id",
                "base_sys_user.email","base_sys_user.head_img","base_sys_user.name","base_sys_user.nick_name",
                "base_sys_user.phone","base_sys_user.remark","base_sys_user.status",
                "base_sys_user.update_time","base_sys_user.username",
                "base_sys_department.name");
        } else {
            qw.groupBy(BASE_SYS_USER_ENTITY.ID);
        }
        return mapper.paginate(page, qw);
    }

    @Override
    public void personUpdate(Long userId, Dict body) {
        BaseSysUserEntity userEntity = getById(userId);
        RadePreconditions.checkEmpty(userEntity, "用户不存在");
        userEntity.setNickName(body.getStr("nickName"));
        userEntity.setHeadImg(body.getStr("headImg"));
        // 修改密码
        if (StrUtil.isNotEmpty(body.getStr("password"))) {
            userEntity.setPassword(MD5.create().digestHex(body.getStr("password")));
            userEntity.setPasswordV(userEntity.getPasswordV() + 1);
            radeCache.set("admin:passwordVersion:" + userId, userEntity.getPasswordV());
        }
        updateById(userEntity);
    }

    @Override
    public void move(Long departmentId, Long[] userIds) {
        UpdateChain.of(BaseSysUserEntity.class)
            .set(BaseSysUserEntity::getDepartmentId, departmentId)
            .in(BaseSysUserEntity::getId, (Object) userIds).update();
    }

    @Override
    public Long add(JSONObject requestParams, BaseSysUserEntity entity) {
        BaseSysUserEntity check = getOne(
            QueryWrapper.create().eq(BaseSysUserEntity::getUsername, entity.getUsername()));
        RadePreconditions.check(check != null, "用户名已存在");
        entity.setPassword(MD5.create().digestHex(entity.getPassword()));
        super.add(requestParams, entity);
        return entity.getId();
    }

    @Override
    public boolean update(JSONObject requestParams, BaseSysUserEntity entity) {
        RadePreconditions.check(
            StrUtil.isNotEmpty(entity.getUsername()) && entity.getUsername().equals("admin"),
            "非法操作");
        BaseSysUserEntity userEntity = getById(entity.getId());
        if (StrUtil.isNotEmpty(entity.getPassword())) {
            entity.setPasswordV(entity.getPasswordV() + 1);
            entity.setPassword(MD5.create().digestHex(entity.getPassword()));
            radeCache.set("admin:passwordVersion:" + entity.getId(), entity.getPasswordV());
        } else {
            entity.setPassword(userEntity.getPassword());
            entity.setPasswordV(userEntity.getPasswordV());
        }
        // 被禁用
        if (entity.getStatus() == 0) {
            RadeSecurityUtil.adminLogout(entity);
        }
        return super.update(requestParams, entity);
    }

    @Override
    public void modifyAfter(JSONObject requestParams, BaseSysUserEntity baseSysUserEntity,
        ModifyEnum type) {
        if (type != ModifyEnum.DELETE && requestParams.get("roleIdList", Long[].class) != null) {
            // 刷新权限
            baseSysPermsService.updateUserRole(baseSysUserEntity.getId(),
                requestParams.get("roleIdList", Long[].class));
        }
    }

    @Override
    public Object info(Long id) {
        BaseSysUserEntity userEntity = getById(id);
        Long[] roleIdList = baseSysPermsService.getRoles(id);
        BaseSysDepartmentEntity departmentEntity = baseSysDepartmentMapper.selectOneById(
            userEntity.getDepartmentId());
        userEntity.setPassword(null);
        return Dict.parse(userEntity).set("roleIdList", roleIdList).set("departmentName",
            departmentEntity != null ? departmentEntity.getName() : null);
    }
}