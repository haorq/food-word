package com.meiyuan.catering.core.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.aliyun.oss.common.utils.HttpUtil.urlEncode;

/**
 * 向指定 URL 发送POST方法的请求
 *
 * @author admin
 */
@Slf4j
public class HttpUtil {
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @params url    发送请求的 URL
     * @params paramss 请求的参数集合
     * @return 远程资源的响应结果
     */
    @SuppressWarnings("unused")
    public static String sendPost(String url, Map<String, String> paramss) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            // 发送请求参数
            if (paramss != null) {
                StringBuilder params = new StringBuilder();
                for (Map.Entry<String, String> entry : paramss.entrySet()) {
                    if (params.length() > 0) {
                        params.append("&");
                    }
                    params.append(entry.getKey());
                    params.append("=");
                    params.append(entry.getValue());
                }
                out.write(params.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @params url    发送请求的 URL
     * @params params 请求的参数集合
     * @return 远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, String> params){
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (CollectionUtils.isNotEmpty(params)) {
                params.forEach(builder::addParameter);
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 创建百度短网址
     *
     * @params longUrl 长网址：即原网址
     *                termOfValidity
     *                有效期：默认值为long-term
     * @return 成功：短网址
     * 失败：返回空字符串
     */
    public static Result<String> createShortUrl(String longUrl, String termOfValidity, String shortUrl, String token) {
        String paramss = "{\"Url\":\"" + longUrl + "\",\"TermOfValidity\":\"" + termOfValidity + "\"}";

        BufferedReader reader = null;
        try {
            // 创建连接
            URL url = new URL(shortUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 设置请求方式
            connection.setRequestMethod("POST");
            // 设置发送数据的格式
            connection.setRequestProperty("Content-Type", "application/json");
            // 设置发送数据的格式"
            connection.setRequestProperty("Token", token);

            // 发起请求
            connection.connect();
            // utf-8编码
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            out.append(paramss);
            out.flush();
            out.close();

            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();

            // 抽取生成短网址
            BaiduDwz.UrlResponse urlResponse = JsonUtil.fromJson(res, BaiduDwz.UrlResponse.class);
            if (urlResponse.getCode() == 0) {
                return Result.succ(urlResponse.getShortUrl());
            } else {
                log.error(urlResponse.getErrMsg());
            }
            return Result.fail("创建短链失败");
        } catch (IOException e) {
            log.error("请求百度短连接失败：{}", e.getMessage());
        }
        return Result.fail("创建短链失败");
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return: {@link String} 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return: {@link String} 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 设置参数
     *
     * @param map 参数map
     * @param path 需要赋值的path
     * @param charset 编码格式 默认编码为utf-8(取消默认)
     * @return 已经赋值好的url 只需要访问即可
     */
    public static String setParmas(Map<String, String> map, String path, String charset) throws Exception {
        String result = "";
        boolean hasParams = false;
        if (path != null && !"".equals(path)) {
            if (map != null && map.size() > 0) {
                StringBuilder builder = new StringBuilder();
                Set<Map.Entry<String, String>> params = map.entrySet();
                for (Map.Entry<String, String> entry : params) {
                    String key = entry.getKey().trim();
                    String value = entry.getValue().trim();
                    if (hasParams) {
                        builder.append("&");
                    } else {
                        hasParams = true;
                    }
                    if(charset != null && !"".equals(charset)){
                        //builder.append(key).append("=").append(URLDecoder.(value, charset));
                        builder.append(key).append("=").append(urlEncode(value, charset));
                    }else{
                        builder.append(key).append("=").append(value);
                    }
                }
                result = builder.toString();
            }
        }
        return doUrlPath(path, result).toString();
    }

    /**
     * describe: 设置连接参数
     * @author: yy
     * @date: 2020/9/10 14:29
     * @param path 路径
     * @param query 连接参数
     * @return: {@link URL}
     * @version 1.4.0
     **/
    private static URL doUrlPath(String path, String query) throws Exception {
        URL url = new URL(path);
        if (org.apache.commons.lang.StringUtils.isEmpty(path)) {
            return url;
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(url.getQuery())) {
            if (path.endsWith("?")) {
                path += query;
            } else {
                path = path + "?" + query;
            }
        } else {
            if (path.endsWith("&")) {
                path += query;
            } else {
                path = path + "&" + query;
            }
        }
        return new URL(path);
    }

    /**
     * 根据返回的头信息返回具体信息 
     *
     * @param contentType
     *            ContentType请求头信息 
     * @return Result.type==1 表示文本消息, 
     */
    private static File contentType(String contentType, HttpURLConnection conn, String savePath) {
        File file = null;
        try {
            if (conn == null) {
                return null;
            }
            InputStream input = conn.getInputStream();
            if ("image/gif".equals(contentType)) { // gif图片
                file = inputStreamToMedia(input, savePath, "gif");
            } else if ("image/jpeg".equals(contentType)) { // jpg图片
                file = inputStreamToMedia(input, savePath, "jpg");
            } else if ("image/jpg".equals(contentType)) { // jpg图片
                file = inputStreamToMedia(input, savePath, "jpg");
            } else if ("image/png".equals(contentType)) { // png图片
                file = inputStreamToMedia(input, savePath, "png");
            } else if ("image/bmp".equals(contentType)) { // bmp图片
                file = inputStreamToMedia(input, savePath, "bmp");
            } else if ("audio/x-wav".equals(contentType)) { // wav语音
                file = inputStreamToMedia(input, savePath, "wav");
            } else if ("audio/x-ms-wma".equals(contentType)) { // wma语言
                file = inputStreamToMedia(input, savePath, "wma");
            } else if ("audio/mpeg".equals(contentType)) { // mp3语言
                file = inputStreamToMedia(input, savePath, "mp3");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    /**
     * 将字符流转换为图片文件 
     *
     * @param input 字符流 
     * @param savePath 图片需要保存的路径 
     * @param type jpg/png等
     * @return
     */
    private static File inputStreamToMedia(InputStream input, String savePath, String type) {
        File file = new File(savePath);
        try {
            String paramPath = file.getParent(); // 路径
            String fileName = file.getName();
            String newName = fileName.substring(0, fileName.lastIndexOf(".")) + "." + type;// 根据实际返回的文件类型后缀
            savePath = paramPath + "\\" + newName;
            file = new File(savePath);
            if (!file.exists()) {
                boolean bool = file.getParentFile().mkdir();
                bool = bool && file.createNewFile();
                if(!bool){
                    log.error("创建临时图片文件失败！");
                    return file;
                }
            }
            FileOutputStream output = new FileOutputStream(file);
            int len = 0;
            byte[] array = new byte[1024];
            while ((len = input.read(array)) != -1) {
                output.write(array, 0, len);
            }
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 默认的下载素材方法
     *
     * @param method  http方法 POST/GET
     * @param apiPath api路径
     * @return 是否下载成功 Reuslt.success==true 表示下载成功
     */
    public static File getQrCodeImgFile(TreeMap<String, String> params, String method, String apiPath) {
        try {
            URL realUrl = new URL(setParmas(params, apiPath, StandardCharsets.UTF_8.name()));
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod(method);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String contentType = conn.getContentType();
                return contentType(contentType, conn, "D:/temp/qrImg.jpg");
            }
            log.error("请求公众号图片下载地址失败：{}", conn.getResponseCode() + " " + conn.getResponseMessage());
            return null;
        } catch (Exception e) {
            log.error("下载公众号图片失败：{}", e.getMessage());
            return null;
        }
    }
}
