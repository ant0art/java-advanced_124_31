package itmo.java.advanced_124_31.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import itmo.java.advanced_124_31.model.enums.CarClass;
import itmo.java.advanced_124_31.model.enums.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDTORequest {

	String name;
	Color color;
	CarClass carClass;
	Integer vehicleYear;
	String stateNumber;
	Boolean babyChair;
	Integer seats;
}
