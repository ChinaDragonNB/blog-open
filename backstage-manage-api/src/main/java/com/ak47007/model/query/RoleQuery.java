package com.ak47007.model.query;

import com.ak47007.model.base.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AK47007
 * date 2021/4/26 19:12
 * describes:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQuery extends BaseQuery {

    @ApiModelProperty(value = "角色中文名")
    private String roleName;
}
