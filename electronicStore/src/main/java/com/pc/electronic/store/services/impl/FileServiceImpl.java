package com.pc.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pc.electronic.store.exceptions.BadApiRequest;
import com.pc.electronic.store.exceptions.GlobalExceptionHandler;
import com.pc.electronic.store.services.FileService;

import org.slf4j.LoggerFactory;


@Service
public class FileServiceImpl implements FileService {
	
	private org.slf4j.Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		// TODO Auto-generated method stub
		String originalFilename=file.getOriginalFilename();
		logger.info("Filename:{}", originalFilename);
		String filename=UUID.randomUUID().toString();
		String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtention=filename+extension;
		String fullPathWithFileName=path+fileNameWithExtention;
		
		if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
			File folder=new File(path);
			if(!folder.exists()) {
				folder.mkdirs();
			} 
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
			return fileNameWithExtention;
		} else {
			throw new BadApiRequest("File with this "+extension+" is not allowed");
		}
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String fullPath=path+File.separator+name;
		InputStream inputStream=new FileInputStream(fullPath);
		return inputStream;
	}

}
