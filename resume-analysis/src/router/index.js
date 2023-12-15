import Vue from 'vue'
import VueRouter from 'vue-router'
import Resume from '../views/resume/index.vue'
import Index from '../components/index.vue'
Vue.use(VueRouter)
const routes = [
  {
    path: "/",
    name: "",
    meta: { title: '' },
    component: Index
  },
  {
    path: "/resume",
    name: "resume",
    meta: { title: '简历解析' },
    component: Resume
  },
  {
    path: "/contract",
    name: "contract",
    meta: { title: '合同解析' },
    component: () => import('../views/contract/index.vue'),
  }
]
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}
const router = new VueRouter({
  routes
})

//在导航前修改页面标题
router.beforeEach((to, form, next) => {
  document.title = to.meta.title || '大师请解析！！！'
  next()
})

export default router
