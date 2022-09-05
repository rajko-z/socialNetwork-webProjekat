package services;

import exceptions.BadRequestException;
import model.DirectMessage;
import model.Role;
import model.User;
import repository.DirectMessageRepository;
import repository.RepoFactory;
import util.Constants;

import java.util.List;

public class DirectMessageService {


    private final DirectMessageRepository directMessageRepository;


    public DirectMessageService() {directMessageRepository = RepoFactory.directMessageRepository;}




    public DirectMessage sendDirectMessage(User from, User to, String text) {
        if (from.getUsername().equals(to.getUsername()))
            throw new BadRequestException("Can't send message to yourself");

        boolean adminSent = from.getRole().equals(Role.ADMIN);     //
        DirectMessage created = this.directMessageRepository.add(new DirectMessage(from ,to,text,adminSent));
        directMessageRepository.saveData(Constants.FILE_DIRECT_MESSAGE_HEADER);
        return created;
    }


    public List<DirectMessage> getChat (User from, User to){
        return  directMessageRepository.getChat(from.getId(),to.getId());
    }




}
