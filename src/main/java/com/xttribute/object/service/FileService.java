package com.xttribute.object.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xttribute.object.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xttribute.object.dto.FileDto;
import com.xttribute.object.entity.InputFile;
import com.xttribute.object.exception.GCPFileUploadException;
import com.xttribute.object.repository.FileRepository;
import com.xttribute.object.util.DataBucketUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository = null;
    private final DataBucketUtil dataBucketUtil = new DataBucketUtil();
    //@Value("${gcp.config.file}")
    //private String gcpConfigFile;
    public List<FileDto> uploadFiles(MultipartFile[] files , String folder) {
        LOGGER.debug("Start file uploading service");
        List<FileDto> inputFiles = new ArrayList<>();

        Arrays.asList(files).forEach(file -> {
            String originalFileName = file.getOriginalFilename();
            if(originalFileName == null){
                throw new BadRequestException("Original file name is null");
            }
            Path path = new File(originalFileName).toPath();

            try {
                String contentType = Files.probeContentType(path);
                FileDto fileDto = dataBucketUtil.uploadFile(file, originalFileName, contentType, folder);

                if (fileDto != null) {
                    inputFiles.add(fileDto);
                    LOGGER.debug("File uploaded successfully, file name: {} and url: {}",fileDto.getFileName(), fileDto.getFileUrl() );
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while uploading. Error: ", e);
                throw new GCPFileUploadException("Error occurred while uploading");
            }
        });

       // fileRepository.saveAll(inputFiles);
        LOGGER.debug("File details successfully saved in the database");
        return inputFiles;
    }
}