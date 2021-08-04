package com.ak47007.controller;

import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.model.Link;
import com.ak47007.model.SysUser;
import com.ak47007.model.query.LinkQuery;
import com.ak47007.utils.UserUtil;
import com.ak47007.model.base.Result;
import com.ak47007.service.LinksService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author AK47007
 * @date 2019/7/13
 */
@Api(tags = "友情链接模块接口")
@RestController
@RequestMapping(value = "links")
@Slf4j
@AllArgsConstructor
public class LinksController {

    private final LinksService linksService;

    private final UserUtil userUtil;


    /**
     * 友情链接列表
     */
    @ApiOperation(value = "友情链接列表")
    @PostMapping(value = "listLink")
    public Result<PageInfo<Link>> listLink(@ApiParam @RequestBody LinkQuery query, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        query.setUserId(user.getId());
        List<Link> links = linksService.listLink(query);
        PageInfo<Link> linkPageInfo = new PageInfo<>(links);
        return Result.success(linkPageInfo);
    }

    /**
     * 链接信息
     */
    @ApiOperation(value = "链接信息")
    @ApiImplicitParam(name = "id", value = "链接ID", required = true)
    @GetMapping(value = "linkInfo")
    public Result<Link> linkInfo(@RequestParam Long id) {
        Link link = linksService.linkInfo(id);
        return Result.success(link);
    }


    /**
     * 审核链接
     */
    @ApiOperation(value = "审核链接")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "链接ID", required = true),
            @ApiImplicitParam(name = "result", value = "结果 0:不通过,1:重新审核,2:通过", required = true)
    })
    @PostMapping(value = "checkLink")
    @OperatorLog(operModule = "友情连接", operDesc = "审核链接")
    public Result<?> checkLink(@RequestParam Long id, @RequestParam int result) throws Exception {
        return linksService.checkLink(id, result);
    }

    /**
     * 修改链接信息
     */
    @ApiOperation(value = "编辑链接信息")
    @PostMapping(value = "editLink")
    @OperatorLog(operModule = "友情连接", operType = OperatorTypeEnum.EDIT, operDesc = "修改友情链接信息")
    public Result<?> editLink(@ApiParam @RequestBody Link link) {
        return linksService.editLink(link);
    }

    /**
     * 删除链接
     */
    @ApiOperation(value = "删除链接")
    @ApiImplicitParam(name = "id", value = "链接ID", required = true)
    @PostMapping(value = "delLink")
    @OperatorLog(operModule = "友情连接", operType = OperatorTypeEnum.DELETE, operDesc = "删除友情链接")
    public Result<?> delLink(@RequestParam Long id) {
        return linksService.delLink(id);
    }
}
