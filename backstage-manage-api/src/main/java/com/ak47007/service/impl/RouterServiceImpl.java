package com.ak47007.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ak47007.mapper.*;
import com.ak47007.model.SysRoleRouter;
import com.ak47007.model.SysRouter;
import com.ak47007.model.base.Result;
import com.ak47007.model.vo.TreeNodeVO;
import com.ak47007.service.RouterService;
import com.ak47007.utils.AssertUtil;
import com.ak47007.utils.TreeNodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author AK47007
 * @date 2019/12/11
 */
@Service
@Slf4j
@AllArgsConstructor
public class RouterServiceImpl implements RouterService {

    private final SysRouterMapper sysRouterMapper;

    private final SysRoleRouterMapper sysRoleRouterMapper;



    @Override
    public Result<?> findRouter(Long roleId) {
        List<SysRouter> routerList = sysRouterMapper.selectList(null);
        if (!routerList.isEmpty()) {
            // 找出该角色所拥有的模块
            List<SysRoleRouter> roleRouterList = sysRoleRouterMapper.findByRoleId(roleId);
            // 处理后的路由
            List<SysRouter> routerHandlerList = roleRouterList.stream().map(roleRouter ->
                    routerList.stream().filter(l -> l.getId().equals(roleRouter.getRouterId())).findFirst().orElse(null)
            ).collect(Collectors.toList());

            routerHandlerList.removeAll(Collections.singleton(null));
            // 分好层级的路由菜单
            List<SysRouter> resultRouterList = routerHandlerList.stream().filter(router -> router.getParentId().equals(0L)).map(router -> TreeNodeUtil.getRouterMenu(routerHandlerList, router)).sorted(Comparator.comparing(SysRouter::getOrderIndex)).collect(Collectors.toList());
            if (!resultRouterList.isEmpty()) {
                return Result.success(resultRouterList);
            }
        }
        return Result.error();
    }


    @Override
    public List<TreeNodeVO> routerTree() {
        // 全部路由菜单项
        List<SysRouter> routerList = sysRouterMapper.selectList(null);

        // 处理好的树
        return routerList.stream().filter(router -> router.getParentId().equals(0L))
                .map(router -> TreeNodeUtil.treeRoot(routerList, router))
                .collect(Collectors.toList());
    }

    @Override
    public Result<?> save(int type, SysRouter router) {
        int result;
        switch (type) {
            case 1:
                router.setHidden(false);
                router.setNoCache(true);
                router.setAlwaysShow(true);
                if (router.getParentId().equals(0L)) {
                    // 顶级菜单项就重新添加一个序号
                    router.setOrderIndex(sysRouterMapper.getMaxOrderIndex() + 1);
                } else {
                    // 子项的序号跟父级菜单项一致
                    Integer orderIndex = sysRouterMapper.selectById(router.getParentId()).getOrderIndex();
                    router.setOrderIndex(orderIndex);
                }

                result = sysRouterMapper.insert(router);
                if (result > 0) {
                    return Result.success("添加路由成功", router.getId());
                } else {
                    return Result.error("添加路由失败");
                }
            case 2:
                result = sysRouterMapper.updateById(router);
                if (result > 0) {
                    return Result.success("保存成功");
                } else {
                    return Result.error("保存失败");
                }
            case 3:
                result = sysRouterMapper.deleteById(router.getId());
                if (result > 0) {
                    return Result.success("删除成功");
                } else {
                    return Result.error("删除失败");
                }
            default:
                return Result.error("非法操作");
        }
    }

    @Override
    public List<SysRouter> orderList() {
        LambdaQueryWrapper<SysRouter> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(SysRouter::getOrderIndex);
        wrapper.ne(SysRouter::getOrderIndex, 0);
        wrapper.eq(SysRouter::getParentId, 0L);
        List<SysRouter> list = sysRouterMapper.selectList(wrapper);
        return list;
    }

    @Override
    public Result<?> updateOrderIndex(long id, int index, int type) {
        if (type == 1) {
            // 将上面的序号+1 将自己的-1
            if (index - 1 <= 0) {
                return Result.error("该路由已移至最顶端");
            }
            // 找出上面的路由信息
            SysRouter orderRouter = findOneByOrderIndex(index - 1);
            // 将上面的路由序号替换成现在这个路由的序号
            sysRouterMapper.updateOrderIndexById(index, orderRouter.getId());
            index--;
        } else if (type == 2) {
            // 下移就是将下面的-1 自己+1
            int maxOrderIndex = sysRouterMapper.getMaxOrderIndex();
            if (index + 1 > maxOrderIndex) {
                return Result.error("该路由已移至最顶端");
            }
            // 找出下面的路由信息
            SysRouter orderRouter = findOneByOrderIndex(index + 1);
            // 将当前序号替换成下面的序号
            index = orderRouter.getOrderIndex();
            // 将下面的路由序号-1
            sysRouterMapper.updateOrderIndexById(index - 1, orderRouter.getId());
        }
        int result = sysRouterMapper.updateOrderIndexById(index, id);
        if (result > 0) {
            return Result.success();
        }

        return Result.error("移动失败");
    }

    @Override
    public SysRouter routerInfo(long id) {
        SysRouter router = sysRouterMapper.selectById(id);
        AssertUtil.notNull(router, "该路由不存在");
        return router;
    }


    private SysRouter findOneByOrderIndex(int index) {
        LambdaQueryWrapper<SysRouter> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRouter::getOrderIndex, index);
        wrapper.eq(SysRouter::getParentId, 0L);
        return sysRouterMapper.selectOne(wrapper);
    }


}




