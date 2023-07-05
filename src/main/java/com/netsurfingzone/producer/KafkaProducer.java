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
import java.text.SimpleDateFormat;
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
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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

				String contactToEmail  = message.getContact().getTo();
				String contactCcEmail  = message.getContact().getCc();

				if(contactToEmail==null || contactCcEmail==null || message.getEventName().getEventName()==null ){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid payload");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid payload', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid payload");
					return response;
				}

				//Validation's of eventName
				if (message.getEventName().getEventName().equals("")) {
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Event Name is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Event Name is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Event Name is mandatory");
					return response;
				}else if(!(regexConfig.validateEventName(message.getEventName().getEventName()))){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid event name format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid event name format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid event name format");
					return response;
				}

				//Validation's of email format
				if (message.getContact().getCc().contains(",") || message.getContact().getTo().contains(",")) {
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid email format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid email format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid email format");
					return response;
				} else if (message.getContact().getTo().equals("")) {
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Email is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Email is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Email is mandatory");
					return response;
				}

					String[] contactToEmailSplit = contactToEmail.split(";");
					if(contactToEmailSplit.length==1) {
						if(!(regexConfig.isBase64(contactToEmail))) {
							Date date = new Date();
							String strDate = formatter.format(date);
							logger.info("One of the email address is not encrypted");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
						}else {
							String encryptToEmail = encryptionConfig.decrypt(contactToEmail);
							message.getContact().setTo(encryptToEmail);
						}
					}else{
						String s1="";
						for(int t = 0; t < contactToEmailSplit.length; t++){
							if(!(regexConfig.isBase64(contactToEmailSplit[t]))) {
								Date date = new Date();
								String strDate = formatter.format(date);
								logger.info("One of the email address is not encrypted");
								jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
								return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
							}else{
								String encryptToEmail = encryptionConfig.decrypt(contactToEmailSplit[t]);
								s1+=encryptToEmail+";";
							}
						}
						StringBuffer sb= new StringBuffer(s1);
						sb.deleteCharAt(sb.length()-1);
						message.getContact().setTo(sb.toString());
					}


