package com.tcl.constant;

public class ApplicationConstant {

	public static final String KAFKA_LOCAL_SERVER_CONFIG = "localhost:9092";
	public static final String TOPIC_NAME = "netsurfingzone-topic-1";
	public static final String TOPIC_NAME_RF_TEMPLATE = "netsurfingzone-topic-2";
	public static final String TOPIC_NAME_SUMMARY = "netsurfingzone-topic-1-summary";
	public static final String TOPIC_NAME_SUMMARY_ATTACHMENTS = "netsurfingzone-topic-2-summary";
	public static final String KAFKA_LISTENER_CONTAINER_FACTORY = "kafkaListenerContainerFactory";
	public static final String KAFKA_LISTENER_CONTAINER_FACTORY_RF_V2 = "kafkaListenerContainerFactory1";
	public static final String GROUP_ID_JSON = "#{T(java.util.UUID).randomUUID().toString()}";//"group-id-json-1";

	public static final String RF_RED_EVENT = "RF_RED_EVENT";
	public static final String RF_GREEN_EVENT = "RF_GREEN_EVENT";
	public static final String Planned_Work_Cancel_Protecting = "Planned Work Cancel Protecting";
	public static final String Planned_Work_Unprotected = "Planned Work Unprotected";
	public static final String Planned_Work_Remdr_Protecting = "Planned Work Remdr Protecting";
	public static final String Planned_Work_Com_Protecting = "Planned Work Com-Protecting";
	public static final String Extension_Work_Protected = "Extension_Work Protected";
	public static final String pe_rescheduled = "pe_rescheduled";
	public static final String Planned_Work_Remdr_Protected = "Planned Work Remdr Protected";
	public static final String Extension_Work_Protecting = "Extension_Work Protecting";
	public static final String pe_scheduled = "pe_scheduled";
	public static final String Planned_Work_Comunsuc_Protected = "Planned Work Comunsuc-Protected";
	public static final String Planned_Work_Protection_Failure = "Planned Work Protection Failure";
	public static final String pe_closure_successful = "pe_closure_successful";
	public static final String pe_implement_extension = "pe_implement_extension";
	public static final String Planned_Work_Cancel_Protected = "Planned Work Cancel Protected";
	public static final String Planned_Work_Com_Protected = "Planned Work Com-Protected";
	public static final String pe_closure_unsuccessful = "pe_closure_unsuccessful";
	public static final String pe_reminder = "pe_reminder";
	public static final String Planned_Work_Com_Unprotected = "Planned Work Com-Unprotected";
	public static final String Planned_Work_Comuns_Unprotected = "Planned Work Comuns-Unprotected";
	public static final String Planned_Work_Cancel_Unprotected = "Planned Work Cancel Unprotected";
	public static final String ReSchedule_Protection_Failure = "ReSchedule_Protection_Failure";
	public static final String pe_canceled = "pe_canceled";
	public static final String ReSchedule_Work_Protecting = "ReSchedule_Work Protecting";
	public static final String DeleteNotificationForSIA = "DeleteNotificationForSIA";
	public static final String Planned_Work_Comunsu_Protecting = "Planned Work Comunsu-Protecting";
	public static final String Planned_Work_Protecting = "Planned Work Protecting";
	public static final String Extension_Work_Unprotected = "Extension_Work Unprotected";
	public static final String ReSchedule_Work_Protected = "ReSchedule_Work Protected";
	public static final String Planned_Work_Remdr_Unprotected = "Planned Work Remdr Unprotected";
	public static final String Conflicting_PE_Notifications = "Conflicting PE Notifications";

	public static final String Notification_Success = "Notification sent successfully";
	public static final String Notification_Duplicacy = "Notification already sent";
	public static final String Subject = "Incident Summary Notification //";
	public static final String Message_Received_Producer = "Message received in producer =";
	public static final String Message_Sent_Consumer = "Message sent successfully in consumer =";
	public static final String Message_Received_Consumer = "Message received in consumer =";
}
