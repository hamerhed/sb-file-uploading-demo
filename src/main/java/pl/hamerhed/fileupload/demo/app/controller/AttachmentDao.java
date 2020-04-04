package pl.hamerhed.fileupload.demo.app.controller;

import javax.validation.constraints.NotEmpty;

class AttachmentDao {
	
	@NotEmpty
	private String title;
	
	private AttachmentDao() {}
	
	public AttachmentDao(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "AttachmentDao [title=" + title + "]";
	}
	
	
}
