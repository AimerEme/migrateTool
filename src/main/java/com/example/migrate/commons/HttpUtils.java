package com.example.migrate.commons;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.*;

/**
 * The type Http utils.
 */
public class HttpUtils {
    private HttpUtils() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 发送get请求
     *
     * @param url 请求地址
     * @return String url string
     * @throws HttpException the http exception
     * @throws IOException   the io exception
     */
    public static String getUrlString(String url) throws HttpException,
            IOException {
        return getUrlString(url, null);
    }

    /**
     * 发送get请求,带参数
     *
     * @param url    请求地址
     * @param params the params
     * @return String url string
     * @throws HttpException the http exception
     * @throws IOException   the io exception
     */
    public static String getUrlString(String url, Map<String, String> params)
            throws HttpException, IOException {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        if (null != params) {
            Set<String> set = params.keySet();
            NameValuePair[] valuePairs = new NameValuePair[set.size()];
            int i = 0;
            for (String key : set) {
                NameValuePair param = new NameValuePair(key, params.get(key));
                valuePairs[i++] = param;
            }
            getMethod.setQueryString(valuePairs);
        }
        try {
            int respCode = httpClient.executeMethod(getMethod);
            if (respCode == HttpStatus.SC_OK) {
                InputStream respStr = getMethod.getResponseBodyAsStream();
                return IOUtils.toString(respStr);
            } else {
                throw new IOException("获取接入码失败,服务器返回码为：" + respCode);
            }
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
            }
        }

    }

    /**
     * 解析Post请求结果为字符串
     *
     * @param method
     * @return
     * @throws Exception
     */
    private static String getResponseBody(PostMethod method) throws Exception {
        StringBuilder stringBuffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()))) {
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
        }

        return stringBuffer.toString();
    }


    /**
     * 解析Post请求结果为字符串，设置charset
     *
     * @param method
     * @param charset
     * @return
     * @throws Exception
     */
    private static String getResponseBody(PostMethod method, String charset) throws Exception {
        StringBuilder stringBuffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset))) {
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 解析Get请求结果为字符串
     *
     * @param method
     * @return
     * @throws Exception
     */
    private static String getResponseBody(GetMethod method) throws Exception {
        StringBuilder stringBuffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()))) {
            String str = "";
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
        }

        return stringBuffer.toString();
    }

    /**
     * Post请求表单提交.
     *
     * @param url    请求地址
     * @param params the params
     * @return String string
     * @throws HttpException the http exception
     * @throws IOException   the io exception
     */
    public static String postUrlString(String url, Map<String, String> params)
            throws HttpException, IOException {
        PostMethod postMethod = null;
        try {
            HttpClient httpClient = new HttpClient();
            postMethod = new PostMethod(url);
            postMethod.getParams().setParameter(
                    HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            Set<String> set = params.keySet();
            for (String key : set) {
                NameValuePair param = new NameValuePair(key, params.get(key));
                postMethod.addParameter(param);
            }
            int respCode = httpClient.executeMethod(postMethod);
            if (respCode == HttpStatus.SC_OK) {
                String respStr = postMethod.getResponseBodyAsString();
                return respStr;
            } else {
                throw new IOException("访问URL失败：" + respCode);
            }
        } finally {
            if (null != postMethod) {
                postMethod.releaseConnection();
            }
        }

    }

    /**
     * Post请求.
     *
     * @param url  请求地址
     * @param body the body
     * @return String string
     * @throws Exception the exception
     */
    public static String postUrlStringBody(String url, String body)
            throws Exception {
        return postUrlStringBody(url, "application/json", body);
    }

    /**
     * Post请求.
     *
     * @param url     请求地址
     * @param body    the body
     * @param timeout 请求超时时间
     * @return String string
     * @throws Exception the exception
     */
    public static String postUrlStringBody(String url, String body, int timeout) throws Exception {
        return postUrlStringBody(url, "application/json", body, timeout);
    }

    /**
     * Post请求.
     *
     * @param url         the url
     * @param contentType the content type
     * @param body        the body
     * @return the string
     * @throws Exception the exception
     */
    public static String postUrlStringBody(String url, String contentType, String body, int timeout) throws Exception {
        return postUrlStringBody(url, contentType, body, "utf-8", timeout);
    }

    /**
     * Post请求.
     *
     * @param url         the url
     * @param contentType the content type
     * @param body        the body
     * @return the string
     * @throws Exception the exception
     */
    public static String postUrlStringBody(String url, String contentType,
                                           String body) throws Exception {
        return postUrlStringBody(url, contentType, body, "utf-8", 15000);
    }

    /**
     * Post请求.
     *
     * @param url             the url
     * @param contentType     the content type
     * @param body            the body
     * @param characterEncode the character encode
     * @return the string
     * @throws Exception the exception
     */
    public static String postUrlStringBody(String url, String contentType,
                                           String body, String characterEncode, int timeout) throws Exception {
        StopWatch clock = new StopWatch();
        clock.start(); // 计时开始
        PostMethod postMethod = null;
        try {
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
            postMethod = new PostMethod(url);
            RequestEntity requestEntity = new StringRequestEntity(body,
                    contentType, characterEncode);
            postMethod.setRequestEntity(requestEntity);
            int respCode = httpClient.executeMethod(postMethod);
            if (respCode == HttpStatus.SC_OK) {
                String respStr = getResponseBody(postMethod, characterEncode);
                LOGGER.info("http 请求响应 ： " + respStr);
                return respStr;
            } else {
                throw new Exception("访问http接口失败,url:" + url + "服务器Http返回码为：" + respCode);
            }
        } catch (HttpHostConnectException e) {
            throw e;
        } catch (ConnectTimeoutException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
            }
            clock.stop();
            LOGGER.info("http url:{},请求耗时:{}", url, clock.getTime());
        }
    }

    /**
     * post字符串
     *
     * @param url    请求地址
     * @param body   the body
     * @param header the header
     * @return String string
     * @throws Exception the exception
     */
    public static String postUrlStringBody(String url, String body, Map<String, String> header) throws Exception {
        LOGGER.info("postUrlStringBody url:{}", url);
        PostMethod postMethod = null;
        try {
            HttpClient httpClient = new HttpClient();
            postMethod = new PostMethod(url);
            RequestEntity requestEntity = new StringRequestEntity(body, "application/json", "utf-8");
            postMethod.setRequestEntity(requestEntity);
            if (header != null) {
                Set<String> keys = header.keySet();
                for (String key : keys) {
                    postMethod.addRequestHeader(key, header.get(key));
                }
            }
            LOGGER.info("postUrlStringBody reqBody:{}", body);
            int respCode = httpClient.executeMethod(postMethod);
            LOGGER.info("postUrlStringBody respCode:{}", respCode);
            if (respCode == HttpStatus.SC_OK) {
                String respStr = getResponseBody(postMethod, "utf-8");
                LOGGER.info("postUrlStringBody respStr:{}", respStr);
                return respStr;
            } else {
                throw new IOException("访问http接口失败,服务器Http返回码为：" + respCode);
            }
        } finally {
            if (null != postMethod) {
                postMethod.releaseConnection();
            }
        }
    }

    /**
     * post字符串 支付回调通知
     *
     * @param url    请求地址
     * @param body   the body
     * @param header the header
     * @return String string
     * @throws Exception the exception
     */
    public static String postUrlStringBodyForNotify(String url, String body,
                                                    Map<String, String> header) throws Exception {
        LOGGER.info("psstUrlStringBody1:" + url);
        PostMethod postMethod = null;
        try {
            HttpClient httpClient = new HttpClient();
            ///////////////////////////////////////////////////
            //HTTP连接的超时时间，
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            //HTTP连接成功后，等待读取数据或者写数据的最大超时时间
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
            DefaultHttpMethodRetryHandler retryhandler = new DefaultHttpMethodRetryHandler(0, false);
            httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler);
            ///////////////////////////////////////////////////

            postMethod = new PostMethod(url);
            RequestEntity requestEntity = new StringRequestEntity(body,
                    "application/json", "utf-8");
            postMethod.setRequestEntity(requestEntity);
            if (header != null) {
                Set<String> keys = header.keySet();
                for (String key : keys) {
                    postMethod.addRequestHeader(key, header.get(key));
                }
            }
            LOGGER.info("psstUrlStringBody2:" + url);
            int respCode = httpClient.executeMethod(postMethod);
            LOGGER.info("respCode:" + respCode);
            if (respCode == HttpStatus.SC_OK) {
                String respStr = getResponseBody(postMethod, "utf-8");
                LOGGER.info("respStr:" + respStr);
                return respStr;
            } else {
                throw new IOException("访问http接口失败,服务器Http返回码为：" + respCode);
            }
        } finally {
            if (null != postMethod) {
                postMethod.releaseConnection();
            }
        }
    }

    /**
     * Post url byte body byte [ ].
     *
     * @param url  请求地址
     * @param body the body
     * @return String byte [ ]
     * @throws Exception the exception
     */
    public static byte[] postUrlByteBody(String url, byte[] body)
            throws Exception {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        try {
            RequestEntity requestEntity = new ByteArrayRequestEntity(body);
            postMethod.setRequestEntity(requestEntity);
            int respCode = httpClient.executeMethod(postMethod);
            if (respCode == HttpStatus.SC_OK) {
                return postMethod.getResponseBody();
            } else {
                throw new IOException("访问接口失败：" + respCode);
            }
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
            }
        }
    }

    /**
     * 发送带文件的数据
     *
     * @param url   请求地址
     * @param param the param
     * @return String String
     * @throws Exception the exception
     */
    public static String postUrlMutiPartData(String url,
                                             Map<String, Object> param) throws Exception {
        String rst = null;
        PostMethod postMethod = new PostMethod(url);
        try {
            Part[] parts = buildPart(param);
            MultipartRequestEntity mre = new MultipartRequestEntity(parts,
                    postMethod.getParams());
            postMethod.setRequestEntity(mre);
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams()
                    // 设置连接时间
                    .setConnectionTimeout(1000000);
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                rst = getResponseBody(postMethod);
                LOGGER.debug("发送成功，返回数据为：" + rst);
            } else {
                throw new Exception("发送失败，返回码：" + status);
            }
        } catch (Exception e) {
            LOGGER.error("发送失败！", e);
            throw e;
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
            }
        }
        return rst;

    }

    /**
     * 组装文件数据
     *
     * @param param
     * @return
     * @throws Exception
     */
    private static Part[] buildPart(Map<String, Object> param) throws Exception {
        List<Part> parts = new ArrayList<Part>();
        Set<String> names = param.keySet();
        for (String name : names) {
            Object value = param.get(name);
            if (value instanceof String) {
                Part bussinessIdPart = new StringPart(name,
                        StringUtils.defaultString((String) value), "UTF-8");
                parts.add(bussinessIdPart);
            } else if (value instanceof File) {
                Part bussinessIdPart = new FilePart(name, (File) value);
                parts.add(bussinessIdPart);
            } else if (value instanceof List) {
                @SuppressWarnings("rawtypes")
                List list = (List) value;
                for (Object object : list) {
                    if (object instanceof File) {
                        Part bussinessIdPart = new FilePart(URLEncoder.encode(
                                name, "utf-8").toString(), (File) object);
                        parts.add(bussinessIdPart);
                    } else if (object instanceof String) {
                        Part bussinessIdPart = new StringPart(name,
                                StringUtils.defaultString((String) object),
                                "UTF-8");
                        parts.add(bussinessIdPart);
                    }
                }
            }
        }
        int size = parts.size();
        Part[] partsArr = new Part[size];
        for (int i = 0; i < partsArr.length; i++) {
            partsArr[i] = parts.get(i);
        }
        return partsArr;
    }

    /**
     * 通过HttpURLConnection发送post请求.
     *
     * @param targetURL     the target url
     * @param urlParameters the url parameters
     * @return the string
     */
    public static String postURL(String targetURL, String urlParameters) {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        int responseCode = 0;
        InputStream is=null;
        BufferedReader rd=null;
        DataOutputStream wr =null;
        try {
            // Create connection
            url = new URL(targetURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "text/html");

            httpURLConnection.setRequestProperty("Content-Length",
                    "" + Integer.toString(urlParameters.getBytes().length));
            httpURLConnection.setRequestProperty("Content-Language", "en-US");

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            // Send request
            wr = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Get Response
            is = httpURLConnection.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append("\r");
            }
            rd.close();
            httpURLConnection.disconnect();
            return response.toString();

        } catch (Exception e) {
            return null;
        } finally {
            try{
                if(rd!=null){
                    rd.close();
                }
                if(is!=null){
                    is.close();
                }
            }catch (IOException e){
                LOGGER.error("请求异常", e);
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /**
     * 通过HttpURLConnection发送get请求.
     *
     * @param urlstr        the urlstr
     * @param urlParameters the url parameters
     * @return the string
     */
    public static String get(String urlstr, String urlParameters) {
        StringBuffer response = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        Reader reader=null;
        BufferedWriter out=null;
        try {
            URL url = new URL(urlstr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(50000);
            httpURLConnection.setReadTimeout(50000);
            httpURLConnection.connect();

            out = new BufferedWriter(new OutputStreamWriter(
                    httpURLConnection.getOutputStream(), "GBK"));
            out.write(urlParameters);
            out.flush();
            out.close();

            reader = new InputStreamReader(
                    httpURLConnection.getInputStream(), StandardCharsets.UTF_8);
            reader = new BufferedReader(reader);
            char[] buffer = new char[1024];
            for (int n = 0; n >= 0; ) {
                n = reader.read(buffer, 0, buffer.length);
                if (n > 0) {
                    response.append(buffer, 0, n);
                }
            }

            httpURLConnection.disconnect();
            return response.toString();
        } catch (Exception ex) {
            LOGGER.error("通过HttpURLConnection发送get请求异常", ex);
            return null;
        } finally {
            try{
                if(reader!=null){
                    reader.close();
                }
                if(out!=null){
                    out.close();
                }
            }catch (IOException e){
                LOGGER.error("请求异常", e);
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /**
     * 通过HttpURLConnection发送post请求.
     *
     * @param urlstr        the urlstr
     * @param urlParameters the url parameters
     * @return the string
     */
    public static String post(String urlstr, String urlParameters) {

        StringBuffer response = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        BufferedWriter out =null;
        Reader reader=null;
        try {
            URL url = new URL(urlstr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(100000);
            httpURLConnection.setReadTimeout(100000);
            httpURLConnection.addRequestProperty("Content-type",
                    "application/json");
            httpURLConnection.connect();

            out = new BufferedWriter(new OutputStreamWriter(
                    httpURLConnection.getOutputStream(), StandardCharsets.UTF_8));
            out.write(urlParameters);
            out.flush();
            out.close();

            reader = new InputStreamReader(
                    httpURLConnection.getInputStream(), StandardCharsets.UTF_8);
            reader = new BufferedReader(reader);
            char[] buffer = new char[1024];
            for (int n = 0; n >= 0; ) {
                n = reader.read(buffer, 0, buffer.length);
                if (n > 0) {
                    response.append(buffer, 0, n);
                }
            }
            reader.close();
            httpURLConnection.disconnect();
            return response.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        } finally {
            try{
                if(reader!=null){
                    reader.close();
                }
                if(out!=null){
                    out.close();
                }
            }catch (IOException e){
                LOGGER.error("请求异常", e);
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        // }
    }

    /**
     * 通过http下载文件.
     *
     * @param baseDir the base dir
     * @param url     the url
     * @return the file
     */
    public static File downLoadHttpFile(String baseDir, String url) {
        File file = null;

        if (url.toLowerCase().startsWith("http")) {
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            try (InputStream input = getMethod.getResponseBodyAsStream()){
                int respCode = httpClient.executeMethod(getMethod);
                if (respCode == HttpStatus.SC_OK) {
                    String fileName = getFileName(getMethod, url);
                    String suffix = "";
                    if (fileName.indexOf(".") >= 0) {
                        suffix = fileName.substring(fileName.lastIndexOf("."));
                    }
                    // 设置本地保存的文件
                    file = new File(baseDir + UUID.randomUUID() + suffix);


                    saveFile(file, input, suffix);

                } else {
                    throw new IOException("获取接入码失败,服务器返回码为：" + respCode);
                }
            } catch (Exception e) {
                LOGGER.error("文件下载失败,url[" + url + "]", e);
                return null;
            } finally {
                getMethod.releaseConnection();
            }
        }
        return file;
    }

    /**
     * 保存文件
     *
     * @param outpFile
     * @param input
     * @param suffix
     * @throws IOException
     */
    private static void saveFile(File outpFile, InputStream input, String suffix) throws IOException {


        try( FileOutputStream output = new FileOutputStream(outpFile);
             InputStreamReader is = new InputStreamReader(input);
             BufferedReader br=new BufferedReader(is);
             PrintWriter pw = new PrintWriter(new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(outpFile), StandardCharsets.UTF_8)))) {
            if (suffix != null && (suffix.equalsIgnoreCase(".txt")
                    || suffix.equalsIgnoreCase(".html")
                    || suffix.equalsIgnoreCase(".htm"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    pw.println(line);
                }
                pw.flush();
            } else {
                // 得到网络资源并写入文件
                byte[] bytes = new byte[1024];
                int j = 0;
                while ((j = input.read(bytes)) != -1) {
                    output.write(bytes, 0, j);
                }
            }
        } catch (Exception e) {
            throw new IOException("HttpUtils.saveFile异常" + e);
        }
    }

    /**
     * 获取下载地址中的文件名
     *
     * @param getMethod
     * @param url
     * @return
     */
    private static String getFileName(GetMethod getMethod, String url) {
        String filename = null;
        if (getMethod != null) {
            Header contentHeader = getMethod
                    .getResponseHeader("Content-Disposition");
            if (contentHeader != null) {
                HeaderElement[] values = contentHeader.getElements();
                if (values.length == 1) {
                    NameValuePair param = values[0]
                            .getParameterByName("filename");
                    if (param != null) {
                        try {
                            filename = param.getValue();
                        } catch (Exception e) {
                            LOGGER.error("获取文件名失败", e);
                        }
                    }
                }
            }
        }
        if (filename == null) {
            filename = url.substring(url.lastIndexOf("/") + 1);
        }
        return filename;
    }

    /**
     * 针对微信发送请求，前置需要使用证书,如果不需要使用证书请不要使用这个接口
     *
     * @param url         请求地址
     * @param data<XML格式> 请求报文
     * @param certPath    使用的证书路径
     * @param mchId       证书密码默认为您的商户ID（如：10010000）
     * @return 响应报文
     */
    public static String doPostByWXSSL(String url, String data, String certPath, String mchId) {
        StringBuilder message = new StringBuilder();
        try (FileInputStream instream = new FileInputStream(new File(certPath))) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(instream, mchId.toCharArray());
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, mchId.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpPost httpost = new HttpPost(url);

            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);

            HttpEntity entity = response.getEntity();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8))) {
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    message.append(text);
                }
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error("doPostByWXSSL异常", e);
        }
        return message.toString();
    }
}
