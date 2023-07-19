package com.netsurfingzone.consumer;

import com.azure.core.http.ProxyOptions;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.requests.AttachmentCollectionPage;
import com.microsoft.graph.requests.AttachmentCollectionResponse;
import com.microsoft.graph.requests.GraphServiceClient;
import com.netsurfingzone.config.FileUploadHelper;
import com.netsurfingzone.config.RegexConfig;
import com.netsurfingzone.dto.*;
import com.netsurfingzone.payload.SuccessResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
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

	@Resource(name = "refreshToken")
	private Map<String, String> tokenValueMap;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	FileUploadHelper fileUploadHelper;

	RegexConfig regexConfig = new RegexConfig();

    @Autowired
    private JdbcTemplate jdbcTemplate;

	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public ResponseEntity<?> receivedRfTemplateMessageV1(Notify message) throws IOException, MessagingException {
		logger.info("Message received in consumer = " + message.toString());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(message);
		String toList = message.getContact().getTo();
		String ccList = message.getContact().getCc();
		String content = message.getEventName().getEventName();
		String subject = message.getNotification().getType();

		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
		Recipient toRecipients = null;
		EmailAddress emailAddress = null;
		String[] strArray = toList.split(";");
		for (int j = 0; j < strArray.length; j++) {
			toRecipients = new Recipient();
			emailAddress = new EmailAddress();
			emailAddress.address = strArray[j];
			toRecipients.emailAddress = emailAddress;
			toRecipientsList.add(toRecipients);
		}

		LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
		Recipient ccRecipients = null;
		EmailAddress  ccEmailAddress = null;
		String[] strCCArray = ccList.split(";");
		for (int k = 0; k < strCCArray.length; k++) {
			ccRecipients = new Recipient();
			ccEmailAddress = new EmailAddress();
			ccEmailAddress.address = strCCArray[k];
			ccRecipients.emailAddress = ccEmailAddress;
			ccRecipientsList.add(ccRecipients);
		}

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
		//helper.setTo(toList);

		String html ="";
		if(message.getEventName().getEventName().equalsIgnoreCase("RF_RED_EVENT")) {
			html = templateEngine.process("RF_RED_EVENT", context);
		}else{
			html = templateEngine.process("RF_GREEN_EVENT", context);
		}

		String result = //sendMail();
				sendMail(toRecipientsList,ccRecipientsList,content,subject,html);
		//sendMailHTTP();

		logger.info("Notification sent successfully in mail = " + result.toString());
		return SuccessResponse.successHandler(HttpStatus.OK,false,"Succesfully consumed data",result.toString());

	}

