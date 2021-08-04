package com.ak47007.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/5 17:29
 * describes:
 */
@Data
public class AuthorityInfoVO {

    @ApiModelProperty(value = "权限英文名")
    private String authorityNameEn;

    @ApiModelProperty(value = "权限中文名")
    private String authorityNameCn;

    @ApiModelProperty(value = "状态")
    private Boolean state;

    /**
     * 该权限拥有的接口
     */
    @ApiModelProperty(value = "该权限拥有的接口ID集合")
    private List<Long> apiIds;
}
