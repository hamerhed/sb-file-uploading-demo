package pl.hamerhed.fileupload.demo.app.controller;

public class FileUploadResult {
	private String filename;

	private String key;

	private String title;

	private String originalFileName;

	private long fileLength;

	private FileUploadResult() {
	}

	public FileUploadResult(String filename, String key, String title, String originalFileName, long fileLength) {
		super();
		this.filename = filename;
		this.key = key;
		this.title = title;
		this.originalFileName = originalFileName;
		this.fileLength = fileLength;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	@Override
	public String toString() {
		return "FileUploadResult [filename=" + filename + ", key=" + key + ", title=" + title + ", originalFileName="
				+ originalFileName + ", fileLength=" + fileLength + "]";
	}

}
