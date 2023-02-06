package itmo.java.advanced_124_31.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import itmo.java.advanced_124_31.model.enums.DriverOrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverDTORequest {

	String name;
	String surname;
	String phoneNumber;
	String birthday;
	DriverOrderStatus driverOrderStatus;
}
