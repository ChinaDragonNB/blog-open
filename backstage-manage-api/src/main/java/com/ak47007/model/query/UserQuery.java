package com.ak47007.model.query;

import com.ak47007.model.base.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AK47007
 * date 2021/5/1 17:50
 * describes:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQuery extends BaseQuery {

    @ApiModelProperty(value = "昵称")
    private String nickName;

}
