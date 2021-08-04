package com.ak47007.service;

import com.ak47007.model.SysLoginLog;
import com.ak47007.model.SysOperLog;
import com.ak47007.model.query.LogQuery;
import com.ak47007.model.vo.ViewBrowseList;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/7/11
 * Describe:
 */
public interface LogService {


    /**
     * 浏览记录数据
     */
    List<ViewBrowseList> listArticleLog(LogQuery query);

    /**
     * 操作日志数据
     */
    List<SysOperLog> listOperLog(LogQuery query);

    /**
     * 登录日志数据
     */
    List<SysLoginLog> listLoginLog(LogQuery query);
}
