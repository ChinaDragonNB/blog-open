package com.ak47007.mapper;

import com.ak47007.model.SysAuthorityApi;
import com.ak47007.model.vo.AuthorityApiVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author AK47007
 * date 2021/5/1 17:03
 * describes:
 */
public interface SysAuthorityApiMapper extends BaseMapper<SysAuthorityApi> {

    /**
     * 根据权限id查询
     *
     * @param authorityId 权限id
     */
    List<AuthorityApiVO> findByAuthorityId(@Param("authorityId") Long authorityId);

    /**
     * 查询全部
     */
    List<AuthorityApiVO> findAll();

    /**
     * 根据权限id删除数据
     *
     * @param authorityId 权限id
     */
    int deleteByAuthorityId(@Param("authorityId") Long authorityId);


}