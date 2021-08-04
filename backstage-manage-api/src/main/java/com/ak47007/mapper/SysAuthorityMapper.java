package com.ak47007.mapper;

import com.ak47007.model.SysAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface SysAuthorityMapper extends BaseMapper<SysAuthority> {


    /**
     * 获取最大排序号
     *
     * @param parentId 父级ID
     */
    Integer getMaxOrder(@Param("parentId") Long parentId);
}