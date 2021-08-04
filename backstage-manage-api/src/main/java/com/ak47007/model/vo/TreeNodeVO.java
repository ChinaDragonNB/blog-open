package com.ak47007.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author AK47007
 * CreateDate： 2019/11/20
 */
@Data
@AllArgsConstructor
public class TreeNodeVO {

    /**
     * 节点ID
     */
    @ApiModelProperty(value = "节点ID")
    private Long id;

    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称")
    private String label;

    /**
     * 是否禁用
     */
    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    public TreeNodeVO() {
        this.disabled = false;
    }

    /**
     * 子级节点
     */
    @ApiModelProperty(value = "子级节点")
    private List<TreeNodeVO> children;
}
