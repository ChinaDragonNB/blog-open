package com.ak47007.service.impl;

import java.util.List;

import com.ak47007.mapper.SysRoleAuthorityMapper;
import com.ak47007.model.SysRoleAuthority;
import com.ak47007.service.RoleAuthorityService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author AK47007
 * create: 2019/11/21
 */
@Service
@AllArgsConstructor
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    private final SysRoleAuthorityMapper sysRoleAuthorityMapper;


    @Override
    public List<SysRoleAuthority> findAllByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRoleAuthority::getRoleId, roleId);
        return sysRoleAuthorityMapper.selectList(wrapper);
    }

    @Override
    public int deleteByRoleId(Long roleId) {
        return sysRoleAuthorityMapper.deleteById(roleId);
    }

    @Override
    public void insertData(Long roleId, Long authorityId) {
        SysRoleAuthority roleAuthority = new SysRoleAuthority();
        roleAuthority.setRoleId(roleId);
        roleAuthority.setAuthorityId(authorityId);
        sysRoleAuthorityMapper.insert(roleAuthority);
    }


    @Override
    public void insertBatchData(Long roleId, List<Long> authorityIds) {
        if (roleId == null || authorityIds.isEmpty()) {
            return;
        }
        List<SysRoleAuthority> existList = sysRoleAuthorityMapper.findAllByRoleId(roleId);
        for (Long authorityId : authorityIds) {
            SysRoleAuthority obj = existList.stream().filter(l -> l.getAuthorityId().equals(authorityId)).findFirst().orElse(null);
            if (obj == null) {
                insertData(roleId, authorityId);
            }
        }
    }


}
