package com.ak47007.model.vo.main;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * date 2021/4/24 21:19
 * describes: 标签柱状图
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagColumnVO {


    @ApiModelProperty(value = "标签名称")
    private String name;


    @ApiModelProperty(value = "数量")
    private Integer y;
}
