package com.ak47007.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ak47007.mapper.SysAuthorityApiMapper;
import com.ak47007.mapper.SysAuthorityMapper;
import com.ak47007.mapper.SysRoleAuthorityMapper;
import com.ak47007.model.SysAuthorityApi;
import com.ak47007.model.SysAuthority;
import com.ak47007.model.SysRoleAuthority;
import com.ak47007.model.base.Result;
import com.ak47007.model.dto.AuthorityDTO;
import com.ak47007.model.vo.AuthorityApiVO;
import com.ak47007.model.vo.TreeNodeVO;
import com.ak47007.service.AuthorityService;
import com.ak47007.utils.AssertUtil;
import com.ak47007.utils.TreeNodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @uthor： Ak47007
 * CreateDate： 2019/11/21
 */
@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final SysAuthorityMapper sysAuthorityMapper;

    private final SysRoleAuthorityMapper sysRoleAuthorityMapper;

    private final SysAuthorityApiMapper sysAuthorityApiMapper;


    @Override
    public List<TreeNodeVO> authorityTree() {
        LambdaQueryWrapper<SysAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(SysAuthority::getOrder);
        // 全部数据
        List<SysAuthority> authorityList = sysAuthorityMapper.selectList(wrapper);
        // ElementUI 树形格式数据
        List<TreeNodeVO> treeNodeList = authorityList.stream()
                .filter(authority -> authority.getParentId().equals(0L))
                .map(authority -> TreeNodeUtil.treeRoot(authorityList, authority))
                .collect(Collectors.toList());
        return treeNodeList;
    }


    @Override
    public SysAuthority authorityInfo(long id) {
        SysAuthority sysAuthority = sysAuthorityMapper.selectById(id);
        AssertUtil.notNull(sysAuthority, "权限信息获取失败");
        List<AuthorityApiVO> authorityApiList = sysAuthorityApiMapper.findByAuthorityId(id);
        List<Long> apiIds = authorityApiList.stream().map(AuthorityApiVO::getApiId).collect(Collectors.toList());
        sysAuthority.setApiIds(apiIds);
        return sysAuthority;
    }

    @Override
    public Result<Long> save(int type, AuthorityDTO dto) {
        List<Long> apiIds = dto.getApiIds();
        SysAuthority authority = dto.getAuthority();

        List<SysAuthority> childNodes = null;
        List<SysRoleAuthority> roleAuthorityList = null;
        int result = 0;

        Long authorityId = authority.getId();
        switch (type) {
            case 1:
                authority.setCreateTime(LocalDateTime.now());
                Integer maxOrder = sysAuthorityMapper.getMaxOrder(authority.getParentId());
                authority.setOrder(maxOrder);
                result = sysAuthorityMapper.insert(authority);
                if (result > 0) {
                    authorityId = authority.getId();
                    return Result.success("保存成功", authorityId);
                }
                for (Long apiId : apiIds) {
                    SysAuthorityApi authorityApi = new SysAuthorityApi();
                    authorityApi.setApiId(apiId);
                    authorityApi.setAuthorityId(authorityId);
                    sysAuthorityApiMapper.insert(authorityApi);
                }
                break;
            case 2:
                authority.setParentId(null);
                //如果该节点下有子节点，并且该节点选择了锁定，则不进行操作
                childNodes = findAllByParentId(authorityId);
                boolean isDisabled = !authority.getState();
                if (!childNodes.isEmpty() && isDisabled) {
                    return Result.error("保存失败，该节点下还有子节点,不能锁定该节点");
                }
                roleAuthorityList = getRoleAuthorityList(authorityId);
                if (!roleAuthorityList.isEmpty() && isDisabled) {
                    return Result.error("保存失败，该节点已被使用,不能锁定该节点");
                }
                sysAuthorityMapper.updateById(authority);
                // 先删掉以前的权限与接口,然后再新增
                sysAuthorityApiMapper.deleteByAuthorityId(authorityId);
                for (Long apiId : apiIds) {
                    SysAuthorityApi authorityApi = new SysAuthorityApi();
                    authorityApi.setApiId(apiId);
                    authorityApi.setAuthorityId(authorityId);
                    sysAuthorityApiMapper.insert(authorityApi);
                }
                return Result.success("保存成功");
            case 3:
                //先找一找该节点还有没有子节点
                childNodes = findAllByParentId(authorityId);
                if (!childNodes.isEmpty()) {
                    return Result.error("删除失败，该节点下还有子节点");
                }
                roleAuthorityList = getRoleAuthorityList(authorityId);
                if (!roleAuthorityList.isEmpty()) {
                    return Result.error("删除失败，该节点已被使用");
                }
                result = sysAuthorityMapper.deleteById(authorityId);
                if (result > 0) {
                    return Result.success("删除成功");
                }
                break;
            default:
                return Result.error("非法操作");
        }
        return Result.error("操作失败");
    }

    @Override
    public List<SysAuthority> findAllByParentId(Long parentId) {
        LambdaQueryWrapper<SysAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysAuthority::getParentId, parentId);
        return sysAuthorityMapper.selectList(wrapper);
    }

    @Override
    public List<SysAuthority> getCommon() {
        LambdaQueryWrapper<SysAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(SysAuthority::getOrder);
        wrapper.eq(SysAuthority::getIsCommon, Boolean.TRUE);
        // 全部数据
        List<SysAuthority> authorityList = sysAuthorityMapper.selectList(wrapper);
        return authorityList;
    }

    /**
     * 该权限被哪些角色使用
     *
     * @param authorityId 权限id
     */
    private List<SysRoleAuthority> getRoleAuthorityList(Long authorityId) {
        LambdaQueryWrapper<SysRoleAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRoleAuthority::getAuthorityId, authorityId);
        return sysRoleAuthorityMapper.selectList(wrapper);
    }


}


