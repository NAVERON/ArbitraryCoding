package com.eron.attendance.demos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ClientFormLogin {
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        try {
            HttpGet httpget = new HttpGet("http://dubai.dubizzle.com/");
            httpget.addHeader("User-Agent", USER_AGENT);
            CloseableHttpResponse getResponse = httpclient.execute(httpget);
            try {
                HttpEntity entity = getResponse.getEntity();

                System.out.println("GET: " + getResponse.getStatusLine());
                EntityUtils.consume(entity);

                List<Cookie> cookies = cookieStore.getCookies();
                System.out.println("Get cookies:");
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        Cookie cookie =  cookies.get(i);
                        System.err.println(
                                "Cookie: " + cookie.getName() +
                                ", Value: " + cookie.getValue() +
                                ", IsPersistent?: " + cookie.isPersistent() +
                                ", Expiry Date: " + cookie.getExpiryDate() +
                                ", Comment: " + cookie.getComment());
                    }
                }
            } finally {
                getResponse.close();
            }

            HttpPost httpPost = new HttpPost("http://dubai.dubizzle.com/accounts/login/");
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("userName", "metakova@gmail.com"));
            urlParameters.add(new BasicNameValuePair("password", "Meta_123"));
            urlParameters.add(new BasicNameValuePair("next", "/"));

            HttpEntity postParams = new UrlEncodedFormEntity(urlParameters, Consts.UTF_8);
            httpPost.setEntity(postParams);

            CloseableHttpResponse postResponse = httpclient.execute(httpPost);
            System.out.println("POST: " + postResponse.getStatusLine());

            List<Cookie> postCookies = cookieStore.getCookies();
            if (postCookies.isEmpty()) {
                System.out.println("None");
            } else {
                System.out.println("Post Cookies:");
                for (int i = 0; i < postCookies.size(); i++) {
                    Cookie cookie =  postCookies.get(i);
                    System.err.println(
                            "Cookie: " + cookie.getName() +
                            ", Value: " + cookie.getValue() +
                            ", IsPersistent?: " + cookie.isPersistent() +
                            ", Expiry Date: " + cookie.getExpiryDate() +
                            ", Comment: " + cookie.getComment());
                }
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                   postResponse.getEntity().getContent()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    if(!inputLine.matches("^\\s*$")){
                        response.append(inputLine);
                        response.append("\n");
                    }
                }
                reader.close();
                // Print Response Content
                System.out.println(response);

            } finally {
                postResponse.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
