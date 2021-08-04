package com.ak47007.service;

import java.util.List;

import com.ak47007.model.Link;
import com.ak47007.model.base.Result;
import com.ak47007.model.query.LinkQuery;
import com.ak47007.model.vo.main.LinkLineVO;

/**
 * @author AK47007
 * @date 2019/7/13
 */
public interface LinksService {


    /**
     * 链接列表
     *
     * @param query 查询参数
     */
    List<Link> listLink(LinkQuery query);

    /**
     * 链接信息
     */
    Link linkInfo(Long id);

    /**
     * 编辑链接
     */
    Result editLink(Link link);

    /**
     * 删除链接
     */
    Result delLink(Long id);

    /**
     * 获得总数量
     */
    int getCount(long userId);

    /**
     * 审核链接
     *
     * @param linksId 链接ID
     * @param result  审核结果
     */
    Result checkLink(Long linksId, int result) throws Exception;

    /**
     * 折线图
     *
     * @param userId 当前登录用户id
     */
    Result<LinkLineVO> linkLine(Long userId) throws IllegalAccessException;

}


