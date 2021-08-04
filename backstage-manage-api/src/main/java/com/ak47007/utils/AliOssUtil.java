package com.ak47007.utils;

import com.aliyun.oss.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ak47007
 * @date 2020/5/24
 * 描述：
 * ConfigurationProperties 注解需要引入依赖
 * <dependency>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-configuration-processor</artifactId>
 * <optional>true</optional>
 * </dependency>
 */
@ConfigurationProperties(prefix = "ali-oss")
@Component
@Data
public class AliOssUtil {

    /**
     * 默认地域节点
     */
    private String defaultEndpoint;

    /**
     * 自定义地域节点
     */
    private String customEndpoint;

    /**
     * 访问ID
     */
    private String accessKeyId;

    /**
     * 访问密钥
     */
    private String accessKeySecret;

    /**
     * 存储空间
     */
    private String bucketName;


    /**
     * 获得OSSClient实例
     *
     * @return OSS实例对象
     */
    public OSS getOssClient() {
        return new OSSClientBuilder().build(defaultEndpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 获得OSSClient实例
     *
     * @param endpoint 地域节点
     * @return OSS实例对象
     */
    public OSS getOssClient(String endpoint) {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 获得OSSClient实例
     *
     * @param conf 配置
     * @return OSS实例对象
     */
    public OSS getOssClient(ClientBuilderConfiguration conf) {
        return new OSSClientBuilder().build(defaultEndpoint, accessKeyId, accessKeySecret, conf);
    }


    /**
     * 以文件流上传文件
     *
     * @param file 文件
     * @return 返回文件链接
     */
    public String uploadFile(String fileName, MultipartFile file) throws IOException {
        // 文件不存在
        if (file == null) {
            return null;
        }
        OSS ossClient = null;
        InputStream inputStream = null;
        try {
            StringBuilder sb = new StringBuilder(fileName);
            //创建ClientConfiguration实例，按照您的需要修改默认参数。
            ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
            // 开启支持CNAME。CNAME是指将自定义域名绑定到存储空间上。
            conf.setSupportCname(true);
            // 创建OSSClient实例。
            ossClient = getOssClient(conf);
            // 上传内容到指定的存储空间（bucketName）并存放到目录 folderName中
            // 文件输入流
            inputStream = file.getInputStream();
            ossClient.putObject(bucketName, sb.toString(), inputStream);
            // 链接中加上两//,支持http与https
            sb.insert(0, "//");
            // 带上自定义域名
            sb.insert(2, customEndpoint);
            // 域名后面加 /
            sb.insert(customEndpoint.length() + 2, "/");
            return sb.toString();
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
            // 关闭文件流
            inputStream.close();
        }
        return null;

    }


}
