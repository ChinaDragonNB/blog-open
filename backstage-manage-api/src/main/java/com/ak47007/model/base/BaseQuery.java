package com.ak47007.model.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author AK47007
 * date 2021/5/1 16:43
 * describes:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseQuery extends BasePage {

    /**
     * 排序列名
     */
    @ApiModelProperty(value = "排序列名")
    private String columnName;

    /**
     * 排序方式
     */
    @ApiModelProperty(value = "排序方式")
    private String order;


}
