package com.ak47007.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * date 2021/5/15 18:17
 * describes:
 */
@Data
public class LabelValueVO {

    @ApiModelProperty(value = "显示名称")
    private String label;

    @ApiModelProperty(value = "值")
    private Object value;
}
