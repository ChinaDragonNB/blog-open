package com.ak47007.model.dto;

import com.ak47007.model.Article;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/15 18:28
 * describes:
 */
@Data
public class ArticleDTO {

    private Article article;

    /**
     * 是否操作过标签
     */
    @ApiModelProperty(value = "是否操作过标签", required = true)
    private Boolean isCheckOks;

    /**
     * 选择的标签ID集合
     */
    @ApiModelProperty(value = "选择的标签ID集合", required = true)
    private List<Long> tagIds;

    /**
     * 是否添加过图片
     */
    @ApiModelProperty(value = "是否添加过图片", required = true)
    private Boolean isAddImage;

    /**
     * 作者ID
     */
    @ApiModelProperty(hidden = true)
    private Long userId;


}
