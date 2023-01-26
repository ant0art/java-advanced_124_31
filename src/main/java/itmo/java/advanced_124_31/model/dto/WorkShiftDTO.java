package itmo.java.advanced_124_31.model.dto;

import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.enums.WorkShiftGrade;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkShiftDTO {

	Driver driver;
	Car car;
	WorkShiftGrade grade;
	LocalDateTime closedAt;
}

