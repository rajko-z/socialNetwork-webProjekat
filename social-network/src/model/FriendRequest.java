package model;

import util.DateUtil;

import java.time.LocalDateTime;

public class FriendRequest extends Entity{
	private User from;
	private User to;
	private LocalDateTime createdAt;
	
	public FriendRequest() {
		super();
	}
	
	public FriendRequest(Long id, User from, User to, LocalDateTime createdAt) {
		super(id);
		this.from = from;
		this.to = to;
		this.createdAt = createdAt;
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

	@Override
	public String formatEntityForFile() {
		DateUtil dateUtil = new DateUtil();
		return String.format("%d|%d|%d|%s",
                this.getId(),
                this.getFrom().getId(),
                this.getTo().getId(),
                dateUtil.getStringFormatFromLocalDateTime(this.getCreatedAt())
                );
	}

	@Override
	public String toString() {
		return "FriendRequest{" +
				"from=" + from.getUserName() +
				", to=" + to.getUserName() +
				", createdAt=" + createdAt +
				'}';
	}
}
