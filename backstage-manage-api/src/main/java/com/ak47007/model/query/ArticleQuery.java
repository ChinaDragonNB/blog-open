package com.ak47007.model.query;

import com.ak47007.model.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

/**
 * @author AK47007
 * date  2021/1/3 16:42
 * describe：
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQuery extends BaseQuery {

    /**
     * 文章作者
     */
    @ApiModelProperty(hidden = true)
    private Long userId;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "文章发布开始时间")
    private LocalDate publishTimeStart;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "文章发布结束时间")
    private LocalDate publishTimeEnd;

}
