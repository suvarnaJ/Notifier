package com.netsurfingzone.deserializer;

import com.netsurfingzone.dto.AccDetails;
import com.netsurfingzone.dto.Notify;
import com.netsurfingzone.dto.SummaryPayload;
import org.springframework.core.serializer.Deserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.io.InputStream;

public class kafkaSummaryDeserializer extends ErrorHandlingDeserializer<SummaryPayload> implements Deserializer<SummaryPayload> {

    @Override
    public SummaryPayload deserialize(InputStream inputStream) {
        return null;
    }

}
