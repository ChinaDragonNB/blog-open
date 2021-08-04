package com.ak47007.mapper;

import com.ak47007.model.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ak47007
 * @date 2020/5/23
 * 描述：
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    /**
     * 根据文章ID删除
     *
     * @param articleId 文章ID
     */
    int deleteByArticleId(@Param("articleId") Long articleId);


    /**
     * 查询该标签的数量
     *
     * @param tagId 标签ID
     */
    int tagCount(Long tagId);

    /**
     * 根据文章ID查询对应的标签ID
     *
     * @param articleId 文章ID
     */
    List<Long> findTagByArticleId(@Param("articleId") Long articleId);

    /**
     * 获取最大ID
     *
     * @return 结果数
     */
    int getMaxId();

    /**
     * 删除未选择的文章标签
     *
     * @param articleId 文章ID
     * @param tagIdList 不删除的文章标签
     */
    void deleteBatch(@Param("articleId") long articleId, @Param("tagIdList") List tagIdList);
}