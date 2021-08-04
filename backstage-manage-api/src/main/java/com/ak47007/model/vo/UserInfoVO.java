package com.ak47007.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author AK47007
 * date 2021/4/23 22:45
 * describes:
 */
@Data
public class UserInfoVO {

    @ApiModelProperty(value = "博客主页")
    private String blogHome;

    @ApiModelProperty(value = "头像")
    private String loginImg;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "位置")
    private String position;


}
