package com.ak47007.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.ak47007.mapper.*;
import com.ak47007.model.*;
import com.ak47007.model.base.Result;
import com.ak47007.model.dto.RoleDTO;
import com.ak47007.model.query.RoleQuery;
import com.ak47007.service.AuthorityService;
import com.ak47007.service.RoleAuthorityService;
import com.ak47007.service.RoleRouterService;
import com.ak47007.service.RoleService;
import com.ak47007.utils.AssertUtil;
import com.ak47007.utils.ElementUISortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Ak47007
 * CreateDate： 2019/11/20
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final SysRoleMapper sysRoleMapper;

    private final SysAuthorityMapper sysAuthorityMapper;

    private final SysRoleAuthorityMapper sysRoleAuthorityMapper;

    private final SysUserMapper sysUserMapper;

    private final SysRouterMapper sysRouterMapper;

    private final SysRoleRouterMapper sysRoleRouterMapper;

    private final AuthorityService authorityService;

    private final RoleRouterService roleRouterService;

    private final RoleAuthorityService roleAuthorityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> save(int type, RoleDTO dto) {
        int result;
        SysRole role = dto.getRole();
        SysUser user = dto.getUser();
        List<Long> authorityIds = dto.getAuthorityIds();
        List<Long> routerIds = dto.getRouterIds();
        Boolean checkFlag1 = dto.getCheckFlag1();
        Boolean checkFlag2 = dto.getCheckFlag2();
        Long roleId = role.getId();
        switch (type) {
            case 1:
                role.setCreateTime(LocalDateTime.now());
                role.setCreateUser(user.getId());
                result = sysRoleMapper.insert(role);
                roleId = role.getId();
                if (result > 0) {
                    List<SysAuthority> common = authorityService.getCommon();
                    for (SysAuthority sysAuthority : common) {
                        roleAuthorityService.insertData(roleId, sysAuthority.getId());
                    }
                    roleAuthorityService.insertBatchData(roleId, authorityIds);
                    roleRouterService.insertBatchData(roleId, routerIds);
                    return Result.success("保存成功");
                }
                break;
            case 2:
                if (isUsed(role.getId()) && !role.getState()) {
                    return Result.error("保存失败,不能锁定该角色,该角色已被使用");
                }
                result = sysRoleMapper.updateById(role);
                if (result > 0) {
                    if (checkFlag1) {
                        roleAuthorityService.insertBatchData(roleId, authorityIds);
                        sysRoleAuthorityMapper.deleteBatch(roleId, authorityIds);
                    }
                    if (checkFlag2) {
                        roleRouterService.insertBatchData(roleId, routerIds);
                        sysRoleRouterMapper.deleteBatch(roleId, routerIds);
                    }
                    return Result.success("保存成功,重新登录后即 可刷新所拥有的模块");
                }
                break;
            case 3:
                if (isUsed(roleId)) {
                    return Result.error("删除失败,该角色已被其他用户使用");
                }
                result = sysRoleAuthorityMapper.deleteByRoleId(roleId);
                if (result > 0) result = sysRoleRouterMapper.deleteByRoleId(roleId);
                if (result > 0) result = sysRoleMapper.deleteById(roleId);
                if (result > 0) return Result.success("删除成功");
                break;
            default:
                return Result.error("非法操作");
        }
        return Result.error("保存失败");
    }


    @Override
    public List<SysRole> list(RoleQuery query) {

        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();

        String roleName = query.getRoleName();
        if (StrUtil.isNotBlank(roleName)) {
            wrapper.like(SysRole::getRoleNameCn, roleName);
        }

        String sql = ElementUISortUtil.sortSql(query.getColumnName(), query.getOrder(), " id DESC ");
        wrapper.last(sql);

        query.startPage();
        return sysRoleMapper.selectList(wrapper);
    }

    @Override
    public SysRole roleInfo(long roleId) {
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        AssertUtil.notNull(sysRole, "角色信息获取失败");
        // 该角色所拥有的权限
        List<SysRoleAuthority> roleAuthorityList = sysRoleAuthorityMapper.findAllByRoleId(roleId);
        List<SysAuthority> authorityList = sysAuthorityMapper.selectList(null);
        List<SysAuthority> alreadyAuthoritys = roleAuthorityList.stream().map(l ->
                authorityList.stream().filter(authority ->
                        l.getAuthorityId().equals(authority.getId())
                ).findFirst().orElse(null)
        ).collect(Collectors.toList());
        alreadyAuthoritys.removeAll(Collections.singleton(null));

        sysRole.setAuthorityList(alreadyAuthoritys);

        // 该角色拥有的模块
        List<SysRoleRouter> roleRouterList = sysRoleRouterMapper.findByRoleId(roleId);
        if (!roleRouterList.isEmpty()) {
            // 顶级路由菜单,这里已经把* 跟 404 过滤掉了
            List<SysRouter> routerTops = sysRouterMapper.findByParentId(0L);
            // 顶级路由菜单的ID
            List<Long> topIds = routerTops.stream().map(SysRouter::getId).collect(Collectors.toList());
            // 由于角色路由表中保存了顶级路由菜单,所以现在需要把它过滤掉
            List<Long> routerIds = roleRouterList.stream().filter(roleRouter -> !topIds.contains(roleRouter.getRouterId())).map(SysRoleRouter::getRouterId).collect(Collectors.toList());
            sysRole.setRouterIds(routerIds);
        }

        return sysRole;
    }


    /**
     * 角色是否被使用
     *
     * @param roleId 角色id
     */
    private Boolean isUsed(Long roleId) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getRoleId, roleId);
        List<SysUser> sysUsers = sysUserMapper.selectList(wrapper);
        return !sysUsers.isEmpty();
    }

}



