package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.dto.WorkShiftDTO;
import itmo.java.advanced_124_31.model.entity.WorkShift;
import itmo.java.advanced_124_31.model.enums.WorkShiftStatus;
import java.util.List;

public interface WorkShiftService {

	WorkShiftDTO create(WorkShiftDTO workShiftDTO);

	WorkShiftDTO get(Long id);

	WorkShiftDTO update(Long id, WorkShiftDTO workShiftDTO);

	void delete(Long id);

	List<WorkShiftDTO> getWorkShifts();

	WorkShift getWorkShift(Long id);

	void updateStatus(WorkShift workShift, WorkShiftStatus status);
}
