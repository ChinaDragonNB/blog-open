package com.ak47007.service.impl;

import com.ak47007.mapper.SysDictMapper;
import com.ak47007.mapper.SysLoginLogMapper;
import com.ak47007.mapper.SysOperLogMapper;
import com.ak47007.mapper.ViewBrowseListMapper;
import com.ak47007.model.SysDict;
import com.ak47007.model.SysLoginLog;
import com.ak47007.model.SysOperLog;
import com.ak47007.model.query.LogQuery;
import com.ak47007.model.vo.ViewBrowseList;
import com.ak47007.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author AK47007
 * @date 2020/7/11
 * Describe:
 */
@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {

    private final ViewBrowseListMapper viewBrowseListMapper;

    private final SysOperLogMapper sysOperLogMapper;

    private final SysDictMapper sysDictMapper;

    private final SysLoginLogMapper sysLoginLogMapper;


    @Override
    public List<ViewBrowseList> listArticleLog(LogQuery query) {
        query.startPage();
        return viewBrowseListMapper.selectList(null);
    }

    @Override
    public List<SysOperLog> listOperLog(LogQuery query) {
        query.startPage();

        LambdaQueryWrapper<SysOperLog> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(SysOperLog::getOperTime);
        List<SysOperLog> list = sysOperLogMapper.selectList(wrapper);

        String logCode = "OPERATOR_TYPE";

        List<SysDict> dictList = sysDictMapper.getItemsByCode(logCode);
        if (!dictList.isEmpty()) {
            list.forEach(log -> {
                SysDict sysDict = dictList.stream().filter(l -> log.getOperType().equals(Integer.parseInt(l.getDictValue()))).findFirst().orElse(null);
                if (sysDict != null) {
                    log.setOperTypeName(sysDict.getDictName());
                }
            });
        }
        return list;
    }

    @Override
    public List<SysLoginLog> listLoginLog(LogQuery query) {
        query.startPage();
        List<SysLoginLog> list = sysLoginLogMapper.findAll();
        return list;
    }
}
