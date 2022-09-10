package repository;

import model.DirectMessage;
import model.User;
import util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DirectMessageRepository extends GenericRepository<DirectMessage> {
	
 
	private final DateUtil dateUtil;
	private UserRepository userRepository;
	
	public DirectMessageRepository(String filePath,  UserRepository userRepository) {
		super(filePath);
		this.userRepository = userRepository;
		this.dateUtil = new DateUtil();
		
	}
	
	
	//# id | from user id | to user id | text | createdAt | adminSent
	@Override 
	protected DirectMessage createEntityFromTokens(String[] tokens) {    
		Long id = Long.parseLong(tokens[0]);
		User from = this.userRepository.getById(Long.parseLong(tokens[1]));
		User to = this.userRepository.getById(Long.parseLong(tokens[2]));
		String text = tokens[3];
		LocalDateTime createdAt = dateUtil.parseStringToLocalDateTime(tokens[4]);
		boolean adminSent = Boolean.parseBoolean(tokens[5]);
		
		return new DirectMessage(id,from,to,createdAt,text,adminSent);
		 
	}



	public List<DirectMessage> getChat(long idUserSend, long idUserTo)
	{
		List<DirectMessage> directMessageList;
		directMessageList =  this.data.values().stream()
				.filter(u ->
						(u.getFrom().getId().equals(idUserSend) && u.getTo().getId().equals(idUserTo))||(u.getFrom().getId().equals(idUserTo) && u.getTo().getId().equals(idUserSend))  )
				.collect(Collectors.toList());

		return directMessageList;

	}


	public List<DirectMessage> getChatFromAdmin( long idUserTo)
	{
		List<DirectMessage> directMessageList;
		directMessageList =  this.data.values().stream()
				.filter(u ->
						 u.getTo().getId().equals(idUserTo) && u.isAdminSent()==true )
				.collect(Collectors.toList());

		return directMessageList;

	}
	
}
