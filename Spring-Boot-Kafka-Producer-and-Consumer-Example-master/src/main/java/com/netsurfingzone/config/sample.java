package com.netsurfingzone.config;

import com.azure.core.http.ProxyOptions;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class sample {
    private static final Logger logger = LoggerFactory.getLogger(sample.class);
    public static void main(String[] args) throws Exception {
        String res = //getAuth();
                    sendMail();
                    //requestFactory();
                    //getToken();
                    //getAccessToken();
                    //sendMailHTTP();
    }

    public static String sendMailHTTP() throws UnsupportedEncodingException {
        String PROXY_SERVER_HOST ="10.133.12.181" ;
        String PROXY_SERVER_PORT = "80";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, Integer.parseInt(PROXY_SERVER_PORT)));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        System.out.println("Getting token from getAuth method............."+getAuth());

        //String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IlZhZ3RIM2NCRVhlWlhqemRoVkNCRG1nZldKV0lNUDJkMTViMTVETURza2ciLCJhbGciOiJSUzI1NiIsIng1dCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSIsImtpZCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yMDIxMDQ2Mi0yYzVlLTRlYzgtYjNlMi0wYmU5NTBmMjkyY2EvIiwiaWF0IjoxNjY4NjIxMDM0LCJuYmYiOjE2Njg2MjEwMzQsImV4cCI6MTY2ODYyNDkzNCwiYWlvIjoiRTJaZ1lKQlp4TFpkNTVnUXo2MnovWHJmbHRmekFnQT0iLCJhcHBfZGlzcGxheW5hbWUiOiJDb25maWd1cmluZyBVQVQgZW1haWxzIGZvciBTZXJ2aWNlIFN1cHBvcnQgVWF0IiwiYXBwaWQiOiI2NGQzM2M0NC0yZDQwLTRkMGYtYTczYS1kZDBlZDk5NTBlMWYiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yMDIxMDQ2Mi0yYzVlLTRlYzgtYjNlMi0wYmU5NTBmMjkyY2EvIiwiaWR0eXAiOiJhcHAiLCJvaWQiOiJkMDM0MDlkZi0zNTFmLTQwZTAtYWU5Yi1jNjg1YWFkMzhhNGEiLCJyaCI6IjAuQVE4QVlnUWhJRjRzeUU2ejRndnBVUEtTeWdNQUFBQUFBQUFBd0FBQUFBQUFBQUFQQUFBLiIsInJvbGVzIjpbIk1haWwuUmVhZFdyaXRlIiwiVXNlci5SZWFkLkFsbCIsIk1haWwuUmVhZCIsIk1haWwuU2VuZCIsIk1haWwuUmVhZEJhc2ljIl0sInN1YiI6ImQwMzQwOWRmLTM1MWYtNDBlMC1hZTliLWM2ODVhYWQzOGE0YSIsInRlbmFudF9yZWdpb25fc2NvcGUiOiJBUyIsInRpZCI6IjIwMjEwNDYyLTJjNWUtNGVjOC1iM2UyLTBiZTk1MGYyOTJjYSIsInV0aSI6IldfbFoyWXBudzBXTG5XYndDb3c5QUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjA5OTdhMWQwLTBkMWQtNGFjYi1iNDA4LWQ1Y2E3MzEyMWU5MCJdLCJ4bXNfdGNkdCI6MTQyMzYwNTEyN30.AanRI-l963itOcv3Pd30LwYNTyawHByZUZWgk0yfAiNKN0q6FVp816GRj2P5_zuQJ34QigkLXoLAqxHHOerpZ8csqa8jOm5jQwHTgy_iBtV0S7znp5jwBX7ECtRWAW3lrFm7ks0hQqI6JcQCBq8arzGZ9ynqUyh9OiaRXQxo192hkEDvVB7enryKDDnLhsFr26xHfMNCojNeWA0gvqWyBNyrrLF6X2cfO3d1oEuVRn0M9jw8kdS0GjPwFchI0XBDUHxg48NcA0IFTkSuYJKQpa-98sJYBtl7LcDSsk9uNJWK5bDnRkYVi0wPcuLOpSWNwbj1T0gx5I1ai7z80MmdCA";
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
        postM.setHeader("Authorization", "Bearer " + getAuth());
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

    public static String getToken() throws Exception {

        System.out.println("Setting proxy ...");

        String host ="10.133.12.181" ;
        String port = "80";

        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("https.protocols", "TLSv1.2 TLSv1.3");
        System.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port);

        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", port);

        System.out.println("Using proxy: " + host + ":" + port);

        System.out.println("Trying to get Token from MS Graph API ...");

        String graphUrlToRefreshToken = "https://login.microsoftonline.com/20210462-2c5e-4ec8-b3e2-0be950f292ca/oauth2/v2.0/token";
        String res = "",tokenValue ="";
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

                JsonObject jsonResp = new Gson().fromJson(res, JsonObject.class); // String to JSONObject

                if (jsonResp.has("access_token"))
                    tokenValue = jsonResp.get("access_token").toString().replace("\"", "");
                System.out.println(tokenValue); // toString returns quoted values!

                System.out.println("Token Received ..."+res.toString());
            }else {
                System.out.println("Failed to get Token  ..."+res.toString());
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokenValue;
    }


    public static String getAuth() {

        String PROXY_SERVER_HOST ="10.133.12.181" ;
        String PROXY_SERVER_PORT = "80";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, Integer.parseInt(PROXY_SERVER_PORT)));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        ConfidentialClientApplication app = null;
        String appSecret = "320j1.n-tD.aa114YHC0z-42beLV45tGcc",
                microformatAppId="64d33c44-2d40-4d0f-a73a-dd0ed9950e1f",
                tenantId="20210462-2c5e-4ec8-b3e2-0be950f292ca";

        IClientCredential credential = ClientCredentialFactory.createFromSecret(appSecret);
        try {
            app = ConfidentialClientApplication.builder(microformatAppId, credential).authority("https://login.microsoftonline.com/"+tenantId+"/").build();
        }catch(MalformedURLException e) {
            System.out.println(e.getMessage());
        }

        ClientCredentialParameters parameters = ClientCredentialParameters.builder(Collections.singleton("https://graph.microsoft.com/.default")).build();

        CompletableFuture<IAuthenticationResult> future = app.acquireToken(parameters);

        try {
            IAuthenticationResult result = future.get();
            System.out.println("result->"+result.accessToken().toString());
            return result.accessToken().toString();
        }catch(ExecutionException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*public static String getAccessToken() throws UnsupportedOperationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.133.12.181", 80));
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create("grant_type=client_credentials&client_id=64d33c44-2d40-4d0f-a73a-dd0ed9950e1f&scope=https://graph.microsoft.com/.default&client_secret=320j1.n-tD.aa114YHC0z-42beLV45tGcc",mediaType);
                *//*create(
                "grant_type=client_credentials&client_id=64d33c44-2d40-4d0f-a73a-dd0ed9950e1f&scope=https://graph.microsoft.com/.default&client_secret=320j1.n-tD.aa114YHC0z-42beLV45tGcc",
                mediaType);*//*


        Request request = new Request.Builder()
                .url("https://login.microsoftonline.com/20210462-2c5e-4ec8-b3e2-0be950f292ca/oauth2/v2.0/token")
                .method("POST", body).addHeader("Content-Type", "application/x-www-form-urlencoded").build();

        Response response = client.newCall(request).execute();
        // String token = response.body().string();
        return mapper.readTree(response.body().string()).get("access_token").asText();
    }
