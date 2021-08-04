package com.ak47007.mapper;

import com.ak47007.model.SysRouter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/4 17:09
 * describes:
 */
public interface SysRouterMapper extends BaseMapper<SysRouter> {
    List<SysRouter> findByParentId(@Param("parentId") Long parentId);

    /**
     * 获取目前排序最高的序号
     */
    int getMaxOrderIndex();

    /**
     * 更改排序序号
     *
     * @param updatedOrderIndex 序号
     * @param id                id
     */
    int updateOrderIndexById(@Param("updatedOrderIndex") Integer updatedOrderIndex, @Param("id") Long id);


}