package repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Comment;
import model.Image;
import model.Post;
import model.PostType;
import util.CSVFormatUtil;
import util.DateUtil;

public class PostRepository extends GenericRepository<Post> {

	private final CommentRepository commentRepository;
	private final ImageRepository imageRepository;

	private final CSVFormatUtil csvUtil;
	private final DateUtil dateUtil;


	public PostRepository(String filePath, CommentRepository commentRepository, ImageRepository imageRepository) {
		super(filePath);
		this.commentRepository = commentRepository;
		this.imageRepository = imageRepository;
		this.csvUtil = new CSVFormatUtil();
		this.dateUtil = new DateUtil();
	}

	// # id | type of post(IMAGE, REGULAR, PROFILE) | text | list of comments ids | createdAt | imageId | isDeleted
	@Override
	protected Post createEntityFromTokens(String[] tokens) {
		Long id = Long.parseLong(tokens[0]);
		PostType postType = PostType.REGULAR_POST;
		if (tokens[1].charAt(0) == 'I') {
			postType = PostType.IMAGE_POST;
		} else if (tokens[1].charAt(0) == 'P') {
			postType = PostType.PROFILE_POST;
		}
		
		String text = tokens[2];
		List<Long> commentIds = csvUtil.createListOfIdsFromString(tokens[3]);
		List<Comment> comments = getListOfCommentsFromIds(commentIds);
		
		LocalDateTime createdAt = dateUtil.parseStringToLocalDateTime(tokens[4]);
		Image image = null;
		if (!tokens[5].equals("null"))
			image = imageRepository.getById(Long.parseLong(tokens[5]));

		boolean isDeleted = Boolean.parseBoolean(tokens[6]);
		
		return new Post(id,text, postType, comments, createdAt, image, isDeleted);
	}

	private List<Comment> getListOfCommentsFromIds(List<Long> commentIds) {
		List<Comment> retVal = new ArrayList<Comment>();
		for (Long i : commentIds) {
			retVal.add(commentRepository.getById(i));
		}
		return retVal;
	}
	

}
