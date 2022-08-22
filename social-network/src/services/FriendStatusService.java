package services;

import lombok.NoArgsConstructor;
import model.FriendRequest;
import model.FriendStatus;
import model.User;
import repository.FriendRequestRepository;
import repository.RepoFactory;


public class FriendStatusService {

    private final FriendRequestRepository friendRequestRepo;

    public FriendStatusService() {
        friendRequestRepo = RepoFactory.friendRequestRepo;
    }

    public FriendStatus getFriendStatusBetweenUsers(User currentUser, User compareUser) {
        if (usersAreFriends(currentUser, compareUser))
            return FriendStatus.FRIENDS;
        if (userSendFriendRequestToOtherUser(currentUser, compareUser))
            return FriendStatus.PENDING;
        if (userSendFriendRequestToOtherUser(compareUser, currentUser))
            return FriendStatus.ACCEPT;
        return FriendStatus.NOT_FRIENDS;
    }

    public boolean usersAreFriends(User u1, User u2) {
        User isFriend = u1.getFriends().stream()
                .filter(f -> f.getUsername().equals(u2.getUsername()))
                .findFirst().orElse(null);
        return isFriend != null;
    }
    public boolean userSendFriendRequestToOtherUser(User from, User to) {
        FriendRequest fr = friendRequestRepo.getAll().stream()
                .filter(f -> f.getFrom().getId().equals(from.getId()) &&
                        f.getTo().getId().equals(to.getId()))
                .findFirst().orElse(null);
        return fr != null;
    }


}
