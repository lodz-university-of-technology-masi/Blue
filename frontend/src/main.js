import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Axios from 'axios'
import Vuex from 'vuex'
import VueHtml2Canvas from 'vue-html2canvas';
import BootstrapVue from 'bootstrap-vue'
import browserDetect from "vue-browser-detect-plugin";
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'




Vue.prototype.$http = Axios;

Vue.use(BootstrapVue);
Vue.use(Vuex);
Vue.use(VueHtml2Canvas);
Vue.use(browserDetect);

Vue.config.productionTip = false;

Axios.interceptors.response.use(response => {
  return response
}, error => {
  if(error.response.status === 401) {
    router.push('/login');
    return Promise.reject(error)
  } else if (error.response.status === 403) {
    router.push('/not_authorized');
    return Promise.reject(error);
  } else if (error.response.status !== 400) {
    router.push('/error');
    return Promise.reject(error);
  }
});


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
