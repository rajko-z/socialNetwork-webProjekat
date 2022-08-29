package model;

import util.DateUtil;

import java.time.LocalDateTime;


public class Comment extends Entity {
	private String text;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private User user;
	private boolean isDeleted;

	public Comment() {
		super();
	}
	
	public Comment(Long id, String text, LocalDateTime createdAt, LocalDateTime modifiedAt, User user, boolean isDeleted) {
		super(id);
		this.text = text;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.user = user;
		this.isDeleted = isDeleted;
	}

	public Comment(String text, User user) {
		this.text = text;
		this.user = user;
		this.createdAt = LocalDateTime.now();
		this.modifiedAt = null;
		this.isDeleted = false;
	}


	public boolean isDeleted() {
		return isDeleted;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public User getUser() {
		return user;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	@Override
	public String formatEntityForFile() {
		DateUtil dateUtil = new DateUtil();

		String createdAtFormat = dateUtil.getStringFormatFromLocalDateTime(this.createdAt);

		String modifiedAtFormat = "null";
		if (this.modifiedAt != null)
			modifiedAtFormat = dateUtil.getStringFormatFromLocalDateTime(this.modifiedAt);

		return String.format("%d|%d|%s|%s|%s|%s",
                this.getId(),
                this.getUser().getId(),
                this.getText(),
                createdAtFormat,
                modifiedAtFormat,
                this.isDeleted()
                );
	}

	@Override
	public String toString() {
		return "Comment{" +
				"text='" + text + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				", userName=" + user.getUsername() +
				", isDeleted=" + isDeleted +
				'}';
	}
}
