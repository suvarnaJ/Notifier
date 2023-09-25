package com.tcl.deserializer;

import com.tcl.dto.SummaryPayload;
import org.springframework.core.serializer.Deserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.io.InputStream;

public class kafkaSummaryDeserializer extends ErrorHandlingDeserializer<SummaryPayload> implements Deserializer<SummaryPayload> {

    @Override
    public SummaryPayload deserialize(InputStream inputStream) {
        return null;
    }

}
