import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import ManageRedactors from './views/ManageRedactors.vue'
import ManagePositions from './views/ManagePositions.vue'
import ManageTests from './views/ManageTests.vue'
import SelectTest from "./views/SelectTest";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: {
        guest: true
      }
    },
    {
      path: "/register",
      name: 'register',
      component: Register,
      meta: {
        guest: true
      }
    },
    {
      path: '/redactors',
      name: 'manage_redactors',
      component: ManageRedactors
    },
    {
      path: '/positions',
      name: 'manage_positions',
      component: ManagePositions
    },
    {
      path: '/tests',
      name: 'manage_tests',
      component: ManageTests
    },
    {
      path: '/select_test',
      name: 'select_test',
      component: SelectTest
    }
  ]
})
