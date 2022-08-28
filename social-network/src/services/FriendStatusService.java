package services;

import exceptions.BadRequestException;
import exceptions.InternalAppException;
import model.FriendRequest;
import model.FriendStatus;
import model.User;
import repository.FriendRequestRepository;
import repository.RepoFactory;
import util.Constants;

import java.util.List;
import java.util.stream.Collectors;


public class FriendStatusService {

    private final FriendRequestRepository friendRequestRepo;

    public FriendStatusService() {
        friendRequestRepo = RepoFactory.friendRequestRepo;
    }

    public FriendStatus getFriendStatusBetweenUsers(User currentUser, User compareUser) {
        if (currentUser.getUsername().equals(compareUser.getUsername()))
            return FriendStatus.FRIENDS;
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


    public FriendRequest sendFriendRequest(User from, User to) {
        if (from.getUsername().equals(to.getUsername()))
            throw new BadRequestException("Can't send request to yourself");

        FriendStatus friendStatus = getFriendStatusBetweenUsers(from, to);
        if (friendStatus == FriendStatus.FRIENDS)
            throw new BadRequestException("You and " + to.getUsername() + " are already friends");
        if (friendStatus == FriendStatus.PENDING)
            throw new BadRequestException("You already sent friend request to " + to.getUsername());
        if (friendStatus == FriendStatus.ACCEPT)
            throw new BadRequestException(to.getUsername() + " already sent you friend request, accept it if you want to be friends");

        FriendRequest created = this.friendRequestRepo.add(new FriendRequest(from ,to));
        friendRequestRepo.saveData(Constants.FILE_FRIEND_REQUESTS_HEADER);
        return created;
    }


    public void removeFriend(User currentUser, User toRemove) {
        if (currentUser.getUsername().equals(toRemove.getUsername()))
            throw new BadRequestException("Can't remove yourself from friends");

        FriendStatus friendStatus = getFriendStatusBetweenUsers(currentUser, toRemove);
        if (friendStatus != FriendStatus.FRIENDS)
            throw new BadRequestException("You have to be friend with " + toRemove.getUsername() + " for this action");

        currentUser.getFriends().remove(toRemove);
        toRemove.getFriends().remove(currentUser);
        RepoFactory.userRepo.saveData(Constants.FILE_USERS_HEADER);
    }

    public void acceptFriendRequest(User from, User to) {
        if (from.getUsername().equals(to.getUsername()))
            throw new BadRequestException("Can't accept request from yourself");

        FriendStatus friendStatus = getFriendStatusBetweenUsers(from, to);
        if (friendStatus != FriendStatus.PENDING)
            throw new BadRequestException(from.getUsername() + " did not sent you friend request");

        to.getFriends().add(from);
        from.getFriends().add(to);
        RepoFactory.userRepo.saveData(Constants.FILE_USERS_HEADER);
        boolean isSuccess = this.friendRequestRepo.deleteRequest(from, to);
        if (!isSuccess)
            throw new InternalAppException("Error happened after accepting friend request");
    }

    public List<FriendRequest> getFriendRequestsForUser(User currentUser) {
        return friendRequestRepo.getAll().stream().filter(
                r -> r.getTo().getUsername().equals(currentUser.getUsername())
        ).collect(Collectors.toList());
    }

    public List<FriendRequest> getFriendRequestsThatUserSent(User currentUser) {
        return friendRequestRepo.getAll().stream().filter(
                r -> r.getFrom().getUsername().equals(currentUser.getUsername())
        ).collect(Collectors.toList());
    }
}
