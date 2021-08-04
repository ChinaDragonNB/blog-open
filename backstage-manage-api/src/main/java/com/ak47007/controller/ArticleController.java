package com.ak47007.controller;

import cn.hutool.core.util.StrUtil;
import com.ak47007.annotation.OperatorLog;
import com.ak47007.constant.SortConstant;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.model.Article;
import com.ak47007.model.ArticleContent;
import com.ak47007.model.SysUser;
import com.ak47007.model.Tag;
import com.ak47007.model.dto.ArticleDTO;
import com.ak47007.model.query.ArticleQuery;
import com.ak47007.model.query.TagQuery;
import com.ak47007.model.vo.LabelValueVO;
import com.ak47007.model.vo.ListTagVO;
import com.ak47007.model.vo.ViewArticleList;
import com.ak47007.oos.AliOssService;
import com.ak47007.service.*;
import com.ak47007.utils.*;
import com.ak47007.model.base.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author AK47007
 * @date 2019/7/13
 */
@Api(tags = "文章模块接口")
@RestController
@RequestMapping(value = "article")
@Slf4j
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final TagsService tagsService;

    private final ArticleTagService articleTagService;

    private final UserUtil userUtil;

    private final ArticleContentService articleContentService;

    private final AliOssService aliOssService;

    /**
     * 文章列表
     */
    @ApiOperation(value = "文章列表")
    @PostMapping(value = "listArticle")
    public Result<PageInfo<ViewArticleList>> listArticle(@ApiParam @RequestBody ArticleQuery query, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        query.setUserId(user.getId());
        List<ViewArticleList> list = articleService.list(query);
        PageInfo<ViewArticleList> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }

    /**
     * 文章信息
     */
    @ApiOperation(value = "文章信息")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true)
    @GetMapping(value = "articleInfo")
    public Result<Article> articleInfo(@RequestParam(value = "id") Long id) {
        Article article = articleService.getInfo(id);
        return Result.success(article);
    }

    /**
     * 写文章
     */
    @ApiOperation(value = "写文章")
    @PostMapping(value = "writeArticle")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.INSERT, operDesc = "写文章")
    public Result<?> writeArticle(@ApiParam @RequestBody ArticleDTO dto, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        dto.setUserId(user.getId());
        return articleService.save(1, dto);
    }

    /**
     * 编辑文章
     */
    @ApiOperation(value = "编辑文章")
    @PostMapping(value = "editArticle")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.EDIT, operDesc = "编辑文章")
    public Result<?> editArticle(@ApiParam @RequestBody ArticleDTO dto, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        dto.setUserId(user.getId());
        return articleService.save(2, dto);
    }

    /**
     * 删除文章
     */
    @ApiOperation(value = "删除文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true)
    @PostMapping(value = "delArticle")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.DELETE, operDesc = "删除文章")
    public Result<?> delArticle(@RequestParam(value = "id") Long id, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        return articleService.deleteArticle(id, user.getId());
    }


    /**
     * 查询标签
     */
    @ApiOperation(value = "标签列表")
    @GetMapping(value = "listTag")
    public Result<ListTagVO> listTag(HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);

        TagQuery query = new TagQuery();
        query.setColumnName(Tag.COL_TAG_NAME);
        query.setOrder(SortConstant.ASC);
        query.setUserId(user.getId());
        query.setPageSize(9999);


        // 根据参数查询数据
        List<Tag> tagList = tagsService.listTag(query);

        AssertUtil.notEmpty(tagList, "您还没有添加标签,请添加标签后再来写文章吧!");

        // 找出标签类型为单个的标签
        List<Tag> singleTags = tagList.stream().filter(tags -> tags.getTagType() == 1).collect(Collectors.toList());
        // 找出全部标签,并给他们转换一下
        List<LabelValueVO> allTags = tagList.stream().map(tags -> {
            LabelValueVO vo = new LabelValueVO();
            vo.setLabel(tags.getTagName());
            vo.setValue(tags.getTagId());
            return vo;
        }).collect(Collectors.toList());


        ListTagVO vo = new ListTagVO();
        vo.setAllTags(allTags);
        vo.setSingleTags(singleTags);

        return Result.success(vo);
    }

    /**
     * 文章拥有的标签
     */
    @ApiOperation(value = "文章拥有的标签", notes = "文章默认选中的标签")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true)
    @GetMapping(value = "selectedTags")
    public Result<List<Long>> selectedTags(@RequestParam(value = "id") Long id) {
        List<Long> tagList = articleTagService.haveTags(id);
        return Result.success(tagList);
    }

    /**
     * 替换文章内容
     */
    @ApiOperation(value = "替换文章内容")
    @ApiImplicitParam(name = "articleId", value = "文章ID", required = true)
    @PostMapping(value = "replaceArticleContent")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.IMPORT, operDesc = "导入文章,替换文章内容")
    public Result<?> replaceArticleContent(@RequestParam Long articleId, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        return articleService.replaceArticleContent(articleId, user);
    }


    /**
     * 上传封面
     */
    @ApiOperation(value = "上传封面")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "image", value = "文件图片信息", required = true)
            , @ApiImplicitParam(name = "uploadPrefix", value = "上传的路径", required = true)})
    @PostMapping(value = "uploadCover")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.UPLOAD, operDesc = "上传文章封面")
    public Result<?> uploadCover(@RequestParam(value = "image", required = false) MultipartFile file,
                                 String uploadPrefix) throws IOException {
        return uploadImage(file, uploadPrefix);
    }

    /**
     * 上传文章图片
     */
    @ApiOperation(value = "上传文章图片", notes = "文章内容里面的图片")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "image", value = "文件图片信息", required = true)
            , @ApiImplicitParam(name = "uploadPrefix", value = "上传的路径", required = true)})
    @PostMapping(value = "uploadImg")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.UPLOAD, operDesc = "上传文章内容图片")
    public Result<?> uploadImg(@RequestParam(value = "image", required = false) MultipartFile file,
                               String uploadPrefix) throws IOException {
        return uploadImage(file, uploadPrefix);
    }

    private Result<?> uploadImage(@RequestParam(value = "image", required = false) MultipartFile file, String uploadPrefix) throws IOException {
        String url = aliOssService.uploadImage(uploadPrefix, file);
        if (StrUtil.isBlank(url)) {
            return Result.error("上传失败");
        }
        return Result.success("上传成功", url);
    }


    /**
     * 导出文章至md
     */
    @ApiOperation(value = "导出文章", notes = "导出md文件,在查看文章页面才能导出")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true)
    @GetMapping(value = "exportMarkDown")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.EXPORT, operDesc = "导出文章")
    public void exportMarkDown(@RequestParam long id, HttpServletResponse response) {
        Article article = articleService.getInfo(id);
        try {
            DownloadUtil.downloadFile(article.getTitle(), ".md", articleContentService.getArticleContentById(id), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出所有文章
     */
    @ApiOperation(value = "导出所有文章")
    @GetMapping(value = "exportAll")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.EXPORT, operDesc = "导出所有文章")
    public void exportAll(HttpServletResponse response, HttpServletRequest request) throws FileNotFoundException {
        // 文件格式
        String fileType = ".md";

        SysUser user = userUtil.getUser(request);
        ArticleQuery query = new ArticleQuery();
        query.setPageSize(999);
        query.setUserId(user.getId());
        List<ViewArticleList> articleList = articleService.list(query);
        // 文章内容
        List<ArticleContent> list = articleContentService.list();
        List<Map<String, String>> fileInfos = articleList.stream().map(article -> {
            Map<String, String> map = new HashMap<>(2);
            map.put("fileName", article.getTitle() + fileType);
            ArticleContent articleContent = list.stream().filter(content -> content.getArticleId().equals(article.getId()))
                    .findFirst().orElse(new ArticleContent());
            map.put("fileContent", articleContent.getArticleContent());
            return map;
        }).collect(Collectors.toList());
        DownloadUtil.downloadZip(fileInfos, "全部文章", response);
    }

    /**
     * 导入文章
     */
    @ApiOperation(value = "导入文章")
    @ApiImplicitParam(name = "file", value = "文件信息", required = true)
    @PostMapping(value = "uploadMarkDown")
    @OperatorLog(operModule = "文章管理", operType = OperatorTypeEnum.IMPORT, operDesc = "导入文章")
    public Result<?> uploadMarkdown(MultipartFile file, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        return articleService.uploadMarkDown(file, user);
    }


}
