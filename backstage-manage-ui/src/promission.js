import router from './router'
import { getRouterApi } from '@/api/router'

// 获取组件的方法
const _import = require('./router/_import_' + process.env.NODE_ENV)
// Layout 是架构组件，不在后台返回，在文件里单独引入
import Layout from '@/layout'
// Token工具
import { getToken } from '@/utils/auth'

import {
    Message
} from 'element-ui'

// 用来获取后台拿到的路由
var getRouter
router.beforeEach((to, from, next) => {
    // 略过登录页
    if (to.path == '/login') {
        next()
    } else {
        const token = getToken()
        // 如果取出来的token为null的话,就代表没有登录.
        if (token == null) {
            // Message({
            //   message: '请重新登录',
            //   type: 'error',
            //   duration: 2000
            // })
            router.push('/login')
        } else {
            //不加这个判断，路由会陷入死循环
            if (!getRouter) {
                if (!getJSON('router')) {
                    findRouter(to, next)
                } else {  // 从localStorage拿到了路由
                    // 拿到路由
                    getRouter = getJSON('router')
                    routerGo(to, next)
                }
            } else {
                if (global.antRouter == '') {
                    findRouter(to, next)
                } else {
                    next()
                }
            }
        }
    }
})

function findRouter(to, next) {
    getRouterApi().then(res => {
        if (res.data.resultCode == 1) {
            getRouter = res.data.resultData
            // 存储路由到localStorage
            setJSON('router', getRouter)
            // 执行路由跳转方法
            routerGo(to, next)
        }
    })

}

/**
 * 路由跳转
 * @param to
 * @param next
 */
function routerGo(to, next) {
    // 过滤路由
    getRouter = filterAsyncRouter(getRouter)
    //动态添加路由
    router.addRoutes(getRouter)
    //将路由数据传递给全局变量，做侧边栏菜单渲染工作
    global.antRouter = getRouter
    next({ ...to, replace: true })
}

/**
 * 路由过滤
 * @param asyncRouterMap
 * @returns {*}
 */
function filterAsyncRouter(asyncRouterMap) { //遍历后台传来的路由字符串，转换为组件对象
    const accessedRouters = asyncRouterMap.filter(route => {
        if (route.component) {
            if (route.component === 'Layout') { //Layout组件特殊处理
                route.component = Layout
            } else {
                route.component = _import(route.component)
            }
        }
        if (route.children && route.children.length) {
            route.children = filterAsyncRouter(route.children)
        }
        return true
    })

    return accessedRouters
}

// localStorage 存储数组对象的方法
function setJSON(key, obj) {
    localStorage.setItem(key, JSON.stringify(obj))
}

// localStorage 获取数组对象的方法
function getJSON(key) {
    return JSON.parse(window.localStorage.getItem(key))
}
