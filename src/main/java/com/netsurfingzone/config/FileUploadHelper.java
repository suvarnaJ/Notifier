package com.netsurfingzone.config;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;

@Component
public class FileUploadHelper {

    public final String UPLOAD_DIR="target/classes/static/files/";

    public boolean uploadFile(MultipartFile multipartFile){
        boolean f = false;
        try{
            //read
            InputStream is = multipartFile.getInputStream();
            byte data[] = new byte[is.available()];
            is.read(data);
            //write
            FileOutputStream fos = new FileOutputStream(UPLOAD_DIR+ File.separator+multipartFile.getOriginalFilename());
            fos.write(data);
            fos.flush();
            fos.close();
            f=true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return f;

    }

    public String load(String filename) throws IOException {
        try {
            File file = ResourceUtils.getFile(UPLOAD_DIR+filename);
            String content = new String(Files.readAllBytes(file.toPath()));
            return content;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
