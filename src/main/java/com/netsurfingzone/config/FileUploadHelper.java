package com.netsurfingzone.config;

import com.netsurfingzone.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FileUploadHelper {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(FileUploadHelper.class);

    public final String UPLOAD_DIR_PRODUCTION="/TCLNotifier/repo/Notifier/src/main/resources/static/files";
    public final String UPLOAD_DIR_LOCAL="src\\main\\resources\\static\\files";

    public boolean uploadFile(MultipartFile multipartFile, HttpServletRequest request){
        boolean f = false;
        try{
            //read
            InputStream is = multipartFile.getInputStream();
            byte data[] = new byte[is.available()];
            is.read(data);
            //write
            FileOutputStream fos = new FileOutputStream(UPLOAD_DIR_PRODUCTION+File.separator+multipartFile.getOriginalFilename());
            fos.write(data);
            fos.flush();
            fos.close();
            f=true;
        }catch (Exception ex){
            Date date = new Date();
            String strDate = formatter.format(date);
            logger.info("Incorrect path to save the file");
            jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Incorrect path to save the file', '" + Constant.API_Name.SUMMARY_NOTIFICATION + "', '"+strDate+"')");
            ex.printStackTrace();
        }
        return f;

    }

    public String load(String filename) throws IOException {
        try {
            File file = ResourceUtils.getFile(UPLOAD_DIR_PRODUCTION+File.separator+filename);
            String content = new String(Files.readAllBytes(file.toPath()));
            return content;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
