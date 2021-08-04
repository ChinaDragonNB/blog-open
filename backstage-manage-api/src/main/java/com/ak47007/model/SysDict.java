package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value = "`sys_dict`")
public class SysDict {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    @TableField(value = "`parent_id`")
    private Integer parentId;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    @TableField(value = "`dict_name`")
    private String dictName;

    /**
     * 字典代码
     */
    @ApiModelProperty(value = "字典代码")
    @TableField(value = "`dict_code`")
    private String dictCode;

    /**
     * 字典类别,1 字典目录 2字典项
     */
    @ApiModelProperty(value = "字典类别,1 字典目录 2字典项")
    @TableField(value = "`dict_type`")
    private Integer dictType;

    /**
     * 字典值
     */
    @ApiModelProperty(value = "字典值")
    @TableField(value = "`dict_value`")
    private String dictValue;

    /**
     * 字典状态
     */
    @ApiModelProperty(value = "字典状态")
    @TableField(value = "`dict_state`")
    private Boolean dictState;

    /**
     * 字典排序
     */
    @ApiModelProperty(value = "字典排序")
    @TableField(value = "`dict_sort`")
    private Integer dictSort;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField(value = "`dict_remark`")
    private String dictRemark;

    public static final String COL_ID = "id";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_DICT_NAME = "dict_name";

    public static final String COL_DICT_CODE = "dict_code";

    public static final String COL_DICT_TYPE = "dict_type";

    public static final String COL_DICT_VALUE = "dict_value";

    public static final String COL_DICT_STATE = "dict_state";

    public static final String COL_DICT_SORT = "dict_sort";

    public static final String COL_DICT_REMARK = "dict_remark";
}