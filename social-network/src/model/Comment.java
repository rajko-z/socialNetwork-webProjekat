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
