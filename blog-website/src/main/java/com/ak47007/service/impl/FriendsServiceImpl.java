package com.ak47007.service.impl;

import com.ak47007.enums.LinkStatusEnum;
import com.ak47007.mapper.LinksMapper;
import com.ak47007.model.Links;
import com.ak47007.service.FriendsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class FriendsServiceImpl implements FriendsService {

    private final LinksMapper linksMapper;

    @Override
    public List<Links> getFriends() {

        LambdaQueryWrapper<Links> wrapper = Wrappers.lambdaQuery();
        // 只需要已通过的链接
        wrapper.eq(Links::getCheckStatus, LinkStatusEnum.PASS.getValue());
        wrapper.orderByAsc(new Random().nextBoolean());
        List<Links> links = linksMapper.selectList(wrapper);
        return links;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyFriend(Links links) {
        links.setCheckStatus(LinkStatusEnum.CHECKING.getValue());
        links.setUserId(1L);
        linksMapper.insert(links);
    }
}
