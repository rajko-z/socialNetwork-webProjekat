package repository;

import model.Image;

public class ImageRepository extends GenericRepository<Image> {

	public ImageRepository(String filePath) {
		super(filePath);
	}

	@Override
	protected Image createEntityFromTokens(String[] tokens) {
		Long id = Long.parseLong(tokens[0]);
		String name = tokens[1];
		return new Image(id, name);
	}
	
	
	
	
}
