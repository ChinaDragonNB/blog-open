package com.ak47007.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.ak47007.mapper.*;
import com.ak47007.model.*;
import com.ak47007.model.base.Result;
import com.ak47007.model.query.UserQuery;
import com.ak47007.security.UserEntity;
import com.ak47007.service.UserService;
import com.ak47007.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author AK47007
 * @date 2019/7/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final SysUserMapper sysUserMapper;

    private final SysRoleMapper sysRoleMapper;

    private final SysAuthorityMapper sysAuthorityMapper;

    private final SysRoleAuthorityMapper sysRoleAuthorityMapper;

    private final UserUtil userUtil;

    private final SysLoginLogMapper logLoginMapper;

    private final LocationUtil locationUtil;


    @Override
    public Result<?> login(String userName, String loginIp, Boolean remember) {
        // 使用用户名和加密后的密码与数据库中的用户名密码匹配
        SysUser user = findByUserName(userName);
        AssertUtil.notNull(user, "用户名或密码错误");
        roleAndPermission(user);
        String loginPlace = locationUtil.getPlace(loginIp);
        int result = logLoginMapper.insert(SysLoginLog.builder().userId(user.getId()).ipAddr(loginIp).loginAddr(loginPlace).build());
        if (result > 0) {
            //  将token存入redis并将token返回给前端
            return Result.success("登录成功", userUtil.setUser(user, remember));
        } else {
            return Result.error("登录失败,服务器出现异常");
        }
    }


    @Override
    public List<SysUser> findList(UserQuery query) {

        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        String sql = ElementUISortUtil.sortSql(query.getColumnName(), query.getOrder(), " id DESC ");
        wrapper.last(sql);

        String nickName = query.getNickName();
        if (StrUtil.isNotBlank(nickName)) {
            wrapper.like(SysUser::getNickName, nickName);
        }

        query.startPage();
        List<SysUser> userList = sysUserMapper.selectList(wrapper);
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        for (SysUser sysUser : userList) {
            sysRoles.stream().filter(l -> l.getId().equals(sysUser.getRoleId())).findFirst().ifPresent(sysRole -> sysUser.setRoleName(sysRole.getRoleNameCn()));
        }
        return userList;

    }

    @Override
    public Result<?> save(int type, SysUser user) {
        int result;
        switch (type) {
            case 1:
                // MD5加密密码
                user.setUserPass(SecureUtil.md5(user.getUserPass()).toUpperCase());
                user.setCreateTime(LocalDateTime.now());
                result = sysUserMapper.insert(user);
                if (result > 0) {
                    return Result.success("保存成功");
                }
                break;
            case 2:
                result = sysUserMapper.updateById(user);
                if (result > 0) {
                    return Result.success("保存成功");
                }
                break;
            case 3:
                result = sysUserMapper.deleteById(user.getId());
                if (result > 0) {
                    return Result.success("删除成功");
                }
                break;
            default:
                return Result.error("非法操作");
        }
        return Result.error("操作失败");
    }


    @Override
    public SysUser userInfo(long id) {
        SysUser user = sysUserMapper.selectById(id);
        AssertUtil.notNull(user, "用户信息获取失败");
        return user;
    }

    /**
     * 查询用户名是否存在
     *
     * @param userName 用户名
     * @return 实现了UserDetails 的实体类
     * @throws UsernameNotFoundException 未找到用户异常
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SysUser user = findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        SysRole role = sysRoleMapper.selectById(user.getRoleId());

        UserEntity userEntity = new UserEntity(user);
        if (role != null) {
            user.setRole(role);

            // 找出该角色所拥有的权限
            List<SysRoleAuthority> roleAuthorityList = sysRoleAuthorityMapper.findAllByRoleId(role.getId());
            List<Long> authorityIds = roleAuthorityList.stream().map(SysRoleAuthority::getAuthorityId).collect(Collectors.toList());
            List<SysAuthority> sysAuthorities = getAuthorities(authorityIds);

            // 权限集合 如果他们提供了正确的用户名和密码并且启用了用户，则应授予调用者权限。不是空的。
            ArrayList<GrantedAuthority> grantedAuthorityList = sysAuthorities.stream().map(l -> new SimpleGrantedAuthority(l.getAuthorityNameEn())).collect(Collectors.toCollection(ArrayList::new));
            // 角色需要ROLE_前缀
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getRoleNameEn()));

            userEntity.setAuthorities(grantedAuthorityList);
        }

        // 为了不影响原来的实体类,这里重新进行赋值
        return userEntity;
    }

    private SysUser findByUserName(String userName) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getUserName, userName);
        return sysUserMapper.selectOne(wrapper);
    }


    /**
     * 为用户注入角色与权限
     *
     * @param user 用户
     */
    private void roleAndPermission(SysUser user) {
        // 找出所属角色
        SysRole role = sysRoleMapper.selectById(user.getRoleId());
        if (role != null) {
            // 找出角色拥有的权限
            List<SysRoleAuthority> roleAuthorityList = sysRoleAuthorityMapper.findAllByRoleId(role.getId());
            if (!roleAuthorityList.isEmpty()) {
                List<Long> authorityIds = roleAuthorityList.stream().map(SysRoleAuthority::getAuthorityId).collect(Collectors.toList());
                List<SysAuthority> sysAuthorities = getAuthorities(authorityIds);
                // 为角色注入拥有的权限
                role.setAuthorityList(sysAuthorities);
            }
            // 为用户注入所属角色
            user.setRole(role);
        }
    }

    /**
     * 获得权限信息列表
     *
     * @param authorityIds 权限id集合
     * @return
     */
    private List<SysAuthority> getAuthorities(List<Long> authorityIds) {
        LambdaQueryWrapper<SysAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.in(SysAuthority::getId, authorityIds);
        return sysAuthorityMapper.selectList(wrapper);
    }
}


