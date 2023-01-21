package itmo.java.advanced_124_31.model.dto;

import itmo.java.advanced_124_31.service.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDTO {

	String name;
	Integer wheels;
	Color color;
	Integer vehicleYear;
}
