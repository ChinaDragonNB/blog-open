package com.ak47007.controller;

import cn.hutool.core.util.StrUtil;
import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.model.SysUser;
import com.ak47007.model.query.TagQuery;
import com.ak47007.oos.AliOssService;
import com.ak47007.utils.UserUtil;
import com.ak47007.model.base.Result;
import com.ak47007.model.Tag;
import com.ak47007.service.TagsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author AK47007
 * @date 2019/7/13
 */
@Api(tags = "标签模块接口")
@RestController
@RequestMapping(value = "tags")
@AllArgsConstructor
public class TagsController {

    private final TagsService tagsService;

    private final UserUtil userUtil;

    private final AliOssService aliOssService;


    /**
     * 标签列表
     */
    @ApiOperation(value = "标签列表")
    @PostMapping(value = "listTag")
    public Result<PageInfo<Tag>> listTag(@ApiParam @RequestBody TagQuery query, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        query.setUserId(user.getId());
        List<Tag> tags = tagsService.listTag(query);
        PageInfo<Tag> pageInfo = new PageInfo<>(tags);
        return Result.success(pageInfo);
    }


    /**
     * 标签信息
     */
    @ApiOperation(value = "标签列表")
    @ApiImplicitParam(name = "id", value = "标签ID", required = true)
    @GetMapping(value = "tagInfo")
    public Result<Tag> tagInfo(@RequestParam(value = "id") Long id) {
        return Result.success(tagsService.tagInfo(id));
    }

    /**
     * 添加标签
     */
    @ApiOperation(value = "标签列表")
    @PostMapping(value = "addTag")
    @OperatorLog(operModule = "标签管理", operType = OperatorTypeEnum.INSERT, operDesc = "添加新标签")
    public Result<?> addTag(@ApiParam @RequestBody Tag tag, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        tag.setUserId(user.getId());
        return tagsService.save(1, tag);
    }

    /**
     * 编辑标签
     */
    @ApiOperation(value = "编辑标签")
    @PostMapping(value = "editTag")
    @OperatorLog(operModule = "标签管理", operType = OperatorTypeEnum.EDIT, operDesc = "编辑标签信息")
    public Result<?> editTag(@ApiParam @RequestBody Tag tag, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        tag.setUserId(user.getId());
        return tagsService.save(2, tag);
    }

    /**
     * 删除标签
     */
    @ApiOperation(value = "删除标签")
    @ApiImplicitParam(name = "tagId", value = "标签ID", required = true)
    @PostMapping(value = "delTag")
    @OperatorLog(operModule = "标签管理", operType = OperatorTypeEnum.DELETE, operDesc = "删除标签")
    public Result<?> delTag(@RequestParam(value = "tagId") Long tagId) {
        Tag tag = new Tag();
        tag.setTagId(tagId);
        return tagsService.save(3, tag);
    }


    /**
     * 上传标签logo
     */
    @ApiOperation(value = "上传标签logo")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "image", value = "logo文件信息", required = true),
            @ApiImplicitParam(name = "uploadPrefix", value = "上传路径", required = true)
    })
    @PostMapping(value = "uploadLogo")
    @OperatorLog(operModule = "标签管理", operType = OperatorTypeEnum.UPLOAD, operDesc = "上传标签logo")
    public Result<?> uploadImage(@RequestParam(value = "image", required = false) MultipartFile file, String uploadPrefix) throws IOException {
        String url = aliOssService.uploadImage(uploadPrefix, file);
        if (StrUtil.isBlank(url)) {
            return Result.error("上传失败");
        }
        return Result.success("上传成功", url);
    }

}
