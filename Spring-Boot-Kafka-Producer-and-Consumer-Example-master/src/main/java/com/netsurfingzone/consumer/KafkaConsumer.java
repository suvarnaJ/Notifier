package com.netsurfingzone.consumer;

import com.azure.core.http.ProxyOptions;
import com.azure.core.util.Configuration;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.requests.GraphServiceClient;
import com.netsurfingzone.dto.*;
import net.bytebuddy.build.Plugin;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netsurfingzone.constant.ApplicationConstant;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@Component
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	Configuration configuration ;
	@Resource(name = "refreshToken")
	private Map<String, String> tokenValueMap;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;


	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void receivedMessage(Notify message) throws IOException, MessagingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(message);
		String toList = message.getContact().getTo();
		String ccList = message.getContact().getCc();
		String content = message.getEventName().getEventName();
		String subject = message.getNotification().getType();

		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
		Recipient toRecipients = new Recipient();
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.address = toList;//"suvarna.jagadale@tatacommunications.com";
		toRecipients.emailAddress = emailAddress;
		toRecipientsList.add(toRecipients);

		Mail mail = new Mail();
		Map model = new HashMap();
		model.put("ticketRef", message.getAdditionalInfo().getTicketRef());
		model.put("eventDescription", message.getAdditionalInfo().getEventDescription());
		model.put("bsName",  message.getAdditionalInfo().getBsName());
		model.put("circle", message.getAdditionalInfo().getCircle());
		model.put("city", message.getAdditionalInfo().getCity());
		model.put("bsType", message.getAdditionalInfo().getBsType());
		model.put("ip", message.getAdditionalInfo().getIp());
		model.put("siteID", message.getAdditionalInfo().getSiteID());
		model.put("infraProvider", message.getAdditionalInfo().getInfraProvider());
		model.put("iorID", message.getAdditionalInfo().getIorID());
		model.put("bsoCktID", message.getAdditionalInfo().getBsoCktID());
		model.put("outageStartTime", message.getAdditionalInfo().getOutageStartTime());
		model.put("impactedCustomer", message.getAdditionalInfo().getImpactedCustomer());
		model.put("sia", message.getAdditionalInfo().getSia());
		mail.setModel(model);

		MimeMessage msg = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(mail.getModel());
		helper.setTo(toList);
		//String html = templateEngine.process("newsletter-template", context);
		String html ="";
		System.out.println("template name ==="+message.getEventName().getEventName());
		if(message.getEventName().getEventName().equalsIgnoreCase("RF_RED_EVENT")) {
			html = templateEngine.process("RF_RED_EVENT", context);
		}else{
			 html = templateEngine.process("RF_GREEN_EVENT", context);
		}

		logger.info("Json message received using Kafka listener " + html);
		String result = //sendMail();
				sendMail(toRecipientsList,ccList,content,subject,html);
				//sendMailHTTP();

		logger.info("Result = " + result.toString());

	}


	public String sendMailHTTP() throws UnsupportedEncodingException {
		//sk.george@tatacommunications.com,piyush.shah2@tatacommunications.com
		String token = tokenValueMap.get("tokenValue");
		System.out.println("Refreshed token value  ----------"+token);
		String jsonRequest = "{\n" +
				"  \"message\": {\n" +
				"    \"subject\": \"Greeting from Graph API\",\n" +
				"    \"body\": {\n" +
				"      \"contentType\": \"html\",\n" +
				"      \"content\": \"Hi Team, Greeting from Spring boot kafka Graph API POC .........<html><head>The HTML content example</head><body><h3 title=\\\"Hello HTML!\\\">Titled bold head tag example</h3></body></html> Regards , Suvarna Jagadale\"\n" +
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
				System.out.println("Sending mail  ----------"+res.toString());
			}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// print result
		System.out.println("Mail sent ,Please check  ----------"+res.toString());

		return res.toString();
	}

	public String getToken() throws UnsupportedEncodingException {
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

	@Bean
	public Map<String, String> refreshToken() throws UnsupportedEncodingException {
		final Map<String, String> tokenMap = new HashMap<>();
		/*tokenMap.put("tokenValue", getToken());*/
		tokenMap.put("tokenValue", "getToken");
		return tokenMap;
	}

	//public  String sendMail(){
	public  String sendMail(LinkedList<Recipient> toList,String ccList,String content,String subject,String html){
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
				//.proxyOptions(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("10.133.12.181", 80)))
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
		message.subject = subject;
		ItemBody body = new ItemBody();
		body.contentType = BodyType.HTML;
		body.content = html.toString();
		message.body = body;
		/*LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
		Recipient toRecipients = new Recipient();
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.address = toList;//"suvarna.jagadale@tatacommunications.com";
		toRecipients.emailAddress = emailAddress;
		toRecipientsList.add(toRecipients);*/
		message.toRecipients = toList;
		LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
		Recipient ccRecipients = new Recipient();
		EmailAddress emailAddress1 = new EmailAddress();
		emailAddress1.address = ccList;//"suvarna.jagadale@tatacommunications.com";
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

		return "";
	}

	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME_SUMMARY, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void recSummaryNotification(SummaryPayload summaryPayload) throws IOException, MessagingException {

		int i =0;
		String ccList = "",toList="",content="",subject="",htmlContent = "";
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(summaryPayload);
		System.out.println("jsonString..............."+jsonString);

		List<SummaryTable> summaryTableList = new ArrayList<SummaryTable>();
		Map model = new HashMap();

		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();

		for(i=0;i<summaryPayload.getAccDetailsList().size();i++){
			SummaryTable summaryTable = new SummaryTable();

			Recipient toRecipients = new Recipient();
			EmailAddress emailAddress = new EmailAddress();
			toList = summaryPayload.getAccDetailsList().get(i).getToEmail().toString();
			String[] strArray  = toList.split(";");
			for (int j = 0; j<strArray.length; j++){
				emailAddress.address = strArray[j];//"suvarna.jagadale@tatacommunications.com";
				toRecipients.emailAddress = emailAddress;
				toRecipientsList.add(toRecipients);
			}


			ccList = summaryPayload.getAccDetailsList().get(i).getCcEmail().toString();
			content = summaryPayload.getAccDetailsList().get(i).getAccountname().toString();
			subject = summaryPayload.getAccDetailsList().get(i).getAccountname().toString();

			summaryTable.setTicketNumber( summaryPayload.getAccDetailsList().get(i).getTicketNumber().toString());
			summaryTable.setServiceID( summaryPayload.getAccDetailsList().get(i).getServiceID().toString());
			summaryTable.setAccountName( summaryPayload.getAccDetailsList().get(i).getAccountname().toString());
			summaryTable.setBandwidth( summaryPayload.getAccDetailsList().get(i).getBandwidth().toString());
			summaryTable.setCategory( summaryPayload.getAccDetailsList().get(i).getCategory().toString());
			summaryTable.setState( summaryPayload.getAccDetailsList().get(i).getState().toString());
			summaryTable.setStatusReason( summaryPayload.getAccDetailsList().get(i).getStatusReason().toString());
			summaryTableList.add(summaryTable);
		}
		model.put("list", summaryTableList);

		MimeMessage msg = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(model);
		//helper.setTo(toList);

		String html ="";
		html = templateEngine.process("summary_notification_template.html", context);

		logger.info("Json message received using Kafka listener " + html);
		String result = //sendMail();
				sendMail(toRecipientsList,ccList,content,subject,html);
		//sendMailHTTP();

		logger.info("Result = " + result.toString());
	}

}
