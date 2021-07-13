package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  AK47007
 * @date 2020/5/18
 * 友情链接
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "links")
public class Links implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 网站名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 网站链接
     */
    @TableField(value = "link")
    private String link;

    /**
     * logo链接
     */
    @TableField(value = "logo")
    private String logo;

    /**
     * 常用邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 状态：0、未通过 1、待审核、2、已通过
     */
    @TableField(value = "check_status")
    private Integer checkStatus;

    /**
     * 通过时间
     */
    @TableField(value = "pass_time")
    private LocalDateTime passTime;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_TITLE = "title";

    public static final String COL_LINK = "link";

    public static final String COL_LOGO = "logo";

    public static final String COL_EMAIL = "email";

    public static final String COL_CHECK_STATUS = "check_status";

    public static final String COL_PASS_TIME = "pass_time";

    public static final String COL_USER_ID = "user_id";
}