package com.ak47007.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ak47007.model.SysRoleRouter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author AK47007
 * date 2021/5/1 17:23
 * describes:
 */
public interface SysRoleRouterMapper extends BaseMapper<SysRoleRouter> {

    /**
     * 根据角色id查询
     *
     * @param roleId 角色id
     */
    List<SysRoleRouter> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id删除
     *
     * @param roleId 角色id
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除角色路由
     *
     * @param roleId 角色id
     * @param ids    不能删除的路由id集合
     */
    void deleteBatch(@Param("roleId") Long roleId, @Param("ids") List<Long> ids);

}