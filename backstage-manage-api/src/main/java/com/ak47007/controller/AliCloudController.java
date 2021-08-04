package com.ak47007.controller;

import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.oos.AliOssService;
import com.ak47007.model.query.ObjectQuery;
import com.ak47007.model.vo.BucketVO;
import com.ak47007.model.vo.ObjectVO;
import com.ak47007.model.base.Result;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author AK47007
 * date 2020/11/14 15:58
 * describe：阿里云接口
 */
@Api(tags = "阿里云模块接口")
@RestController
@RequestMapping(value = "aliCloud")
@AllArgsConstructor
public class AliCloudController {

    private final AliOssService aliOssService;


    /**
     * 获取Bucket列表
     */
    @ApiOperation(value = "获取Bucket列表")
    @GetMapping(value = "getBucketList")
    public Result<List<BucketVO>> getBucketList() {
        List<BucketVO> bucketList = aliOssService.getBucketList();
        return Result.success(bucketList);
    }

    /**
     * 获取文件列表
     */
    @ApiOperation(value = "获取文件列表")
    @GetMapping(value = "getObjectList")
    public Result<List<ObjectVO>> getObjectList(ObjectQuery query) {
        List<ObjectVO> objectList = aliOssService.getObjectList(query);
        return Result.success(objectList);
    }

    /**
     * 删除文件
     */
    @ApiOperation(value = "删除文件")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true), @ApiImplicitParam(name = "key", value = "文件名", required = true)})
    @PostMapping(value = "deleteObject")
    @OperatorLog(operModule = "阿里云", operType = OperatorTypeEnum.DELETE, operDesc = "删除文件")
    public Result<?> deleteObject(String bucketName, String key) {
        return aliOssService.delObject(bucketName, key);
    }

    /**
     * 添加目录
     */
    @ApiOperation(value = "添加目录")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "bucketName", value = "bucket名称", required = true)
            , @ApiImplicitParam(name = "folderName", value = "目录名", required = true), @ApiImplicitParam(name = "path", value = "路径", required = true)})
    @PostMapping(value = "addFolder")
    @OperatorLog(operModule = "阿里云", operType = OperatorTypeEnum.INSERT, operDesc = "新建目录")
    public Result<?> addFolder(String bucketName, String folderName, String path) {
        return aliOssService.addFolder(bucketName, folderName, path);
    }


    /**
     * 上传文件
     *
     * @param file 文件
     * @return 结果
     */
    @ApiOperation(value = "上传文件")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "file", value = "文件信息", required = true), @ApiImplicitParam(name = "path", value = "路径", required = true)})
    @PostMapping(value = "uploadFile")
    @OperatorLog(operModule = "阿里云", operType = OperatorTypeEnum.UPLOAD, operDesc = "上传文件")
    public Result<?> uploadFile(MultipartFile file, String path) {
        return aliOssService.uploadFile(file, path);
    }

}
