package com.ak47007.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * @date 2020/11/14 22:40
 * describe：对象(文件/文件夹) 参数对象
 */
@Data
public class ObjectQuery {

    @ApiModelProperty(value = "bucket名称", required = true)
    private String bucketName;

    @ApiModelProperty(value = "路径", required = true)
    private String path;

}
