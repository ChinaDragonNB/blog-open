package com.ak47007.service.impl;

import java.util.List;

import com.ak47007.mapper.SysAuthorityApiMapper;
import com.ak47007.model.vo.AuthorityApiVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.ak47007.service.AuthorityApiService;


@Service
@AllArgsConstructor
public class AuthorityApiServiceImpl implements AuthorityApiService {

    private final SysAuthorityApiMapper sysAuthorityApiMapper;


    @Override
    public List<AuthorityApiVO> findByAuthorityId(Long authorityId) {
        return sysAuthorityApiMapper.findByAuthorityId(authorityId);
    }


}
