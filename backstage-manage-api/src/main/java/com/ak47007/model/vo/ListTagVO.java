package com.ak47007.model.vo;

import com.ak47007.model.Tag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/15 18:15
 * describes:
 */
@Data
public class ListTagVO {


    /**
     * 所有标签
     */
    @ApiModelProperty(value = "所有标签列表")
    private List<LabelValueVO> allTags;

    /**
     * 类型为单个的标签
     */
    @ApiModelProperty(value = "类型为单个的标签列表")
    private List<Tag> singleTags;


}
