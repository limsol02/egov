package egovframework.example.sample.service;

public class Score {
	private int score_id;
	private int participant_id;
	private int sheet_id;
	private int score;
	private int judge_id;
	
	public Score() {}
	public int getScore_id() {
		return score_id;
	}
	public void setScore_id(int score_id) {
		this.score_id = score_id;
	}
	public int getParticipant_id() {
		return participant_id;
	}
	public void setParticipant_id(int participant_id) {
		this.participant_id = participant_id;
	}
	public int getSheet_id() {
		return sheet_id;
	}
	public void setSheet_id(int sheet_id) {
		this.sheet_id = sheet_id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getJudge_id() {
		return judge_id;
	}
	public void setJudge_id(int judge_id) {
		this.judge_id = judge_id;
	}
	
}
