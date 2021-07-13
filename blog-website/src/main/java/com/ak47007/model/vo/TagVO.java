package com.ak47007.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  AK47007
 * @date 2020/5/20
 * 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "view_tb_tag")
public class TagVO implements Serializable {

    @TableId
    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(value = "tag_name")
    private String tagName;

    @TableField(value = "tag_logo")
    private String tagLogo;

    @TableField(value = "tag_type")
    private Integer tagType;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "count_used")
    private Long countUsed;

    private static final long serialVersionUID = 1L;

    public static final String COL_TAG_ID = "tag_id";

    public static final String COL_TAG_NAME = "tag_name";

    public static final String COL_TAG_LOGO = "tag_logo";

    public static final String COL_TAG_TYPE = "tag_type";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_COUNT_USED = "count_used";
}