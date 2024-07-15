package egovframework.example.sample.service;

public class FileStorage {
	private int file_id;
	private String fname;
	private String path;
	public int getFile_id() {
		return file_id;
	}
	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public FileStorage(String fname, String path) {
		super();
		this.fname = fname;
		this.path = path;
	}
	public FileStorage() {
		// TODO Auto-generated constructor stub
	}
}
