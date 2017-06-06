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
 具体的使用，包括get \ post \文件下载 \文件上传，
 </pre>
 
 #### get请求，url = http://douban.api/6548683?no=122
 <pre>
 	HashMap<String, String> map = new HashMap<>();//参数
	map.put("no","122");
        RetrofitService.newBuilder(MainActivity.this);//创建配置类，MainActivity.this为当前activity的上下文
                .showLoading(true)//是否显示加载框（默认显示）
                .loadType(RetrofitService.LOADTYPE.CIRCLE)//加载框类型（圆形和水平进度条）
                .baseUrl(SDCore.getInstance().getBaseUrl())//设置baseUrl（默认为SDCore中设置的url）
                .connectTimeout(10) //连接超时时间
                .readTimeout(10)//读取超时时间
                .writeTimeout(10)//写入超时时间
                .showToast(true)//是否显示错误提示（默认显示）
                .build()//配置类完成
                .getRequest(new JsonCallBack() {
                    @Override
                    public void onSuccess(JSONObject dataInfo) {
                        if (dataInfo != null) {
                            tv.setText(dataInfo.toString());
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                }, "6548683", map);//get请求，JsonCallBack结果回调，"6548683"为拼接在url后面的，map为参数
</pre>
#### post url=http://douban.api/login/login?format=json&
<pre>
	final HashMap<String, String> prams = new HashMap<>();//post参数
        prams.put("mobile", "15751164916");
        prams.put("password", "1234567");
        prams.put("isBoss", "0");
        HashMap<String, String> defaultPrams = new HashMap<String, String>();//拼接在url后面的参数
        defaultPrams.put("format", "json");
        RetrofitService.newBuilder(MainActivity.this)
                .showLoading(true)//是否显示加载框（默认显示）
                .loadType(RetrofitService.LOADTYPE.CIRCLE)//加载框类型（圆形和水平进度条）
                .baseUrl(SDCore.getInstance().getBaseUrl())//设置baseUrl（默认为SDCore中设置的url）
                .connectTimeout(10) //
                .readTimeout(10)//
                .writeTimeout(10)//
                .showToast(true)//是否显示错误提示（默认显示）
                .build()
                .postRequest(new JsonCallBack() {
                    @Override
                    public void onSuccess(JSONObject dataInfo) {
                        tv.setText(dataInfo.toString());
                    }

                    @Override
                    public void onError(String message) {

                    }
                }, "login/login", defaultPrams, prams);
</pre>
#### 文件下载 url = http://douban.api/downloadUrl
<pre>
RetrofitService.newBuilder(MainActivity.this)
                .showLoading(true)
                .loadType(RetrofitService.LOADTYPE.HORIZONTAL)
		.baseUrl(SDCore.getInstance().getBaseUrl())//设置baseUrl（默认为SDCore中设置的url）
                .build()
                .downloadFile(new FileCallBack() {
                    @Override
                    public void onProcessUpdate(int value) {
                        Log.d("1--", value + "");
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("1--", "success");
                    }

                    @Override
                    public void onError() {
                        Log.d("1--", "error");
                    }
                }, downloadUrl, "/sdcard/sdcore/", "abc.apk");//结果回调，拼接在URL后面的字符串，存储路径，存储的文件名
</pre>

#### 文件上传
<pre>
	ArrayList<File> files = new ArrayList<File>();
        files.add(new File("/sdcard/123.png"));//文件集合
	RetrofitService.newBuilder(MainActivity.this)
		.baseUrl(SDCore.getInstance().getBaseUrl())//设置baseUrl（默认为SDCore中设置的url）
                .build()
                .uploadFiles(new JsonCallBack() {
                    @Override
                    public void onSuccess(JSONObject dataInfo) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                }, "upload",files);//结果回调，拼接在url后面的字符串，文件集合
</pre>
</pre>
</pre>
