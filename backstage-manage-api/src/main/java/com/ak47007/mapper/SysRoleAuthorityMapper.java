package com.ak47007.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ak47007.model.SysRoleAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author AK47007
 * date 2021/5/1 17:17
 * describes:
 */
public interface SysRoleAuthorityMapper extends BaseMapper<SysRoleAuthority> {

    /**
     * 根据角色id删除数据
     *
     * @param roleId 角色id
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id查询数据
     *
     * @param roleId 角色id
     */
    List<SysRoleAuthority> findAllByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除未选择的权限
     *
     * @param roleId       角色id
     * @param authorityIds 不能删除的权限
     */
    void deleteBatch(@Param("roleId") Long roleId, @Param("authorityIds") List<Long> authorityIds);

}