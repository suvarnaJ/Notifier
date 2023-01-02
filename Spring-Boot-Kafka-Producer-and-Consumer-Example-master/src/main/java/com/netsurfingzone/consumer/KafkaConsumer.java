package com.netsurfingzone.consumer;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netsurfingzone.constant.ApplicationConstant;
import com.netsurfingzone.dto.Student;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void receivedMessage(Student message) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(message);

		logger.info("Json message received using Kafka listener " + jsonString);
		String result = sendMailHTTP();
		logger.info("Result = " + result.toString());
	}

	public String sendMailHTTP() throws UnsupportedEncodingException {
		//sk.george@tatacommunications.com,piyush.shah2@tatacommunications.com
		String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IndGMUhNN0N4QXZSaUZveWVxNmp5UF93Yzd5UEIzVmhFVHBLc0plRzNLemsiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSIsImtpZCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yMDIxMDQ2Mi0yYzVlLTRlYzgtYjNlMi0wYmU5NTBmMjkyY2EvIiwiaWF0IjoxNjY4NzQ5ODY1LCJuYmYiOjE2Njg3NDk4NjUsImV4cCI6MTY2ODc1Mzc2NSwiYWlvIjoiRTJaZ1lGaHk4dXZTVW0ySjBQV2hDMk5mZkU3UkFRQT0iLCJhcHBfZGlzcGxheW5hbWUiOiJDb25maWd1cmluZyBVQVQgZW1haWxzIGZvciBTZXJ2aWNlIFN1cHBvcnQgVWF0IiwiYXBwaWQiOiI2NGQzM2M0NC0yZDQwLTRkMGYtYTczYS1kZDBlZDk5NTBlMWYiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yMDIxMDQ2Mi0yYzVlLTRlYzgtYjNlMi0wYmU5NTBmMjkyY2EvIiwiaWR0eXAiOiJhcHAiLCJvaWQiOiJkMDM0MDlkZi0zNTFmLTQwZTAtYWU5Yi1jNjg1YWFkMzhhNGEiLCJyaCI6IjAuQVE4QVlnUWhJRjRzeUU2ejRndnBVUEtTeWdNQUFBQUFBQUFBd0FBQUFBQUFBQUFQQUFBLiIsInJvbGVzIjpbIk1haWwuUmVhZFdyaXRlIiwiVXNlci5SZWFkLkFsbCIsIk1haWwuUmVhZCIsIk1haWwuU2VuZCIsIk1haWwuUmVhZEJhc2ljIl0sInN1YiI6ImQwMzQwOWRmLTM1MWYtNDBlMC1hZTliLWM2ODVhYWQzOGE0YSIsInRlbmFudF9yZWdpb25fc2NvcGUiOiJBUyIsInRpZCI6IjIwMjEwNDYyLTJjNWUtNGVjOC1iM2UyLTBiZTk1MGYyOTJjYSIsInV0aSI6ImpoZVhnZUZjbjBTMk9nSnBzVXBrQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjA5OTdhMWQwLTBkMWQtNGFjYi1iNDA4LWQ1Y2E3MzEyMWU5MCJdLCJ4bXNfdGNkdCI6MTQyMzYwNTEyN30.Xqkz7e1YanhwnhbqPanDxkjB11dqnpQ4WmKJU9qbomI6FMriq37dpTo2EwGHXxUNWAD_bWxFHnyNj7sRtp3wq1O9NpMU2nfL0eiVyJ5ZqlZurZAkzT_Cg1-MdzbsSJXyUtWvWGAM87M7YSyjYUYHn1YSrG0G9t3JxOQWo2BMxfzTiNzHG45VbIc8kC9OqAzWuEcunwbrivp6sR2jGd_oVki8lIWwtXltEtA23J3uXk8LJ34SsHMBbIANXsybRS458OcLfM-JW6ck-TT5zInO6ar1DkoORxRnaKJtEklgLwekdkeJE-G4IfUpz_AUY-2zpeJhRV9zEQtbiOfC3IEbkA";
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
}