//	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME_RF_TEMPLATE, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
//	public ResponseEntity<?> receivedRfTemplateMessageV2(Notify message) throws IOException, MessagingException {
//		logger.info("Message received in consumer = " + message.toString());
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonString = mapper.writeValueAsString(message);
//		String toList = message.getContact().getTo();
//		String ccList = message.getContact().getCc();
//		String content = message.getEventName().getEventName();
//		String subject = message.getAdditionalInfo().getAccDetails().get(0).getAccountname();
//		List<SummaryTable> summaryTableList = new ArrayList<SummaryTable>();
//
//		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
//		Recipient toRecipients = null;
//		EmailAddress emailAddress = null;
//		String[] strArray = toList.split(";");
//		for (int j = 0; j < strArray.length; j++) {
//			toRecipients = new Recipient();
//			emailAddress = new EmailAddress();
//			emailAddress.address = strArray[j];
//			toRecipients.emailAddress = emailAddress;
//			toRecipientsList.add(toRecipients);
//		}
//
//		LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
//		Recipient ccRecipients = null;
//		EmailAddress  ccEmailAddress = null;
//		String[] strCCArray = ccList.split(";");
//		for (int k = 0; k < strCCArray.length; k++) {
//			ccRecipients = new Recipient();
//			ccEmailAddress = new EmailAddress();
//			ccEmailAddress.address = strCCArray[k];
//			ccRecipients.emailAddress = ccEmailAddress;
//			ccRecipientsList.add(ccRecipients);
//		}
//
//		for(int i =0;i<message.getAdditionalInfo().getAccDetails().size();i++){
//			SummaryTable summaryTable = new SummaryTable();
//			summaryTable.setTicketNumber(message.getAdditionalInfo().getAccDetails().get(i).getTicketNumber());
//			summaryTable.setServiceID(message.getAdditionalInfo().getAccDetails().get(i).getServiceID());
//			summaryTable.setAccountName(message.getAdditionalInfo().getAccDetails().get(i).getAccountname());
//			summaryTable.setBandwidth(message.getAdditionalInfo().getAccDetails().get(i).getBandwidth());
//			summaryTable.setImpact(message.getAdditionalInfo().getAccDetails().get(i).getImpact());
//			summaryTable.setState(message.getAdditionalInfo().getAccDetails().get(i).getState());
//			summaryTable.setStatusReason(message.getAdditionalInfo().getAccDetails().get(i).getStatusReason());
//			summaryTableList.add(summaryTable);
//		}
//
//		Mail mail = new Mail();
//		Map model = new HashMap();
//		model.put("list", summaryTableList);
//		mail.setModel(model);
//
//		MimeMessage msg = emailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(msg,
//				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//				StandardCharsets.UTF_8.name());
//
//		Context context = new Context();
//		context.setVariables(mail.getModel());
//		//helper.setTo(toList);
//
//		String html ="";
//		html = templateEngine.process("summary_notification_template.html", context);
//		String result = //sendMail();
//				sendMail(toRecipientsList,ccRecipientsList,content,subject,html);
//		//sendMailHTTP();
//
//		logger.info("Notification sent successfully in mail = " + result.toString());
//		return SuccessResponse.successHandler(HttpStatus.OK,false,"Succesfully consumed data",result.toString());
//
//	}


	public String sendMailHTTP() throws UnsupportedEncodingException {
		String token = tokenValueMap.get("tokenValue");
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
			}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
			if (statusCode == 200) {
				res = EntityUtils.toString(response.getEntity());

				JsonObject jsonResp = new Gson().fromJson(res, JsonObject.class); // String to JSONObject

				if (jsonResp.has("access_token"))
					tokenValue = jsonResp.get("access_token").toString().replace("\"", "");
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
	public  String sendMail(LinkedList<Recipient> toList,LinkedList<Recipient> ccRecipientsList,String content,String subject,String html){
		String PROXY_SERVER_HOST = "10.133.12.181"; //UAT Proxy - 10.133.12.181   PROD Proxy -121.244.254.154 ;
		java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "SSLv2Hello, SSLv3, TLSv1, TLSv1.1");
		int PROXY_SERVER_PORT = 80;
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, PROXY_SERVER_PORT));
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setProxy(proxy);

		String clientId = "64d33c44-2d40-4d0f-a73a-dd0ed9950e1f";
		String clientSecret = "cOd01o649ogXPxvRHHnMvnW3LGi7ZSfrqUsa+HWlYGE=";
		String tenantId = "20210462-2c5e-4ec8-b3e2-0be950f292ca";
		String redirect_url = "https://graph.microsoft.com/";
		final String GRAPH_DEFAULT_SCOPE = "https://graph.microsoft.com/.default";

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

		Message message = new Message();
		message.subject = "Incident Summary Notification // "+subject;
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

		// Comment these snippet if production is readiness
		//message.ccRecipients = ccRecipientsList;

		/*LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
		Recipient ccRecipients = new Recipient();
		EmailAddress emailAddress1 = new EmailAddress();
		emailAddress1.address = ccList;//"suvarna.jagadale@tatacommunications.com";
		ccRecipients.emailAddress = emailAddress1;
		ccRecipientsList.add(ccRecipients);
		message.ccRecipients = ccRecipientsList;*/

		boolean saveToSentItems = true;
		graphClient.users("service.supportuat@tatacommunications.com").
				sendMail(UserSendMailParameterSet.
						newBuilder().
						withMessage(message).
						withSaveToSentItems(saveToSentItems).
						build()).
				buildRequest().
				post();

		System.out.println("+++++++++++Successfully send email with no attachment+++++++++++++");

		return "";
	}

	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME_SUMMARY, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public ResponseEntity<?> receivedSummaryNotificationMessage(SummaryPayload summaryPayload) throws IOException, MessagingException {
		logger.info("Message received in consumer = " + summaryPayload.toString());
		int i = 0;
		String ccList = "",toList = "",content = "",subject = "",htmlContent = "";
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(summaryPayload);

		List<SummaryTable> summaryTableList = new ArrayList<SummaryTable>();
		Map model = new HashMap();

		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
		LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();

		for(i = 0; i < summaryPayload.getAccDetailsList().size(); i++) {

			SummaryTable summaryTable = new SummaryTable();
				Recipient toRecipients = null;//= new Recipient();
				Recipient ccRecipients = null;//= new Recipient();
				EmailAddress emailAddress ;//= new EmailAddress();
				if(i == 0) {
					toList = summaryPayload.getAccDetailsList().get(i).getToEmail();
					String[] strArray = toList.split(";");
					for (int j = 0; j < strArray.length; j++) {
						toRecipients = new Recipient();
						emailAddress = new EmailAddress();
						emailAddress.address = strArray[j];
						toRecipients.emailAddress = emailAddress;
						toRecipientsList.add(toRecipients);
					}
				}

				if(i == 0) {
					ccList = summaryPayload.getAccDetailsList().get(i).getCcEmail();
					String[] strCcArray = ccList.split(";");
					for (int k = 0; k < strCcArray.length; k++) {
						ccRecipients = new Recipient();
						emailAddress = new EmailAddress();
						emailAddress.address = strCcArray[k];
						ccRecipients.emailAddress = emailAddress;
						ccRecipientsList.add(ccRecipients);
					}
				}

			/*ccList = summaryPayload.getAccDetailsList().get(i).getCcEmail().toString();
			content = summaryPayload.getAccDetailsList().get(i).getAccountname().toString();
			subject = summaryPayload.getAccDetailsList().get(i).getAccountname().toString();*/

				summaryTable.setTicketNumber(summaryPayload.getAccDetailsList().get(i).getTicketNumber());
				summaryTable.setServiceID(summaryPayload.getAccDetailsList().get(i).getServiceID());
				summaryTable.setAccountName(summaryPayload.getAccDetailsList().get(i).getAccountname());
				summaryTable.setBandwidth(summaryPayload.getAccDetailsList().get(i).getBandwidth());
				summaryTable.setImpact(summaryPayload.getAccDetailsList().get(i).getImpact());
				summaryTable.setState(summaryPayload.getAccDetailsList().get(i).getState());
				summaryTable.setStatusReason(summaryPayload.getAccDetailsList().get(i).getStatusReason());
				summaryTableList.add(summaryTable);
		}
		model.put("list", summaryTableList);
		ccList = summaryPayload.getAccDetailsList().get(0).getCcEmail();
		content = summaryPayload.getAccDetailsList().get(0).getAccountname();
		subject = summaryPayload.getAccDetailsList().get(0).getAccountname();

		MimeMessage msg = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(model);
		//helper.setTo(toList);

		String html = "";
		html = templateEngine.process("summary_notification_template.html", context);

		String result = //sendMail();
				sendMail(toRecipientsList,ccRecipientsList,content,subject,html);
		//sendMailHTTP();

		logger.info("Notification sent successfully in mail = " + result.toString());
		return SuccessResponse.successHandler(HttpStatus.OK,false,"Succesfully consumed data",result.toString());
	}


