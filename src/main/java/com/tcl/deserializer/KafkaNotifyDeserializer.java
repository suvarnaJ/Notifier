package com.tcl.deserializer;

import com.tcl.dto.Notify;
import org.springframework.core.serializer.Deserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.io.InputStream;

public class KafkaNotifyDeserializer extends ErrorHandlingDeserializer<Notify> implements Deserializer<Notify> {

    @Override
    public Notify deserialize(InputStream inputStream) {
        return null;
    }

}
