package com.loosoo100.campus100.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyHttpUtils {

    /**
     * 使用xutils上传图片到服务器
     *
     * @param uploadHost 服务器地址
     * @param picUrl     图片本地地址
     */
    public static void uploadImageXUtils(String uploadHost, String picUrl) {
        // 使用Xutils上传图片
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                });
    }

    public static void uploadManyImageXUtils(String uploadHost,
                                             List<String> imgList) {
        // 使用Xutils上传图片
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        for (int i = 0; i < imgList.size(); i++) {
            // 这里拼成【attachment[i]】的形式，并加上【multipart/form-data】属性
//			params.addBodyParameter("attachment[" + i + "]",
//					new File(imgList.get(i)), "multipart/form-data");
            params.addBodyParameter("pic" + i,
                    new File(imgList.get(i)), "image/jpeg");
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                });
    }

    /**
     * 使用xutils下载
     *
     * @param downloadHost 下载的路径
     * @param url          存放本地路径
     */
    public static void downloadXUtils(String downloadHost, String url) {
        File file = new File(url);
        if (!file.exists()) {
            file.mkdirs();
        }
        HttpUtils http = new HttpUtils();
        HttpHandler handler = http.download(downloadHost, url, true, true,
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        LogUtils.d("开始下载");
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        LogUtils.d("正在下载");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        LogUtils.d("下载成功");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.d("下载失败" + msg);
                    }
                });
    }

    /**
     * 使用OkHttp上传图片(未完善)
     *
     * @param uploadHost 服务器地址
     * @param picUrl     图片本地地址
     */
    public static void uploadImageOkhttp(String uploadHost, String picUrl) {
        File file = new File(picUrl);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(
                MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition",
                                "form-data; name=\"pic\";filename=\"aa.png\""),
                        fileBody).build();

        Request request = new Request.Builder().url(uploadHost)
                .post(requestBody).build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
            }
        });
    }

    /**
     * okhttp post请求
     *
     * @param urlHost 请求地址
     */
    public static void doHttpPostOkhttp(String urlHost) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userName", "小刘收到吗");
        Request request = new Request.Builder().url(urlHost)
                .post(builder.build()).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
            }
        });
    }

    /**
     * okhttp get请求
     *
     * @param urlHost 请求地址
     */
    public static void doHttpGetOkhttp(String urlHost) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(urlHost).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    /**
     * 普通post请求
     *
     * @param url     地址
     * @param content 数据内容
     */
    public static String postData(String url, String content) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            OutputStream out = conn.getOutputStream();
            out.write(content.getBytes());
            out.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
