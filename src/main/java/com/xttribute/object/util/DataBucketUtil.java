package com.xttribute.object.util;


import com.xttribute.object.exception.BadRequestException;
import com.xttribute.object.exception.FileWriteException;
import com.xttribute.object.exception.GCPFileUploadException;
import com.xttribute.object.exception.InvalidFileTypeException;
import com.xttribute.object.service.FileService;

import jakarta.annotation.PostConstruct;

import com.xttribute.object.dto.FileDto;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataBucketUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBucketUtil.class);

   // @Value("${gcp.config.file}")
    //private String gcpConfigFile;
   private String gcpConfigFile="xttribute_gcp_file_handler.json";
 
   // @Value("${gcp.project.id:umehe-267901}")
    private String gcpProjectId ="umehe-267901";

    //@Value("${gcp.bucket.id:xttribute.app}")
    private String gcpBucketId ="xttribute_app";

    //@Value("${gcp.dir.name:dev}")
    //private String gcpDirectoryName="dev1";

    public FileDto uploadFile(MultipartFile multipartFile, String fileName, String contentType, String folder) {

        try{
            LOGGER.debug("Start file uploading process on GCS");
            byte[] fileData = FileUtils.readFileToByteArray(convertFile(multipartFile));
            System.out.println(gcpConfigFile);
            InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();
            LOGGER.debug("break point");
            StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

            Storage storage = options.getService();
            Bucket bucket = storage.get(gcpBucketId,Storage.BucketGetOption.fields());

            RandomString id = new RandomString(6, ThreadLocalRandom.current());
            Blob blob = bucket.create(folder + "/" + fileName + "-" + id.nextString() + checkFileExtension(fileName), fileData, contentType);

            if(blob != null){
                LOGGER.debug("File successfully uploaded to GCS");
                return new FileDto(blob.getName(), blob.getMediaLink());
            }

        }catch (Exception e){
            LOGGER.error("An error occurred while uploading data. Exception: ", e);
            throw new GCPFileUploadException("An error occurred while storing data to GCS");
        }
        throw new GCPFileUploadException("An error occurred while storing data to GCS");
    }

    private File convertFile(MultipartFile file) {

        try{
            if(file.getOriginalFilename() == null){
                throw new BadRequestException("Original file name is null");
            }
            File convertedFile = new File(file.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(convertedFile);
            outputStream.write(file.getBytes());
            outputStream.close();
            LOGGER.debug("Converting multipart file : {}", convertedFile);
            return convertedFile;
        }catch (Exception e){
            throw new FileWriteException("An error has occurred while converting the file");
        }
    }

    private String checkFileExtension(String fileName) {
        if(fileName != null && fileName.contains(".")){
            String[] extensionList = {".png", ".jpeg", ".pdf", ".doc", ".mp3",".JPG"};

            for(String extension: extensionList) {
                if (fileName.endsWith(extension)) {
                    LOGGER.debug("Accepted file type : {}", extension);
                    return extension;
                }
            }
        }
        LOGGER.error("Not a permitted file type");
        throw new InvalidFileTypeException("Not a permitted file type");
    }
}