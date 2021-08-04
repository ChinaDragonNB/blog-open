package com.ak47007.service;

import java.util.List;

import com.ak47007.model.Article;
import com.ak47007.model.SysUser;
import com.ak47007.model.dto.ArticleDTO;
import com.ak47007.model.query.ArticleQuery;
import com.ak47007.model.vo.LatelyArticleVO;
import com.ak47007.model.base.Result;
import com.ak47007.model.vo.ViewArticleList;
import com.ak47007.model.vo.main.ArticlePieVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author AK47007
 * @date 2019/7/13
 */
public interface ArticleService {

    /**
     * 文章列表
     *
     * @param query 参数对象
     */
    List<ViewArticleList> list(ArticleQuery query);

    /**
     * 文章信息
     *
     * @param id id
     */
    Article getInfo(Long id);

    /**
     * 保存文章
     *
     * @param type 保存类型 1新增  2编辑
     * @param dto  数据对象
     */
    Result<?> save(int type, ArticleDTO dto);


    /**
     * 获得数量
     *
     * @param userId 用户ID
     */
    int getCount(long userId);

    /**
     * 获得总访问数
     *
     * @param userId 用户ID
     */
    Integer getSumViews(long userId);

    /**
     * 文章饼图
     * @param userId 当前登陆用户id
     */
    List<ArticlePieVO> getArticlePie(Long userId);


    /**
     * 删除文章
     *
     * @param articleId 文章ID
     */
    Result<?> deleteArticle(long articleId, long userId);

    /**
     * 上传MarkDown文件并解析内容保存文章
     *
     * @param file 文件
     * @param user 当前登录人
     */
    Result<?> uploadMarkDown(MultipartFile file, SysUser user);


    /**
     * 替换文章内容
     *
     * @param articleId 文章Id
     * @param user      当前登录人
     */
    Result<?> replaceArticleContent(Long articleId, SysUser user);


    /**
     * 查询最近的文章
     * @param userId 当前登陆用户ID
     */
    List<LatelyArticleVO> getLatelyArticle(Long userId);


}




