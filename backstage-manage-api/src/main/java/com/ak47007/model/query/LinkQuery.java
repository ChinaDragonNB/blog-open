package com.ak47007.model.query;

import com.ak47007.model.base.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LinkQuery extends BaseQuery {

    /**
     * 类型 1 已通过的链接 2 待审核的链接
     */
    @ApiModelProperty(value = "类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "网站标题")
    private String title;

    @ApiModelProperty(hidden = true)
    private Long userId;
}
