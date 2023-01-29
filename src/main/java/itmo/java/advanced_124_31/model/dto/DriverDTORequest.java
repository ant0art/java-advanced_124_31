package itmo.java.advanced_124_31.model.dto;

import itmo.java.advanced_124_31.model.enums.DriverOrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverDTORequest {
	
	String name;
	String surname;
	String phoneNumber;
	String birthday;
	DriverOrderStatus driverOrderStatus;
}
