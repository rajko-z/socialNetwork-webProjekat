package repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import model.*;
import util.CSVFormatUtil;
import util.DateUtil;

public class UserRepository extends GenericRepository<User> {
	
	private final DateUtil dateUtil;
	private final CSVFormatUtil csvUtil;
	
	public UserRepository(String filePath) {
		super(filePath);
		this.dateUtil = new DateUtil();
		this.csvUtil = new CSVFormatUtil();
	}


	public User getByUsername(String username) {
		return this.data.values().stream()
				.filter(u -> u.getUsername().equals(username))
				.findFirst().orElse(null);
	}

	public User getByUsernameAndPassword(String username, String password) {
		return this.data.values().stream()
				.filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
				.findFirst().orElse(null);
	}

	@Override
	protected User createEntityFromTokens(String[] tokens) {
		Long id = Long.parseLong(tokens[0]);
		String userName = tokens[1];
		String password = tokens[2];
		String name = tokens[3];
		String surname = tokens[4];
		LocalDate dateOfBirth = dateUtil.parseStringToLocalDate(tokens[5]);
		Gender gender = tokens[6].equals("M") ? Gender.MALE : Gender.FEMALE;
		boolean isAccountPrivate = Boolean.parseBoolean(tokens[7]);
		boolean isBlocked = Boolean.parseBoolean(tokens[8]);
		Role role = tokens[9].equals("A") ? Role.ADMIN : Role.REGULAR;
		
		Long postIdForProfileImage = null;
		if (!tokens[10].equals("null"))
			postIdForProfileImage = Long.parseLong(tokens[10]);
		
		List<Long> postIds = csvUtil.createListOfIdsFromString(tokens[11]);
		List<Long> friendIds = csvUtil.createListOfIdsFromString(tokens[12]);
		
		User user = new User(id, userName, password, name, surname, dateOfBirth, gender, isAccountPrivate, isBlocked, role);
		user._profileImageId = postIdForProfileImage;
		user._postIds = postIds;
		user._friendIds = friendIds;
		
		return user;
	}

	public void setMissingBidirectionalParams(PostRepository postRepository) {
		for (Map.Entry<Long, User> e : this.data.entrySet()) {
			User user = e.getValue();

			List<User> friends = getListOfFriendsFromIds(user._friendIds);
			List<Post> posts = getListOfPostsFromIds(user._postIds, postRepository);
			if (user._profileImageId != null && user._profileImageId != 0)
				user.setProfileImage(postRepository.getById(user._profileImageId));

			user.setFriends(friends);
			user.setPosts(posts);

			this.data.put(e.getKey(), user);
		}
	}

	private List<User> getListOfFriendsFromIds(List<Long> friendIds) {
		List<User> retVal = new ArrayList<User>();
		for (Long i : friendIds) {
			retVal.add(this.getById(i));
		}
		return retVal;
	}
	private List<Post> getListOfPostsFromIds(List<Long> postIds, PostRepository postRepository) {
		List<Post> retVal = new ArrayList<Post>();
		for (Long i : postIds) {
			retVal.add(postRepository.getById(i));
		}
		return retVal;
	}






}
