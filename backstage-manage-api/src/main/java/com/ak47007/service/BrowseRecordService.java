package com.ak47007.service;

import com.ak47007.model.vo.main.BrowseRecordVO;

public interface BrowseRecordService {

    /**
     * 网站日常访问图表
     *
     * @param userId 当前登录用户id
     */
    BrowseRecordVO browseCharts(Long userId);

}
