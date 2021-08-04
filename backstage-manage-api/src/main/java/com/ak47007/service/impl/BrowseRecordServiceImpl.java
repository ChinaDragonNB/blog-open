package com.ak47007.service.impl;

import com.ak47007.mapper.SysBrowseLogMapper;
import com.ak47007.model.dto.BrowseRecordDTO;
import com.ak47007.model.vo.main.BrowseRecordVO;
import com.ak47007.service.BrowseRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;

@Service
@AllArgsConstructor
public class BrowseRecordServiceImpl implements BrowseRecordService {

    private final SysBrowseLogMapper sysBrowseLogMapper;

    @Override
    public BrowseRecordVO browseCharts(Long userId) {
        List<BrowseRecordDTO> browseCountList = sysBrowseLogMapper.browseCount(userId);
        dateToTimestamp(browseCountList);
        List<BrowseRecordDTO> browseIpList = sysBrowseLogMapper.browseIp(userId);
        dateToTimestamp(browseIpList);
        return new BrowseRecordVO(browseCountList, browseIpList);
    }

    /**
     * 日期转时间戳
     */
    private void dateToTimestamp(List<BrowseRecordDTO> browseRecordDTOS) {
        for (BrowseRecordDTO dto : browseRecordDTOS) {
            dto.setX(dto.getDate().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
    }


}
