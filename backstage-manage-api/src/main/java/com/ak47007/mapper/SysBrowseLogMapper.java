package com.ak47007.mapper;

import com.ak47007.model.SysBrowseLog;
import com.ak47007.model.dto.BrowseRecordDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysBrowseLogMapper extends BaseMapper<SysBrowseLog> {
    /**
     * 网站日常访问量走势图
     */
    List<BrowseRecordDTO> browseCount(Long userId);

    /**
     * 网站日常访问人数走势图
     */
    List<BrowseRecordDTO> browseIp(Long userId);

    /**
     * 根据文章id查询记录
     *
     * @param articleId 文章id
     */
    List<SysBrowseLog> selectByArticleId(@Param("articleId") Long articleId);
}