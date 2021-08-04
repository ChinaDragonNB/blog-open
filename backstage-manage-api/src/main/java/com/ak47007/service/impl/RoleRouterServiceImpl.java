package com.ak47007.service.impl;

import com.ak47007.mapper.SysRoleRouterMapper;
import com.ak47007.model.SysRoleRouter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.ak47007.service.RoleRouterService;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleRouterServiceImpl implements RoleRouterService {

    private final SysRoleRouterMapper sysRoleRouterMapper;

    @Override
    public List<SysRoleRouter> findByRoleId(Long roleId) {
        return sysRoleRouterMapper.findByRoleId(roleId);
    }

    @Override
    public void insertBatchData(Long roleId,List<Long> routerIds) {
        if (roleId == null || routerIds.isEmpty()) {
            return;
        }
        // 该角色现有的路由
        List<SysRoleRouter> existList = sysRoleRouterMapper.findByRoleId(roleId);
        for (Long routerId : routerIds) {
            SysRoleRouter obj = existList.stream().filter(l -> l.getRouterId().equals(routerId)).findFirst().orElse(null);
            // 如果不存在这个路由就新增
            if (obj == null) {
                SysRoleRouter roleRouter = new SysRoleRouter();
                roleRouter.setRoleId(roleId);
                roleRouter.setRouterId(routerId);
                sysRoleRouterMapper.insert(roleRouter);
            }
        }

    }

}
