package com.ak47007.service;

import com.ak47007.model.vo.TagVO;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/5/20
 */
public interface TagService {

    /**
     * 获取标签数据
     */
    List<TagVO> getTagList();
}