//	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME_SUMMARY_ATTACHMENTS, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
//	public ResponseEntity<?> receivedSummaryNotificationMessageWithAttachment(SummaryPayload summaryPayload) throws IOException, MessagingException {
//		logger.info("Message received in consumer = " + summaryPayload.toString());
//		int i = 0;
//		String ccList = "",toList = "",content = "",subject = "",htmlContent = "";
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonString = mapper.writeValueAsString(summaryPayload);
//
//		List<SummaryTable> summaryTableList = new ArrayList<SummaryTable>();
//		Map model = new HashMap();
//
//		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
//		LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
//
//		for(i = 0; i < summaryPayload.getAccDetailsList().size(); i++) {
//
//			SummaryTable summaryTable = new SummaryTable();
//			Recipient toRecipients = null;//= new Recipient();
//			Recipient ccRecipients = null;//= new Recipient();
//			EmailAddress emailAddress ;//= new EmailAddress();
//			if(i == 0) {
//				toList = summaryPayload.getAccDetailsList().get(i).getToEmail();
//				String[] strArray = toList.split(";");
//				for (int j = 0; j < strArray.length; j++) {
//					toRecipients = new Recipient();
//					emailAddress = new EmailAddress();
//					emailAddress.address = strArray[j];
//					toRecipients.emailAddress = emailAddress;
//					toRecipientsList.add(toRecipients);
//				}
//			}
//
//			if(i == 0) {
//				ccList = summaryPayload.getAccDetailsList().get(i).getCcEmail();
//				String[] strCcArray = ccList.split(";");
//				for (int k = 0; k < strCcArray.length; k++) {
//					ccRecipients = new Recipient();
//					emailAddress = new EmailAddress();
//					emailAddress.address = strCcArray[k];
//					ccRecipients.emailAddress = emailAddress;
//					ccRecipientsList.add(ccRecipients);
//				}
//			}
//
//			/*ccList = summaryPayload.getAccDetailsList().get(i).getCcEmail().toString();
//			content = summaryPayload.getAccDetailsList().get(i).getAccountname().toString();
//			subject = summaryPayload.getAccDetailsList().get(i).getAccountname().toString();*/
//
//			summaryTable.setTicketNumber(summaryPayload.getAccDetailsList().get(i).getTicketNumber());
//			summaryTable.setServiceID(summaryPayload.getAccDetailsList().get(i).getServiceID());
//			summaryTable.setAccountName(summaryPayload.getAccDetailsList().get(i).getAccountname());
//			summaryTable.setBandwidth(summaryPayload.getAccDetailsList().get(i).getBandwidth());
//			summaryTable.setImpact(summaryPayload.getAccDetailsList().get(i).getImpact());
//			summaryTable.setState(summaryPayload.getAccDetailsList().get(i).getState());
//			summaryTable.setStatusReason(summaryPayload.getAccDetailsList().get(i).getStatusReason());
//			summaryTableList.add(summaryTable);
//		}
//		model.put("list", summaryTableList);
//		ccList = summaryPayload.getAccDetailsList().get(0).getCcEmail();
//		content = summaryPayload.getAccDetailsList().get(0).getAccountname();
//		subject = summaryPayload.getAccDetailsList().get(0).getAccountname();
//
//		MimeMessage msg = emailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(msg,
//				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//				StandardCharsets.UTF_8.name());
//
//		Context context = new Context();
//		context.setVariables(model);
//		//helper.setTo(toList);
//
//		String html = "";
//		html = templateEngine.process("summary_notification_template.html", context);
//
//		String result = //sendMail();
//				sendMailWithAttachment(toRecipientsList,ccRecipientsList,content,subject,html,summaryPayload.getFileName());
//		//sendMailHTTP();
//
//		logger.info("Notification sent successfully in mail = " + result.toString());
//		return SuccessResponse.successHandler(HttpStatus.OK,false,"Succesfully consumed data",result.toString());
//	}


	//public  String sendMail(){
