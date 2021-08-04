package com.ak47007.mapper;

import com.ak47007.model.Link;
import com.ak47007.model.dto.LinkLineDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LinkMapper extends BaseMapper<Link> {

    /**
     * 获得总数量
     */
    int getCount(@Param("userId") long userId);

    /**
     * 折线图
     */
    LinkLineDTO linkLine(@Param("status") int status,@Param("userId") long userId);
}