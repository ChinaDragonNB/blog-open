package com.ak47007.controller;

import com.ak47007.model.SysUser;
import com.ak47007.model.base.Result;
import com.ak47007.model.vo.UserInfoVO;
import com.ak47007.service.ValidateCodeService;
import com.ak47007.utils.LocationUtil;
import com.ak47007.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "认证接口")
@RestController
@RequestMapping(value = "auth")
@AllArgsConstructor
public class AuthController {

    private final ValidateCodeService validateCodeService;

    private final UserUtil userUtil;

    private final LocationUtil locationUtil;


    /**
     * 生成验证码
     */
    @ApiOperation(value = "获取验证码")
    @ApiImplicitParam(name = "key", value = "验证码key", required = true)
    @GetMapping(value = "captcha", produces = "image/png")
    public void captcha(String key, HttpServletResponse response) throws Exception {
        this.validateCodeService.create(key, response);
    }


    /**
     * 当前用户信息
     */
    @ApiOperation(value = "获取当前登陆用户信息")
    @GetMapping(value = "getUserInfo")
    public Result<UserInfoVO> navBar(HttpServletRequest request) {
        SysUser user = getUser(request);
        String locationInfo = locationUtil.locationInfo(request);
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        vo.setPosition(locationInfo);
        return Result.success(vo);
    }

    private SysUser getUser(HttpServletRequest request) {
        return userUtil.getUser(request);
    }
}
