package pl.hamerhed.fileupload.demo.app.controller;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import liquibase.util.Validate;

@RestController
public class FileUploadController {
	private static final String BASE_PATH = "target" + File.separator;
	
	private static final org.slf4j.Logger clog = org.slf4j.LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired
	private Validator validator;
	
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

	/**
	 * Enables uploading multiple files with set of metadata for each of file item.
	 * The workaround is to pass all metadata as a request part.
	 * The client needs to take care of preparing metadata and connection key between MultipartFile file and metadata item.
	 * It seems that Spring preserves items order so here the index-based approach was used. 
	 * But it looks like @Valid does not work on arrays so the validation must be done by own before domain code execution.
	 * @param id
	 * @param files
	 * @param metadata
	 * @return
	 * @throws BindException 
	 */
	@PutMapping(path = "/updateMultiFiles/{id}", consumes = {"multipart/form-data"})
	public ResponseEntity updateMultipleFilesWithMetadata(@PathVariable("id") Long id, @RequestPart("files") @Valid MultipartFile[] files,
			@RequestPart("metadata") FileMetadata[] metadata
			 ) throws BindException {
		clog.debug("metadata len " + metadata.length);
		clog.debug("files len " + files.length);
		
		//TODO make own validation
		for (FileMetadata item : metadata) {
			//TODO a batter way of errors processing and executing controllerAdvice
			Set<ConstraintViolation<FileMetadata>> errors = validate(item);
			if(errors.size() > 0) {
				return ResponseEntity.badRequest().build();
			}
		}
		
		int counter = 0;
		for (FileMetadata fileMetadata : metadata) {
			for (MultipartFile file : files) {
				if(file.getOriginalFilename().equals(fileMetadata.getFilename())) {
					counter++;
					break;
				}
			}
		}
		
		if(counter != files.length) return ResponseEntity.badRequest().body("different length");
		
		//domain code
		List<FileUploadResult> results = new ArrayList<>();
		for(int i = 0; files != null && i < files.length; i++) {
			FileMetadata item = metadata[i];
			FileUploadResult res = new FileUploadResult(item.getFilename(), item.getKey(), item.getTitle(), files[i].getOriginalFilename(), files[i].getSize());
			results.add(res);
		}
		return ResponseEntity.ok(results);
	}
	
	
	private Set<ConstraintViolation<FileMetadata>> validate(FileMetadata metadata) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(metadata);
	}
	
	@GetMapping(path = "/test")
	public String test() {
		return "ok";
	}
}
