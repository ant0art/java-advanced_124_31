package itmo.java.advanced_124_31.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {
	GREEN("зелёный"),
	BLUE("синий"),
	BLACK("чёрный"),
	WHITE("белый"),
	RED("красный"),
	GRAY("серый"),
	YELLOW("желтый");

	private final String name;

}
