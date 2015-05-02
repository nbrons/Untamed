package com.brons.untamed2;

public class Message {
	private String eid;
	private String pid;
	private String pak;
	private Boolean is_unseen;
	private Boolean is_author;
	private String mask_class;
	private String mask_hex;
	private String summary_text;
	private String message_state;
	private String message_state_format;
	private long update_timestamp;
	private String update_time_format;
	private long expire_timestamp;
	private String expire_format;
	
	public Message(String eid, String pid, String pak, int is_unseen, int is_author, String mask_class, String mask_hex,
			String summary_text, String message_state, long update_timestamp,
			long expire_timestamp){
		
		setEid(eid);
		setPid(pid);
		setPak(pak);
		if(is_unseen==1){
			setIs_unseen(true);
		}
		else setIs_unseen(false);
		
		if(is_author==1){
			setIs_author(true);
		}
		else setIs_author(false);
		
		setMask_class(mask_class);
		setMask_hex(mask_hex);
		setSummary_text(summary_text);
		setMessage_state(message_state);
		setUpdate_timestamp(update_timestamp);
		setExpire_timestamp(expire_timestamp);
		
	}
	
	public Message(String eid, String pid, String pak, int is_unseen, int is_author, String mask_class, String mask_hex,
			String summary_text, String message_state, long update_timestamp){
		
		setEid(eid);
		setPid(pid);
		setPak(pak);
		if(is_unseen==1){
			setIs_unseen(true);
		}
		else setIs_unseen(false);
		
		if(is_author==1){
			setIs_author(true);
		}
		else setIs_author(false);
		
		setMask_class(mask_class);
		setMask_hex(mask_hex);
		setSummary_text(summary_text);
		setMessage_state(message_state);
		setUpdate_timestamp(update_timestamp);
		
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPak() {
		return pak;
	}

	public void setPak(String pak) {
		this.pak = pak;
	}

	public Boolean getIs_unseen() {
		return is_unseen;
	}

	public void setIs_unseen(Boolean is_unseen) {
		this.is_unseen = is_unseen;
	}

	public Boolean getIs_author() {
		return is_author;
	}

	public void setIs_author(Boolean is_author) {
		this.is_author = is_author;
	}

	public String getMask_class() {
		return mask_class;
	}

	public void setMask_class(String mask_class) {
		this.mask_class = mask_class;
	}

	public String getSummary_text() {
		return summary_text;
	}

	public void setSummary_text(String summary_text) {
		this.summary_text = summary_text;
	}

	public String getMessage_state() {
		return message_state;
	}

	public void setMessage_state(String message_state) {
		this.message_state = message_state;
	}


	public String getMessage_state_format() {
		return message_state_format;
	}

	public void setMessage_state_format(String message_state_format) {
		this.message_state_format = message_state_format;
	}

	public long getUpdate_timestamp() {
		return update_timestamp;
	}

	public void setUpdate_timestamp(long update_timestamp) {
		this.update_timestamp = update_timestamp;
	}

	public String getUpdate_time_format() {
		return update_time_format;
	}

	public void setUpdate_time_format(String update_time_format) {
		this.update_time_format = update_time_format;
	}


	public long getExpire_timestamp() {
		return expire_timestamp;
	}

	public void setExpire_timestamp(long expire_timestamp) {
		this.expire_timestamp = expire_timestamp;
	}

	public String getExpire_format() {
		return expire_format;
	}

	public void setExpire_format(String expire_format) {
		this.expire_format = expire_format;
	}

	public String getMask_hex() {
		return mask_hex;
	}

	public void setMask_hex(String mask_hex) {
		this.mask_hex = mask_hex;
	}

}
