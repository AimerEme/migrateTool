package com.example.migrate.commons;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 通过httpPost（Stream load）导入数据到StarRocks
 * @author wangbeichen
 * @Date 2022/02/15
 */
public class StarRocksStreamLoad {
    private static String starRocksUser;
    private static String starRocksPassword;
    private static String loadUrl;

    private static final Logger logger = LoggerFactory.getLogger(StarRocksStreamLoad.class);

    public StarRocksStreamLoad(String host, String database, String table, String user, String password, int port){
        starRocksUser = user;
        starRocksPassword = password;
        loadUrl = String.format("http://%s:%s/api/%s/%s/_stream_load",
                host,
                port,
                database,
                table);
    }

    final HttpClientBuilder httpClientBuilder = HttpClients
            .custom()
            .setRedirectStrategy(new DefaultRedirectStrategy() {
                @Override
                protected boolean isRedirectable(String method) {
                    return true;
                }
            });


    /**
     * 发送数组
     * @param content 构造后的数组
     */
    public String sendData(String content,String separator,String columns) throws IOException {

        try (CloseableHttpClient client = httpClientBuilder.build()) {
            HttpPut put = new HttpPut(loadUrl);
            StringEntity entity = new StringEntity(content, "UTF-8");
            put.setHeader(HttpHeaders.EXPECT, "100-continue");
            put.setHeader("column_separator",separator);
            put.setHeader(HttpHeaders.AUTHORIZATION, basicAuthHeader(starRocksUser, starRocksPassword));
            // the label header is optional, not necessary
            // use label header can ensure at most once semantics
            put.setEntity(entity);
            put.setHeader("columns",columns);

            try (CloseableHttpResponse response = client.execute(put)) {
                String loadResult = "";
                if (response.getEntity() != null) {
                    loadResult = EntityUtils.toString(response.getEntity());
                }
                final int statusCode = response.getStatusLine().getStatusCode();
                // statusCode 200 just indicates that starrocks be service is ok, not stream load
                // you should see the output content to find whether stream load is success
                if (statusCode != 200) {
                    throw new IOException(
                            String.format("Stream load failed, statusCode=%s load result=%s", statusCode, loadResult));
                }

                return loadResult;
            }
        }
    }

    /**
     * 发送数组
     * @param content 构造后的数组
     */
    public String deleteData(String content,String separator,String columns) throws IOException {

        try (CloseableHttpClient client = httpClientBuilder.build()) {
            HttpPut put = new HttpPut(loadUrl);
            StringEntity entity = new StringEntity(content, "UTF-8");
            put.setHeader(HttpHeaders.EXPECT, "100-continue");
            put.setHeader("column_separator",separator);
            put.setHeader(HttpHeaders.AUTHORIZATION, basicAuthHeader(starRocksUser, starRocksPassword));
            // the label header is optional, not necessary
            // use label header can ensure at most once semantics
            put.setEntity(entity);
            put.setHeader("columns",columns + ",__op = 'delete'");

            try (CloseableHttpResponse response = client.execute(put)) {
                String loadResult = "";
                if (response.getEntity() != null) {
                    loadResult = EntityUtils.toString(response.getEntity());
                }
                final int statusCode = response.getStatusLine().getStatusCode();
                // statusCode 200 just indicates that starrocks be service is ok, not stream load
                // you should see the output content to find whether stream load is success
                if (statusCode != 200) {
                    throw new IOException(
                            String.format("Stream load failed, statusCode=%s load result=%s", statusCode, loadResult));
                }

                return loadResult;
            }
        }
    }


    private String basicAuthHeader(String username, String password) {
        final String tobeEncode = username + ":" + password;
        byte[] encoded = Base64.encodeBase64(tobeEncode.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encoded);
    }

}