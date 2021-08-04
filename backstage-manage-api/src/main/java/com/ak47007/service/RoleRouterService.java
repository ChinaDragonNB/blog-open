package com.ak47007.service;

import com.ak47007.model.SysRoleRouter;

import java.util.List;

public interface RoleRouterService {


    /**
     * 根据角色ID查询拥有的路由
     *
     * @param roleId 角色ID
     */
    List<SysRoleRouter> findByRoleId(Long roleId);

    /**
     * 插入批量数据
     *
     * @param roleId    角色id
     * @param routerIds 选择的路由id集合
     */
    void insertBatchData(Long roleId, List<Long> routerIds);

}
