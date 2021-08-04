import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);


export const constantRoutes = [{
  path: '/login',
  component: () =>
    import('@/views/login/index'),
  hidden: true
}
];

export default new Router({
  mode: 'history',
  base: '/backstage/',
  routes: constantRoutes
});
