const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,

  // SpringBoot 와 연동 설정

  // 빌드 시 static 경로 설정
  outputDir: '../src/main/resources/static', // Build Directory
	devServer: {
		proxy: 'http://localhost:8081' // Spring Boot Server
	}
})
