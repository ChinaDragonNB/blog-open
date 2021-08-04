package com.ak47007.service;

import java.util.List;

import com.ak47007.model.SysRole;
import com.ak47007.model.base.Result;
import com.ak47007.model.dto.RoleDTO;
import com.ak47007.model.query.RoleQuery;

/**
 * @author Ak47007
 * CreateDate： 2019/11/20
 */
public interface RoleService {


    /**
     * 保存数据
     *
     * @param type 操作类型
     * @param dto  封装的数据对象
     */
    Result<?> save(int type, RoleDTO dto);

    /**
     * 角色列表
     */
    List<SysRole> list(RoleQuery query);

    /**
     * 角色信息
     *
     * @param roleId 角色ID
     */
    SysRole roleInfo(long roleId);


}



