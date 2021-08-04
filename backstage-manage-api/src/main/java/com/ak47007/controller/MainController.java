package com.ak47007.controller;

import com.ak47007.model.SysUser;
import com.ak47007.model.WebsiteTools;
import com.ak47007.model.vo.LatelyArticleVO;
import com.ak47007.model.vo.main.*;
import com.ak47007.service.*;
import com.ak47007.utils.UserUtil;
import com.ak47007.model.base.Result;
import com.ak47007.utils.LocationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author AK47007
 * date 2019/9/25
 * describe:首页数据接口
 */
@Api(tags = "首页接口")
@Slf4j
@RequestMapping(value = "main")
@RestController
@AllArgsConstructor
public class MainController {

    private final ArticleService articleService;

    private final TagsService tagsService;

    private final LinksService linksService;

    private final BrowseRecordService browseRecordService;

    private final WebsiteToolsService websiteToolsService;

    private final UserUtil userUtil;


    /**
     * 统计各个模块的数量
     */
    @ApiOperation(value = "统计数量", notes = "统计文章数量、标签数量、友情链接数量、文章浏览数量")
    @GetMapping(value = "getStatistics")
    public Result<StatisticsVO> getNum(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        int articleNum = articleService.getCount(userId);
        int viewsSum = articleService.getSumViews(userId);
        int tagsNum = tagsService.getCount(userId);
        int linksNum = linksService.getCount(userId);
        return Result.success(new StatisticsVO(articleNum, viewsSum, tagsNum, linksNum));
    }

    /**
     * 文章饼图信息
     */
    @ApiOperation(value = "文章统计图表")
    @GetMapping(value = "articlePie")
    public Result<List<ArticlePieVO>> articlePie(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        List<ArticlePieVO> articlePieList = articleService.getArticlePie(userId);
        if (!articlePieList.isEmpty()) {
            return Result.success(articlePieList);
        } else {
            return Result.error("文章饼图没有数据,图表渲染失败");
        }
    }

    /**
     * 标签柱状图
     */
    @ApiOperation(value = "标签统计图表")
    @GetMapping(value = "tagColumn")
    public Result<List<TagColumnVO>> tagColumn(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        List<TagColumnVO> tagPieList = tagsService.tagColumn(userId);
        if (tagPieList != null && tagPieList.size() > 0) {
            return Result.success(tagPieList);
        } else {
            return Result.error("标签柱状图没有数据,图表渲染失败");
        }
    }

    /**
     * 链接折线图信息
     */
    @ApiOperation(value = "友情链接统计图表")
    @GetMapping(value = "linkLine")
    public Result<LinkLineVO> linkLine(HttpServletRequest request) throws IllegalAccessException {
        Long userId = getUser(request).getId();
        return linksService.linkLine(userId);
    }


    /**
     * 网站日常访问量走势图
     */
    @ApiOperation(value = "文章访问量统计图表")
    @GetMapping(value = "browseCharts")
    public Result<BrowseRecordVO> browseCharts(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        BrowseRecordVO browserRecord = browseRecordService.browseCharts(userId);
        return Result.success(browserRecord);
    }

    /**
     * 查询最近的文章
     */
    @ApiOperation(value = "最近文章")
    @GetMapping(value = "getLatelyArticle")
    public Result<List<LatelyArticleVO>> getLatelyArticle(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        List<LatelyArticleVO> list = articleService.getLatelyArticle(userId);
        return Result.success(list);
    }


    /**
     * 查询实用网站
     */
    @ApiOperation(value = "实用网站列表")
    @GetMapping(value = "getWebsiteTools")
    public List<WebsiteTools> getWebsiteTools() {
        return websiteToolsService.getWebsiteTools();
    }


    private SysUser getUser(HttpServletRequest request) {
        return userUtil.getUser(request);
    }

}
