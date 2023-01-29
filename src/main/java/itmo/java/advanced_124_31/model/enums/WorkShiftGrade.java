package itmo.java.advanced_124_31.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkShiftGrade {

	ECO(0),
	COMFORT(1),
	COMFORT_PLUS(2),
	PREMIUM(3),
	BUSINESS(4);

	private final int grade;
}
