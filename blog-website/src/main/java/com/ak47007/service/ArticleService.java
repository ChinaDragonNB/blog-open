package com.ak47007.service;

import com.ak47007.model.query.ArticleQuery;
import com.ak47007.model.vo.ArticleVO;
import com.ak47007.model.vo.ViewArticleCard;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/5/18
 */
public interface ArticleService {

    /**
     * 文章数据
     *
     * @param pageNum 当前页
     * @param tag     查询标签
     * @param title   查询标题
     */
    ModelAndView getData(int pageNum, String tag, String title);

    /**
     * 获取文章
     *
     * @param articleQuery 参数
     */
    List<ViewArticleCard> getArticleList(ArticleQuery articleQuery);


    /**
     * 文章详情信息获取
     *
     * @param uuid uuid
     * @param ip   访问者ip地址
     */
    ArticleVO articleDetail(String uuid, String ip);

    /**
     * 获取文章信息
     *
     * @param uuid 文章唯一标识
     */
    ArticleVO getArticleInfo(String uuid);
}
