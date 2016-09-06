package cn.vansky.framework.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA .
 * Auth: CK
 * Date: 2016/4/28
 */
public class HttpClientUtils {

    public static String execute(Map<String, Object> params, String url) {
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        form.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                    }
                }
            }
            HttpEntity reqEntity = new UrlEncodedFormEntity(form, "utf-8");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            HttpPost post = new HttpPost(url);
            post.setEntity(reqEntity);
            post.setConfig(requestConfig);
            HttpClient client = HttpClientBuilder.create().build();
            return client.execute(post, new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    HttpEntity entity = response.getEntity();
                    return EntityUtils.toString(entity, "utf-8");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String executeGet(String url) {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            HttpGet post = new HttpGet(url);
            post.setConfig(requestConfig);
            HttpClient client = HttpClientBuilder.create().build();
            return client.execute(post, new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    HttpEntity entity = response.getEntity();
                    return EntityUtils.toString(entity, "utf-8");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
