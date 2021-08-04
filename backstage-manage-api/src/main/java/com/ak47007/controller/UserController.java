package com.ak47007.controller;

import cn.hutool.core.util.StrUtil;
import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.model.SysUser;
import com.ak47007.model.base.Result;
import com.ak47007.model.query.RoleQuery;
import com.ak47007.model.query.UserQuery;
import com.ak47007.oos.AliOssService;
import com.ak47007.service.RoleService;
import com.ak47007.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * @author AK47007
 * date 2019/7/13
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping(value = "user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final AliOssService aliOssService;


    /**
     * 用户列表
     */
    @ApiOperation(value = "用户列表")
    @PostMapping(value = "list")
    public Result<PageInfo<SysUser>> list(@ApiParam @RequestBody UserQuery query) {
        List<SysUser> list = userService.findList(query);
        PageInfo<SysUser> sysUserPageInfo = new PageInfo<>(list);
        return Result.success(sysUserPageInfo);
    }

    /**
     * 添加用户
     */
    @ApiOperation(value = "添加用户")
    @PostMapping(value = "addUser")
    @OperatorLog(operModule = "系统配置-用户管理", operType = OperatorTypeEnum.INSERT, operDesc = "添加用户")
    public Result<?> addUser(@ApiParam @RequestBody SysUser user) {
        return userService.save(1, user);
    }

    /**
     * 编辑用户
     */
    @ApiOperation(value = "编辑用户")
    @PostMapping(value = "editUser")
    @OperatorLog(operModule = "系统配置-用户管理", operType = OperatorTypeEnum.EDIT, operDesc = "编辑用户信息")
    public Result<?> editUser(@ApiParam @RequestBody SysUser user) {
        return userService.save(2, user);
    }

    /**
     * 删除用户
     */
    @ApiOperation(value = "删除用户")
    @PostMapping(value = "delUser")
    @OperatorLog(operModule = "系统配置-用户管理", operType = OperatorTypeEnum.DELETE, operDesc = "删除用户")
    public Result<?> delUser(@ApiParam(name = "id", value = "用户ID", required = true) @RequestBody Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        return userService.save(3, user);
    }

    /**
     * 用户信息
     */
    @ApiOperation(value = "用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    @GetMapping(value = "userInfo")
    public Result<SysUser> userInfo(@RequestParam(value = "id") long userId) {
        SysUser user = userService.userInfo(userId);
        return Result.success(user);
    }


    /**
     * 可选角色列表
     */
    @ApiOperation(value = "角色列表", notes = "用户可选的角色列表")
    @GetMapping(value = "roleList")
    public Result<?> roleList() {
        return Result.success(roleService.list(new RoleQuery()));
    }


    /**
     * 上传头像
     */
    @ApiOperation(value = "上传头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "头像文件信息", required = true),
            @ApiImplicitParam(name = "uploadPrefix", value = "上传路径", required = true)
    })
    @PostMapping(value = "uploadAvatar")
    @OperatorLog(operModule = "系统配置-用户管理", operType = OperatorTypeEnum.UPLOAD, operDesc = "上传用户头像")
    public Result<String> uploadImage(@RequestParam(value = "image", required = false) MultipartFile file,
                                      String uploadPrefix) throws IOException {
        String url = aliOssService.uploadImage(uploadPrefix, file);
        if (StrUtil.isBlank(url)) {
            return Result.error("上传失败");
        }
        return Result.success("上传成功", url);
    }
}
