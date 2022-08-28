package services;

import exceptions.BadRequestException;
import model.*;
import repository.DirectMessageRepository;
import repository.RepoFactory;
import util.Constants;

public class DirectMessageService {


    private final DirectMessageRepository directMessageRepository;


    public DirectMessageService() {directMessageRepository = RepoFactory.directMessageRepository;}




    public DirectMessage sendDirectMessage(User from, User to, String text) {
        if (from.getUsername().equals(to.getUsername()))
            throw new BadRequestException("Can't send message to yourself");

        boolean adminSent = from.getRole().equals(Role.ADMIN);     //
        DirectMessage created = this.directMessageRepository.add(new DirectMessage(from ,to,text,adminSent));
        directMessageRepository.saveData(Constants.FILE_FRIEND_REQUESTS_HEADER);
        return created;
    }




}