//					String[] contactCcEmailSplit = contactCcEmail.split(";");
//					if(contactCcEmailSplit.length==1) {
//						if(!(regexConfig.isBase64(contactCcEmail))) {
//				            Date date = new Date();
//					        String strDate = formatter.format(date);
//							logger.info("One of the email address is not encrypted");
//							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
//							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
//						}else{
//							String encryptCcEmail = encryptionConfig.decrypt(contactCcEmail);
//							message.getContact().setCc(encryptCcEmail);
//						}
//					}else{
//						String s1="";
//						for(int t = 0; t < contactCcEmailSplit.length; t++){
//							if(!(regexConfig.isBase64(contactCcEmailSplit[t]))) {
//				                Date date = new Date();
//				                String strDate = formatter.format(date);
//								logger.info("One of the email address is not encrypted");
//								jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
//								return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
//							}else{
//								String encryptCcEmail = encryptionConfig.decrypt(contactCcEmailSplit[t]);
//								s1+=encryptCcEmail+";";
//							}
//						}
//						StringBuffer sb= new StringBuffer(s1);
//						sb.deleteCharAt(sb.length()-1);
//						message.getContact().setCc(sb.toString());
//					}

				//Validation's of toEmail
				String[] contactToEmailSplitValid = message.getContact().getTo().split(";");
				for (int t = 0; t < contactToEmailSplitValid.length; t++) {
					if (!(regexConfig.validateEmail(contactToEmailSplitValid[t]))) {
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("To List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'To List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "To List is invalid");
						return response;
					}
				}

				//Validation's of ccEmail
//				String[] contactCcEmailSplitValid = message.getContact().getCc().split(";");
//				for (int c = 0; c < contactCcEmailSplitValid.length; c++) {
//					if (!(regexConfig.validateEmail(contactCcEmailSplitValid[c]))) {
//				        Date date = new Date();
//				        String strDate = formatter.format(date);
//						logger.info("Cc List is invalid");
//						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Cc List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
//						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Cc List is invalid");
//						return response;
//					}
//				}
				kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message);
				logger.info("Data sent successfully." + message.toString());
				alreadyExecuted = false;
				response = SuccessResponse.successHandler(HttpStatus.OK, false, "Notification sent successfully",null);
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				Date date = new Date();
				String strDate = formatter.format(date);
				jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '" + e.getMessage() + "', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
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
				String ticketNumber  = summaryPayload.getAccDetailsList().get(i).getTicketNumber();
				String cc_Email  = summaryPayload.getAccDetailsList().get(i).getCcEmail();
				String serviceID  = summaryPayload.getAccDetailsList().get(i).getServiceID();
				String accountName  = summaryPayload.getAccDetailsList().get(i).getAccountname();
				String bandwidth  = summaryPayload.getAccDetailsList().get(i).getBandwidth();
				String impact  = summaryPayload.getAccDetailsList().get(i).getImpact();
				String state  = summaryPayload.getAccDetailsList().get(i).getState();
				String statusReason  = summaryPayload.getAccDetailsList().get(i).getStatusReason();
				String openedAt  = summaryPayload.getAccDetailsList().get(i).getOpenedAt();


				if(to_Email==null || ticketNumber==null || serviceID==null || accountName==null || bandwidth==null || impact==null || state==null || statusReason==null || openedAt==null ){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid payload");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Invalid payload', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
					return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid payload");
				}

				//Validation's of email format
				if(summaryPayload.getAccDetailsList().get(i).getCcEmail().contains(",") || summaryPayload.getAccDetailsList().get(i).getToEmail().contains(",")){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid email format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
					return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
				}else if(summaryPayload.getAccDetailsList().get(i).getToEmail().equals("")){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Email is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Email is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
					return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Email is mandatory");
				}

				//Validation's of accountName
				if(summaryPayload.getAccDetailsList().get(i).getAccountname().equals("")){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Account Name is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
					return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Account Name is mandatory");
				}

					String[] to_Email_Split = to_Email.split(";");
					if(to_Email_Split.length==1) {
						if(!(regexConfig.isBase64(to_Email))){
							Date date = new Date();
							String strDate = formatter.format(date);
							logger.info("One of the email address is not encrypted");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'One of the email address is not encrypted', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"One of the email address is not encrypted");
						}else{
							String encryptToEmail = encryptionConfig.decrypt(summaryPayload.getAccDetailsList().get(i).getToEmail());
							summaryPayload.getAccDetailsList().get(i).setToEmail(encryptToEmail);
						}
					}else{
						String s1="";
						for(int t = 0; t < to_Email_Split.length; t++){
							if(!(regexConfig.isBase64(to_Email_Split[t]))){
								Date date = new Date();
								String strDate = formatter.format(date);
								logger.info("One of the email address is not encrypted");
								jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'One of the email address is not encrypted', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
								return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"One of the email address is not encrypted");
							}else{
								String encryptToEmail = encryptionConfig.decrypt(to_Email_Split[t]);
								s1+=encryptToEmail+";";
							}
						}
						StringBuffer sb= new StringBuffer(s1);
						sb.deleteCharAt(sb.length()-1);
						summaryPayload.getAccDetailsList().get(i).setToEmail(sb.toString());
					}

				//Validation's of toEmail
				String toEmail = summaryPayload.getAccDetailsList().get(i).getToEmail();
				String[] toEmailSplit = toEmail.split(";");
				System.out.println(toEmailSplit.length);
				for(int t = 0; t < toEmailSplit.length; t++){
					if(!(regexConfig.validateEmail(toEmailSplit[t]))){
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("To List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
					}
				}

				//Validation's of ccEmail
//				String ccEmail = summaryPayload.getAccDetailsList().get(i).getCcEmail();
//				String[] ccEmailSplit = ccEmail.split(";");
//				for(int c = 0; c < ccEmailSplit.length; c++){
//					if(!(regexConfig.validateEmail(ccEmailSplit[c]))){
//		                Date date = new Date();
//					    String strDate = formatter.format(date);
//						logger.info("Cc List is invalid");
//				        jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Cc List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
//						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Cc List is invalid");
//					}
//				}

			}

			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME_SUMMARY, summaryPayload);
			logger.info("Notification sent successfully" + summaryPayload.toString());
			return SuccessResponse.successHandler(HttpStatus.OK,false,"Notification sent successfully",null);
		} catch (Exception e) {
			e.printStackTrace();
			Date date = new Date();
			String strDate = formatter.format(date);
			jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '"+e.getMessage()+"', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
			return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,e.getMessage());
		}
	}


	@CrossOrigin(
			origins = { "http://localhost:8081" },     // You can add your allowed origins here
			methods = { RequestMethod.GET },   // Request Method
			allowCredentials = "true",
			maxAge = -1,
			allowedHeaders = CorsConfiguration.ALL,   // Allowed Headers
			exposedHeaders = {})
	@PostMapping("/email/rfTemplate")
	public ResponseEntity<?> sendData(@RequestBody Notify message) {
		ResponseEntity<?> response;
		while(!alreadyExecuted) {
			try {

				String contactToEmail  = message.getContact().getTo();
				String contactCcEmail  = message.getContact().getCc();

				if(contactToEmail==null || contactCcEmail==null || message.getEventName().getEventName()==null ){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid payload");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid payload', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid payload");
					return response;
				}

				//Validation's of eventName
				if (message.getEventName().getEventName().equals("")) {
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Event Name is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Event Name is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Event Name is mandatory");
					return response;
				}else if(!(regexConfig.validateEventName(message.getEventName().getEventName()))){
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid event name format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid event name format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid event name format");
					return response;
				}

				//Validation's of email format
				if (message.getContact().getCc().contains(",") || message.getContact().getTo().contains(",")) {
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Invalid email format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid email format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid email format");
					return response;
				} else if (message.getContact().getTo().equals("")) {
					Date date = new Date();
					String strDate = formatter.format(date);
					logger.info("Email is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Email is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Email is mandatory");
					return response;
				}

				String[] contactToEmailSplit = contactToEmail.split(";");
				if(contactToEmailSplit.length==1) {
					if(!(regexConfig.isBase64(contactToEmail))) {
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("One of the email address is not encrypted");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
					}else {
						String encryptToEmail = encryptionConfig.decrypt(contactToEmail);
						message.getContact().setTo(encryptToEmail);
					}
				}else{
					String s1="";
					for(int t = 0; t < contactToEmailSplit.length; t++){
						if(!(regexConfig.isBase64(contactToEmailSplit[t]))) {
							Date date = new Date();
							String strDate = formatter.format(date);
							logger.info("One of the email address is not encrypted");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
						}else{
							String encryptToEmail = encryptionConfig.decrypt(contactToEmailSplit[t]);
							s1+=encryptToEmail+";";
						}
					}
					StringBuffer sb= new StringBuffer(s1);
					sb.deleteCharAt(sb.length()-1);
					message.getContact().setTo(sb.toString());
				}


//					String[] contactCcEmailSplit = contactCcEmail.split(";");
//					if(contactCcEmailSplit.length==1) {
//						if(!(regexConfig.isBase64(contactCcEmail))) {
//		                    Date date = new Date();
//					        String strDate = formatter.format(date);
//							logger.info("One of the email address is not encrypted");
//							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
//							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
//						}else{
//							String encryptCcEmail = encryptionConfig.decrypt(contactCcEmail);
//							message.getContact().setCc(encryptCcEmail);
//						}
//					}else{
//						String s1="";
//						for(int t = 0; t < contactCcEmailSplit.length; t++){
//							if(!(regexConfig.isBase64(contactCcEmailSplit[t]))) {
//		                        Date date = new Date();
//					            String strDate = formatter.format(date);
//								logger.info("One of the email address is not encrypted");
//								jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'One of the email address is not encrypted', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
//								return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "One of the email address is not encrypted");
//							}else{
//								String encryptCcEmail = encryptionConfig.decrypt(contactCcEmailSplit[t]);
//								s1+=encryptCcEmail+";";
//							}
//						}
//						StringBuffer sb= new StringBuffer(s1);
//						sb.deleteCharAt(sb.length()-1);
//						message.getContact().setCc(sb.toString());
//					}

				//Validation's of toEmail
				String[] contactToEmailSplitValid = message.getContact().getTo().split(";");
				for (int t = 0; t < contactToEmailSplitValid.length; t++) {
					if (!(regexConfig.validateEmail(contactToEmailSplitValid[t]))) {
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("To List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'To List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "To List is invalid");
						return response;
					}
				}

				//Validation's of ccEmail
//				String[] contactCcEmailSplitValid = message.getContact().getCc().split(";");
//				for (int c = 0; c < contactCcEmailSplitValid.length; c++) {
//					if (!(regexConfig.validateEmail(contactCcEmailSplitValid[c]))) {
//		                Date date = new Date();
//					    String strDate = formatter.format(date);
//						logger.info("Cc List is invalid");
//						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Cc List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
//						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Cc List is invalid");
//						return response;
//					}
//				}

				for(int i = 0; i < message.getAdditionalInfo().getAccDetails().size(); i++) {

					String additionalInfo_to_Email  = message.getAdditionalInfo().getAccDetails().get(i).getToEmail();
					String ticketNumber  = message.getAdditionalInfo().getAccDetails().get(i).getTicketNumber();
					String additionalInfo_cc_Email  = message.getAdditionalInfo().getAccDetails().get(i).getCcEmail();
					String serviceID  = message.getAdditionalInfo().getAccDetails().get(i).getServiceID();
					String accountName  = message.getAdditionalInfo().getAccDetails().get(i).getAccountname();
					String bandwidth  = message.getAdditionalInfo().getAccDetails().get(i).getBandwidth();
					String impact  = message.getAdditionalInfo().getAccDetails().get(i).getImpact();
					String state  = message.getAdditionalInfo().getAccDetails().get(i).getState();
					String statusReason  = message.getAdditionalInfo().getAccDetails().get(i).getStatusReason();
					String openedAt  = message.getAdditionalInfo().getAccDetails().get(i).getOpenedAt();


					if(additionalInfo_to_Email==null || ticketNumber==null || serviceID==null || accountName==null || bandwidth==null || impact==null || state==null || statusReason==null || openedAt==null ){
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("Invalid payload");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'Invalid payload', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid payload");
					}

					//Validation's of email format
					if(message.getAdditionalInfo().getAccDetails().get(i).getCcEmail().contains(",") || message.getAdditionalInfo().getAccDetails().get(i).getToEmail().contains(",")){
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("Invalid email format");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
					}else if(message.getAdditionalInfo().getAccDetails().get(i).getToEmail().equals("")){
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("Email is mandatory");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'Email is mandatory', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Email is mandatory");
					}

					//Validation's of accountName
					if(message.getAdditionalInfo().getAccDetails().get(i).getAccountname().equals("")){
						Date date = new Date();
						String strDate = formatter.format(date);
						logger.info("Account Name is mandatory");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Account Name is mandatory");
					}

					String[] to_Email_Split = additionalInfo_to_Email.split(";");
					if(to_Email_Split.length==1) {
						if(!(regexConfig.isBase64(additionalInfo_to_Email))){
							Date date = new Date();
							String strDate = formatter.format(date);
							logger.info("One of the email address is not encrypted");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'One of the email address is not encrypted', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"One of the email address is not encrypted");
						}else{
							String encryptToEmail = encryptionConfig.decrypt(message.getAdditionalInfo().getAccDetails().get(i).getToEmail());
							message.getAdditionalInfo().getAccDetails().get(i).setToEmail(encryptToEmail);
						}
					}else{
						String s1="";
						for(int t = 0; t < to_Email_Split.length; t++){
							if(!(regexConfig.isBase64(to_Email_Split[t]))){
								Date date = new Date();
								String strDate = formatter.format(date);
								logger.info("One of the email address is not encrypted");
								jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'One of the email address is not encrypted', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
								return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"One of the email address is not encrypted");
							}else{
								String encryptToEmail = encryptionConfig.decrypt(to_Email_Split[t]);
								s1+=encryptToEmail+";";
							}
						}
						StringBuffer sb= new StringBuffer(s1);
						sb.deleteCharAt(sb.length()-1);
						message.getAdditionalInfo().getAccDetails().get(i).setToEmail(sb.toString());
					}

					//Validation's of toEmail
					String toEmail = message.getAdditionalInfo().getAccDetails().get(i).getToEmail();
					String[] toEmailSplit = toEmail.split(";");
					System.out.println(toEmailSplit.length);
					for(int t = 0; t < toEmailSplit.length; t++){
						if(!(regexConfig.validateEmail(toEmailSplit[t]))){
							Date date = new Date();
							String strDate = formatter.format(date);
							logger.info("To List is invalid");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
						}
					}

					//Validation's of ccEmail
//				String ccEmail = message.getAdditionalInfo().getAccDetails().get(i).getCcEmail();
//				String[] ccEmailSplit = ccEmail.split(";");
//				for(int c = 0; c < ccEmailSplit.length; c++){
//					if(!(regexConfig.validateEmail(ccEmailSplit[c]))){
//		                Date date = new Date();
//					    String strDate = formatter.format(date);
//						logger.info("Cc List is invalid");
//				        jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'Cc List is invalid', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
//						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Cc List is invalid");
//					}
//				}

				}
				kafkaTemplate.send(ApplicationConstant.TOPIC_NAME_RF_TEMPLATE, message);
				logger.info("Data sent successfully." + message.toString());
				alreadyExecuted = false;
				response = SuccessResponse.successHandler(HttpStatus.OK, false, "Notification sent successfully",null);
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				Date date = new Date();
				String strDate = formatter.format(date);
				jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '" + e.getMessage() + "', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
				response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, e.getMessage());
				return response;
			}
		}
		return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Notification already sent");
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
