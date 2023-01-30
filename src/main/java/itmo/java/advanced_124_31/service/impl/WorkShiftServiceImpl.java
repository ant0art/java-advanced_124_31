package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.WorkShiftDTO;
import itmo.java.advanced_124_31.model.entity.WorkShift;
import itmo.java.advanced_124_31.model.enums.WorkShiftStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.WorkShiftRepository;
import itmo.java.advanced_124_31.service.WorkShiftService;
import itmo.java.advanced_124_31.utils.PaginationUtil;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkShiftServiceImpl implements WorkShiftService {

	private final WorkShiftRepository workShiftRepository;

	private final ObjectMapper mapper;

	/**
	 * Returns a {@link WorkShiftDTO} object after adding new object WorkShift
	 *
	 * @param workShiftDTO new work shift to add
	 * @return a class object {@link WorkShiftDTO} if everything is well
	 * @see WorkShift
	 */
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

	/**
	 * Returns a {@link WorkShiftDTO} object by the given id if it is found
	 *
	 * @param id work shift ID
	 * @return the entity DTO with the given id
	 */
	@Override
	public WorkShiftDTO get(Long id) {
		return mapper.convertValue(getWorkShift(id), WorkShiftDTO.class);
	}

	/**
	 * Returns a {@link WorkShiftDTO} object after updating fields of its entity
	 *
	 * @param id           ID of object to be updated
	 * @param workShiftDTO WorkShiftDTO with fields to update
	 * @return a class object {@link WorkShiftDTO} if everything is well
	 * @see WorkShift
	 */
	@Override
	public WorkShiftDTO update(Long id, WorkShiftDTO workShiftDTO) {

		AtomicReference<WorkShiftDTO> dto = new AtomicReference<>(new WorkShiftDTO());
		workShiftRepository.findById(id).ifPresentOrElse(d -> {
			copyPropertiesIgnoreNull(mapper.convertValue(workShiftDTO, WorkShift.class),
					d);
			updateStatus(d, WorkShiftStatus.UPDATED);
			dto.set(mapper.convertValue(workShiftRepository.save(d), WorkShiftDTO.class));
		}, () -> {
			throw new CustomException(
					String.format("Work shift with id: %d not found. Nothing to update",
							id), HttpStatus.NOT_FOUND);
		});
		return dto.get();
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@Override
	public void delete(Long id) {
		WorkShift workShift = getWorkShift(id);
		updateStatus(workShift, WorkShiftStatus.DELETED);
		workShiftRepository.save(workShift);
	}

	/**
	 * Returns a list of all objects work shifts in a limited size list sorted by
	 * chosen parameter
	 *
	 * @param page    serial number of page to show
	 * @param perPage elements on page
	 * @param sort    main parameter of sorting
	 * @param order   ASC or DESC
	 * @return List<WorkShiftDTO> of sorted elements
	 * @see PaginationUtil
	 * @see Pageable
	 * @see Page
	 */
	@Override
	public List<WorkShiftDTO> getWorkShifts(Integer page, Integer perPage, String sort,
			Sort.Direction order) {
		Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
		//view 1
		Page<WorkShift> pageResult = workShiftRepository.findAll(pageRequest);

		List<WorkShiftDTO> content = pageResult.getContent().stream()
				.map(d -> mapper.convertValue(d, WorkShiftDTO.class))
				.collect(Collectors.toList());
		return content;
	}

	/**
	 * Copy properties from one object to another field to field (excluding class)
	 * ignoring null
	 *
	 * @param source source object, must not be Null
	 * @param target target object, must not be Null
	 */
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

	/**
	 * Returns the entity with the given id if it is found
	 *
	 * @param id work shift ID
	 * @return the entity with the given id
	 * @see WorkShift
	 */
	@Override
	public WorkShift getWorkShift(Long id) {
		return workShiftRepository.findById(id).orElseThrow(() -> new CustomException(
				String.format("WorkShift with ID: %s not found", id),
				HttpStatus.NOT_FOUND));
	}

	/**
	 * Change the state of {@link WorkShift}-entity by chosen and set the entity field
	 * updatedAt new local date time
	 *
	 * @param workShift driver-entity
	 * @param status    new state of entity
	 * @see LocalDateTime
	 */
	@Override
	public void updateStatus(WorkShift workShift, WorkShiftStatus status) {
		workShift.setStatus(status);
		workShift.setUpdatedAt(LocalDateTime.now());
	}
}