//	public  String sendMailWithAttachment(LinkedList<Recipient> toList,LinkedList<Recipient> ccRecipientsList,String content,String subject,String html,String file) throws IOException {
//		String PROXY_SERVER_HOST = "10.133.12.181"; //UAT Proxy - 10.133.12.181   PROD Proxy -121.244.254.154 ;
//		java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "SSLv2Hello, SSLv3, TLSv1, TLSv1.1");
//		int PROXY_SERVER_PORT = 80;
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, PROXY_SERVER_PORT));
//		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//		requestFactory.setProxy(proxy);
//
//		String clientId = "64d33c44-2d40-4d0f-a73a-dd0ed9950e1f";
//		String clientSecret = "cOd01o649ogXPxvRHHnMvnW3LGi7ZSfrqUsa+HWlYGE=";
//		String tenantId = "20210462-2c5e-4ec8-b3e2-0be950f292ca";
//		String redirect_url = "https://graph.microsoft.com/";
//		final String GRAPH_DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
//
//		ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
//				.clientId(clientId)
//				.clientSecret(clientSecret)
//				.tenantId(tenantId)
//				.proxyOptions(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress(PROXY_SERVER_HOST, PROXY_SERVER_PORT)))
//				.build();
//
//		List<String> scopes = new ArrayList<>();
//		scopes.add(GRAPH_DEFAULT_SCOPE);
//
//		TokenCredentialAuthProvider tokenCredAuthProvider =
//				new TokenCredentialAuthProvider(scopes, clientSecretCredential);
//
//		GraphServiceClient graphClient = GraphServiceClient
//				.builder()
//				.authenticationProvider(tokenCredAuthProvider)
//				.buildClient();
//
//		Message message = new Message();
//		message.subject = "Incident Summary Notification // "+subject;
//		ItemBody body = new ItemBody();
//		body.contentType = BodyType.HTML;
//		body.content = html.toString();
//		message.body = body;
//		/*LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
//		Recipient toRecipients = new Recipient();
//		EmailAddress emailAddress = new EmailAddress();
//		emailAddress.address = toList;//"suvarna.jagadale@tatacommunications.com";
//		toRecipients.emailAddress = emailAddress;
//		toRecipientsList.add(toRecipients);*/
//
//		message.toRecipients = toList;
//
//		String encodeFileContent = fileUploadHelper.load(file);
//		String encode = Base64.getEncoder().encodeToString(encodeFileContent.getBytes());
//		LinkedList<Attachment> attachmentsList = new LinkedList<Attachment>();
//		FileAttachment attachments = new FileAttachment();
//		attachments.name = file;
//		attachments.contentType = "text/plain";
//		attachments.contentBytes = Base64.getDecoder().decode(encode);
//		attachments.oDataType = "#microsoft.graph.fileAttachment";
//		attachmentsList.add(attachments);
//		AttachmentCollectionResponse attachmentCollectionResponse = new AttachmentCollectionResponse();
//		attachmentCollectionResponse.value = attachmentsList;
//		AttachmentCollectionPage attachmentCollectionPage = new AttachmentCollectionPage(attachmentCollectionResponse, null);
//		message.attachments = attachmentCollectionPage;
//
//		// Comment these snippet if production is readiness
//		//message.ccRecipients = ccRecipientsList;
//
//		/*LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
//		Recipient ccRecipients = new Recipient();
//		EmailAddress emailAddress1 = new EmailAddress();
//		emailAddress1.address = ccList;//"suvarna.jagadale@tatacommunications.com";
//		ccRecipients.emailAddress = emailAddress1;
//		ccRecipientsList.add(ccRecipients);
//		message.ccRecipients = ccRecipientsList;*/
//
//		boolean saveToSentItems = true;
//		graphClient.users("service.supportuat@tatacommunications.com").
//				sendMail(UserSendMailParameterSet.
//						newBuilder().
//						withMessage(message).
//						withSaveToSentItems(saveToSentItems).
//						build()).
//				buildRequest().
//				post();
//
//		System.out.println("+++++++++++Successfully send email with attachment+++++++++++++");
//
//		return "";
//	}
}
