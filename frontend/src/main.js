// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router/index'
import Axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import echarts from 'echarts'
Vue.prototype.$echarts = echarts
Vue.use(ElementUI)

// Axios.defaults.baseURL = '/api'

// Axios.defaults.baseURL = process.env.API_ROOT;

Vue.prototype.$ajax = Axios

Vue.config.productionTip = false
// Vue.prototype.HOST = '/api'

/* eslint-disable no-new */
new Vue({
  // el: '#app',
  router,
  render: h => h(App),
  // components: { App },
  // template: '<App/>'
}).$mount('#app')
