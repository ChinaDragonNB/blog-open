package com.ak47007.model.vo.main;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * date 2021/4/23 22:39
 * describes: 首页统计数量视图对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsVO {

    /**
     * 文章数量
     */
    @ApiModelProperty(value = "文章数量")
    private Integer articleNum;

    /**
     * 访问数量
     */
    @ApiModelProperty(value = "访问数量")
    private Integer viewsSum;

    /**
     * 标签数量
     */
    @ApiModelProperty(value = "标签数量")
    private Integer tagsNum;

    /**
     * 友情链接数量
     */
    @ApiModelProperty(value = "友情链接数量")
    private Integer linksNum;
}
