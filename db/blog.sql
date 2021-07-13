

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `uuid` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'guid',
  `title` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `cover` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '封面图片',
  `article_describe` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章描述',
  `publish_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发布时间',
  `edit_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `tag_id` bigint(0) NOT NULL COMMENT '标签',
  `state` int(0) NOT NULL COMMENT '发布状态：1.public  2.private',
  `is_stickie` int(0) NOT NULL COMMENT '是否置顶 1:是 0 否',
  `article_type` int(0) NOT NULL DEFAULT 0 COMMENT '文章类型,1 原创 2 转载 3 翻译',
  `user_id` bigint(0) NOT NULL COMMENT '文章作者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `fk_unique`(`uuid`) USING BTREE,
  INDEX `fk_tag_article`(`tag_id`) USING BTREE,
  INDEX `fk_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint(0) NOT NULL COMMENT '评论的文章',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论人名称',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id,如果评论人登录后在评论的话,这里才有值',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像链接',
  `comment_conetnt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `comment_time` datetime(0) NOT NULL COMMENT '评论时间',
  `comment_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论人ip',
  `state` int(0) NOT NULL COMMENT '评论状态,1:待审核,2:已通过,3:审核未通过,4:已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_comment_article_id`(`article_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_comment
-- ----------------------------

-- ----------------------------
-- Table structure for article_content
-- ----------------------------
DROP TABLE IF EXISTS `article_content`;
CREATE TABLE `article_content`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint(0) NOT NULL COMMENT '文章ID',
  `article_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_article_content_id`(`article_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_content
-- ----------------------------

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `article_id` bigint(0) NULL DEFAULT NULL COMMENT '文章id',
  `tag_id` bigint(0) NULL DEFAULT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_id`(`article_id`) USING BTREE,
  INDEX `fk_tag_id`(`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1299 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文章标签中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for links
-- ----------------------------
DROP TABLE IF EXISTS `links`;
CREATE TABLE `links`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '网站名称',
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '网站链接',
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'logo链接',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '常用邮箱',
  `check_status` int(0) NOT NULL COMMENT '状态：0、未通过 1、待审核、2、已通过',
  `pass_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '通过时间',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_links_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '友情链接' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of links
-- ----------------------------

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `api_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口地址',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口描述',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父级id',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `update_user` bigint(0) NULL DEFAULT NULL COMMENT '修改人',
  `order` int(0) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_api
-- ----------------------------
INSERT INTO `sys_api` VALUES (1, '/user/**', '用户管理', 0, '2021-06-06 13:30:51', 1, '2021-06-10 11:07:38', NULL, 2);
INSERT INTO `sys_api` VALUES (2, '/role/**', '角色管理', 0, '2021-06-06 13:31:19', 1, '2021-06-10 11:07:40', NULL, 3);
INSERT INTO `sys_api` VALUES (3, '/authority/**', '权限管理', 0, '2021-06-06 13:32:25', 1, '2021-06-10 11:07:41', NULL, 4);
INSERT INTO `sys_api` VALUES (4, '/router/**', '路由管理', 0, '2021-06-06 13:33:01', 1, '2021-06-10 11:07:42', NULL, 5);
INSERT INTO `sys_api` VALUES (5, '/article/**', '文章管理', 0, '2021-06-06 13:33:29', 1, '2021-06-10 11:07:43', NULL, 6);
INSERT INTO `sys_api` VALUES (6, '/tags/**', '标签管理', 0, '2021-06-06 13:33:50', 1, '2021-06-10 11:07:43', NULL, 7);
INSERT INTO `sys_api` VALUES (7, '/links/**', '友情链接', 0, '2021-06-06 13:34:08', 1, '2021-06-10 11:07:44', NULL, 8);
INSERT INTO `sys_api` VALUES (8, '/aliCloud/**', '阿里云管理', 0, '2021-06-06 13:34:25', 1, '2021-06-10 11:07:45', NULL, 9);
INSERT INTO `sys_api` VALUES (9, '/log/**', '日志管理', 0, '2021-06-06 13:34:44', 1, '2021-06-10 11:07:46', NULL, 10);
INSERT INTO `sys_api` VALUES (10, '/user/list', '用户列表', 1, '2021-06-06 13:40:08', 1, '2021-06-10 10:57:35', NULL, 1);
INSERT INTO `sys_api` VALUES (11, '/user/addUser', '添加用户', 1, '2021-06-06 13:41:30', 1, '2021-06-10 10:57:36', NULL, 2);
INSERT INTO `sys_api` VALUES (12, '/user/editUser', '编辑用户', 1, '2021-06-06 13:43:59', 1, '2021-06-10 10:57:37', NULL, 3);
INSERT INTO `sys_api` VALUES (13, '/user/delUser', '删除用户', 1, '2021-06-06 13:44:43', 1, '2021-06-10 10:57:37', NULL, 4);
INSERT INTO `sys_api` VALUES (14, '/user/userInfo', '用户信息', 1, '2021-06-06 13:44:57', 1, '2021-06-10 10:57:38', NULL, 5);
INSERT INTO `sys_api` VALUES (15, '/user/roleList', '选择角色', 1, '2021-06-06 13:45:16', 1, '2021-06-10 10:57:39', NULL, 6);
INSERT INTO `sys_api` VALUES (16, '/user/uploadAvatar', '上传头像', 1, '2021-06-06 13:45:33', 1, '2021-06-10 10:57:39', NULL, 7);
INSERT INTO `sys_api` VALUES (17, '/role/list', '角色列表', 2, '2021-06-06 13:45:55', 1, '2021-06-10 11:07:54', NULL, 1);
INSERT INTO `sys_api` VALUES (18, '/role/roleInfo', '角色信息', 2, '2021-06-06 13:46:05', 1, '2021-06-10 11:07:54', NULL, 2);
INSERT INTO `sys_api` VALUES (19, '/role/addRole', '添加角色', 2, '2021-06-06 13:46:14', 1, '2021-06-10 11:07:55', NULL, 3);
INSERT INTO `sys_api` VALUES (20, '/role/editRole', '编辑角色', 2, '2021-06-06 13:46:23', 1, '2021-06-10 11:07:56', NULL, 4);
INSERT INTO `sys_api` VALUES (21, '/role/delRole', '删除角色', 2, '2021-06-06 13:46:35', 1, '2021-06-10 11:07:57', NULL, 5);
INSERT INTO `sys_api` VALUES (22, '/role/authorityTree', '选择权限', 2, '2021-06-06 13:46:45', 1, '2021-06-10 11:07:58', NULL, 6);
INSERT INTO `sys_api` VALUES (23, '/role/routerTree', '选择路由', 2, '2021-06-06 13:46:55', 1, '2021-06-10 11:07:58', NULL, 7);
INSERT INTO `sys_api` VALUES (24, '/authority/tree', '权限列表', 3, '2021-06-06 13:47:14', 1, '2021-06-10 11:08:00', NULL, 1);
INSERT INTO `sys_api` VALUES (25, '/authority/authorityInfo', '权限信息', 3, '2021-06-06 13:47:21', 1, '2021-06-10 11:08:01', NULL, 2);
INSERT INTO `sys_api` VALUES (26, '/authority/addAuthority', '添加权限', 3, '2021-06-06 13:47:28', 1, '2021-06-10 11:08:02', NULL, 3);
INSERT INTO `sys_api` VALUES (27, '/authority/editAuthority', '编辑权限', 3, '2021-06-06 13:47:36', 1, '2021-06-10 11:08:02', NULL, 4);
INSERT INTO `sys_api` VALUES (28, '/authority/delAuthority', '删除权限', 3, '2021-06-06 13:47:45', 1, '2021-06-10 11:08:03', NULL, 5);
INSERT INTO `sys_api` VALUES (29, '/authority/listApi', '接口列表', 3, '2021-06-06 13:47:53', 1, '2021-06-10 11:08:05', NULL, 6);
INSERT INTO `sys_api` VALUES (30, '/router/tree', '路由列表', 4, '2021-06-06 13:48:13', 1, '2021-06-10 11:08:06', NULL, 1);
INSERT INTO `sys_api` VALUES (31, '/router/routerInfo', '路由信息', 4, '2021-06-06 13:48:20', 1, '2021-06-10 11:08:07', NULL, 2);
INSERT INTO `sys_api` VALUES (32, '/router/addRouter', '添加路由', 4, '2021-06-06 13:48:31', 1, '2021-06-10 11:08:08', NULL, 3);
INSERT INTO `sys_api` VALUES (33, '/router/editRouter', '编辑路由', 4, '2021-06-06 13:48:41', 1, '2021-06-10 11:08:08', NULL, 4);
INSERT INTO `sys_api` VALUES (34, '/router/deleteRouter', '删除路由', 4, '2021-06-06 13:48:48', 1, '2021-06-10 11:08:11', NULL, 5);
INSERT INTO `sys_api` VALUES (35, '/router/menuList', '菜单列表', 4, '2021-06-06 13:49:27', 1, '2021-06-10 11:08:20', NULL, 6);
INSERT INTO `sys_api` VALUES (36, '/router/updateSort', '路由排序', 4, '2021-06-06 13:49:37', 1, '2021-06-10 11:08:22', NULL, 7);
INSERT INTO `sys_api` VALUES (38, '/article/listArticle', '文章列表', 5, '2021-06-06 13:51:39', 1, '2021-06-10 11:08:23', NULL, 1);
INSERT INTO `sys_api` VALUES (39, '/article/articleInfo', '文章信息', 5, '2021-06-06 13:51:46', 1, '2021-06-10 11:08:23', NULL, 2);
INSERT INTO `sys_api` VALUES (40, '/article/writeArticle', '写文章', 5, '2021-06-06 13:51:52', 1, '2021-06-10 11:08:24', NULL, 3);
INSERT INTO `sys_api` VALUES (41, '/article/editArticle', '编辑文章', 5, '2021-06-06 13:51:58', 1, '2021-06-10 11:08:25', NULL, 4);
INSERT INTO `sys_api` VALUES (42, '/article/delArticle', '删除文章', 5, '2021-06-06 13:52:05', 1, '2021-06-10 11:08:26', NULL, 5);
INSERT INTO `sys_api` VALUES (43, '/article/listTag', '选择文章分类', 5, '2021-06-06 13:52:25', 1, '2021-06-10 11:08:26', NULL, 6);
INSERT INTO `sys_api` VALUES (44, '/article/selectedTags', '选择多个标签', 5, '2021-06-06 13:52:40', 1, '2021-06-10 11:08:29', NULL, 7);
INSERT INTO `sys_api` VALUES (45, '/article/replaceArticleContent', '替换文章内容', 5, '2021-06-06 13:53:00', 1, '2021-06-10 11:08:33', NULL, 8);
INSERT INTO `sys_api` VALUES (46, '/article/uploadCover', '上传封面', 5, '2021-06-06 13:53:07', 1, '2021-06-10 11:08:34', NULL, 9);
INSERT INTO `sys_api` VALUES (47, '/article/uploadImg', '上传文章图片', 5, '2021-06-06 13:53:16', 1, '2021-06-10 11:08:35', NULL, 10);
INSERT INTO `sys_api` VALUES (48, '/article/exportMarkDown', '导出文章', 5, '2021-06-06 13:53:24', 1, '2021-06-10 11:08:36', NULL, 11);
INSERT INTO `sys_api` VALUES (49, '/article/exportAll', '导出全部文章', 5, '2021-06-06 13:53:32', 1, '2021-06-10 11:08:37', NULL, 12);
INSERT INTO `sys_api` VALUES (50, '/article/uploadMarkDown', '导入文章', 5, '2021-06-06 13:53:42', 1, '2021-06-10 11:08:38', NULL, 13);
INSERT INTO `sys_api` VALUES (51, '/tags/listTag', '标签列表', 6, '2021-06-06 13:53:58', 1, '2021-06-10 11:08:40', NULL, 1);
INSERT INTO `sys_api` VALUES (52, '/tags/tagInfo', '标签信息', 6, '2021-06-06 13:54:06', 1, '2021-06-10 11:08:40', NULL, 2);
INSERT INTO `sys_api` VALUES (53, '/tags/addTag', '添加标签', 6, '2021-06-06 13:54:24', 1, '2021-06-10 11:08:41', NULL, 3);
INSERT INTO `sys_api` VALUES (54, '/tags/editTag', '编辑标签', 6, '2021-06-06 13:54:31', 1, '2021-06-10 11:08:42', NULL, 4);
INSERT INTO `sys_api` VALUES (55, '/tags/delTag', '删除标签', 6, '2021-06-06 13:54:37', 1, '2021-06-10 11:08:43', NULL, 5);
INSERT INTO `sys_api` VALUES (56, '/tags/uploadLogo', '上传标签LOGO', 6, '2021-06-06 13:54:46', 1, '2021-06-10 11:08:44', NULL, 6);
INSERT INTO `sys_api` VALUES (57, '/links/listLink', '链接列表', 7, '2021-06-06 13:55:15', 1, '2021-06-10 11:08:45', NULL, 1);
INSERT INTO `sys_api` VALUES (58, '/links/linkInfo', '链接信息', 7, '2021-06-06 13:55:31', 1, '2021-06-10 11:08:46', NULL, 2);
INSERT INTO `sys_api` VALUES (59, '/links/checkLink', '审核链接', 7, '2021-06-06 13:55:45', 1, '2021-06-10 11:08:46', NULL, 3);
INSERT INTO `sys_api` VALUES (60, '/links/editLink', '编辑链接', 7, '2021-06-06 13:55:55', 1, '2021-06-10 11:08:47', NULL, 4);
INSERT INTO `sys_api` VALUES (61, '/links/delLink', '删除链接', 7, '2021-06-06 13:56:03', 1, '2021-06-10 11:08:48', NULL, 5);
INSERT INTO `sys_api` VALUES (62, '/aliCloud/getBucketList', 'Bucket列表', 8, '2021-06-06 13:56:23', 1, '2021-06-10 11:08:49', NULL, 1);
INSERT INTO `sys_api` VALUES (63, '/aliCloud/getObjectList', '文件列表', 8, '2021-06-06 13:56:30', 1, '2021-06-10 11:08:50', NULL, 2);
INSERT INTO `sys_api` VALUES (64, '/aliCloud/deleteObject', '删除文件', 8, '2021-06-06 13:56:39', 1, '2021-06-10 11:08:51', NULL, 3);
INSERT INTO `sys_api` VALUES (65, '/aliCloud/addFolder', '添加目录', 8, '2021-06-06 13:56:47', 1, '2021-06-10 11:08:52', NULL, 4);
INSERT INTO `sys_api` VALUES (66, '/aliCloud/uploadFile', '上传文件', 8, '2021-06-06 13:57:07', 1, '2021-06-10 11:08:53', NULL, 5);
INSERT INTO `sys_api` VALUES (67, '/log/articleLogs', '文章日志', 9, '2021-06-06 13:57:29', 1, '2021-06-10 11:08:54', NULL, 1);
INSERT INTO `sys_api` VALUES (68, '/log/operLogs', '操作日志', 9, '2021-06-06 13:57:38', 1, '2021-06-10 11:08:55', NULL, 2);
INSERT INTO `sys_api` VALUES (69, '/log/loginLogs', '登陆日志', 9, '2021-06-06 13:57:52', 1, '2021-06-10 11:08:56', NULL, 3);
INSERT INTO `sys_api` VALUES (70, '', '公共接口', 0, '2021-06-10 10:59:49', 1, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (71, '/main/getStatistics', '模块数量统计', 70, '2021-06-10 11:01:04', 1, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (72, '/main/articlePie', '文章图表', 70, '2021-06-10 11:01:38', 1, '2021-06-10 11:02:04', NULL, 2);
INSERT INTO `sys_api` VALUES (73, '/main/tagColumn', '标签图表', 70, '2021-06-10 11:02:00', 1, '2021-06-10 11:02:08', NULL, 3);
INSERT INTO `sys_api` VALUES (74, '/main/linkLine', '友情链接图表', 70, '2021-06-10 11:02:29', 1, NULL, NULL, 4);
INSERT INTO `sys_api` VALUES (75, '/main/browseCharts', '浏览记录图表', 70, '2021-06-10 11:03:11', 1, NULL, NULL, 5);
INSERT INTO `sys_api` VALUES (76, '/main/getLatelyArticle', '最近文章', 70, '2021-06-10 11:03:18', 1, NULL, NULL, 6);
INSERT INTO `sys_api` VALUES (77, '/main/getWebsiteTools', '实用网站', 70, '2021-06-10 11:03:31', 1, NULL, NULL, 7);
INSERT INTO `sys_api` VALUES (78, '/auth/getUserInfo', '当前用户信息', 70, '2021-06-10 11:05:12', 1, NULL, NULL, 8);

-- ----------------------------
-- Table structure for sys_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_authority`;
CREATE TABLE `sys_authority`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `authority_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限英文名',
  `authority_name_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限中文名',
  `parent_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '父级权限,最高级时为0',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '权限创建时间',
  `create_user` bigint(0) NOT NULL COMMENT '权限创建人',
  `state` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 0锁定 1正常',
  `order` int(0) NULL DEFAULT NULL COMMENT '排序',
  `is_common` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为公共权限,每个角色都有的',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_autority_user`(`create_user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_authority
-- ----------------------------
INSERT INTO `sys_authority` VALUES (1, 'manage_user', '用户管理', 0, '2021-06-06 14:13:01', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (2, 'manage_role', '角色管理', 0, '2021-06-06 14:13:17', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (3, 'manage_authority', '权限管理', 0, '2021-06-06 14:13:40', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (4, 'manage_router', '路由管理', 0, '2021-06-06 14:14:04', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (5, 'manage_article', '文章管理', 0, '2021-06-06 14:14:16', 1, 1, 6, 0);
INSERT INTO `sys_authority` VALUES (6, 'manage_tags', '标签管理', 0, '2021-06-06 14:14:28', 1, 1, 7, 0);
INSERT INTO `sys_authority` VALUES (7, 'manage_links', '友情链接', 0, '2021-06-06 14:14:43', 1, 1, 8, 0);
INSERT INTO `sys_authority` VALUES (8, 'manage_alicloud', '阿里云', 0, '2021-06-06 14:14:58', 1, 1, 9, 0);
INSERT INTO `sys_authority` VALUES (9, 'manage_log', '日志管理', 0, '2021-06-06 14:15:13', 1, 1, 10, 0);
INSERT INTO `sys_authority` VALUES (10, 'user_list', '用户列表', 1, '2021-06-06 14:34:40', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (11, 'user_add', '添加用户', 1, '2021-06-06 14:35:02', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (12, 'user_edit', '编辑用户', 1, '2021-06-06 14:35:16', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (13, 'user_del', '删除用户', 1, '2021-06-06 14:36:00', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (14, 'user_info', '用户信息', 1, '2021-06-06 14:36:22', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (15, 'role_list', '角色列表', 2, '2021-06-06 14:36:54', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (16, 'role_add', '添加角色', 2, '2021-06-06 14:37:14', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (17, 'role_edit', '编辑角色', 2, '2021-06-06 14:37:36', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (18, 'role_del', '删除角色', 2, '2021-06-06 14:37:49', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (19, 'role_info', '角色信息', 2, '2021-06-06 14:38:02', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (20, 'authority_list', '权限列表', 3, '2021-06-08 10:05:57', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (21, 'authority_add', '添加权限', 3, '2021-06-08 10:06:22', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (22, 'authority_edit', '编辑权限', 3, '2021-06-08 10:06:37', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (23, 'authority_info', '权限信息', 3, '2021-06-08 10:06:54', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (24, 'authority_del', '删除权限', 3, '2021-06-08 10:07:06', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (25, 'router_list', '路由列表', 4, '2021-06-08 10:12:29', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (26, 'router_add', '添加路由', 4, '2021-06-08 10:14:39', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (27, 'router_info', '路由信息', 4, '2021-06-08 10:14:56', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (28, 'router_edit', '编辑路由', 4, '2021-06-08 10:15:10', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (29, 'router_del', '删除路由', 4, '2021-06-08 10:15:26', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (30, 'router_sort', '路由排序', 4, '2021-06-08 10:15:42', 1, 1, 6, 0);
INSERT INTO `sys_authority` VALUES (31, 'article_list', '文章列表', 5, '2021-06-08 10:17:27', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (32, 'article_write', '写文章', 5, '2021-06-08 10:17:53', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (33, 'article_info', '文章信息', 5, '2021-06-08 10:18:26', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (34, 'article_edit', '编辑文章', 5, '2021-06-08 10:19:00', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (35, 'article_del', '删除文章', 5, '2021-06-08 10:19:19', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (36, 'tag_list', '标签列表', 6, '2021-06-08 10:19:41', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (37, 'tag_add', '添加标签', 6, '2021-06-08 10:19:59', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (38, 'tag_info', '标签信息', 6, '2021-06-08 15:07:13', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (39, 'tag_edit', '编辑标签', 6, '2021-06-08 15:07:46', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (40, 'tag_del', '删除标签', 6, '2021-06-08 15:08:12', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (41, 'link_list', '链接列表', 7, '2021-06-08 15:08:42', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (42, 'link_info', '链接信息', 7, '2021-06-08 15:09:08', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (43, 'link_edit', '编辑链接', 7, '2021-06-08 15:10:20', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (44, 'link_check', '审核链接', 7, '2021-06-08 15:10:43', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (45, 'link_del', '删除链接', 7, '2021-06-08 15:11:00', 1, 1, 5, 0);
INSERT INTO `sys_authority` VALUES (46, 'alicloud_list', '文件列表', 8, '2021-06-08 15:12:03', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (47, 'alicloud_add_folder', '添加目录', 8, '2021-06-08 15:12:36', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (48, 'alicloud_upload', '上传文件', 8, '2021-06-08 15:12:58', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (49, 'alicloud_del', '删除文件', 8, '2021-06-08 15:13:11', 1, 1, 4, 0);
INSERT INTO `sys_authority` VALUES (50, 'article_log_list', '文章日志', 9, '2021-06-08 15:13:50', 1, 1, 1, 0);
INSERT INTO `sys_authority` VALUES (51, 'oper_log_list', '操作日志', 9, '2021-06-08 15:14:13', 1, 1, 2, 0);
INSERT INTO `sys_authority` VALUES (52, 'login_log_list', '登陆日志', 9, '2021-06-08 15:14:26', 1, 1, 3, 0);
INSERT INTO `sys_authority` VALUES (53, 'common', '公共权限', 0, '2021-06-10 11:12:19', 1, 1, 1, 1);

-- ----------------------------
-- Table structure for sys_authority_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_authority_api`;
CREATE TABLE `sys_authority_api`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `authority_id` bigint(0) NULL DEFAULT NULL COMMENT '权限id',
  `api_id` bigint(0) NULL DEFAULT NULL COMMENT '接口路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 155 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_authority_api
-- ----------------------------
INSERT INTO `sys_authority_api` VALUES (61, 10, 10);
INSERT INTO `sys_authority_api` VALUES (62, 11, 11);
INSERT INTO `sys_authority_api` VALUES (63, 11, 15);
INSERT INTO `sys_authority_api` VALUES (64, 11, 16);
INSERT INTO `sys_authority_api` VALUES (65, 12, 12);
INSERT INTO `sys_authority_api` VALUES (66, 12, 14);
INSERT INTO `sys_authority_api` VALUES (67, 12, 15);
INSERT INTO `sys_authority_api` VALUES (68, 12, 16);
INSERT INTO `sys_authority_api` VALUES (69, 13, 13);
INSERT INTO `sys_authority_api` VALUES (70, 14, 14);
INSERT INTO `sys_authority_api` VALUES (71, 15, 17);
INSERT INTO `sys_authority_api` VALUES (72, 16, 19);
INSERT INTO `sys_authority_api` VALUES (73, 16, 22);
INSERT INTO `sys_authority_api` VALUES (74, 16, 23);
INSERT INTO `sys_authority_api` VALUES (75, 17, 20);
INSERT INTO `sys_authority_api` VALUES (76, 17, 18);
INSERT INTO `sys_authority_api` VALUES (77, 17, 22);
INSERT INTO `sys_authority_api` VALUES (78, 17, 23);
INSERT INTO `sys_authority_api` VALUES (79, 18, 21);
INSERT INTO `sys_authority_api` VALUES (80, 19, 18);
INSERT INTO `sys_authority_api` VALUES (81, 20, 24);
INSERT INTO `sys_authority_api` VALUES (82, 21, 26);
INSERT INTO `sys_authority_api` VALUES (83, 21, 29);
INSERT INTO `sys_authority_api` VALUES (84, 22, 25);
INSERT INTO `sys_authority_api` VALUES (85, 22, 29);
INSERT INTO `sys_authority_api` VALUES (86, 22, 27);
INSERT INTO `sys_authority_api` VALUES (87, 23, 25);
INSERT INTO `sys_authority_api` VALUES (88, 24, 28);
INSERT INTO `sys_authority_api` VALUES (89, 25, 30);
INSERT INTO `sys_authority_api` VALUES (90, 26, 32);
INSERT INTO `sys_authority_api` VALUES (91, 27, 31);
INSERT INTO `sys_authority_api` VALUES (92, 28, 33);
INSERT INTO `sys_authority_api` VALUES (93, 29, 34);
INSERT INTO `sys_authority_api` VALUES (94, 30, 35);
INSERT INTO `sys_authority_api` VALUES (95, 30, 36);
INSERT INTO `sys_authority_api` VALUES (101, 32, 40);
INSERT INTO `sys_authority_api` VALUES (102, 32, 43);
INSERT INTO `sys_authority_api` VALUES (103, 32, 44);
INSERT INTO `sys_authority_api` VALUES (104, 32, 46);
INSERT INTO `sys_authority_api` VALUES (105, 32, 47);
INSERT INTO `sys_authority_api` VALUES (106, 33, 39);
INSERT INTO `sys_authority_api` VALUES (107, 33, 48);
INSERT INTO `sys_authority_api` VALUES (108, 31, 38);
INSERT INTO `sys_authority_api` VALUES (109, 31, 49);
INSERT INTO `sys_authority_api` VALUES (110, 31, 50);
INSERT INTO `sys_authority_api` VALUES (111, 31, 45);
INSERT INTO `sys_authority_api` VALUES (117, 35, 42);
INSERT INTO `sys_authority_api` VALUES (118, 36, 51);
INSERT INTO `sys_authority_api` VALUES (119, 37, 53);
INSERT INTO `sys_authority_api` VALUES (120, 37, 56);
INSERT INTO `sys_authority_api` VALUES (121, 38, 52);
INSERT INTO `sys_authority_api` VALUES (122, 39, 54);
INSERT INTO `sys_authority_api` VALUES (123, 39, 56);
INSERT INTO `sys_authority_api` VALUES (124, 39, 52);
INSERT INTO `sys_authority_api` VALUES (125, 34, 41);
INSERT INTO `sys_authority_api` VALUES (126, 34, 43);
INSERT INTO `sys_authority_api` VALUES (127, 34, 44);
INSERT INTO `sys_authority_api` VALUES (128, 34, 46);
INSERT INTO `sys_authority_api` VALUES (129, 34, 47);
INSERT INTO `sys_authority_api` VALUES (130, 34, 39);
INSERT INTO `sys_authority_api` VALUES (131, 40, 55);
INSERT INTO `sys_authority_api` VALUES (132, 41, 57);
INSERT INTO `sys_authority_api` VALUES (133, 42, 58);
INSERT INTO `sys_authority_api` VALUES (134, 43, 60);
INSERT INTO `sys_authority_api` VALUES (135, 43, 58);
INSERT INTO `sys_authority_api` VALUES (136, 44, 59);
INSERT INTO `sys_authority_api` VALUES (137, 45, 55);
INSERT INTO `sys_authority_api` VALUES (138, 46, 62);
INSERT INTO `sys_authority_api` VALUES (139, 46, 63);
INSERT INTO `sys_authority_api` VALUES (140, 47, 65);
INSERT INTO `sys_authority_api` VALUES (141, 48, 66);
INSERT INTO `sys_authority_api` VALUES (142, 49, 64);
INSERT INTO `sys_authority_api` VALUES (143, 50, 67);
INSERT INTO `sys_authority_api` VALUES (144, 51, 68);
INSERT INTO `sys_authority_api` VALUES (145, 52, 69);
INSERT INTO `sys_authority_api` VALUES (146, 53, 71);
INSERT INTO `sys_authority_api` VALUES (147, 53, 72);
INSERT INTO `sys_authority_api` VALUES (148, 53, 73);
INSERT INTO `sys_authority_api` VALUES (149, 53, 74);
INSERT INTO `sys_authority_api` VALUES (150, 53, 75);
INSERT INTO `sys_authority_api` VALUES (151, 53, 76);
INSERT INTO `sys_authority_api` VALUES (152, 53, 77);
INSERT INTO `sys_authority_api` VALUES (153, 53, 78);

-- ----------------------------
-- Table structure for sys_browse_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_browse_log`;
CREATE TABLE `sys_browse_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` bigint(0) NOT NULL COMMENT '访问的文章id',
  `browse_ip` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问者IP',
  `browse_time` datetime(0) NOT NULL COMMENT '访问时间',
  `ip_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip所在位置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21057 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章浏览记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_browse_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(0) NOT NULL COMMENT '父级id',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `dict_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典代码',
  `dict_type` int(0) NOT NULL COMMENT '字典类别,1 字典目录 2字典项',
  `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值',
  `dict_state` tinyint(1) NOT NULL DEFAULT 1 COMMENT '字典状态',
  `dict_sort` int(0) NOT NULL COMMENT '字典排序',
  `dict_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 0, '根目录', 'ROOT', 1, NULL, 1, 0, NULL);
INSERT INTO `sys_dict` VALUES (2, 1, '操作类型', 'OPERATOR_TYPE', 1, NULL, 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (3, 2, '其他', 'OTHER', 2, '0', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (4, 2, '新增', 'INSERT', 2, '1', 1, 2, NULL);
INSERT INTO `sys_dict` VALUES (5, 2, '修改', 'EDIT', 2, '2', 1, 3, NULL);
INSERT INTO `sys_dict` VALUES (6, 2, '删除', 'DELETE', 2, '3', 1, 4, NULL);
INSERT INTO `sys_dict` VALUES (7, 2, '导入', 'IMPORT', 2, '4', 1, 5, NULL);
INSERT INTO `sys_dict` VALUES (8, 2, '导出', 'EXPORT', 2, '5', 1, 6, NULL);
INSERT INTO `sys_dict` VALUES (9, 2, '上传', 'UPLOAD', 2, '6', 1, 7, NULL);
INSERT INTO `sys_dict` VALUES (10, 2, '下载', 'DOWNLOAD', 2, '7', 1, 8, NULL);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `user_id` bigint(0) NOT NULL COMMENT '登录人',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ip地址',
  `login_addr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录地址,根据ip获取',
  `login_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 229 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oper_module` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作模块',
  `oper_type` int(0) NOT NULL COMMENT '操作类型,与字典表项的值对应',
  `oper_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作描述',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求url',
  `oper_method` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作方法',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `oper_req_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求参数',
  `oper_res_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '返回参数',
  `oper_user_id` bigint(0) NOT NULL COMMENT '操作人id',
  `oper_nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作人昵称',
  `oper_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主机ip',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作地点',
  `oper_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name_en` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色英文名',
  `role_name_cn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色中文名',
  `create_time` datetime(0) NOT NULL COMMENT '角色创建时间',
  `create_user` bigint(0) NOT NULL COMMENT '角色创建人',
  `state` tinyint(1) NOT NULL COMMENT '状态 0锁定 1正常',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_role_user`(`create_user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'ROLE_ADMIN', '管理员', '2019-11-20 14:58:07', 1, 1);

-- ----------------------------
-- Table structure for sys_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_authority`;
CREATE TABLE `sys_role_authority`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(0) NOT NULL COMMENT '权限名',
  `authority_id` bigint(0) NOT NULL COMMENT '父级权限,最高级时为0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_authority_role_role_id`(`role_id`) USING BTREE,
  INDEX `fk_authority_role_authority_id`(`authority_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_authority
-- ----------------------------
INSERT INTO `sys_role_authority` VALUES (12, 1, 10);
INSERT INTO `sys_role_authority` VALUES (13, 1, 11);
INSERT INTO `sys_role_authority` VALUES (14, 1, 12);
INSERT INTO `sys_role_authority` VALUES (15, 1, 13);
INSERT INTO `sys_role_authority` VALUES (16, 1, 14);
INSERT INTO `sys_role_authority` VALUES (17, 1, 15);
INSERT INTO `sys_role_authority` VALUES (18, 1, 16);
INSERT INTO `sys_role_authority` VALUES (19, 1, 17);
INSERT INTO `sys_role_authority` VALUES (20, 1, 18);
INSERT INTO `sys_role_authority` VALUES (21, 1, 19);
INSERT INTO `sys_role_authority` VALUES (22, 1, 20);
INSERT INTO `sys_role_authority` VALUES (23, 1, 21);
INSERT INTO `sys_role_authority` VALUES (24, 1, 22);
INSERT INTO `sys_role_authority` VALUES (25, 1, 23);
INSERT INTO `sys_role_authority` VALUES (26, 1, 24);
INSERT INTO `sys_role_authority` VALUES (27, 1, 25);
INSERT INTO `sys_role_authority` VALUES (28, 1, 26);
INSERT INTO `sys_role_authority` VALUES (29, 1, 27);
INSERT INTO `sys_role_authority` VALUES (30, 1, 28);
INSERT INTO `sys_role_authority` VALUES (31, 1, 29);
INSERT INTO `sys_role_authority` VALUES (32, 1, 30);
INSERT INTO `sys_role_authority` VALUES (33, 1, 31);
INSERT INTO `sys_role_authority` VALUES (34, 1, 32);
INSERT INTO `sys_role_authority` VALUES (35, 1, 33);
INSERT INTO `sys_role_authority` VALUES (36, 1, 34);
INSERT INTO `sys_role_authority` VALUES (37, 1, 35);
INSERT INTO `sys_role_authority` VALUES (38, 1, 36);
INSERT INTO `sys_role_authority` VALUES (39, 1, 37);
INSERT INTO `sys_role_authority` VALUES (40, 1, 38);
INSERT INTO `sys_role_authority` VALUES (41, 1, 39);
INSERT INTO `sys_role_authority` VALUES (42, 1, 40);
INSERT INTO `sys_role_authority` VALUES (43, 1, 41);
INSERT INTO `sys_role_authority` VALUES (44, 1, 42);
INSERT INTO `sys_role_authority` VALUES (45, 1, 43);
INSERT INTO `sys_role_authority` VALUES (46, 1, 44);
INSERT INTO `sys_role_authority` VALUES (47, 1, 45);
INSERT INTO `sys_role_authority` VALUES (48, 1, 46);
INSERT INTO `sys_role_authority` VALUES (49, 1, 47);
INSERT INTO `sys_role_authority` VALUES (50, 1, 48);
INSERT INTO `sys_role_authority` VALUES (51, 1, 49);
INSERT INTO `sys_role_authority` VALUES (52, 1, 50);
INSERT INTO `sys_role_authority` VALUES (53, 1, 51);
INSERT INTO `sys_role_authority` VALUES (54, 1, 52);
INSERT INTO `sys_role_authority` VALUES (55, 1, 53);

-- ----------------------------
-- Table structure for sys_role_router
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_router`;
CREATE TABLE `sys_role_router`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(0) NOT NULL COMMENT '角色Id',
  `router_id` bigint(0) NOT NULL COMMENT '路由id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_router_role_id`(`role_id`) USING BTREE,
  INDEX `fk_router_router_id`(`router_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 269 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_router
-- ----------------------------
INSERT INTO `sys_role_router` VALUES (1, 1, 2);
INSERT INTO `sys_role_router` VALUES (2, 1, 3);
INSERT INTO `sys_role_router` VALUES (3, 1, 4);
INSERT INTO `sys_role_router` VALUES (4, 1, 5);
INSERT INTO `sys_role_router` VALUES (5, 1, 6);
INSERT INTO `sys_role_router` VALUES (6, 1, 7);
INSERT INTO `sys_role_router` VALUES (7, 1, 8);
INSERT INTO `sys_role_router` VALUES (8, 1, 28);
INSERT INTO `sys_role_router` VALUES (9, 1, 9);
INSERT INTO `sys_role_router` VALUES (10, 1, 10);
INSERT INTO `sys_role_router` VALUES (11, 1, 11);
INSERT INTO `sys_role_router` VALUES (12, 1, 12);
INSERT INTO `sys_role_router` VALUES (13, 1, 13);
INSERT INTO `sys_role_router` VALUES (14, 1, 14);
INSERT INTO `sys_role_router` VALUES (15, 1, 15);
INSERT INTO `sys_role_router` VALUES (16, 1, 16);
INSERT INTO `sys_role_router` VALUES (17, 1, 17);
INSERT INTO `sys_role_router` VALUES (18, 1, 18);
INSERT INTO `sys_role_router` VALUES (19, 1, 19);
INSERT INTO `sys_role_router` VALUES (20, 1, 20);
INSERT INTO `sys_role_router` VALUES (21, 1, 21);
INSERT INTO `sys_role_router` VALUES (22, 1, 26);
INSERT INTO `sys_role_router` VALUES (23, 1, 27);
INSERT INTO `sys_role_router` VALUES (24, 1, 29);
INSERT INTO `sys_role_router` VALUES (25, 1, 30);
INSERT INTO `sys_role_router` VALUES (26, 1, 31);
INSERT INTO `sys_role_router` VALUES (27, 1, 32);
INSERT INTO `sys_role_router` VALUES (28, 1, 33);
INSERT INTO `sys_role_router` VALUES (29, 1, 34);

-- ----------------------------
-- Table structure for sys_router
-- ----------------------------
DROP TABLE IF EXISTS `sys_router`;
CREATE TABLE `sys_router`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件Name',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件标题',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向路径',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标名',
  `hidden` tinyint(1) NULL DEFAULT NULL COMMENT '是否隐藏',
  `parent_id` bigint(0) NOT NULL COMMENT '父级路由,如果没有,则为0',
  `no_cache` tinyint(1) NOT NULL COMMENT '该路由是否不缓存',
  `describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '该路由的描述',
  `always_show` tinyint(1) NOT NULL COMMENT '如果设置为true，将始终显示根菜单,如果不设置alwaysShow, 当项目有多个子路由时，它将成为嵌套模式，否则不显示根菜单',
  `active_menu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '如果设置路径，侧栏将突出显示您设置的路径,一般只有子级菜单隐藏的时候才会设置',
  `order_index` int(0) NULL DEFAULT NULL COMMENT '路由显示顺序',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '路由表,左侧菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_router
-- ----------------------------
INSERT INTO `sys_router` VALUES (2, NULL, '404', '/404', '404', NULL, NULL, 1, 0, 0, '404页面', 0, NULL, 0, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (3, NULL, ' ', '/', 'Layout', '/main', NULL, 0, 0, 0, '根路由', 0, NULL, 0, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (4, 'Main', '首页', 'main', 'main/index', NULL, 'sidebar-main', 0, 3, 0, '首页菜单', 0, NULL, 0, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (5, 'System', '系统配置', '/system', 'Layout', '/system/user', 'sidebar-system', 0, 0, 0, '系统配置父级菜单', 1, NULL, 1, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (6, 'User', '用户管理', 'user', 'user/list', NULL, 'sidebar-system-user', 0, 5, 0, '用户管理菜单项', 0, NULL, 1, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (7, 'Role', '角色管理', 'role', 'role/list', NULL, 'sidebar-system-role', 0, 5, 0, '角色管理菜单项', 0, NULL, 1, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (8, 'Authority', '权限管理', 'authority', 'authority/index', NULL, 'sidebar-system-authority', 0, 5, 0, '权限管理菜单项', 0, NULL, 1, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (9, 'Article', '文章管理', '/article', 'Layout', '/article/list', 'sidebar-article', 0, 0, 0, '文章父级菜单', 1, NULL, 2, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (10, 'ArticleList', '文章列表', 'list', 'article/list', NULL, 'sidebar-article-list', 0, 9, 0, '文章列表菜单项', 0, NULL, 2, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (11, 'ArticleWriter', '写文章', 'writer', 'article/writer', NULL, 'sidebar-article-writer', 0, 9, 0, '写文章菜单项', 0, NULL, 2, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (12, 'ArticleEdit', '编辑文章', 'edit/:id', 'article/edit', NULL, NULL, 1, 9, 1, '编辑文章菜单项', 0, '/article/list', 2, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (13, 'ArticleView', '查看文章', 'view/:id', 'article/view', NULL, NULL, 1, 9, 1, '查看文章菜单项', 0, '/article/list', 2, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (14, 'Tags', '标签管理', '/tags', 'Layout', '/tags/list', 'sidebar-tags', 0, 0, 0, '标签父级菜单', 1, NULL, 3, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (15, 'ListTags', '标签列表', 'list', 'tags/list', NULL, 'sidebar-tags-list', 0, 14, 0, '标签列表菜单项', 0, NULL, 3, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (16, 'AddTags', '添加标签', 'addTags', 'tags/add', NULL, 'sidebar-tags-add', 0, 14, 0, '添加标签菜单项', 0, NULL, 3, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (17, 'Links', '友情链接', '/links', 'Layout', '/links/list', 'sidebar-links', 0, 0, 0, '友情链接父级菜单项', 1, NULL, 4, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (18, 'ListLinks', '链接列表', 'list', 'links/list', NULL, 'sidebar-links-list', 0, 17, 0, '链接列表菜单项', 0, NULL, 4, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (19, 'CheckLinks', '审核链接', 'checkLinks', 'links/check', NULL, 'sidebar-links-check', 0, 17, 0, '审核链接菜单项', 0, NULL, 4, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (20, 'AliCloud', '阿里云', '/aliCloud', 'Layout', '/aliCloud/bucket', 'sidebar-alicloud', 0, 0, 0, '', 1, NULL, 6, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (21, 'AliCloudBucketList', 'Bucket列表', 'bucket', 'alicloud/bucketList', NULL, 'sidebar-alicloud-bucketlist', 0, 20, 0, '', 0, NULL, 6, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (26, 'AliCloudFileList', '文件管理', 'object/:bucketName', 'alicloud/fileList', NULL, NULL, 1, 20, 1, '文件管理', 0, '', 6, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (27, '', '*', '*', NULL, '/404', NULL, 1, 0, 1, '其它未定义的请求都重定向至404', 0, NULL, 0, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (28, 'Router', '路由管理', 'router', 'router/index', NULL, 'sidebar-system-router', 0, 5, 0, '路由管理菜单项', 0, NULL, 1, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (29, 'Comment', '评论管理', '/comment', 'Layout', '/comment/list', 'sidebar-comment', 1, 0, 0, '评论管理父级菜单项', 1, NULL, 5, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (30, 'ComentList', '评论列表', 'comment/list', 'comment/list', NULL, 'sidebar-comment-list', 1, 29, 0, '评论管理列表', 0, NULL, 5, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (31, 'Log', '日志管理', '/log', 'Layout', NULL, 'sidebar-log', 0, 0, 1, '日志记录', 1, NULL, 7, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (32, 'ArticleBrowseLog', '浏览日志', 'articleLog', 'log/articleLog', NULL, 'sidebar-log-articlelog', 0, 31, 1, '文章浏览日志', 0, NULL, 7, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (33, 'OperLog', '操作日志', 'operLog', 'log/operLog', NULL, 'sidebar-log-operlog', 0, 31, 1, '系统操作日志', 0, NULL, 7, '2020-05-09 17:35:16', 1);
INSERT INTO `sys_router` VALUES (34, 'LoginLog', '登录日志', 'loginLog', 'log/loginLog', NULL, 'sidebar-log-loginlog', 0, 31, 1, '登录日志', 0, NULL, 7, '2020-05-09 17:35:16', 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称,用于显示',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名,用于登录',
  `user_pass` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码,用于登录',
  `login_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户头像',
  `role_id` bigint(0) NOT NULL COMMENT '拥有角色',
  `state` tinyint(1) NOT NULL COMMENT '状态 0锁定 1正常',
  `create_time` datetime(0) NOT NULL COMMENT '用户创建时间',
  `blog_home` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客主页',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'AK47007', 'admin', 'E10ADC3949BA59ABBE56E057F20F883E', 'https://www.ak47007.com/007.jpg', 1, 1, '2019-11-20 15:21:40', 'https://www.ak47007.com');

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags`  (
  `tag_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `tag_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `tag_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签logo',
  `tag_type` int(0) NOT NULL DEFAULT 1 COMMENT '标签类型:1.单个 2.多个',
  `user_id` bigint(0) NOT NULL COMMENT '标签创建人',
  PRIMARY KEY (`tag_id`) USING BTREE,
  INDEX `fk_tag_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tags
-- ----------------------------

-- ----------------------------
-- Table structure for website_tools
-- ----------------------------
DROP TABLE IF EXISTS `website_tools`;
CREATE TABLE `website_tools`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `website_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网站名称',
  `website_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网站链接',
  `add_date_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加网站时间',
  `desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网站描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实用网站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of website_tools
-- ----------------------------
INSERT INTO `website_tools` VALUES (1, '来此加密', 'https://letsencrypt.osfipin.com/', '2020-11-12 21:56:16', '可以申请免费的泛域名证书');
INSERT INTO `website_tools` VALUES (2, '图案文字生成', 'http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20', '2020-11-12 21:56:16', '可以生成图案字符串');
INSERT INTO `website_tools` VALUES (3, '时间戳转换', 'https://tool.lu/timestamp/', '2020-11-12 21:56:16', '时间转换');
INSERT INTO `website_tools` VALUES (4, '英语词汇宝典', 'https://learn-english.dev/', '2020-11-12 21:56:16', '开发人员常用英语词汇宝典');
INSERT INTO `website_tools` VALUES (5, 'Smallpdf', 'https://smallpdf.com/', '2020-11-12 21:56:16', '支持各种文件转PDF与互转');
INSERT INTO `website_tools` VALUES (6, 'Dillinger', 'https://dillinger.io/', '2020-11-12 21:56:16', '在线的MarkDown编辑器');
INSERT INTO `website_tools` VALUES (7, 'gitignore.io', 'https://www.toptal.com/developers/gitignore', '2020-11-12 22:01:40', '生成git忽略文件');
INSERT INTO `website_tools` VALUES (8, 'Address Generator', 'https://fakeaddressgenerator.com/', '2020-11-12 22:02:56', '生成一个外国的地址');
INSERT INTO `website_tools` VALUES (9, 'ascii-generator', 'https://www.topster.net/ascii-generator/', '2020-11-12 22:03:44', '图片转图案字符串');
INSERT INTO `website_tools` VALUES (10, '颜色对照表', 'http://bbs.bianzhirensheng.com/color01.html', '2020-11-12 23:42:11', '找颜色');
INSERT INTO `website_tools` VALUES (11, 'ASCII对照表\r\n', 'https://tool.oschina.net/commons?type=4', '2020-11-12 23:42:44', 'ASCII码对照');

-- ----------------------------
-- View structure for view_article_card
-- ----------------------------
DROP VIEW IF EXISTS `view_article_card`;
CREATE  VIEW `view_article_card` AS select `a`.`id` AS `id`,`a`.`uuid` AS `uuid`,`a`.`cover` AS `cover`,`a`.`title` AS `title`,`a`.`article_describe` AS `article_describe`,`a`.`publish_time` AS `publish_time`,`a`.`edit_time` AS `edit_time`,`a`.`tag_id` AS `tag_id`,`c`.`tag_name` AS `tag_name`,`a`.`state` AS `state`,`a`.`is_stickie` AS `is_stickie`,`a`.`article_type` AS `article_type`,`a`.`user_id` AS `user_id`,(select (count(0) + 0) from `sys_browse_log` where (`sys_browse_log`.`article_id` = `a`.`id`)) AS `count_view`,(select (count(0) + 0) from `article_comment` where (`article_comment`.`article_id` = `a`.`id`)) AS `count_comment` from ((`article` `a` left join `article_content` `b` on((`a`.`id` = `b`.`article_id`))) left join `tags` `c` on((`c`.`tag_id` = `a`.`tag_id`))) where (`a`.`state` = 1) order by `a`.`publish_time` desc;

-- ----------------------------
-- Records of website_tools
-- ----------------------------

-- ----------------------------
-- View structure for view_article_list
-- ----------------------------
DROP VIEW IF EXISTS `view_article_list`;
CREATE  VIEW `view_article_list` AS select `a`.`id` AS `id`,`a`.`title` AS `title`,if((length(`a`.`article_describe`) > 25),concat(substr(`a`.`article_describe`,1,25),'...'),`a`.`article_describe`) AS `article_describe`,(select count(0) from `sys_browse_log` `b` where (`b`.`article_id` = `a`.`id`)) AS `views`,`c`.`tag_logo` AS `tag_logo`,`a`.`state` AS `state`,`a`.`publish_time` AS `publish_time`,`a`.`user_id` AS `user_id` from (`article` `a` left join `tags` `c` on((`c`.`tag_id` = `a`.`tag_id`))) order by `a`.`state` desc,`a`.`publish_time` desc;

-- ----------------------------
-- Records of website_tools
-- ----------------------------

-- ----------------------------
-- View structure for view_browse_list
-- ----------------------------
DROP VIEW IF EXISTS `view_browse_list`;
CREATE  VIEW `view_browse_list` AS select `b`.`title` AS `title`,`a`.`id` AS `id`,`a`.`article_id` AS `article_id`,`a`.`browse_ip` AS `browse_ip`,`a`.`browse_time` AS `browse_time`,cast(`a`.`browse_time` as date) AS `browse_date`,`a`.`ip_location` AS `ip_location` from (`sys_browse_log` `a` join `article` `b` on((`a`.`article_id` = `b`.`id`))) order by `a`.`browse_time` desc;

-- ----------------------------
-- Records of website_tools
-- ----------------------------

-- ----------------------------
-- View structure for view_tb_article
-- ----------------------------
DROP VIEW IF EXISTS `view_tb_article`;
CREATE  VIEW `view_tb_article` AS select `a`.`id` AS `id`,`a`.`uuid` AS `uuid`,`a`.`cover` AS `cover`,`a`.`title` AS `title`,`a`.`article_describe` AS `article_describe`,`a`.`publish_time` AS `publish_time`,`a`.`edit_time` AS `edit_time`,`a`.`tag_id` AS `tag_id`,`a`.`state` AS `state`,`a`.`is_stickie` AS `is_stickie`,`a`.`article_type` AS `article_type`,`a`.`user_id` AS `user_id`,`b`.`article_content` AS `article_content`,(select (count(0) + 0) from `sys_browse_log` where (`sys_browse_log`.`article_id` = `a`.`id`)) AS `count_view`,(select (count(0) + 0) from `article_comment` where (`article_comment`.`article_id` = `a`.`id`)) AS `count_comment` from (`article` `a` left join `article_content` `b` on((`a`.`id` = `b`.`article_id`)));

-- ----------------------------
-- Records of website_tools
-- ----------------------------

-- ----------------------------
-- View structure for view_tb_tag
-- ----------------------------
DROP VIEW IF EXISTS `view_tb_tag`;
CREATE  VIEW `view_tb_tag` AS select `a`.`tag_id` AS `tag_id`,`a`.`tag_name` AS `tag_name`,`a`.`tag_logo` AS `tag_logo`,`a`.`tag_type` AS `tag_type`,`a`.`user_id` AS `user_id`,(select count(0) from `article_tag` `b` where (`b`.`tag_id` = `a`.`tag_id`)) AS `count_used` from `tags` `a`;

-- ----------------------------
-- Records of website_tools
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
