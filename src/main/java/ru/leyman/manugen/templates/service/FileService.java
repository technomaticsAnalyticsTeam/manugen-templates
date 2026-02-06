package ru.leyman.manugen.templates.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface FileService {

    String upload(MultipartFile file, String filename);

    Resource download(String filename) throws FileNotFoundException;

}
