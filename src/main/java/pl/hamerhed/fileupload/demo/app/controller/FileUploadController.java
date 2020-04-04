package pl.hamerhed.fileupload.demo.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FileUploadController {
	private static final String BASE_PATH = "target" + File.separator;
	
	private static final org.slf4j.Logger clog = org.slf4j.LoggerFactory.getLogger(FileUploadController.class);
	
	@PostMapping("/upload")
	public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) {
		
		clog.debug("my file is " + file.getSize());
		if(file.getSize() <= 0 ) return ResponseEntity.badRequest().build();
		
		String filename = file.getOriginalFilename();
		Path path = Paths.get(BASE_PATH + filename);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/files/download/")
				.path(filename)
				.toUriString();
		return ResponseEntity.ok(fileDownloadUri);
	}
	
	@PostMapping(path = "/uploadform", consumes = {"multipart/form-data"})
	public ResponseEntity uploadFormToLocalFileSystem(@RequestParam("file") MultipartFile file, @Valid @ModelAttribute AttachmentDao dao) {
		clog.debug("my file is " + file.getSize());
		clog.debug("my model is " + dao);
		
		if(file.getSize() <= 0 ) return ResponseEntity.badRequest().build();
		
		String filename = file.getOriginalFilename();
		Path path = Paths.get(BASE_PATH + filename);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/files/download/")
				.path(filename)
				.toUriString();
		return ResponseEntity.ok(fileDownloadUri);
	}
	
	@PutMapping(path = "/updateform/{id}", consumes = {"multipart/form-data"})
	public ResponseEntity updateFileForm(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id, @Valid @ModelAttribute AttachmentDao dao) {
		clog.debug("update to my file " + file.getSize());
		clog.debug("update to model " + dao);
		
		if(file.getSize() <= 0 ) return ResponseEntity.badRequest().build();
		
		//TODO make update
		return ResponseEntity.ok().build();
	}	
	
	@PostMapping(path = "/updateformByModel/{id}", consumes = {"multipart/form-data"})
	public ResponseEntity updateFileFormByModel(@PathVariable("id") Long id, @Valid @ModelAttribute AttachmentWithFileDao dao) {
		clog.debug("update to model " + dao);
		clog.debug("update to my file " + dao.getFile().getSize());
		
		
		if(dao.getFile().getSize() <= 0 ) return ResponseEntity.badRequest().build();
		
		//TODO make update
		return ResponseEntity.ok().build();
	}	
	
	@PutMapping(path = "/updateformByModel/{id}", consumes = {"multipart/form-data"})
	public ResponseEntity updateFileFormByModelandPut(@PathVariable("id") Long id, @Valid @ModelAttribute AttachmentWithFileDao dao) {
		clog.debug("update to model " + dao);
		clog.debug("update to my file " + dao.getFile().getSize());
		
		
		if(dao.getFile().getSize() <= 0 ) return ResponseEntity.badRequest().build();
		
		//TODO make update
		return ResponseEntity.ok().build();
	}
	
	@PutMapping(path = "/updateFilesByModel/{id}", consumes = {"multipart/form-data"})
	public ResponseEntity updateFilesByModel(@PathVariable("id") Long id, @ModelAttribute AttachmentWithMultipleFilesDao dao) {
		clog.debug("update to model " + dao);
		clog.debug("update to my file " + dao.getFiles().length);
		
		if(dao.getFiles().length <= 0 ) return ResponseEntity.badRequest().build();
		
		//TODO make update
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(path = "/test")
	public String test() {
		return "ok";
	}
}
