package itmo.java.advanced_124_31.model.dto;

import itmo.java.advanced_124_31.model.entity.WorkShift;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverDTOResponse extends DriverDTORequest {
	
	WorkShift workShift;
}
