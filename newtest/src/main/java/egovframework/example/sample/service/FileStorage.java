package egovframework.example.sample.service;

public class FileStorage {
	private int file_id;
	private String fname;
	private String path;
	private int participant_id;
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
	
	public int getParticipant_id() {
		return participant_id;
	}
	public void setParticipant_id(int participant_id) {
		this.participant_id = participant_id;
	}
	public FileStorage(String fname, String path) {
		super();
		this.fname = fname;
		this.path = path;
	}
	public FileStorage() {
		// TODO Auto-generated constructor stub
	}
	public FileStorage(String fname, String path, int participant_id) {
		super();
		this.fname = fname;
		this.path = path;
		this.participant_id = participant_id;
	}
	
}
