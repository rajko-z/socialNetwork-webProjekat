package model;

import java.time.LocalDateTime;

import util.DateUtil;


public class DirectMessage extends Entity {
	private User from;
	private User to;
	private LocalDateTime createdAt;
	private String text;
	private boolean adminSent;
	
	public DirectMessage() {
		super();
	}

	public DirectMessage(Long id,User from, User to, LocalDateTime createdAt, String text, boolean adminSent) {
		super(id);
		this.from = from;
		this.to = to;
		this.createdAt = createdAt;
		this.text = text;
		this.adminSent = adminSent;
	}

	 

 

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getText() {
		return text;
	}

	public boolean isAdminSent() {
		return adminSent;
	}

	@Override
	public String formatEntityForFile() {            //# id | from user id | to user id | text | createdAt | adminSent
		DateUtil dateUtil = new DateUtil();
		return String.format("%d|%d|%d|%s|%s|%s",
                this.getId(),
                this.getFrom().getId(),
                this.getTo().getId(),
                this.getText(),
                dateUtil.getStringFormatFromLocalDateTime(this.getCreatedAt()),
                this.isAdminSent()
                );
	}

	@Override
	public String toString() {
		return "DirectMessage{" +
				"from=" + from.getUsername() +
				", to=" + to.getUsername() +
				", createdAt=" + createdAt +
				", text='" + text + '\'' +
				", adminSent=" + adminSent +
				'}';
	}
}
