package com.ak47007.service.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ak47007.mapper.ArticleMapper;
import com.ak47007.mapper.ArticleTagMapper;
import com.ak47007.model.base.Result;
import com.ak47007.model.query.TagQuery;
import com.ak47007.model.vo.main.TagColumnVO;
import com.ak47007.service.TagsService;
import com.ak47007.utils.AssertUtil;
import com.ak47007.utils.ElementUISortUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.ak47007.mapper.TagMapper;
import com.ak47007.model.Tag;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author AK47007
 * @date 2019/7/13
 */
@Service
@AllArgsConstructor
public class TagsServiceImpl implements TagsService {

    private final TagMapper tagMapper;

    private final ArticleMapper articleMapper;

    private final ArticleTagMapper articleTagMapper;


    @Override
    public List<Tag> listTag(TagQuery query) {

        LambdaQueryWrapper<Tag> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Tag::getUserId, query.getUserId());

        String tagName = query.getTagName();
        if (StrUtil.isNotBlank(tagName)) {
            wrapper.like(Tag::getTagName, tagName);
        }

        String columnName = query.getColumnName();
        String order = query.getOrder();

        String sql = ElementUISortUtil.sortSql(columnName, order, " tag_id DESC ");
        wrapper.last(sql);

        query.startPage();
        return tagMapper.selectList(wrapper);
    }

    @Override
    public Tag tagInfo(Long id) {
        Tag tag = tagMapper.selectById(id);
        AssertUtil.notNull(tag, "查不到该标签信息");
        return tag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result save(int type, Tag tag) {
        Result resultVO = new Result();
        Long tagId = tag.getTagId();
        String tagName = tag.getTagName();
        switch (type) {
            case 1:
                if (isTagNameRepeat(tagName, null)) {
                    return Result.error("标签名称重复");
                }
                tagMapper.insert(tag);
                return Result.success("保存成功");
            case 2:
                if (isTagNameRepeat(tagName, tagId)) {
                    return Result.error("标签名称重复");
                }
                tagMapper.updateById(tag);
                return Result.success("保存成功");
            case 3:
                //找出该标签有没有被文章引用了
                int count1 = articleMapper.tagCount(tagId);
                int count2 = articleTagMapper.tagCount(tagId);
                if (count1 > 0 || count2 > 0) {
                    return Result.error("删除失败,该标签已被使用");
                }
                tagMapper.deleteById(tagId);
                return Result.success("删除成功");
            default:
                return Result.error("非法操作");
        }
    }

    @Override
    public int getCount(long userId) {
        LambdaQueryWrapper<Tag> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Tag::getUserId, userId);
        return tagMapper.selectCount(wrapper);
    }

    @Override
    public List<TagColumnVO> tagColumn(Long userId) {
        return tagMapper.tagColumn(userId);
    }


    /**
     * 标签名是否重复
     *
     * @param tagName 标签名
     * @param tagId   标签id
     */
    private Boolean isTagNameRepeat(String tagName, Long tagId) {
        LambdaQueryWrapper<Tag> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Tag::getTagName, tagName);
        if (tagId != null && tagId != 0L) {
            wrapper.eq(Tag::getTagId, tagId);
        }
        List<Tag> tags = tagMapper.selectList(wrapper);
        return !tags.isEmpty();
    }


}

