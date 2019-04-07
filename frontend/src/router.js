import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
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
      path: '/positions',
      name: 'manage_positions',
      component: ManagePositions
    }
  ]
})
