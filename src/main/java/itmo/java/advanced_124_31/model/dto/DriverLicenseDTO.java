package itmo.java.advanced_124_31.model.dto;

import itmo.java.advanced_124_31.model.enums.DriverLicenseCategory;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverLicenseDTO {
	
	String id;
	List<DriverLicenseCategory> categories;
	String receivedAt;
}

