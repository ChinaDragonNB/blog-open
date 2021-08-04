package com.ak47007.controller;

import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.model.SysAuthority;
import com.ak47007.model.SysRole;
import com.ak47007.model.SysUser;
import com.ak47007.model.dto.RoleDTO;
import com.ak47007.model.query.RoleQuery;
import com.ak47007.model.vo.RoleInfoVO;
import com.ak47007.model.vo.TreeNodeVO;
import com.ak47007.service.AuthorityService;
import com.ak47007.service.RoleService;
import com.ak47007.service.RouterService;
import com.ak47007.utils.UserUtil;
import com.ak47007.model.base.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ak47007
 * @date 2019/11/20
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping(value = "role")
@Slf4j
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    private final AuthorityService authorityService;

    private final UserUtil userUtil;

    private final RouterService routerService;


    /**
     * 角色列表
     */
    @ApiOperation(value = "角色列表")
    @PostMapping(value = "list")
    public Result<PageInfo<SysRole>> list(@ApiParam @RequestBody RoleQuery query) {
        List<SysRole> list = roleService.list(query);
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }


    /**
     * 角色信息
     */
    @ApiOperation(value = "角色信息")
    @ApiImplicitParam(name = "id", value = "角色ID")
    @GetMapping(value = "roleInfo")
    public Result<RoleInfoVO> findById(@RequestParam(value = "id") long roleId) {

        SysRole role = roleService.roleInfo(roleId);
        if (role != null) {

            RoleInfoVO result = new RoleInfoVO();
            result.setState(role.getState());
            result.setRoleNameCn(role.getRoleNameCn());
            result.setRoleNameEn(role.getRoleNameEn());
            List<SysAuthority> authorityList = role.getAuthorityList();
            if (!authorityList.isEmpty()) {
                result.setAuthorityDefaultChecked(authorityList.stream().map(SysAuthority::getId).collect(Collectors.toList()));
            }
            List<Long> routerIds = role.getRouterIds();
            if (routerIds != null && !routerIds.isEmpty()) {
                result.setRouterDefaultChecked(routerIds);
            }
            return Result.success(result);
        }
        return Result.error();
    }

    /**
     * 添加角色
     */
    @ApiOperation(value = "添加角色")
    @PostMapping(value = "addRole")
    @OperatorLog(operModule = "系统配置-角色管理", operType = OperatorTypeEnum.INSERT, operDesc = "添加角色")
    public Result<?> addRole(@ApiParam @RequestBody RoleDTO dto,HttpServletRequest request) {
        setUser(dto,request);
        return roleService.save(1,dto);
    }

    /**
     * 编辑角色
     */
    @ApiOperation(value = "编辑角色")
    @PostMapping(value = "editRole")
    @OperatorLog(operModule = "系统配置-角色管理", operType = OperatorTypeEnum.EDIT, operDesc = "编辑角色信息")
    public Result<?> editRole(@ApiParam @RequestBody RoleDTO dto,HttpServletRequest request) {
        setUser(dto,request);
        return roleService.save(2,dto);
    }

    /**
     * 删除角色
     */
    @ApiOperation(value = "删除角色")
    @PostMapping(value = "delRole")
    @OperatorLog(operModule = "系统配置-角色管理", operType = OperatorTypeEnum.DELETE, operDesc = "删除角色")
    public Result<?> delRole(@ApiParam(name = "id", value = "角色ID", required = true) @RequestBody Long id,HttpServletRequest request) {
        RoleDTO dto = new RoleDTO();
        SysRole role = new SysRole();
        role.setId(id);
        dto.setRole(role);
        setUser(dto,request);
        return roleService.save(3,dto);
    }

    private void setUser(RoleDTO dto,HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        dto.setUser(user);
    }


    /**
     * 权限列表
     */
    @ApiOperation(value = "权限列表", notes = "角色可选择的权限列表")
    @GetMapping(value = "authorityTree")
    public Result<List<TreeNodeVO>> authorityTree() {
        List<TreeNodeVO> list = authorityService.authorityTree();
        return Result.success(list);
    }


    /**
     * 路由列表
     */
    @ApiOperation(value = "模块菜单列表", notes = "角色可选择的模块菜单列表")
    @GetMapping(value = "routerTree")
    public Result<List<TreeNodeVO>> routerTree() {
        List<TreeNodeVO> list = routerService.routerTree();
        return Result.success(list);
    }
}