*/
    public static String requestFactory(){
        String PROXY_SERVER_HOST ="10.133.12.181" ;
        String PROXY_SERVER_PORT = "80";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, Integer.parseInt(PROXY_SERVER_PORT)));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://login.microsoftonline.com", String.class);
        String res = String.valueOf(responseEntity.getStatusCode());

        return res;

    }

    public static String sendMail(){
        String PROXY_SERVER_HOST ="10.133.12.181" ;
        String PROXY_SERVER_PORT = "80";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, Integer.parseInt(PROXY_SERVER_PORT)));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        String clientId = "64d33c44-2d40-4d0f-a73a-dd0ed9950e1f";
        String clientSecret = "320j1.n-tD.aa114YHC0z-42beLV45tGcc";
        String tenantId ="20210462-2c5e-4ec8-b3e2-0be950f292ca";
        String redirect_url ="https://graph.microsoft.com/";
        final String GRAPH_DEFAULT_SCOPE = "https://graph.microsoft.com/.default";


        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .proxyOptions(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("10.133.12.181", 80)))
                .build();

        List<String> scopes = new ArrayList<>();
        scopes.add(GRAPH_DEFAULT_SCOPE);

         TokenCredentialAuthProvider tokenCredAuthProvider =
                new TokenCredentialAuthProvider(scopes, clientSecretCredential);

        System.out.println(tokenCredAuthProvider.toString());

         GraphServiceClient graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(tokenCredAuthProvider)
                .buildClient();

        Message message = new Message();
        message.subject = "Meet for lunch?";
        ItemBody body = new ItemBody();
        body.contentType = BodyType.TEXT;
        body.content = "The new cafeteria is open.";
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = "suvarna.jagadale@tatacommunications.com";
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
        Recipient ccRecipients = new Recipient();
        EmailAddress emailAddress1 = new EmailAddress();
        emailAddress1.address = "suvarna.jagadale@tatacommunications.com";
        ccRecipients.emailAddress = emailAddress1;
        ccRecipientsList.add(ccRecipients);
        message.ccRecipients = ccRecipientsList;

        boolean saveToSentItems = false;
        graphClient.users("service.supportuat@tatacommunications.com").
                sendMail(UserSendMailParameterSet.
                    newBuilder().
                    withMessage(message).
                        withSaveToSentItems(saveToSentItems).
                        build()).
                        buildRequest().
                post();
        /*graphClient.me()
                .sendMail(UserSendMailParameterSet
                        .newBuilder()
                        .withMessage(message)
                        .withSaveToSentItems(saveToSentItems)
                        .build())
                .buildRequest()
                .post();
*/
        return "";
    }





}

