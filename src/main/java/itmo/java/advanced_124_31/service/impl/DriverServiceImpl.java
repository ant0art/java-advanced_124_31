package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.dto.DriverDTOResponse;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.entity.WorkShift;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import itmo.java.advanced_124_31.model.enums.WorkShiftStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
import itmo.java.advanced_124_31.service.DriverService;
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
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
	private final DriverRepository driverRepository;

	private final WorkShiftService workShiftService;

	private final ObjectMapper mapper;

	/**
	 * Set a new driver to database
	 *
	 * @param driverDTORequest new driver to add
	 * @return a class object driverDTO if everything is well
	 */
	@Override
	public DriverDTORequest create(DriverDTORequest driverDTORequest) {
		checkPhoneNumber(driverDTORequest.getPhoneNumber());

		//driverDTO --> driver
		Driver driver = mapper.convertValue(driverDTORequest, Driver.class);
		driver.setStatus(DriverStatus.CREATED);

		//driver --> driverDTO
		DriverDTORequest readedDTO = get(driverRepository.save(driver).getId());
		return readedDTO;
	}

	/**
	 * Read the entity-Driver id and returns it DTO
	 *
	 * @param id ID of driver in database
	 * @return a class object driverDTO
	 */
	@Override
	public DriverDTORequest get(Long id) {
		return mapper.convertValue(getDriver(id), DriverDTORequest.class);
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id               ID of object to be updated
	 * @param driverDTORequest DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@Override
	public DriverDTORequest update(Long id, DriverDTORequest driverDTORequest) {

		AtomicReference<DriverDTORequest> dto = new AtomicReference<>(
				new DriverDTORequest());
		driverRepository.findById(id).ifPresentOrElse(d -> {
			if (driverDTORequest.getPhoneNumber() != null &&
					!driverDTORequest.getPhoneNumber().isEmpty()) {
				checkPhoneNumber(driverDTORequest.getPhoneNumber());
			}
			copyPropertiesIgnoreNull(mapper.convertValue(driverDTORequest, Driver.class),
					d);
			updateStatus(d, DriverStatus.UPDATED);
			dto.set(mapper.convertValue(driverRepository.save(d),
					DriverDTORequest.class));
		}, () -> {
			throw new CustomException(
					String.format("Driver with id: %d not found. Nothing to update", id),
					HttpStatus.NOT_FOUND);
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
		Driver driver = getDriver(id);
		updateStatus(driver, DriverStatus.DELETED);
		driverRepository.save(driver);
	}

	/**
	 * Get the List of all drivers, included in DB
	 *
	 * @return List of DriversDTO
	 */
	@Override
	public List<DriverDTORequest> getDrivers() {
		return driverRepository.findAll().stream().map(e -> get(e.getId()))
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
	public Driver getDriver(Long id) {
		return driverRepository.findById(id).orElseThrow(() -> new CustomException(
				String.format("Driver with ID: %d not found", id), HttpStatus.NOT_FOUND));
	}

	@Override
	public void updateStatus(Driver driver, DriverStatus status) {
		driver.setStatus(status);
		driver.setUpdatedAt(LocalDateTime.now());
	}

	@Override
	public DriverDTOResponse addToWorkShift(Long idWorkShift, Long idDriver, Long idCar) {
		WorkShift workShift = workShiftService.getWorkShift(idWorkShift);
		Driver driver = getDriver(idDriver);
		Car car = driver.getCars().stream().filter(c -> c.getId().equals(idCar))
				.findFirst().orElseThrow(() -> {
					throw new CustomException(String.format(
							"Car with ID number: %d can`t be used by driver with ID: %d",
							idCar, idDriver), HttpStatus.BAD_REQUEST);
				});
		driver.getWorkShifts().add(workShift);
		updateStatus(driver, DriverStatus.UPDATED);
		workShift.setDriver(driver);
		workShiftService.updateStatus(workShift, WorkShiftStatus.UPDATED);
		DriverDTOResponse driverDTOResponse = mapper.convertValue(
				driverRepository.save(driver), DriverDTOResponse.class);

		return driverDTOResponse;
	}

	@Override
	public DriverDTORequest removeDriverFromWorkShift(Long idWorkShift) {
		WorkShift workShift = workShiftService.getWorkShift(idWorkShift);
		Driver driver = workShift.getDriver();
		driver.getWorkShifts().remove(workShift);
		updateStatus(driver, DriverStatus.UPDATED);
		workShift.setDriver(null);
		workShiftService.updateStatus(workShift, WorkShiftStatus.UPDATED);
		DriverDTORequest driverDTORequest = mapper.convertValue(
				driverRepository.save(driver), DriverDTORequest.class);
		return driverDTORequest;
	}

	/**
	 * Checks whether input phone number is correct or not
	 *
	 * @param phoneNumber input phone number
	 */
	private void checkPhoneNumber(String phoneNumber) throws CustomException {
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			throw new CustomException("Phone number is missing", HttpStatus.BAD_REQUEST);
		}

		driverRepository.findByPhoneNumber(phoneNumber).ifPresent(p -> {
			throw new CustomException(
					String.format("driver with number: %s already exists",
							p.getPhoneNumber()), HttpStatus.BAD_REQUEST);
		});
	}
}
