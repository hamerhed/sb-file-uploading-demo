package pl.hamerhed.fileupload.demo.app.controller;

import java.util.Arrays;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

class AttachmentWithMultipleFilesDao {
	@NotEmpty
	private String title;
	
	private MultipartFile[] files;
	
	private AttachmentWithMultipleFilesDao() {
	}

	public AttachmentWithMultipleFilesDao(@NotEmpty String title, MultipartFile[] files) {
		super();
		this.title = title;
		this.files = files;
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "AttachmentWithMultipleFilesDao [title=" + title + ", files=" + Arrays.toString(files) + "]";
	}
	
}
