package model;


public class Image extends Entity {
	private String name;
	
	public Image() {
		super();
	}
	
	public Image(Long id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String formatEntityForFile() {
		return String.format("%d|%s", this.getId(), this.getName());
	}

	@Override
	public String toString() {
		return "Image{" +
				"name='" + name + '\'' +
				'}';
	}
}
