package com.tcl.producer;

import com.tcl.config.*;
import com.tcl.dto.*;
import com.tcl.payload.ErrorResponse;
import com.tcl.payload.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import com.tcl.constant.ApplicationConstant;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.multipart.MultipartFile;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
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

	@Autowired
	FileUploadHelper fileUploadHelper;

	private boolean alreadyExecuted=false;

	RegexConfig regexConfig = new RegexConfig();
	EncryptionConfig encryptionConfig = new EncryptionConfig();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	ConnectingToDB connectingToDB = new ConnectingToDB();
	List<?> emptyList = new ArrayList<>();
	Date date = new Date();

	@CrossOrigin(
			origins = { "http://localhost:8081" },
			methods = { RequestMethod.GET },
			allowCredentials = "true",
			maxAge = -1,
			allowedHeaders = CorsConfiguration.ALL,
			exposedHeaders = {})
	@PostMapping("/email/sendnotification")
	public ResponseEntity<?> sendRfTemplateNotificationV1(@RequestBody Notify message) {
		logger.info(ApplicationConstant.Message_Received_Producer+" "+message.getAdditionalInfo().toString());
		ResponseEntity<?> response = null;
		while(!alreadyExecuted) {
			try {

				message.getContact().setTo("MUKUL.SHARMA1@contractor.tatacommunications.com"); // for testing it is harcoded in lower env.
				String contactToEmail  = message.getContact().getTo();
				String contactCcEmail  = message.getContact().getCc();

				if(contactToEmail==null || contactCcEmail==null || message.getEventName().getEventName()==null ){
					String strDate = formatter.format(date);
					logger.info("Invalid payload");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid payload', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid payload");
					return response;
				}

				if (message.getEventName().getEventName().equals("")) {
					String strDate = formatter.format(date);
					logger.info("Event Name is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Event Name is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Event Name is mandatory");
					return response;
				}else if(!(regexConfig.validateEventName(message.getEventName().getEventName()))){
					String strDate = formatter.format(date);
					logger.info("Invalid event name format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid event name format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid event name format");
					return response;
				}

				if (message.getContact().getCc().contains(";") || message.getContact().getTo().contains(";")) {
					String strDate = formatter.format(date);
					logger.info("Invalid email format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid email format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid email format");
					return response;
				} else if (message.getContact().getTo().equals("")) {
					String strDate = formatter.format(date);
					logger.info("Email is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Email is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Email is mandatory");
					return response;
				}

				String[] contactToEmailSplitValid = message.getContact().getTo().split(",");
				for (int t = 0; t < contactToEmailSplitValid.length; t++) {
					if (!(regexConfig.validateEmail(contactToEmailSplitValid[t]))) {
						String strDate = formatter.format(date);
						logger.info("To List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'To List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "To List is invalid");
						return response;
					}
				}

				if(message.getAdditionalInfo().getSdwanCircuitId().size()==0){
					String strDate = formatter.format(date);
					logger.info("SdwanCircuitId is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'SdwanCircuitId is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "SdwanCircuitId is mandatory");
					return response;
				}

				if(message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.RF_RED_EVENT) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.RF_GREEN_EVENT) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Cancel_Protecting) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Unprotected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Remdr_Protecting) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Com_Protecting) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Extension_Work_Protected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.pe_rescheduled) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Remdr_Protected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Extension_Work_Protecting) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.pe_scheduled) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Comunsuc_Protected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Protection_Failure) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.pe_closure_successful) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.pe_implement_extension) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Cancel_Protected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Com_Protected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.pe_closure_unsuccessful) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.pe_reminder) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Com_Unprotected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Comuns_Unprotected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Cancel_Unprotected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.ReSchedule_Protection_Failure) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.pe_canceled) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.ReSchedule_Work_Protecting) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.DeleteNotificationForSIA) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Comunsu_Protecting) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Protecting) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Extension_Work_Unprotected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.ReSchedule_Work_Protected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Planned_Work_Remdr_Unprotected) || message.getEventName().getEventName().equalsIgnoreCase(ApplicationConstant.Conflicting_PE_Notifications)) {

					Set<String> underlayService = new HashSet<String>();
					Set<String> overlayService = new HashSet<>();

					for(int i=0;i<message.getAdditionalInfo().getSdwanCircuitId().size();i++){
						underlayService.add(message.getAdditionalInfo().getSdwanCircuitId().get(i).getUnderlayService());
						overlayService.add(message.getAdditionalInfo().getSdwanCircuitId().get(i).getOverlayService());
					}

					List<String> parent = overlayService.stream().collect(Collectors.toList());
					List<String> child = underlayService.stream().collect(Collectors.toList());

					StringBuilder sql = new StringBuilder("SELECT display_name,status FROM telecom where status!='Terminated' and parent IN (");
					for (int i = 0; i < parent.size(); i++) {
						sql.append("'" + parent.get(i) + "',");
					}
					sql.append(")");

					List<Map<String, Object>> rowCount = connectingToDB.Execute(sql.toString().replace(",)", ");"));

					List<Object> display_name = null;

					Long count = 0l;

					for (String str : child) {
						display_name = rowCount.stream().map(a -> a.get("display_name")).collect(Collectors.toList());
						Long collect = display_name.stream().filter(a -> a.equals(str)).collect(Collectors.counting());
						count=collect+count;
					}

					if((count>=1 && count<2) || rowCount.size()==1){
						message.getAdditionalInfo().setSiteIsolationOrServiceDegradation("Site Isolation");
						kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message);
						logger.info(ApplicationConstant.Message_Sent_Consumer+" "+message.toString());
						alreadyExecuted = false;
						HashMap<String,String> data = new HashMap<String,String>();
						data.put("value","Site Isolation");
						response = SuccessResponse.successHandler(HttpStatus.OK, false, ApplicationConstant.Notification_Success,data);
					}else if(count>=2){
						message.getAdditionalInfo().setSiteIsolationOrServiceDegradation("Service Degradation");
						kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message);
						logger.info(ApplicationConstant.Message_Sent_Consumer+" "+message.toString());
						alreadyExecuted = false;
						HashMap<String,String> data = new HashMap<String,String>();
						data.put("value","Service Degradation");
						response = SuccessResponse.successHandler(HttpStatus.OK, false, ApplicationConstant.Notification_Success,data);
					}else{
						response = SuccessResponse.successHandler(HttpStatus.OK, false, "Request is acknowledged",emptyList);
					}
				}else {
					String strDate = formatter.format(date);
					logger.info("Event Name not found");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Event Name not found', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Event Name not found");
				}
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				String strDate = formatter.format(date);
				jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '" + e.getMessage() + "', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
				response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, e.getMessage());
				return response;
			}
		}
		return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ApplicationConstant.Notification_Duplicacy);
	}


	@CrossOrigin(
			origins = { "http://localhost:8081" },
			methods = { RequestMethod.GET },
			allowCredentials = "true",
			maxAge = -1,
			allowedHeaders = CorsConfiguration.ALL,
			exposedHeaders = {})
	@PostMapping("/email/rfTemplate")
	public ResponseEntity<?> sendRfTemplateNotificationV2(@RequestBody Notify message) {
		logger.info(ApplicationConstant.Message_Received_Producer+" "+message.toString());
		ResponseEntity<?> response;
		while(!alreadyExecuted) {
			try {

				String contactToEmail  = message.getContact().getTo();
				String contactCcEmail  = message.getContact().getCc();

				if(contactToEmail==null || contactCcEmail==null || message.getEventName().getEventName()==null ){
					String strDate = formatter.format(date);
					logger.info("Invalid payload");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid payload', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid payload");
					return response;
				}

				if (message.getEventName().getEventName().equals("")) {
					String strDate = formatter.format(date);
					logger.info("Event Name is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Event Name is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Event Name is mandatory");
					return response;
				}else if(!(regexConfig.validateEventName(message.getEventName().getEventName()))){
					String strDate = formatter.format(date);
					logger.info("Invalid event name format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid event name format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid event name format");
					return response;
				}

				if (message.getContact().getCc().contains(",") || message.getContact().getTo().contains(",")) {
					String strDate = formatter.format(date);
					logger.info("Invalid email format");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid email format', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid email format");
					return response;
				} else if (message.getContact().getTo().equals("")) {
					String strDate = formatter.format(date);
					logger.info("Email is mandatory");
					jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Email is mandatory', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
					response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Email is mandatory");
					return response;
				}

				String[] contactToEmailSplit = contactToEmail.split(";");
				if(contactToEmailSplit.length==1) {
					if(!(regexConfig.isBase64(contactToEmail))) {
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

				String[] contactToEmailSplitValid = message.getContact().getTo().split(";");
				for (int t = 0; t < contactToEmailSplitValid.length; t++) {
					if (!(regexConfig.validateEmail(contactToEmailSplitValid[t]))) {
						String strDate = formatter.format(date);
						logger.info("To List is invalid");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'To List is invalid', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
						response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "To List is invalid");
						return response;
					}
				}

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
					String product  = message.getAdditionalInfo().getAccDetails().get(i).getProduct();
					String a_end_site_address  = message.getAdditionalInfo().getAccDetails().get(i).getA_end_site_address();
					String latest_update  = message.getAdditionalInfo().getAccDetails().get(i).getLatest_update();


					if(additionalInfo_to_Email==null || ticketNumber==null || serviceID==null || accountName==null || bandwidth==null || impact==null || state==null || statusReason==null || openedAt==null || product==null || a_end_site_address==null || latest_update==null ){
						String strDate = formatter.format(date);
						logger.info("Invalid payload");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'Invalid payload', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid payload");
					}

					if(message.getAdditionalInfo().getAccDetails().get(i).getCcEmail().contains(",") || message.getAdditionalInfo().getAccDetails().get(i).getToEmail().contains(",")){
						String strDate = formatter.format(date);
						logger.info("Invalid email format");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
					}else if(message.getAdditionalInfo().getAccDetails().get(i).getToEmail().equals("")){
						String strDate = formatter.format(date);
						logger.info("Email is mandatory");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'Email is mandatory', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Email is mandatory");
					}

					if(message.getAdditionalInfo().getAccDetails().get(i).getAccountname().equals("")){
						String strDate = formatter.format(date);
						logger.info("Account Name is mandatory");
						jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
						return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Account Name is mandatory");
					}

					String[] to_Email_Split = additionalInfo_to_Email.split(";");
					if(to_Email_Split.length==1) {
						if(!(regexConfig.isBase64(additionalInfo_to_Email))){
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

					String toEmail = message.getAdditionalInfo().getAccDetails().get(i).getToEmail();
					String[] toEmailSplit = toEmail.split(";");
					for(int t = 0; t < toEmailSplit.length; t++){
						if(!(regexConfig.validateEmail(toEmailSplit[t]))){
							String strDate = formatter.format(date);
							logger.info("To List is invalid");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+message.getAdditionalInfo().getAccDetails().get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.RF_TEMPLATE+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
						}
					}

				}
				kafkaTemplate.send(ApplicationConstant.TOPIC_NAME_RF_TEMPLATE, message);
				logger.info(ApplicationConstant.Message_Sent_Consumer+" "+message.toString());
				alreadyExecuted = false;
				response = SuccessResponse.successHandler(HttpStatus.OK, false, ApplicationConstant.Notification_Success,emptyList);
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				String strDate = formatter.format(date);
				jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '" + e.getMessage() + "', '" + Constant.API_Name.RF_TEMPLATE + "', '"+strDate+"')");
				response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, e.getMessage());
				return response;
			}
		}
		return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ApplicationConstant.Notification_Duplicacy);
	}

	@CrossOrigin(
			origins = { "http://localhost:8081" },
			methods = { RequestMethod.GET },
			allowCredentials = "true",
			allowedHeaders = CorsConfiguration.ALL,
			exposedHeaders = {})
	@PostMapping(value = "/email/notifySummary")
	public ResponseEntity<?> sendSummaryNotificationV1(@RequestBody SummaryPayload summaryPayload) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		logger.info(ApplicationConstant.Message_Received_Producer+" "+summaryPayload.toString());
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
						String product  = summaryPayload.getAccDetailsList().get(i).getProduct();
						String a_end_site_address  = summaryPayload.getAccDetailsList().get(i).getA_end_site_address();
						String latest_update  = summaryPayload.getAccDetailsList().get(i).getLatest_update();

						if(to_Email==null || ticketNumber==null || serviceID==null || accountName==null || bandwidth==null || impact==null || state==null || statusReason==null || openedAt==null || product==null || a_end_site_address==null || latest_update==null){
							String strDate = formatter.format(date);
							logger.info("Invalid payload");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Invalid payload', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid payload");
						}

						if(summaryPayload.getAccDetailsList().get(i).getCcEmail().contains(",") || summaryPayload.getAccDetailsList().get(i).getToEmail().contains(",")){
							String strDate = formatter.format(date);
							logger.info("Invalid email format");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
						}else if(summaryPayload.getAccDetailsList().get(i).getToEmail().equals("")){
							String strDate = formatter.format(date);
							logger.info("Email is mandatory");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Email is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Email is mandatory");
						}

						if(summaryPayload.getAccDetailsList().get(i).getAccountname().equals("")){
							String strDate = formatter.format(date);
							logger.info("Account Name is mandatory");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Account Name is mandatory");
						}

						String[] to_Email_Split = to_Email.split(";");
						if(to_Email_Split.length==1) {
							if(!(regexConfig.isBase64(to_Email))){
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

						String toEmail = summaryPayload.getAccDetailsList().get(i).getToEmail();
						String[] toEmailSplit = toEmail.split(";");
						for(int t = 0; t < toEmailSplit.length; t++){
							if(!(regexConfig.validateEmail(toEmailSplit[t]))){
								String strDate = formatter.format(date);
								logger.info("To List is invalid");
								jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
								return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
							}
						}
			}
			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME_SUMMARY, summaryPayload);
			logger.info(ApplicationConstant.Message_Sent_Consumer+" "+summaryPayload.toString());
			return SuccessResponse.successHandler(HttpStatus.OK,false,ApplicationConstant.Notification_Success,emptyList);
		} catch (Exception e) {
			e.printStackTrace();
			String strDate = formatter.format(date);
			jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '"+e.getMessage()+"', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
			return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,e.getMessage());
		}
	}

	@CrossOrigin(
			origins = { "http://localhost:8081" },
			methods = { RequestMethod.GET },
			allowCredentials = "true",
			allowedHeaders = CorsConfiguration.ALL,
			exposedHeaders = {})
	@PostMapping(value = "/email1.1/notifySummaryWithAttachment",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> sendSummaryNotificationV2(@RequestPart("AccDetails") SummaryPayload summaryPayload, @RequestPart("file") MultipartFile file,HttpServletRequest request) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		logger.info(ApplicationConstant.Message_Received_Producer+" "+summaryPayload.toString());
		try {
			if (file.isEmpty()) {
				String strDate = formatter.format(date);
				logger.info("Request must contain file");
				jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Request must contain file', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
				return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,"Request must contain file");
			}else if (!file.getContentType().equals("text/plain")) {
				String strDate = formatter.format(date);
				logger.info("Only TEXT content are allowed");
				jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Only TEXT content are allowed', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
				return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,"Only TEXT content are allowed");
			}else{
					summaryPayload.setFileData(file.getBytes());
					summaryPayload.setFileName(file.getOriginalFilename());
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
						String product  = summaryPayload.getAccDetailsList().get(i).getProduct();
						String a_end_site_address  = summaryPayload.getAccDetailsList().get(i).getA_end_site_address();
						String latest_update  = summaryPayload.getAccDetailsList().get(i).getLatest_update();

						if(to_Email==null || ticketNumber==null || serviceID==null || accountName==null || bandwidth==null || impact==null || state==null || statusReason==null || openedAt==null || product==null || a_end_site_address==null || latest_update==null ){
							String strDate = formatter.format(date);
							logger.info("Invalid payload");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Invalid payload', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid payload");
						}

						if(summaryPayload.getAccDetailsList().get(i).getCcEmail().contains(",") || summaryPayload.getAccDetailsList().get(i).getToEmail().contains(",")){
							String strDate = formatter.format(date);
							logger.info("Invalid email format");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
						}else if(summaryPayload.getAccDetailsList().get(i).getToEmail().equals("")){
							String strDate = formatter.format(date);
							logger.info("Email is mandatory");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'Email is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Email is mandatory");
						}

						if(summaryPayload.getAccDetailsList().get(i).getAccountname().equals("")){
							String strDate = formatter.format(date);
							logger.info("Account Name is mandatory");
							jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
							return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Account Name is mandatory");
						}

						String[] to_Email_Split = to_Email.split(";");
						if(to_Email_Split.length==1) {
							if(!(regexConfig.isBase64(to_Email))){
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

						String toEmail = summaryPayload.getAccDetailsList().get(i).getToEmail();
						String[] toEmailSplit = toEmail.split(";");
						for(int t = 0; t < toEmailSplit.length; t++){
							if(!(regexConfig.validateEmail(toEmailSplit[t]))){
								String strDate = formatter.format(date);
								logger.info("To List is invalid");
								jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+summaryPayload.getAccDetailsList().get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
								return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
							}
						}
					}
			}
			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME_SUMMARY_ATTACHMENTS, summaryPayload);
			logger.info(ApplicationConstant.Message_Sent_Consumer+" "+summaryPayload.toString());
			return SuccessResponse.successHandler(HttpStatus.OK,false,ApplicationConstant.Notification_Success,emptyList);
		} catch (Exception e) {
			e.printStackTrace();
			String strDate = formatter.format(date);
			jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '"+e.getMessage()+"', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
			return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,e.getMessage());
		}
	}

}
