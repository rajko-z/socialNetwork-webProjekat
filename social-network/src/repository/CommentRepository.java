package repository;

import java.time.LocalDateTime;
import model.Comment;
import model.User;
import util.DateUtil;

public class CommentRepository extends GenericRepository<Comment> {
	
	private final DateUtil dateUtil;
	private final UserRepository userRepository;

	public CommentRepository(String filePath, UserRepository userRepository) {
		super(filePath);
		this.dateUtil = new DateUtil();
		this.userRepository = userRepository;
	}

	// id | user_id | text | createAt | modifiedAt | isDeleted
	@Override
	protected Comment createEntityFromTokens(String[] tokens) {
		Long id = Long.parseLong(tokens[0]);
		User user = userRepository.getById(Long.parseLong(tokens[1]));
		String text = tokens[2];
		LocalDateTime createdAt = dateUtil.parseStringToLocalDateTime(tokens[3]);
		LocalDateTime modifiedAt = null;
		if (!tokens[4].equals("null")) {
			modifiedAt = dateUtil.parseStringToLocalDateTime(tokens[4]);
		}
		boolean isDeleted = Boolean.parseBoolean(tokens[5]);

		return new Comment(id, text, createdAt, modifiedAt, user, isDeleted);
	}

}
