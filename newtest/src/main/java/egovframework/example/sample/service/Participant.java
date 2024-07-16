package egovframework.example.sample.service;

public class Participant {
	private int participant_id;
	private int competition_id;
	private String application_title;
	private int total_score;
    private String competitionTitle; // 공모전 이름 필드 추가
	private FileStorage file;
	
	
	public String getCompetitionTitle() {
		return competitionTitle;
	}
	public void setCompetitionTitle(String competitionTitle) {
		this.competitionTitle = competitionTitle;
	}
	public int getParticipant_id() {
		return participant_id;
	}
	public void setParticipant_id(int participant_id) {
		this.participant_id = participant_id;
	}
	public int getCompetition_id() {
		return competition_id;
	}
	public void setCompetition_id(int competition_id) {
		this.competition_id = competition_id;
	}
	public String getApplication_title() {
		return application_title;
	}
	public void setApplication_title(String application_title) {
		this.application_title = application_title;
	}
	public int getTotal_score() {
		return total_score;
	}
	public void setTotal_score(int total_score) {
		this.total_score = total_score;
	}
	public FileStorage getFile() {
		return file;
	}
	public void setFile(FileStorage file) {
		this.file = file;
	}
	
}
