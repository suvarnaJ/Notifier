package com.netsurfingzone.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netsurfingzone.config.RegexConfig;
import com.netsurfingzone.dto.*;
import com.netsurfingzone.payload.ErrorResponse;
import com.netsurfingzone.payload.SuccessResponse;
import org.apache.kafka.common.metrics.stats.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import com.netsurfingzone.constant.ApplicationConstant;
import org.springframework.web.cors.CorsConfiguration;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produce")
public class KafkaProducer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	RegexConfig regexConfig = new RegexConfig();

	@CrossOrigin(
			origins = { "http://localhost:8081" },     // You can add your allowed origins here
			methods = { RequestMethod.GET },   // Request Method
			allowCredentials = "true",
			allowedHeaders = CorsConfiguration.ALL,   // Allowed Headers
			exposedHeaders = {})
	@PostMapping("/email/sendnotification")
	public String sendMessage(@RequestBody Notify message) {
		logger.info("Result = " + message.toString());
		try {
			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message);
			logger.info("json message sent successfully." + message.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json message sent successfully.";
	}

	@CrossOrigin(
			origins = { "http://localhost:8081" },     // You can add your allowed origins here
			methods = { RequestMethod.GET },   // Request Method
			allowCredentials = "true",
			allowedHeaders = CorsConfiguration.ALL,   // Allowed Headers
			exposedHeaders = {})
	@PostMapping("/email/notifySummary")
	public ResponseEntity<?> notifySummary(@RequestBody SummaryPayload summaryPayload) {
		logger.info("Result = " + summaryPayload.toString());
		try {
			for(int i = 0; i < summaryPayload.getAccDetailsList().size(); i++) {

				//Validation's of accountNumber
				if(summaryPayload.getAccDetailsList().get(i).getAccountname().equals("")){
					logger.info("Account Number is mandatory");
					return ErrorResponse.errorHandler(HttpStatus.NOT_FOUND,true,"Account Number is mandatory");
				}

				//Validation's of email format
				if(summaryPayload.getAccDetailsList().get(i).getCcEmail().contains(",") || summaryPayload.getAccDetailsList().get(i).getToEmail().contains(",")){
					logger.info("Invalid email format");
					return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
				}else if(summaryPayload.getAccDetailsList().get(i).getToEmail().equals("")){
					logger.info("Email is mandatory");
					return ErrorResponse.errorHandler(HttpStatus.NOT_FOUND,true,"Email is mandatory");
				}

				//Validation's of toEmail
				String toEmail = summaryPayload.getAccDetailsList().get(i).getToEmail();
				String[] toEmailSplit = toEmail.split(";");
				for(int t = 0; t < toEmailSplit.length; t++){
					if(!(regexConfig.validateEmail(toEmailSplit[t]))){
						logger.info("To List is invalid");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
					}
				}

				//Validation's of ccEmail
//				String ccEmail = summaryPayload.getAccDetailsList().get(i).getCcEmail();
//				String[] ccEmailSplit = ccEmail.split(";");
//				for(int c = 0; c < ccEmailSplit.length; c++){
//					if(!(regexConfig.validateEmail(ccEmailSplit[c]))){
//						logger.info("Cc List is invalid");
//						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Cc List is invalid");
//					}
//				}
			}

			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME_SUMMARY, summaryPayload);
			logger.info("json message sent successfully." + summaryPayload.toString().length());
			return SuccessResponse.successHandler(HttpStatus.OK,false,"Data is extracted",summaryPayload.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,e.getMessage());
		}
	}

	@PostMapping("/email/casen")
	public String sortData(@RequestBody List<Example> example) throws JsonProcessingException {
		logger.info("Result = " + example.toString());
		Map<String,List<Example>> map = new HashMap<>();
		final String prefix = "AccDetails";
		map = example.stream().collect(Collectors.groupingBy(Example::getAccountName));

		try {
			convertObjectIntoAccDetails( map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(Arrays.asList(map));//ObjectMapper().writeValueAsString(Arrays.asList(map).toString())
		return map.toString();
	}

	public void convertObjectIntoAccDetails(Map<String,List<Example>> originalMap) {

		originalMap.forEach((k, v) -> {
			k = "AccDetails";
			SummaryPayload summaryPayload = new SummaryPayload();
			List<AccDetails> accDetailsList  = new ArrayList<>();
			AccDetails accDetails ;
			for(int i = 0; i < v.size(); i++) {
				accDetails = new AccDetails();
				accDetails.setAccountname(v.get(i).getAccountName());
				accDetails.setBandwidth(v.get(i).getBandwidth());
				accDetails.setImpact(v.get(i).getImpact());
				accDetails.setState(v.get(i).getState());
				accDetails.setToEmail(v.get(i).getToEmail());
				accDetails.setCcEmail(v.get(i).getCcEmail());
				accDetails.setOpenedAt(v.get(i).getOpenedAt());
				accDetails.setServiceID(v.get(i).getServiceID());
				accDetails.setStatusReason(v.get(i).getStatusReason());
				accDetails.setTicketNumber(v.get(i).getTicketNumber());
				accDetailsList.add(accDetails);
			}

			summaryPayload.setAccDetailsList(accDetailsList);
			System.out.println((k + ":" + v));
			System.out.println(("account name----->"+summaryPayload.getAccDetailsList().get(0).getAccountname()));

			notifySummary(summaryPayload);
			}
		);
	}


}
