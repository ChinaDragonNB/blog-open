package com.ak47007.oos;

import com.ak47007.model.query.ObjectQuery;
import com.ak47007.model.vo.BucketVO;
import com.ak47007.model.vo.ObjectVO;
import com.ak47007.model.base.Result;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

/**
 * @author ak47007
 * @date 2020/5/24
 * 描述：
 */
public interface AliOssService {

    /**
     * @param prefix 前缀
     * @param file   文件
     * @return 上传后的url
     * @throws IOException 异常
     */
    String uploadImage(String prefix, MultipartFile file) throws IOException;


    /**
     * 获取bucket列表
     *
     * @return 数据集合
     */
    List<BucketVO> getBucketList();

    /**
     * 获取bucket信息
     *
     * @param bucketName bucket名称
     * @return 信息
     */
    BucketVO getBucketInfo(String bucketName);


    /**
     * 获取对象(文件/文件夹)集合
     *
     * @param query 参数对象
     * @return 集合
     */
    List<ObjectVO> getObjectList(ObjectQuery query);

    /**
     * 删除对象
     * @param bucketName bucket名称
     * @param key 文件名
     * @return 结果
     */
    Result<?> delObject(String bucketName, String key);

    /**
     * 添加目录
     * @param bucketName bucket名称
     * @param folderName 目录名
     * @param path 当前路径
     * @return 结果
     */
    Result<?>  addFolder(String bucketName, String folderName, String path);


    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 当前文件夹路径
     * @return 结果
     */
    Result<?>  uploadFile(MultipartFile file, String path);

}
