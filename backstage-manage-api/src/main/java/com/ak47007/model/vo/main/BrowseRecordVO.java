package com.ak47007.model.vo.main;

import com.ak47007.model.dto.BrowseRecordDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author AK47007
 * date 2021/4/24 21:49
 * describes:
 */
@Data
@AllArgsConstructor
public class BrowseRecordVO {


    @ApiModelProperty(value = "浏览数量集合")
    private List<BrowseRecordDTO> browseCount;


    @ApiModelProperty(value = "浏览人IP集合")
    private List<BrowseRecordDTO> browseIp;
}
