package com.ak47007.model.query;

import com.ak47007.model.base.BaseQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * date 2021/5/16 14:40
 * describes:
 */
@Data
public class TagQuery extends BaseQuery {

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(hidden = true)
    private Long userId;
}
