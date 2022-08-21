package model;

import java.time.LocalDate;
import java.util.List;

import util.CSVFormatUtil;

public class User extends Entity{
	private String userName;
	private String password;
	private String name;
	private String surname;
	private LocalDate dateOfBirth;
	private Gender gender;
	private boolean accountPrivate;
	private boolean isBlocked;
	private Role role;
	private Post profileImage;
	private List<Post> posts;
	private List<User> friends;
	
	
	// fields only used for parsing bidirectional associations in csv

	public Long _profileImageId;
	public List<Long> _postIds;
	public List<Long> _friendIds;
	
	
	
	public User() {
		super();
	}
	
	public User(Long id, String userName, String password, String name, String surname,
			LocalDate dateOfBirth, Gender gender,boolean accountPrivate, boolean isBlocked, Role role) {
		super(id);
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.accountPrivate = accountPrivate;
		this.isBlocked = isBlocked;
		this.role = role;
	}


	public boolean isBlocked() {
		return isBlocked;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public boolean isAccountPrivate() {
		return accountPrivate;
	}

	public Role getRole() {
		return role;
	}

	public Post getProfileImage() {
		return profileImage;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setProfileImage(Post profileImage) {
		this.profileImage = profileImage;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	//# id | userName | password | name | surname | dateOfBirth | gender | accountPrivate | isBlocked | role | postId for profile image | posts ids | friends ids

	
	public List<User> getFriends() {
		return friends;
	}

	@Override
	public String formatEntityForFile() {
		
		String postIdForProfileImage = "null";
		if (this.getProfileImage() != null && this.getProfileImage().getId() != 0)
			postIdForProfileImage = this.getProfileImage().getId().toString();
		
		CSVFormatUtil csvUtil = new CSVFormatUtil();

		String postIds = csvUtil.getFormattedStringOfIdsFromListOfEntities(this.getPosts());
		String friendIds = csvUtil.getFormattedStringOfIdsFromListOfEntities(this.getFriends());
		
		return String.format("%d|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s",
                this.getId(),
                this.getUserName(),
                this.getPassword(),
                this.getName(),
                this.getSurname(),
                this.getDateOfBirth().toString(),
                this.getGender().toString().charAt(0),
                this.isAccountPrivate(),
                this.isBlocked(),
                this.getRole().toString().charAt(0),
                postIdForProfileImage,
                postIds,
                friendIds
                );
	}

	@Override
	public String toString() {
		return "User{" +
				"userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", dateOfBirth=" + dateOfBirth +
				", gender=" + gender +
				", accountPrivate=" + accountPrivate +
				", isBlocked=" + isBlocked +
				", role=" + role +
				//", profileImage=" + profileImage +
				//", posts=" + posts +
				//", friends=" + friends.stream().map(User::getUserName) +
				'}';
	}
}
