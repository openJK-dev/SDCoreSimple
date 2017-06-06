# SDCoreSimple
### 1、该项目中包含两个moudle，1-app：使用demo，2-SDCore：网络请求框架
### 2、添加依赖
<pre>
  Step 1. Add it in your root build.gradle at the end of repositories:
  allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
  Step 2. Add the dependency
  dependencies {
	        compile 'com.github.ShanDianDev:SDCoreSimple:v1.0'
	}
</pre>
### 3、使用方法
<pre>
  在项目的application的onCreate方法中写入
  SDCore.getInstance()
                .setBaseUrl("")//非必需，可以在具体的网络请求中写
                .setDebug(true)//设置是否debug
                .setContext(getApplicationContext());//设置全局上下文
 具体的使用，包括get \ post \文件下载 \文件上传，具体见moudle-app
</pre>
