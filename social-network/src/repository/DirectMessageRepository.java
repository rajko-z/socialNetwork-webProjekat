package repository;

import model.DirectMessage;

public class DirectMessageRepository extends GenericRepository<DirectMessage> {
	
	public DirectMessageRepository(String filePath) {
		super(filePath);
	}

	@Override
	protected DirectMessage createEntityFromTokens(String[] tokens) {
		return null;
	}
	
}
