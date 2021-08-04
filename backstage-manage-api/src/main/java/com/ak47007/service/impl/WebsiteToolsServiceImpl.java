package com.ak47007.service.impl;

import com.ak47007.mapper.WebsiteToolsMapper;
import com.ak47007.model.WebsiteTools;
import com.ak47007.service.WebsiteToolsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author AK47007
 * @date 2020/11/12 21:52
 * describe：
 */
@Service
public class WebsiteToolsServiceImpl implements WebsiteToolsService {

    @Resource
    private WebsiteToolsMapper websiteToolsMapper;

    @Override
    public List<WebsiteTools> getWebsiteTools() {
        return websiteToolsMapper.selectList(null);
    }
}
