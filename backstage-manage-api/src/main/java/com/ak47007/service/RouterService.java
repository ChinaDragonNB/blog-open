package com.ak47007.service;

import java.util.List;

import com.ak47007.model.SysRouter;
import com.ak47007.model.SysUser;
import com.ak47007.model.base.Result;
import com.ak47007.model.vo.TreeNodeVO;

public interface RouterService {


    /**
     * 角色所拥有的路由菜单
     *
     * @param roleId 角色id
     */
    Result<?> findRouter(Long roleId);


    /**
     * 查询路由树
     */
    List<TreeNodeVO> routerTree();

    /**
     * 保存信息
     *
     * @param type   保存类型 1: 新增 2:修改 3:删除
     * @param router 路由信息
     */
    Result<?> save(int type, SysRouter router);


    /**
     * 路由排序列表
     */
    List<SysRouter> orderList();

    /**
     * 更新序号
     *
     * @param id    顶级菜单项编号
     * @param index 序号
     * @param type  1 上移 2 下移
     */
    Result<?> updateOrderIndex(long id, int index, int type);

    /**
     * 路由信息
     *
     * @param id id
     */
    SysRouter routerInfo(long id);

}




