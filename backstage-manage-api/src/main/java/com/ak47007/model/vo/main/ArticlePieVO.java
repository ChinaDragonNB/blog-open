package com.ak47007.model.vo.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AK47007
 * date 2021/4/24 21:14
 * describes: 文章饼图
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePieVO {


    private String name;

    private Integer y;

    private Boolean sliced;

    private Boolean selected;
}
