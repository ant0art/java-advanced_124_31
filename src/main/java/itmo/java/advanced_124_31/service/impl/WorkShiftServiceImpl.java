package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.WorkShiftDTO;
import itmo.java.advanced_124_31.model.entity.WorkShift;
import itmo.java.advanced_124_31.model.enums.WorkShiftStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.WorkShiftRepository;
import itmo.java.advanced_124_31.service.WorkShiftService;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkShiftServiceImpl implements WorkShiftService {

	private final WorkShiftRepository workShiftRepository;

	private final ObjectMapper mapper;

	@Override
	public WorkShiftDTO create(WorkShiftDTO workShiftDTO) {

		//workShiftDTO --> workShift
		WorkShift workShift = mapper.convertValue(workShiftDTO, WorkShift.class);
		workShift.setStatus(WorkShiftStatus.CREATED);

		//workShift --> workShiftDTO

		WorkShiftDTO resultDTO = mapper.convertValue(workShiftRepository.save(workShift),
				WorkShiftDTO.class);
		return resultDTO;
	}

	@Override
	public WorkShiftDTO get(Long id) {
		return mapper.convertValue(getWorkShift(id), WorkShiftDTO.class);
	}

	@Override
	public WorkShiftDTO update(Long id, WorkShiftDTO workShiftDTO) {

		AtomicReference<WorkShiftDTO> dto = new AtomicReference<>(new WorkShiftDTO());
		workShiftRepository.findById(id).ifPresentOrElse(d -> {
			copyPropertiesIgnoreNull(mapper.convertValue(workShiftDTO, WorkShift.class),
					d);
			updateStatus(d, WorkShiftStatus.UPDATED);
			dto.set(mapper.convertValue(workShiftRepository.save(d), WorkShiftDTO.class));
		}, () -> {
			log.warn("Nothing to update");
			dto.set(null);
		});
		return dto.get();
	}

	@Override
	public void delete(Long id) {
		WorkShift workShift = getWorkShift(id);
		updateStatus(workShift, WorkShiftStatus.DELETED);
		workShiftRepository.save(workShift);
	}

	@Override
	public List<WorkShiftDTO> getWorkShifts() {
		return workShiftRepository.findAll().stream().map(e -> get(e.getId()))
				.collect(Collectors.toList());
	}

	private void copyPropertiesIgnoreNull(Object source, Object target) {
		BeanWrapper src = new BeanWrapperImpl(source);
		BeanWrapper trg = new BeanWrapperImpl(target);

		for (PropertyDescriptor descriptor : src.getPropertyDescriptors()) {
			String propertyName = descriptor.getName();
			if (propertyName.equals("class")) {
				continue;
			}
			Object propertyValue = src.getPropertyValue(propertyName);
			if (propertyValue != null) {
				trg.setPropertyValue(propertyName, propertyValue);
			}
		}
	}

	@Override
	public WorkShift getWorkShift(Long id) {
		return workShiftRepository.findById(id).orElseThrow(() -> new CustomException(
				String.format("WorkShift with ID: %s not found", id),
				HttpStatus.NOT_FOUND));
	}

	@Override
	public void updateStatus(WorkShift workShift, WorkShiftStatus status) {
		workShift.setStatus(status);
		workShift.setUpdatedAt(LocalDateTime.now());
	}
}
