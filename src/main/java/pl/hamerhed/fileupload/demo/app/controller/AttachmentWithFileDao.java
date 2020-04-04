package pl.hamerhed.fileupload.demo.app.controller;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

class AttachmentWithFileDao {
	@NotEmpty
	private String title;
	
	private MultipartFile file;
	
	private AttachmentWithFileDao() {}
	
	public AttachmentWithFileDao(String title, MultipartFile file) {
		this.title = title;
		this.file = file;
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	
	public MultipartFile getFile() {
		return file;
	}

	//important must be public in case of handling files
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "AttachmentWithFileDao [title=" + title + ", file=" + file + "]";
	}

	
}
