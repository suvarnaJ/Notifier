package main.java.com.netsurfingzone.dto;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sample {

    public static void main(String[] args) throws IOException {
        /*String graphUrlToRefreshToken = "https://login.microsoftonline.com/20210462-2c5e-4ec8-b3e2-0be950f292ca/oauth2/v2.0/token";
        String res = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(graphUrlToRefreshToken);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("client_id","64d33c44-2d40-4d0f-a73a-dd0ed9950e1f"));
        urlParameters.add(new BasicNameValuePair("client_secret","320j1.n-tD.aa114YHC0z-42beLV45tGcc"));
        urlParameters.add(new BasicNameValuePair("grant_type","client_credentials"));
        urlParameters.add(new BasicNameValuePair("redirect_uri","https://graph.microsoft.com/"));
        urlParameters.add(new BasicNameValuePair("refresh_token","24"));
        urlParameters.add(new BasicNameValuePair("scope","https://graph.microsoft.com/.default"));

        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
        httpPost.setEntity(postParams);

        try {
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Token Received ..."+ response.toString());
            if (statusCode == 200) {
                res = EntityUtils.toString(response.getEntity());

                System.out.println("Token Received ..."+res.toString());
            }else {
                System.out.println("Failed to get Token  ..."+res.toString());
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/
    }


}
