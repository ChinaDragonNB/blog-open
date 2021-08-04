package com.ak47007.service;

import java.util.List;

import com.ak47007.model.vo.AuthorityApiVO;

public interface AuthorityApiService {

    /**
     * 查询权限拥有的接口
     *
     * @param authorityId 权限id
     */
    List<AuthorityApiVO> findByAuthorityId(Long authorityId);


}
