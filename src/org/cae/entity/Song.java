package org.cae.entity;

public class Song extends Entity{

	private String songId;
	private String songName;
	private String songSellTime;
	private String songOwner;
	private String songCover;
	private String songCreateTime;
	private String songLastModifyTime;
	private Integer songClick;
	private Short songVideo;
	public Song(){}
	public Song(String songId){
		this.songId=songId;
	}
	public Short getSongVideo() {
		return songVideo;
	}
	public void setSongVideo(Short songVideo) {
		this.songVideo = songVideo;
	}
	public String getSongId() {
		return songId;
	}
	public void setSongId(String songId) {
		this.songId = songId;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getSongSellTime() {
		return songSellTime;
	}
	public void setSongSellTime(String songSellTime) {
		this.songSellTime = songSellTime;
	}
	public String getSongOwner() {
		return songOwner;
	}
	public void setSongOwner(String songOwner) {
		this.songOwner = songOwner;
	}
	public String getSongCover() {
		return songCover;
	}
	public void setSongCover(String songCover) {
		this.songCover = songCover;
	}
	public String getSongCreateTime() {
		return songCreateTime;
	}
	public void setSongCreateTime(String songCreateTime) {
		this.songCreateTime = songCreateTime;
	}
	public String getSongLastModifyTime() {
		return songLastModifyTime;
	}
	public void setSongLastModifyTime(String songLastModifyTime) {
		this.songLastModifyTime = songLastModifyTime;
	}
	public Integer getSongClick() {
		return songClick;
	}
	public void setSongClick(Integer songClick) {
		this.songClick = songClick;
	}
	
}
