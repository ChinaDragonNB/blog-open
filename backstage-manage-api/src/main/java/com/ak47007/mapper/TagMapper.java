package com.ak47007.mapper;

import com.ak47007.model.Tag;
import com.ak47007.model.vo.main.TagColumnVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 获得总数量
     */
    int getCount(long userId);

    /**
     * 标签饼图
     */
    List<TagColumnVO> tagColumn(@Param("userId") Long userId);

}