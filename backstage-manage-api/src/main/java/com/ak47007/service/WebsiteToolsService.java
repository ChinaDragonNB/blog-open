package com.ak47007.service;

import com.ak47007.model.WebsiteTools;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/11/12 21:52
 * describe：
 */
public interface WebsiteToolsService {

    /**
     * 查询实用网站
     */
    List<WebsiteTools> getWebsiteTools();
}
