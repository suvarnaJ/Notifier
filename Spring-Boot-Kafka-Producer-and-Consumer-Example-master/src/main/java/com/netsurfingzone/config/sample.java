package com.netsurfingzone.config;

import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class sample {
    private static final Logger logger = LoggerFactory.getLogger(sample.class);
    public static void main(String[] args) throws IOException {
        String res = "";
                //sendMailHTTP();
    }

    public static String sendMailHTTP() throws UnsupportedEncodingException {
        String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IlZhZ3RIM2NCRVhlWlhqemRoVkNCRG1nZldKV0lNUDJkMTViMTVETURza2ciLCJhbGciOiJSUzI1NiIsIng1dCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSIsImtpZCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yMDIxMDQ2Mi0yYzVlLTRlYzgtYjNlMi0wYmU5NTBmMjkyY2EvIiwiaWF0IjoxNjY4NjIxMDM0LCJuYmYiOjE2Njg2MjEwMzQsImV4cCI6MTY2ODYyNDkzNCwiYWlvIjoiRTJaZ1lKQlp4TFpkNTVnUXo2MnovWHJmbHRmekFnQT0iLCJhcHBfZGlzcGxheW5hbWUiOiJDb25maWd1cmluZyBVQVQgZW1haWxzIGZvciBTZXJ2aWNlIFN1cHBvcnQgVWF0IiwiYXBwaWQiOiI2NGQzM2M0NC0yZDQwLTRkMGYtYTczYS1kZDBlZDk5NTBlMWYiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yMDIxMDQ2Mi0yYzVlLTRlYzgtYjNlMi0wYmU5NTBmMjkyY2EvIiwiaWR0eXAiOiJhcHAiLCJvaWQiOiJkMDM0MDlkZi0zNTFmLTQwZTAtYWU5Yi1jNjg1YWFkMzhhNGEiLCJyaCI6IjAuQVE4QVlnUWhJRjRzeUU2ejRndnBVUEtTeWdNQUFBQUFBQUFBd0FBQUFBQUFBQUFQQUFBLiIsInJvbGVzIjpbIk1haWwuUmVhZFdyaXRlIiwiVXNlci5SZWFkLkFsbCIsIk1haWwuUmVhZCIsIk1haWwuU2VuZCIsIk1haWwuUmVhZEJhc2ljIl0sInN1YiI6ImQwMzQwOWRmLTM1MWYtNDBlMC1hZTliLWM2ODVhYWQzOGE0YSIsInRlbmFudF9yZWdpb25fc2NvcGUiOiJBUyIsInRpZCI6IjIwMjEwNDYyLTJjNWUtNGVjOC1iM2UyLTBiZTk1MGYyOTJjYSIsInV0aSI6IldfbFoyWXBudzBXTG5XYndDb3c5QUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjA5OTdhMWQwLTBkMWQtNGFjYi1iNDA4LWQ1Y2E3MzEyMWU5MCJdLCJ4bXNfdGNkdCI6MTQyMzYwNTEyN30.AanRI-l963itOcv3Pd30LwYNTyawHByZUZWgk0yfAiNKN0q6FVp816GRj2P5_zuQJ34QigkLXoLAqxHHOerpZ8csqa8jOm5jQwHTgy_iBtV0S7znp5jwBX7ECtRWAW3lrFm7ks0hQqI6JcQCBq8arzGZ9ynqUyh9OiaRXQxo192hkEDvVB7enryKDDnLhsFr26xHfMNCojNeWA0gvqWyBNyrrLF6X2cfO3d1oEuVRn0M9jw8kdS0GjPwFchI0XBDUHxg48NcA0IFTkSuYJKQpa-98sJYBtl7LcDSsk9uNJWK5bDnRkYVi0wPcuLOpSWNwbj1T0gx5I1ai7z80MmdCA";
        String jsonRequest = "{\n" +
                "  \"message\": {\n" +
                "    \"subject\": \"Greeting from Graph API\",\n" +
                "    \"body\": {\n" +
                "      \"contentType\": \"Text\",\n" +
                "      \"content\": \"Hi Team, Greeting from Graph API ......... Regards , Suvarna Jagadale\"\n" +
                "    },\n" +
                "    \"toRecipients\": [\n" +
                "      {\n" +
                "        \"emailAddress\": {\n" +
                "          \"address\": \"suvarna.jagadale@tatacommunications.com\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"ccRecipients\": [\n" +
                "      {\n" +
                "        \"emailAddress\": {\n" +
                "          \"address\": \"suvarna.jagadale@tatacommunications.com\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"emailAddress\": {\n" +
                "          \"address\": \"suvarna.jagadale@tatacommunications.com\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"saveToSentItems\": \"false\"\n" +
                "}";
        String graphUrl = "https://graph.microsoft.com/v1.0/users/service.supportuat@tatacommunications.com/sendMail";
        String res = "";
        StringEntity postStr = new StringEntity(jsonRequest);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postM = new HttpPost(graphUrl);
        postM.setHeader("Content-Type", "application/json");
        postM.setHeader("Authorization", "Bearer " + token);
        postM.setEntity(postStr);
        try {

            CloseableHttpResponse response = client.execute(postM);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 201) {
                 res = EntityUtils.toString(response.getEntity());
            }else {
                res = EntityUtils.toString(response.getEntity());
                System.out.println("Response ---------->"+res.toString());
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // print result
        System.out.println("Final Response ---------->"+res.toString());

        return res.toString();
    }



}
