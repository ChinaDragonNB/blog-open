package com.ak47007.service;

import com.ak47007.model.SysRoleAuthority;

import java.util.List;

/**
 * @author AK47007
 * create: 2019/11/21
 */
public interface RoleAuthorityService {

    /**
     * 查询角色拥有的权限
     *
     * @param roleId 角色id
     */
    List<SysRoleAuthority> findAllByRoleId(Long roleId);

    /**
     * 删除角色权限
     *
     * @param roleId 角色id
     */
    int deleteByRoleId(Long roleId);

    /**
     * 插入数据
     * @param roleId 角色ID
     * @param authorityId 权限ID
     */
    void insertData(Long roleId, Long authorityId);

    /**
     * 插入批量数据
     *
     * @param roleId       角色id
     * @param authorityIds 需要插入的权限id集合
     */
    void insertBatchData(Long roleId, List<Long> authorityIds);

}
