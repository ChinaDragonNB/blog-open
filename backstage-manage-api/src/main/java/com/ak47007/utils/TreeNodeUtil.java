package com.ak47007.utils;

import cn.hutool.core.util.StrUtil;
import com.ak47007.model.RouterMeta;
import com.ak47007.model.SysAuthority;
import com.ak47007.model.SysRouter;
import com.ak47007.model.vo.TreeNodeVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AK47007
 * date： 2019/11/20
 */
public class TreeNodeUtil {

    /**
     * 权限管理树,做了处理
     *
     * @param sourceList 所有角色数据
     * @param rootMenu   第一级角色数据
     * @return 返回节点及它的子节点
     */
    public static TreeNodeVO treeRoot(List<SysAuthority> sourceList, SysAuthority rootMenu) {
        if (sourceList == null) {
            return null;
        }
        //第一级
        TreeNodeVO treeNodeVO = new TreeNodeVO();
        treeNodeVO.setId(rootMenu.getId());
        treeNodeVO.setLabel(rootMenu.getAuthorityNameCn());
        treeNodeVO.setDisabled(!rootMenu.getState());
        //子级
        List<TreeNodeVO> childList = new ArrayList<>();
        for (SysAuthority menu : sourceList) {
            if (rootMenu.getId().equals(menu.getParentId())) {
                //子级节点信息
                TreeNodeVO menuChild = treeRoot(sourceList,menu);
                menuChild.setId(menu.getId());
                menuChild.setLabel(menu.getAuthorityNameCn());
                menuChild.setDisabled(!menu.getState());
                childList.add(menuChild);
            }
        }
        if (childList.size() == 0) {
            return treeNodeVO;
        }
        //添加子级项
        treeNodeVO.setChildren(childList);
        return treeNodeVO;
    }

    /**
     * 路由理树,做了处理
     *
     * @param sourceList 所有角色数据
     * @param rootMenu   第一级角色数据
     * @return 返回节点及它的子节点
     */
    public static TreeNodeVO treeRoot(List<SysRouter> sourceList, SysRouter rootMenu) {
        if (sourceList == null) {
            return null;
        }
        //第一级
        TreeNodeVO treeNodeVO = new TreeNodeVO();
        treeNodeVO.setId(rootMenu.getId());
        treeNodeVO.setLabel(rootMenu.getTitle());
        treeNodeVO.setDisabled(false);
        //子级
        List<TreeNodeVO> childList = new ArrayList<>();
        for (SysRouter menu : sourceList) {
            if (rootMenu.getId().equals(menu.getParentId())) {
                //子级节点信息
                TreeNodeVO menuChild = treeRoot(sourceList,menu);
                menuChild.setId(menu.getId());
                menuChild.setLabel(menu.getTitle());
                menuChild.setDisabled(false);
                childList.add(menuChild);
            }
        }
        if (childList.size() == 0) {
            return treeNodeVO;
        }
        //添加子级项
        treeNodeVO.setChildren(childList);
        return treeNodeVO;
    }

    /**
     * 获取路由菜单,不做处理
     *
     * @param routerList 路由集合
     * @param rootRouter 顶级路径菜单对象
     * @return 分层好的路由菜单
     */
    public static SysRouter getRouterMenu(List<SysRouter> routerList,SysRouter rootRouter) {
        if (routerList == null) {
            return null;
        }
        setMeta(rootRouter);
        List<SysRouter> childrenList = new ArrayList<>();
        for (SysRouter router : routerList) {
            // 如果某项的父级ID等于顶级菜单项的ID
            if (router.getParentId().equals(rootRouter.getId())) {
                SysRouter routerMenu = getRouterMenu(routerList,router);
                setMeta(routerMenu);
                childrenList.add(routerMenu);
            }
        }
        if (childrenList.size() > 0) {
            rootRouter.setChildren(childrenList);
        }
        return rootRouter;
    }

    /**
     * 注入 meta
     *
     * @param router 路由对象
     */
    private static void setMeta(SysRouter router) {
        // 有icon图标/title,并且noCache为true(默认为false) 才设置meta
        RouterMeta meta = new RouterMeta();

        if (StrUtil.isNotBlank(router.getIcon())) {
            meta.setIcon(router.getIcon());
        }
        if (StrUtil.isNotBlank(router.getTitle())) {
            meta.setTitle(router.getTitle());
        }
        if (!router.getNoCache()) {
            meta.setNoCache(router.getNoCache());
        }
        // 如果子级菜单是隐藏的话
        if (!router.getParentId().equals(0L) && router.getHidden()) {
            meta.setActiveMenu(router.getActiveMenu());
        }
        router.setMeta(meta);
    }
}
