package com.ak47007.oos;

import com.ak47007.constant.UploadPrefixConstant;
import com.ak47007.enums.StorageClassEnum;
import com.ak47007.model.query.ObjectQuery;
import com.ak47007.model.vo.BucketVO;
import com.ak47007.model.vo.ObjectVO;
import com.ak47007.model.base.Result;
import com.ak47007.utils.AliOssUtil;
import com.ak47007.utils.FileUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ak47007
 * @date 2020/5/24
 * 描述：
 */
@Service
@Slf4j
public class AliOssServiceImpl implements AliOssService {

    @Resource
    private AliOssUtil aliOssUtil;


    /**
     * @param prefix 前缀
     * @param file   文件
     * @return 上传后的url
     * @throws IOException 异常
     */
    @Override
    public String uploadImage(String prefix, MultipartFile file) throws IOException {
        if (file == null) {
            throw new IOException("文件不存在");
        }
        // 文件名
        StringBuilder fileName = new StringBuilder(Objects.requireNonNull(file.getOriginalFilename()));
        StringBuilder folderName = new StringBuilder();
        if (UploadPrefixConstant.MARKDOWN.equals(prefix)) {
            //在写文章的时候上传图片时的操作
            //拿到该文件的后缀
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            //修改其名字
            fileName = new StringBuilder("image_").append(System.currentTimeMillis()).append(suffix);
        }
        folderName.append("images/").append(prefix).append("/");
        fileName.insert(0, folderName.toString());
        // 调用上传文件方法
        return aliOssUtil.uploadFile(fileName.toString(), file);
    }

