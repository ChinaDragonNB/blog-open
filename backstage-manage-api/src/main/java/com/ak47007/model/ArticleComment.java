package com.ak47007.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
    * 评论表
    */
@Data
@TableName(value = "`article_comment`")
public class ArticleComment {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论的文章
     */
    @TableField(value = "`article_id`")
    private Long articleId;

    /**
     * 评论人名称
     */
    @TableField(value = "`user_name`")
    private String userName;

    /**
     * 用户id,如果评论人登录后在评论的话,这里才有值
     */
    @TableField(value = "`user_id`")
    private Long userId;

    /**
     * 头像链接
     */
    @TableField(value = "`img_url`")
    private String imgUrl;

    /**
     * 评论内容
     */
    @TableField(value = "`comment_conetnt`")
    private String commentConetnt;

    /**
     * 评论时间
     */
    @TableField(value = "`comment_time`")
    private LocalDateTime commentTime;

    /**
     * 评论人ip
     */
    @TableField(value = "`comment_ip`")
    private String commentIp;

    /**
     * 评论状态,1:待审核,2:已通过,3:审核未通过,4:已删除
     */
    @TableField(value = "`state`")
    private Integer state;

    public static final String COL_ID = "id";

    public static final String COL_ARTICLE_ID = "article_id";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_IMG_URL = "img_url";

    public static final String COL_COMMENT_CONETNT = "comment_conetnt";

    public static final String COL_COMMENT_TIME = "comment_time";

    public static final String COL_COMMENT_IP = "comment_ip";

    public static final String COL_STATE = "state";
}