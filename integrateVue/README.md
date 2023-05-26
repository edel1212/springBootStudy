# Vue 와 SpringBoot 연동

### 연동 방법

- 1 . port 변경
  - Vue 와 SpringBoot의 port 번호가 같아 둘중 한곳의 포트 번호를 변경해준다.
    - 현 프로젝트에서는 SpringBoot의 포트를 변경함.

✅ application.properties 
```properties
server.port = 8081
```

- 2 . 프로젝트 내부에서 Vue project 생성
  - `vue create 프로젝트명`
- 3 . 생성된 Vue 프로젝트 내 **vue.config.js**파일에 빌드결과 생성 위치 및 프록시 주소 설정
  - 빌드 위치 설정 이유 ? : 스프링 내부 `src -> main -> resources -> static`으로 지정 하여 스프링에서 읽을 수 있게함
  - 프록시 설정 이유 ? : 프록시 위치를 잡아줘야 스프링과 연동이 가능해짐  

✅ vue.config.js
```javascript
    const { defineConfig } = require('@vue/cli-service')
    module.exports = defineConfig({
        transpileDependencies: true,
        
        // SpringBoot 와 연동 설정
        
        // 빌드 시 static 경로 설정
        outputDir: '../src/main/resources/static', // 👉 Build Directory
        devServer: {
            proxy: 'http://localhost:8081' // 👉 Spring Boot Server
        }
    })
```

- 4 . Spring Controller에서 `return "index.html"`로 접근 시 static 내부 index.html에 접근하여 vue화면 로드 

✅ Controller
```java
@Controller
@Log4j2
public class HomeController {
    @GetMapping("/")
    public String mainPage(){
        return "index.html";
    }
}
```

### Vue Router 설정하여 연동 방법

- 1 . Vue 프로젝트 내부 vue router 설치 ✅ 중요 : springBoot가 아닌 vue 프로젝트임
  - `npm install vue-router@4`
- 2 . Vue 프로젝트 내부 router.js 설정 파일 추가
  - hash mode가 아닌 history 모드를 사용
  - 하단 예외 페이지 설정 `path: '/:pathMatch(.*)*'`을 보면 `/`로 들어오는 매칭이 되지 않는 페이지를 전부 예외 컴포넌트로 전송하는데   
  여기서 중요한것이 `routes[]`의 순서이다!

✅ router.js
```javascript
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
```

- 3 . mian.js에 router.js 사용 설정

✅ main.js
```javascript
import { createApp } from 'vue'
import App from './App.vue'

import router from "./router.js";

createApp(App)
.use(router)
.mount('#app')

```


- 4 . springBoot 컨트롤러에서 이제 WhitePage에 잡히지 않게 url을 설정해준다.
✅ controller
```java
package com.integrateVue.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class HomeController {

    @GetMapping(value = {"/", "/web/**"})
    public String mainPage(){
        // return "index.html";  ❌
        return "forward:/index.html"; // 👍 forward: 로 넘겨줘야함!
    }
}
```