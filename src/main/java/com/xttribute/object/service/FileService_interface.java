package com.xttribute.object.service;

import com.xttribute.object.entity.InputFile;
import org.springframework.web.multipart.MultipartFile;
import com.xttribute.object.dto.FileDto;
import java.util.List;

public interface FileService_interface {
     List<FileDto> uploadFiles(MultipartFile[] files);
}