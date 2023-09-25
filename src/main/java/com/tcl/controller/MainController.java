package com.tcl.controller;

import com.azure.core.http.ProxyOptions;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.google.gson.Gson;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.tcl.dto.ErrorLogs;
import com.tcl.payload.ErrorResponse;
import com.tcl.payload.SuccessResponse;
import com.tcl.producer.KafkaProducer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1.0")
@CrossOrigin("*")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${http.proxyserverhost.uat}")
    private String proxyServerHostUat;

    @Value("${http.proxyserverhost.prod}")
    private String proxyServerHostProd;

    @Value("${http.clientid}")
    private String clientId;

    @Value("${http.clientsecret}")
    private String clientSecret;

    @Value("${http.tenantid}")
    private String tenantId;

    @Value("${http.redirecturl}")
    private String redirectUrl;

    @Value("${http.graphdefaultscope}")
    private String graphDefaultScope;

    @Value("${http.servicesupport}")
    private String serviceSupport;

    @GetMapping("/data/fetchData/errorLogs")
    public ResponseEntity<?> findErrorLogs(@RequestParam(required = true) String to,@RequestParam(required = true) String from){
        ResponseEntity<?> response;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        ArrayList<Date> dates = new ArrayList<Date>();
        ArrayList<String> filterDates = new ArrayList<>();
        Gson gson = new Gson();
        try{
            //jdbcTemplate.execute("CREATE TABLE CN_LOG_ERROR(" + "AccountName VARCHAR(255), Status NUMERIC(3), Message VARCHAR(255), API_Name VARCHAR(255), Created_At datetime default CURRENT_TIMESTAMP)");
            //jdbcTemplate.execute("DROP TABLE CN_LOG_ERROR");
            //jdbcTemplate.execute("DELETE FROM CN_LOG_ERROR");
            if(from.equals("") || to.equals("")){
                logger.info("Field can't be null");
                response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Field can't be null");
            }else {
                Object[] error_logs = jdbcTemplate.queryForList("select * from CN_LOG_ERROR").toArray();
                List<ErrorLogs> newErrorLogs =new ArrayList<>();
                for(int i=0;i<error_logs.length;i++){
                    String s = gson.toJson(error_logs[i]);
                    ErrorLogs gsonErrorLogs = gson.fromJson(s, ErrorLogs.class);
                    newErrorLogs.add(gsonErrorLogs);
                }

                Date toDate = simpleDateFormat.parse(to);
                Date fromDate = simpleDateFormat.parse(from);
                simpleDateFormat.format(toDate);
                simpleDateFormat.format(fromDate);
                for(int i  = 0;i<newErrorLogs.size();i++){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = dateFormat.parse(String.valueOf(newErrorLogs.get(i).getCreatedAt()));
                    Date filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(simpleDateFormat.format(date));
                    dates.add(filterDate);
                }
                ArrayList<?> filterDate = (ArrayList<?>) dates.stream().filter(date -> date.after(fromDate) && date.before(toDate) || date.equals(fromDate) || date.equals(toDate)).collect(Collectors.toList());
                if(filterDate.size()==0){
                    response = SuccessResponse.successHandler(HttpStatus.OK,false,"Successfully data fetched",filterDate);
                    return response;
                }else {
                    for(int i=0;i<filterDate.size();i++){
                        filterDates.add(i,simpleDateFormat.format(filterDate.get(i)));
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    newErrorLogs = newErrorLogs.stream().filter(item -> {
                        try {
                            return filterDates.contains(simpleDateFormat.format(dateFormat.parse(String.valueOf(item.getCreatedAt()))));
                        } catch (ParseException e) {
                            logger.info(e.getMessage());
                            e.printStackTrace();
                        }
                        return false;
                    }).collect(Collectors.toList());
                    response = SuccessResponse.successHandler(HttpStatus.OK, false, "Successfully data fetched",newErrorLogs);
                }
                return response;
            }
        }catch (Exception ex){
            logger.info(ex.getMessage());
            response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
        return response;
    }

    @GetMapping("/createTable")
    public ResponseEntity<?> findErrorLogs(){
        try{
            jdbcTemplate.execute("CREATE TABLE CN_LOG_ERROR(" + "AccountName VARCHAR(255), Status NUMERIC(3), Message VARCHAR(255), API_Name VARCHAR(255), Created_At VARCHAR(255))");
            //connectingToDB.Execute("DROP TABLE CN_LOG_ERROR");
            //connectingToDB.Execute("DELETE FROM CN_LOG_ERROR");
            List<Map<String,Object>> data = jdbcTemplate.queryForList("select * from CN_LOG_ERROR");
            return SuccessResponse.successHandler(HttpStatus.OK, false, "Successfully operation performed", data);
        }catch (Exception ex){
            logger.info(ex.getMessage());
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

    @GetMapping("data/fetchData/messageReceiptStatus")
    public ResponseEntity<?> findMessageReceiptStatus(@RequestParam("messageId") String messageId){
        try{

            String PROXY_SERVER_HOST = proxyServerHostUat;
            int PROXY_SERVER_PORT = 80;

            String clientId = this.clientId;
            String clientSecret = this.clientSecret;
            String tenantId = this.tenantId;
            String redirect_url = redirectUrl;
            final String GRAPH_DEFAULT_SCOPE = graphDefaultScope;

            String emailMessageId = messageId;

            ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .tenantId(tenantId)
                    .proxyOptions(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, PROXY_SERVER_PORT)))
                    .build();

            List<String> scopes = new ArrayList<>();
            scopes.add(GRAPH_DEFAULT_SCOPE);

            TokenCredentialAuthProvider tokenCredAuthProvider =
                    new TokenCredentialAuthProvider(scopes, clientSecretCredential);

            GraphServiceClient graphClient = GraphServiceClient
                    .builder()
                    .authenticationProvider(tokenCredAuthProvider)
                    .buildClient();

            Message message = graphClient.me()
                    .messages(emailMessageId)
                    .buildRequest()
                    .get();
//
//            User user = graphClient.users("service.supportuat@tatacommunications.com")
//                    .buildRequest()
//                    .get();

      //      Message message = graphClient.users(user.id).messages(emailMessageId).buildRequest().get();

            Boolean isReadReceiptRequested = message.isReadReceiptRequested;

            return SuccessResponse.successHandler(HttpStatus.OK, false, "Successfully operation performed", isReadReceiptRequested);
        }catch (Exception ex){
            logger.info(ex.getMessage());
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }


}
