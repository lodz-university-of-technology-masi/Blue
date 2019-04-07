import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Axios from 'axios'
import Vuex from 'vuex'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.prototype.$http = Axios;

Vue.use(BootstrapVue);
Vue.use(Vuex);

Vue.config.productionTip = false;



const loadToken = (keyName) => {
  try {
    const loadedToken = localStorage.getItem(keyName);
    if(loadedToken === null) {
      return undefined;
    }
    return loadedToken;
  } catch(err) {
    return undefined;
  }
};


const store = new Vuex.Store({
  state: {
    token: loadToken('jwt'),
    authorities: loadToken('authorities'),
  },
  mutations: {
    updateAuthorities: state => {
      state.token = loadToken('jwt');
      state.authorities = loadToken('authorities')
    }
  }
});

new Vue({
  router,
  render: h => h(App),
  store,
  methods: {
    updateAuthorities() {
      store.commit('updateAuthorities')
    }
  }
}).$mount('#app');
