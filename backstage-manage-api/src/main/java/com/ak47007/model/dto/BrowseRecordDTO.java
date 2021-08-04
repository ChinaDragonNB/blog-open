package com.ak47007.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author AK47007
 * date 2021/4/24 21:50
 * describes:
 */
@Data
public class BrowseRecordDTO implements Serializable {

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty(value = "x轴,时间戳")
    private Long x;

    @ApiModelProperty(value = "y轴,数量")
    private Integer y;
}
