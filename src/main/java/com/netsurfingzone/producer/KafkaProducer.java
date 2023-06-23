package com.netsurfingzone.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netsurfingzone.config.Constant;
import com.netsurfingzone.config.EncryptionConfig;
import com.netsurfingzone.config.RegexConfig;
import com.netsurfingzone.dto.*;
import com.netsurfingzone.payload.ErrorResponse;
import com.netsurfingzone.payload.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import com.netsurfingzone.constant.ApplicationConstant;
import org.springframework.web.cors.CorsConfiguration;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produce")
public class KafkaProducer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private boolean alreadyExecuted=false;

	RegexConfig regexConfig = new RegexConfig();
	EncryptionConfig encryptionConfig = new EncryptionConfig();

	@CrossOrigin(
			origins = { "http://localhost:8081" },     // You can add your allowed origins here
			methods = { RequestMethod.GET },   // Request Method
			allowCredentials = "true",
			maxAge = -1,
			allowedHeaders = CorsConfiguration.ALL,   // Allowed Headers
			exposedHeaders = {})
	@PostMapping("/email/sendnotification")
	public ResponseEntity<?> sendMessage(@RequestBody Notify message) {
		logger.info("Result = " + message.toString());
		ResponseEntity<?> response;
		while(!alreadyExecuted) {
			try {

				String to_Email  = message.getContact().getTo();
				String[] to_Email_Split = to_Email.split(";");
				if(to_Email_Split.length==1) {
					String encryptToEmail = encryptionConfig.decrypt(to_Email);
					message.getContact().setTo(encryptToEmail);
				}else{
					String s1="";
					for(int t = 0; t < to_Email_Split.length; t++){
						String encryptToEmail = encryptionConfig.decrypt(to_Email_Split[t]);
						s1+=encryptToEmail+";";
					}
					StringBuffer sb= new StringBuffer(s1);
					sb.deleteCharAt(sb.length()-1);
					message.getContact().setTo(sb.toString());
				}

				String cc_Email  = message.getContact().getCc();
				String[] cc_Email_Split = cc_Email.split(";");
				if(cc_Email_Split.length==1) {
					String encryptCcEmail = encryptionConfig.decrypt(cc_Email);
					message.getContact().setCc(encryptCcEmail);
				}else{
					String s1="";
					for(int t = 0; t < cc_Email_Split.length; t++){
						String encryptCcEmail = encryptionConfig.decrypt(cc_Email_Split[t]);
						s1+=encryptCcEmail+";";
					}
					StringBuffer sb= new StringBuffer(s1);
					sb.deleteCharAt(sb.length()-1);
					message.getContact().setCc(sb.toString());
				}

				//Validation's of eventName
				if (message.getEventName().getEventName().equals("")) {
					logger.info("Event Name is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 404, 'Event Name is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "')");
					response = ErrorResponse.errorHandler(HttpStatus.NOT_FOUND, true, "Event Name is mandatory");
					return response;
				}

				//Validation's of email format
				if (message.getContact().getCc().contains(",") || message.getContact().getTo().contains(",")) {
					logger.info("Invalid email format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 400, 'Invalid email format', '" + Constant.API_Name.RF_TEMPLATE + "')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid email format");
					return response;
				} else if (message.getContact().getTo().equals("") || message.getContact().getCc().equals("")) {
					logger.info("Email is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 404, 'Email is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "')");
					response = ErrorResponse.errorHandler(HttpStatus.NOT_FOUND, true, "Email is mandatory");
					return response;
				}

				//Validation's of toEmail
				String[] toEmailSplit = message.getContact().getTo().split(";");
				for (int t = 0; t < toEmailSplit.length; t++) {
					if (!(regexConfig.validateEmail(toEmailSplit[t]))) {
						logger.info("To List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 400, 'To List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "')");
						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "To List is invalid");
						return response;
					}
				}

				//Validation's of ccEmail
				String[] ccEmailSplit = message.getContact().getCc().split(";");
				for (int c = 0; c < ccEmailSplit.length; c++) {
					if (!(regexConfig.validateEmail(ccEmailSplit[c]))) {
						logger.info("Cc List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 400, 'Cc List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "')");
						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Cc List is invalid");
						return response;
					}
				}
				kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message);
				logger.info("Data sent successfully." + message.toString());
				alreadyExecuted = true;
				response = SuccessResponse.successHandler(HttpStatus.OK, false, "Data sent successfully", null);
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 400, '" + e.getMessage() + "', '" + Constant.API_Name.RF_TEMPLATE + "')");
				response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, e.getMessage());
				return response;
			}
		}
		return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Notification already sent");
	}

	@CrossOrigin(
			origins = { "http://localhost:8081" },     // You can add your allowed origins here
			methods = { RequestMethod.GET },   // Request Method
			allowCredentials = "true",
			allowedHeaders = CorsConfiguration.ALL,   // Allowed Headers
			exposedHeaders = {})
	@PostMapping("/email/notifySummary")
	public ResponseEntity<?> notifySummary(@RequestBody SummaryPayload summaryPayload) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		logger.info("Result = " + summaryPayload.getAccDetailsList().toString());

		try {
			for(int i = 0; i < summaryPayload.getAccDetailsList().size(); i++) {

				String to_Email  = summaryPayload.getAccDetailsList().get(i).getToEmail();
				String[] to_Email_Split = to_Email.split(";");
				if(to_Email_Split.length==1) {
					String encryptToEmail = encryptionConfig.decrypt(summaryPayload.getAccDetailsList().get(i).getToEmail());
					summaryPayload.getAccDetailsList().get(i).setToEmail(encryptToEmail);
				}else{
					String s1="";
					for(int t = 0; t < to_Email_Split.length; t++){
						String encryptToEmail = encryptionConfig.decrypt(to_Email_Split[t]);
						s1+=encryptToEmail+";";
					}
					StringBuffer sb= new StringBuffer(s1);
					sb.deleteCharAt(sb.length()-1);
					summaryPayload.getAccDetailsList().get(i).setToEmail(sb.toString());
				}

				//Validation's of email format
				if(summaryPayload.getAccDetailsList().get(i).getCcEmail().contains(",") || summaryPayload.getAccDetailsList().get(i).getToEmail().contains(",")){
					logger.info("Invalid email format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"')");
					return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
				}else if(summaryPayload.getAccDetailsList().get(i).getToEmail().equals("")){
					logger.info("Email is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 404, 'Email is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"')");
					return ErrorResponse.errorHandler(HttpStatus.NOT_FOUND,true,"Email is mandatory");
				}

				//Validation's of accountName
				if(summaryPayload.getAccDetailsList().get(i).getAccountname().equals("")){
					logger.info("Account Name is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 404, 'Account Name is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"')");
					return ErrorResponse.errorHandler(HttpStatus.NOT_FOUND,true,"Account Name is mandatory");
				}

				//Validation's of toEmail
				String toEmail = summaryPayload.getAccDetailsList().get(i).getToEmail();
				String[] toEmailSplit = toEmail.split(";");
				System.out.println(toEmailSplit.length);
				for(int t = 0; t < toEmailSplit.length; t++){
					if(!(regexConfig.validateEmail(toEmailSplit[t]))){
						logger.info("To List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
					}
				}

				//Validation's of ccEmail
//				String ccEmail = summaryPayload.getAccDetailsList().get(i).getCcEmail();
//				String[] ccEmailSplit = ccEmail.split(";");
//				for(int c = 0; c < ccEmailSplit.length; c++){
//					if(!(regexConfig.validateEmail(ccEmailSplit[c]))){
//						logger.info("Cc List is invalid");
//				        jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Cc List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"')");
//						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Cc List is invalid");
//					}
//				}

			}

			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME_SUMMARY, summaryPayload);
			logger.info("Notification sent successfully" + summaryPayload.toString().length());
			return SuccessResponse.successHandler(HttpStatus.OK,false,"Notification sent successfully",null);
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name) values ('null', 400, '"+e.getMessage()+"', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"')");
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

					try {
						notifySummary(summaryPayload);
					} catch (NoSuchPaddingException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (InvalidKeyException e) {
						e.printStackTrace();
					}
				}
		);
	}


}
