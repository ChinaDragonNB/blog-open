package com.ak47007.model.vo.main;

import com.ak47007.model.dto.LinkLineDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author AK47007
 * date 2021/4/24 21:26
 * describes:
 */
@Data
@AllArgsConstructor
public class LinkLineVO {

    /**
     * 通过的友情链接数量
     */
    @ApiModelProperty(value = "通过的友情链接数量")
    private List<Integer> pass;

    /**
     * 未通过的友情链接数量
     */
    @ApiModelProperty(value = "未通过的友情链接数量")
    private List<Integer> noPass;


    @ApiModelProperty(value = "网站标题")
    private String title;
}
