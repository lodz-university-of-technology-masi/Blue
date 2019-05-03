import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import ManagePositions from './views/ManagePositions.vue'

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
      path: '/positions',
      name: 'manage_positions',
      component: ManagePositions
    }
  ]
})
