// 👉 import 할 시 from "" 경로가 아닌 라이브러리명을 장성 시 해당 라이브러리를 불러온다
import { createWebHistory, createRouter } from "vue-router";

import Content from "./components/Content.vue";
import Test from "./components/Test.vue";
import NotFound from "./components/errorPage/NotFound.vue";

const routes = [
    {
        path: "/",
        component: Content,
      },
    {
      path: "/web/test",
      component: Test,
    },
    {
      path: "/web/notFound",
      name: "notFound",
      component: NotFound
    },
    {
      path: '/:pathMatch(.*)*',
      component: NotFound
    },
  
  ];

const router = createRouter({
    history: createWebHistory(),
    routes,
  });
  
  export default router;  