package com.tcl.config;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.MalformedURLException;

@Component
public class FileUploadHelper {

    public String load(byte[] file) throws IOException, MalformedURLException {
        String content = new String(file);
        return content;
    }

}
