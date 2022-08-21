package repository;

import java.time.LocalDateTime;

import model.FriendRequest;
import model.User;
import util.DateUtil;

public class FriendRequestRepository extends GenericRepository<FriendRequest> {

	private final UserRepository userRepository;
	private final DateUtil dateUtil;

	public FriendRequestRepository(String filePath, UserRepository userRepository) {
		super(filePath);
		this.userRepository = userRepository;
		this.dateUtil = new DateUtil();
	}
	
	@Override
	protected FriendRequest createEntityFromTokens(String[] tokens) {
		Long id = Long.parseLong(tokens[0]);
		User from = this.userRepository.getById(Long.parseLong(tokens[1]));
		User to = this.userRepository.getById(Long.parseLong(tokens[2]));
		LocalDateTime createdAt = dateUtil.parseStringToLocalDateTime(tokens[3]);
		
		return new FriendRequest(id, from, to, createdAt);
	}
	
	
}
