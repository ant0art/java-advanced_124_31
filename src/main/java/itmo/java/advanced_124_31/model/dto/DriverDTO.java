package itmo.java.advanced_124_31.model.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverDTO {
	String name;
	String surname;
	String birthday;
	List<CarDTO> cars;
}
