package com.ak47007.service;

import java.util.List;

import com.ak47007.model.base.Result;
import com.ak47007.model.Tag;
import com.ak47007.model.query.TagQuery;
import com.ak47007.model.vo.main.TagColumnVO;

/**
 * @author AK47007
 * @date 2019/7/13
 */
public interface TagsService {


    /**
     * 标签列表
     */
    List<Tag> listTag(TagQuery query);

    /**
     * 标签信息
     */
    Tag tagInfo(Long id);

    /**
     * 添加标签
     */
    Result save(int type, Tag tag);

    /**
     * 获得总数量
     *
     * @return
     */
    int getCount(long userId);


    /**
     * 标签饼图
     * @param userId 当前登录人id
     */
    List<TagColumnVO> tagColumn(Long userId);

}

