package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2020/5/18
 * 标签表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tags")
public class Tag implements Serializable {
    /**
     * 标签唯一号码
     */
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Long tagId;

    /**
     * 标签名称
     */
    @TableField(value = "tag_name")
    private String tagName;

    /**
     * 标签logo
     */
    @TableField(value = "tag_logo")
    private String tagLogo;

    /**
     * 标签类型:1.单个 2.多个
     */
    @TableField(value = "tag_type")
    private Integer tagType;

    /**
     * 标签创建人
     */
    @TableField(value = "user_id")
    private Long userId;

    private static final long serialVersionUID = 1L;

    public static final String COL_TAG_ID = "tag_id";

    public static final String COL_TAG_NAME = "tag_name";

    public static final String COL_TAG_LOGO = "tag_logo";

    public static final String COL_TAG_TYPE = "tag_type";

    public static final String COL_USER_ID = "user_id";
}