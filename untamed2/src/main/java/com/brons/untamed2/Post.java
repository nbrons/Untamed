package com.brons.untamed2;

public class Post {
	private int id;
	private String pid;
	private String mask_hex;
	private String mask_class;
	private int is_auth;
	private int community_id;
	private String text;
	private int like_count;
	private int comment_count;
	private long time_posted;
	private int has_voted;
	
	
	public Post(int Id, String Pid, String mask_hex, String mask_class, int is_auth, int community_id, String text, int like_count, int comment_count, long time_posted, int has_voted){
		setId(Id);
		setPid(Pid);
		setMask_hex(mask_hex);
		setMask_class(mask_class);
		setIs_auth(is_auth);
		setCommunity_id(community_id);
		setText(text);
		setLike_count(like_count);
		setComment_count(comment_count);
		setTime_posted(time_posted);
		setHas_voted(has_voted);
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	public String getMask_hex() {
		return mask_hex;
	}


	public void setMask_hex(String mask_hex) {
		this.mask_hex = mask_hex;
	}


	public String getMask_class() {
		return mask_class;
	}


	public void setMask_class(String mask_class) {
		this.mask_class = mask_class;
	}


	public int getIs_auth() {
		return is_auth;
	}


	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}


	public int getCommunity_id() {
		return community_id;
	}


	public void setCommunity_id(int community_id) {
		this.community_id = community_id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public int getLike_count() {
		return like_count;
	}


	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}


	public int getComment_count() {
		return comment_count;
	}


	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}


	public long getTime_posted() {
		return time_posted;
	}


	public void setTime_posted(long time_posted) {
		this.time_posted = time_posted;
	}


	public int getHas_voted() {
		return has_voted;
	}


	public void setHas_voted(int has_voted) {
		this.has_voted = has_voted;
	}



}
