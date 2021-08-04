package com.ak47007.controller;

import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.model.SysRouter;
import com.ak47007.model.SysUser;
import com.ak47007.model.base.Result;
import com.ak47007.model.vo.TreeNodeVO;
import com.ak47007.service.RouterService;
import com.ak47007.utils.UserUtil;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author AK47007
 * @date 2019/12/10
 * Describe: 路由接口
 */
@Api(tags = "路由管理接口")
@RestController
@RequestMapping(value = "router")
@AllArgsConstructor
public class RouterController {

    private final RouterService routerService;

    private final UserUtil userUtil;


    /**
     * 查询路由树
     */
    @ApiOperation(value = "路由列表", notes = "树形显示")
    @GetMapping(value = "tree")
    public Result<List<TreeNodeVO>> tree() {
        List<TreeNodeVO> treeNodeVOList = routerService.routerTree();
        return Result.success(treeNodeVOList);
    }

    /**
     * 路由信息
     */
    @ApiOperation(value = "路由信息")
    @ApiImplicitParam(name = "id", value = "路由ID", required = true)
    @GetMapping(value = "routerInfo")
    public Result<SysRouter> routerInfo(@RequestParam(value = "id") long id) {
        SysRouter router = routerService.routerInfo(id);
        return Result.success(router);

    }

    /**
     * 添加路由
     */
    @ApiOperation(value = "添加路由")
    @PostMapping(value = "addRouter")
    @OperatorLog(operModule = "系统配置-路由管理", operType = OperatorTypeEnum.INSERT, operDesc = "添加路由")
    public Result<?> addRouter(@ApiParam @RequestBody SysRouter router, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        router.setCreateUser(user.getId());
        return routerService.save(1, router);
    }

    /**
     * 编辑路由信息
     */
    @ApiOperation(value = "编辑路由")
    @PostMapping(value = "editRouter")
    @OperatorLog(operModule = "系统配置-路由管理", operType = OperatorTypeEnum.EDIT, operDesc = "编辑路由信息")
    public Result<?> editRouter(@ApiParam @RequestBody SysRouter router) {
        return routerService.save(2, router);
    }

    /**
     * 删除路由
     */
    @ApiOperation(value = "删除路由")
    @PostMapping(value = "deleteRouter")
    @OperatorLog(operModule = "系统配置-路由管理", operType = OperatorTypeEnum.DELETE, operDesc = "删除路由")
    public Result<?> deleteRouter(@ApiParam(name = "id", value = "路由ID", required = true) @RequestBody Long id) {
        SysRouter router = new SysRouter();
        router.setId(id);
        return routerService.save(3, router);
    }

    /**
     * 路由排序列表
     */
    @ApiOperation(value = "路由列表", notes = "排序的时候显示的列表")
    @GetMapping(value = "menuList")
    public Result<List<SysRouter>> menuList() {
        List<SysRouter> routerList = routerService.orderList();
        return Result.success(routerList);
    }

    /**
     * 更新序号
     */
    @ApiOperation(value = "排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "路由ID", required = true),
            @ApiImplicitParam(name = "index", value = "排序序号", required = true),
            @ApiImplicitParam(name = "type", value = "类型 1:上移 2:下移", required = true)
    })
    @PostMapping(value = "updateSort")
    @OperatorLog(operModule = "系统配置-路由管理", operType = OperatorTypeEnum.EDIT, operDesc = "路由排序")
    public Result<?> updateSort(@RequestParam(value = "id") long id, @RequestParam(value = "index") int index, @RequestParam(value = "type") int type) {
        return routerService.updateOrderIndex(id, index, type);
    }


    /**
     * 角色所拥有的路由菜单
     */
    @ApiOperation(value = "菜单列表", notes = "角色所拥有的路由菜单列表")
    @GetMapping(value = "getRouter")
    public Result<?> getRouter(HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        return routerService.findRouter(user.getRoleId());
    }


}
