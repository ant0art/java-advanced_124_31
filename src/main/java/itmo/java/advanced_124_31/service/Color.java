package itmo.java.advanced_124_31.service;

public enum Color {
	GREEN("GREEN"),
	BLUE("BLUE"),
	BLACK("BLACK"),
	RED("RED");

	private String name;

	public String getName() {
		return name;
	}

	/**
	 * @param name String name of Color
	 */
	Color(String name) {
		this.name = name;
	}
}