    @Override
    public List<BucketVO> getBucketList() {
        // 列举存储空间。
        List<BucketVO> buckets = new ArrayList<>();
        // oss实例
        OSS ossClient = null;
        try {
            ossClient = aliOssUtil.getOssClient();
            buckets = ossClient.listBuckets().stream().map(l -> {
                BucketVO vo = new BucketVO();
                String storageClass = l.getStorageClass().name();
                vo.setStorageType(getStorageType(storageClass));
                vo.setBucketName(l.getName());
                vo.setCreateDate(l.getCreationDate());
                BeanUtils.copyProperties(l, vo);
                return vo;
            }).collect(Collectors.toList());
        } catch (OSSException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return buckets;
    }

    @Override
    public BucketVO getBucketInfo(String bucketName) {
        OSS ossClient = null;
        BucketInfo bucketInfo = null;
        try {
            ossClient = aliOssUtil.getOssClient();
            bucketInfo = ossClient.getBucketInfo(bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        if (bucketInfo != null) {
            BucketVO vo = new BucketVO();
            Bucket bucket = bucketInfo.getBucket();
            vo.setBucketName(bucketName);
            vo.setCreateDate(bucket.getCreationDate());
            vo.setStorageType(bucket.getStorageClass().name());
            BeanUtils.copyProperties(bucket, vo);
            return vo;
        }
        return null;
    }

    @Override
    public List<ObjectVO> getObjectList(ObjectQuery query) {
        List<ObjectVO> list = new ArrayList<>();

        String bucketName = query.getBucketName();
        String path = query.getPath();

        // 先获取bucket信息,因为某些bucket地域不同,需要重新实例化OSS实例
        BucketVO bucketInfo = getBucketInfo(bucketName);
        if (bucketInfo != null) {
            String endpoint = bucketInfo.getExtranetEndpoint();
            OSS ossClient = null;
            try {
                ossClient = aliOssUtil.getOssClient(endpoint);
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
                listObjectsRequest.setDelimiter("/");
                listObjectsRequest.setBucketName(bucketName);
                listObjectsRequest.setPrefix(path);
                ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
                List<String> commonPrefixes = objectListing.getCommonPrefixes();
                commonPrefixes.forEach(l -> {
                    ObjectVO objectVO = new ObjectVO();
                    // 文件夹名
                    String filter = l.substring(0, l.length() - 1);
                    String name = filter.substring(filter.lastIndexOf("/") + 1);
                    objectVO.setName(name);
                    objectVO.setPath(l);
                    objectVO.setIsDir(Boolean.TRUE);
                    objectVO.setIconType("file_folder");
                    list.add(objectVO);
                });
                List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();

                if (bucketName.equals(aliOssUtil.getBucketName())) {
                    endpoint = aliOssUtil.getCustomEndpoint();
                }

                String finalEndpoint = endpoint;
                objectSummaries.stream().filter(l -> !l.getKey().equals(path)).forEach(l -> {
                    ObjectVO objectVO = new ObjectVO();
                    // 文件路径
                    String key = l.getKey();
                    // 文件
                    String name = key.substring(key.lastIndexOf("/") + 1);
                    // 文件大小
                    long size = l.getSize();
                    objectVO.setName(name);
                    objectVO.setPath(key);
                    objectVO.setLastModified(l.getLastModified());
                    objectVO.setSize(size);
                    objectVO.setStorageType(getStorageType(l.getStorageClass()));
                    objectVO.setIsDir(Boolean.FALSE);
                    objectVO.setFormattedSize(FileUtil.getPrintSize(size));
                    objectVO.setUrl("//" + finalEndpoint + "/" + key);
                    objectVO.setIconType(getIconType(name));

                    list.add(objectVO);
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
        return list;
    }

    private String getIconType(String name) {
        // 文件后缀
        String fileType = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        String[] picType = {"bmp", "jpg", "png", "tif", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "WMF", "webp"};
        List<String> strings = Arrays.asList(picType);
        if (strings.contains(fileType)) {
            return "file_pic";
        }
        String[] videoType = {"mp4"};
        strings = Arrays.asList(videoType);
        if (strings.contains(fileType)) {
            return "file_video";
        }
        switch (fileType) {
            case "html":
                return "file_html";
            case "css":
                return "file_css";
            case "js":
                return "file_js";
        }
        return null;
    }

    @Override
    public Result delObject(String bucketName, String key) {
        if (Strings.isBlank(bucketName)) {
            return Result.error("未选择bucket");
        }
        if (Strings.isBlank(key)) {
            return Result.error("未找到该文件");
        }
        BucketVO bucketInfo = getBucketInfo(bucketName);
        if (bucketInfo != null) {
            String endpoint = bucketInfo.getExtranetEndpoint();
            OSS ossClient = null;
            try {
                ossClient = aliOssUtil.getOssClient(endpoint);
                boolean exist = ossClient.doesObjectExist(bucketName, key);
                if (!exist) {
                    return Result.error("删除失败,该文件不存在");
                }
                ossClient.deleteObject(bucketName, key);
                return Result.success("删除成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
        return Result.error("删除失败,请刷新后重试");
    }

    @Override
    public Result addFolder(String bucketName, String folderName, String path) {
        if (Strings.isBlank(path)) {
            path = "";
        }
        if (Strings.isBlank(folderName)) {
            return Result.error("请输入目录名");
        }
        BucketVO bucketInfo = getBucketInfo(bucketName);
        if (bucketInfo != null) {
            String endpoint = bucketInfo.getExtranetEndpoint();
            OSS ossClient = null;
            try {
                ossClient = aliOssUtil.getOssClient(endpoint);
                // 上传一个文件到新目录然后将文件删除
                // 创建PutObjectRequest对象。
                String content = "";
                // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
                String objectName = folderName + "/";
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path + objectName, new ByteArrayInputStream(content.getBytes()));
                // 上传字符串。
                ossClient.putObject(putObjectRequest);
                // 检查是否创建完成
                boolean exist = ossClient.doesObjectExist(bucketName, objectName, true);
                if (exist) {
                    // 创建完成后删除文件,保留目录
                    // ossClient.deleteObject(bucketName, objectName);
                }
                return Result.success("创建目录成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
        return Result.error("创建目录失败,请刷新后重试");
    }


    @Override
    public Result uploadFile(MultipartFile file, String path) {
        if (Strings.isBlank(path) || path.equalsIgnoreCase("null")) {
            path = "";
        }
        String fileName = file.getOriginalFilename();
        try {
            aliOssUtil.uploadFile(path + fileName, file);
            return Result.success("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.error("上传失败");
    }

    /**
     * 获得存储类型中文名称
     *
     * @param storageClass 存储类型英文代码
     * @return 中文名称
     */
    private String getStorageType(String storageClass) {
        if (storageClass.equals(StorageClassEnum.STANDARD.getEn())) {
            return StorageClassEnum.STANDARD.getCn();
        } else if (storageClass.equals(StorageClassEnum.INFREQUENT_ACCESS_1.getEn())) {
            return StorageClassEnum.INFREQUENT_ACCESS_1.getCn();
        } else if (storageClass.equals(StorageClassEnum.INFREQUENT_ACCESS_2.getEn())) {
            return StorageClassEnum.INFREQUENT_ACCESS_2.getCn();
        } else if (storageClass.equals(StorageClassEnum.COLD_ARCHIVE.getEn())) {
            return StorageClassEnum.COLD_ARCHIVE.getCn();
        }
        return "";
    }
}
