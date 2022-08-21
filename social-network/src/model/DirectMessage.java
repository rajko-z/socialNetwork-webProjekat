package model;

import java.time.LocalDateTime;


public class DirectMessage extends Entity {
	private User from;
	private User to;
	private LocalDateTime createdAt;
	private String text;
	private boolean adminSent;
	
	public DirectMessage() {
		super();
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
	public String formatEntityForFile() {
		// TODO Auto-generated method stub
		return null;
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
