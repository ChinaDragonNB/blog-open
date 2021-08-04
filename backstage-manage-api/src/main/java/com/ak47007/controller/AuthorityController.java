package com.ak47007.controller;

import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.model.SysApi;
import com.ak47007.model.SysAuthority;
import com.ak47007.model.SysUser;
import com.ak47007.model.dto.AuthorityDTO;
import com.ak47007.model.base.Result;
import com.ak47007.model.vo.AuthorityInfoVO;
import com.ak47007.model.vo.TreeNodeVO;
import com.ak47007.service.ApiService;
import com.ak47007.utils.UserUtil;
import com.ak47007.service.AuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Ak47007
 * @date 2019/11/21
 */
@Api(tags = "权限管理接口")
@RestController
@RequestMapping(value = "authority")
@Slf4j
@AllArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    private final UserUtil userUtil;

    private final ApiService apiService;


    /**
     * 返回树
     */
    @ApiOperation(value = "权限列表", notes = "树形显示")
    @GetMapping(value = "tree")
    public Result<List<TreeNodeVO>> findTree() {
        List<TreeNodeVO> tree = authorityService.authorityTree();
        return Result.success(tree);
    }

    /**
     * 权限信息
     */
    @ApiOperation(value = "权限信息")
    @ApiImplicitParam(name = "id", value = "权限ID", required = true)
    @GetMapping(value = "authorityInfo")
    public Result<AuthorityInfoVO> authorityInfo(@RequestParam(value = "id") long id) {
        SysAuthority authority = authorityService.authorityInfo(id);
        AuthorityInfoVO vo = new AuthorityInfoVO();
        vo.setAuthorityNameCn(authority.getAuthorityNameCn());
        vo.setAuthorityNameEn(authority.getAuthorityNameEn());
        vo.setState(authority.getState());
        vo.setApiIds(authority.getApiIds());
        return Result.success(vo);
    }

    /**
     * 添加权限
     */
    @ApiOperation(value = "添加权限")
    @PostMapping(value = "addAuthority")
    @OperatorLog(operModule = "系统管理-权限管理", operType = OperatorTypeEnum.INSERT, operDesc = "添加权限")
    public Result<?> addAuthority(@ApiParam @RequestBody AuthorityDTO dto, HttpServletRequest request) {
        SysUser user = userUtil.getUser(request);
        dto.getAuthority().setCreateUser(user.getId());
        return authorityService.save(1, dto);
    }

    /**
     * 编辑权限
     */
    @ApiOperation(value = "编辑权限")
    @PostMapping(value = "editAuthority")
    @OperatorLog(operModule = "系统管理-权限管理", operType = OperatorTypeEnum.EDIT, operDesc = "编辑权限")
    public Result<?> editAuthority(@ApiParam @RequestBody AuthorityDTO dto) {
        return authorityService.save(2, dto);
    }

    /**
     * 删除权限
     */
    @ApiOperation(value = "删除权限")
    @PostMapping(value = "delAuthority")
    @OperatorLog(operModule = "系统管理-权限管理", operType = OperatorTypeEnum.DELETE, operDesc = "删除权限")
    public Result<?> delAuthority(@ApiParam(name = "id", value = "权限ID", required = true) @RequestBody Long id) {
        AuthorityDTO dto = new AuthorityDTO();
        SysAuthority sysAuthority = new SysAuthority();
        sysAuthority.setId(id);
        dto.setAuthority(sysAuthority);
        return authorityService.save(3, dto);
    }


    /**
     * 查询所有接口
     */
    @ApiOperation(value = "接口列表")
    @GetMapping(value = "listApi")
    public Result<List<SysApi>> findApi() {
        List<SysApi> apiList = apiService.apiList();
        return Result.success(apiList);
    }


}
