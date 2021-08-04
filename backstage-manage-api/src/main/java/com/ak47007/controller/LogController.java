package com.ak47007.controller;

import com.ak47007.model.SysLoginLog;
import com.ak47007.model.SysOperLog;
import com.ak47007.model.base.Result;
import com.ak47007.model.query.LogQuery;
import com.ak47007.model.vo.ViewBrowseList;
import com.ak47007.service.LogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/7/11
 * Describe: 日志管理接口
 */
@Api(tags = "日志管理模块接口")
@RestController
@RequestMapping(value = "log")
@AllArgsConstructor
public class LogController {

    private final LogService logService;


    /**
     * 文章访问日志列表
     */
    @ApiOperation(value = "文章浏览日志列表")
    @GetMapping(value = "articleLogs")
    public Result<PageInfo<ViewBrowseList>> articleLogs(LogQuery query) {
        List<ViewBrowseList> list = logService.listArticleLog(query);
        PageInfo<ViewBrowseList> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }

    /**
     * 操作日志
     */
    @ApiOperation(value = "操作日志")
    @GetMapping(value = "operLogs")
    public Result<PageInfo<SysOperLog>> operLogs(LogQuery query) {
        List<SysOperLog> list = logService.listOperLog(query);
        PageInfo<SysOperLog> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }

    /**
     * 登录日志
     */
    @ApiOperation(value = "登录日志")
    @GetMapping(value = "loginLogs")
    public Result<PageInfo<SysLoginLog>> loginLogs(LogQuery query) {
        List<SysLoginLog> list = logService.listLoginLog(query);
        PageInfo<SysLoginLog> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }


}
