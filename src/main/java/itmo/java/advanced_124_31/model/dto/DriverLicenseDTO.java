package itmo.java.advanced_124_31.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import itmo.java.advanced_124_31.model.enums.DriverLicenseCategory;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverLicenseDTO {

	String id;
	List<DriverLicenseCategory> categories;
	String receivedAt;
}

