package pl.hamerhed.fileupload.demo.app.controller;

import javax.validation.constraints.NotEmpty;

class FileMetadata {
	
	@NotEmpty
	private String title;
	private String filename;
	private String key;

	private FileMetadata() {
	}

	public FileMetadata(String title, String filename, String key) {
		super();
		this.title = title;
		this.filename = filename;
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "FileMetadata [title=" + title + ", filename=" + filename + ", key=" + key + "]";
	}

}
