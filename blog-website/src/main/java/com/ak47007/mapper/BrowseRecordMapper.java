package com.ak47007.mapper;

import org.apache.ibatis.annotations.Param;

import com.ak47007.model.BrowseRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/7/10
 * Describe:
 */
public interface BrowseRecordMapper extends BaseMapper<BrowseRecord> {


    /**
     * 查询访问过自己网站的IP
     */
    List<String> findIp();

    /**
     * 根据ip修改该IP地理位置
     */
    int updateIpLocationByBrowseIp(@Param("updatedIpLocation") String updatedIpLocation, @Param("browseIp") String browseIp);


}