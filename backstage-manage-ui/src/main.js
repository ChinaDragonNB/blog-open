import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-green/index.css'
import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // 英文
import locale from 'element-ui/lib/locale/lang/zh-CN' // 中文
Vue.use(ElementUI, { locale })
import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import '@/icons' // icon
// 全局
import '@/utils/global'
import './promission'//这里进行路由后台获取的模拟

// mavon
import 'mavon-editor/dist/css/index.css'
import editor from 'mavon-editor'
import VueCookies from 'vue-cookies'
Vue.use(VueCookies)

Vue.use(editor)

Vue.config.productionTip = false
new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})
