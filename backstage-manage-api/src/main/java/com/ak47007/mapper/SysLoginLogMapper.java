package com.ak47007.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.ak47007.model.SysLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author AK47007
 * @date 2020/6/5
 * Describe:
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    List<SysLoginLog> findAll();


}