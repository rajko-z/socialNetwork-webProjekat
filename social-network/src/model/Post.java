package model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import util.CSVFormatUtil;
import util.DateUtil;

public class Post extends Entity{
	private String text;
	private PostType type;
	private List<Comment> comments;
	private LocalDateTime createdAt;
	private Image image;
	private boolean isDeleted;
	
	public Post() {
		super();
	}

	public Post(Long id, String text, PostType type, List<Comment> comments, LocalDateTime createdAt, Image image,
			boolean isDeleted) {
		super(id);
		this.text = text;
		this.type = type;
		this.comments = comments;
		this.createdAt = createdAt;
		this.image = image;
		this.isDeleted = isDeleted;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public String getText() {
		return text;
	}

	public PostType getType() {
		return type;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public String formatEntityForFile() {
		
		CSVFormatUtil csvUtil = new CSVFormatUtil();
		DateUtil dateUtil = new DateUtil();

		String commentIds = csvUtil.getFormattedStringOfIdsFromListOfEntities(this.getComments());
		String imageId = "null";
		if (this.getImage() != null)
			imageId = this.getImage().getId().toString();

		//# id | type of post(IMAGE, REGULAR, PROFILE) | text | list of comments ids | createdAt | imageId | isDeleted
		
		return String.format("%d|%s|%s|%s|%s|%s|%s",
                this.getId(),
                this.getType().toString().charAt(0),
                this.getText(),
                commentIds,
                dateUtil.getStringFormatFromLocalDateTime(this.getCreatedAt()),
                imageId,
                this.isDeleted()
                );
	}

	@Override
	public String toString() {
		return "Post{" +
				"text='" + text + '\'' +
				", type=" + type +
				", comments=" + comments +
				", createdAt=" + createdAt +
				", image=" + image +
				", isDeleted=" + isDeleted +
				'}';
	}
}
