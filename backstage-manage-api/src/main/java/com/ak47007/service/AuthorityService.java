package com.ak47007.service;

import java.util.List;

import com.ak47007.model.SysAuthority;
import com.ak47007.model.base.Result;
import com.ak47007.model.dto.AuthorityDTO;
import com.ak47007.model.vo.TreeNodeVO;

/**
 * @author Ak47007
 * CreateDate： 2019/11/21
 */
public interface AuthorityService {


    /**
     * 权限树
     */
    List<TreeNodeVO> authorityTree();

    /**
     * 权限信息
     *
     * @param id 权限id
     */
    SysAuthority authorityInfo(long id);

    /**
     * 保存数据
     *
     * @param type 保存类型 1新增 2编辑 3删除
     * @param dto  传输数据
     */
    Result<Long> save(int type, AuthorityDTO dto);

    /**
     * 查询子级权限
     *
     * @param parentId 权限id
     */
    List<SysAuthority> findAllByParentId(Long parentId);

    /**
     * 公共权限,所有角色都有的权限
     */
    List<SysAuthority> getCommon();

}


