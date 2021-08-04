package com.ak47007.mapper;

import com.ak47007.model.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;import org.apache.ibatis.annotations.Param;import java.util.List;

public interface SysDictMapper extends BaseMapper<SysDict> {
    /**
     * 根据字典码查询字典子项,不包括目录项
     *
     * @param dictCode 字典码
     */
    List<SysDict> getItemsByCode(@Param("dictCode") String dictCode);
}