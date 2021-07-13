package com.ak47007.service.impl;

import com.ak47007.mapper.TagVOMapper;
import com.ak47007.model.vo.TagVO;
import com.ak47007.service.TagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagVOMapper tagVOMapper;

    @Override
    public List<TagVO> getTagList() {
        LambdaQueryWrapper<TagVO> wrapper = Wrappers.lambdaQuery();
        // 标签名排序
        wrapper.orderByAsc(TagVO::getTagName);
        return tagVOMapper.selectList(wrapper);
    }
}
