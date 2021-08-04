package com.ak47007.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import com.ak47007.mapper.SysApiMapper;
import com.ak47007.model.SysApi;
import com.ak47007.service.ApiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApiServiceImpl implements ApiService {

    private final SysApiMapper sysApiMapper;


    @Override
    public List<SysApi> apiList() {
        LambdaQueryWrapper<SysApi> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(SysApi::getOrder);
        List<SysApi> sysApis = sysApiMapper.selectList(wrapper);
        List<SysApi> parentApis = sysApis.stream().filter(l -> l.getParentId().equals(0L)).collect(Collectors.toList());
        parentApis.forEach(l -> {
            List<SysApi> list = sysApis.stream().filter(api -> api.getParentId().equals(l.getId())).collect(Collectors.toList());
            l.setChildren(list);
        });
        return parentApis;
    }

}

