package com.ak47007.service;


import com.ak47007.model.SysUser;
import com.ak47007.model.base.Result;
import com.ak47007.model.query.UserQuery;

import java.util.List;


/**
 * @author AK47007
 * @date 2019/7/13
 */
public interface UserService {


    /**
     * 登录
     *
     * @param userName 用户名
     * @param loginIp  登录人IP
     * @param remember 记住我
     */
    Result<?> login(String userName, String loginIp, Boolean remember);


    /**
     * 用户列表
     */
    List<SysUser> findList(UserQuery query);

    /**
     * 保存数据
     *
     * @param type 1：新增、2：修改、3：删除
     */
    Result<?> save(int type, SysUser user);

    /**
     * 用户信息
     *
     * @param id 用户id
     */
    SysUser userInfo(long id);

}

