package org.cae.entity;

public class CallRecord extends Entity{

	private Song song;
	private String callId;
	private String callSource;
	private Short callVersion;
	public CallRecord(){}
	public CallRecord(String callId){
		this.callId=callId;
	}
	public Song getSong() {
		return song;
	}
	public void setSong(Song song) {
		this.song = song;
	}
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public String getCallSource() {
		return callSource;
	}
	public void setCallSource(String callSource) {
		this.callSource = callSource;
	}
	public Short getCallVersion() {
		return callVersion;
	}
	public void setCallVersion(Short callVersion) {
		this.callVersion = callVersion;
	}
	
}
