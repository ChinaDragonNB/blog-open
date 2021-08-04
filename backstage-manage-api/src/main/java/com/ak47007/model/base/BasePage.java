package com.ak47007.model.base;

import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * date 2021/4/26 19:17
 * describes:
 */
@Data
public class BasePage {

    @ApiModelProperty(value = "当前页 默认为第一页")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页显示的数量 默认显示10条数据")
    private Integer pageSize = 10;

    public void startPage() {
        PageHelper.startPage(this.getPageNum(), this.getPageSize());
    }


}
