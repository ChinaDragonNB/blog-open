package com.ak47007.model.dto;

import com.ak47007.model.SysAuthority;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AuthorityDTO {


    private SysAuthority authority;

    @ApiModelProperty(value = "选择的接口ID集合", required = true)
    private List<Long> apiIds;

}
