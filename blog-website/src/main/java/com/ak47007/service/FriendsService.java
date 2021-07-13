package com.ak47007.service;

import com.ak47007.model.Links;

import java.util.List;

/**
 * @author ak47007
 * @date 2020/5/21
 * 描述：
 */
public interface FriendsService {

    /**
     * 获取友情链接
     *
     */
    List<Links> getFriends();

    /**
     * 申请友情链接
     *
     * @param links
     */
    void applyFriend(Links links);
}
