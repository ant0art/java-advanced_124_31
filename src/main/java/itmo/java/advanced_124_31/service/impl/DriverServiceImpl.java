package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.dto.DriverDTOResponse;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.entity.WorkShift;
import itmo.java.advanced_124_31.model.enums.CarClass;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import itmo.java.advanced_124_31.model.enums.WorkShiftGrade;
import itmo.java.advanced_124_31.model.enums.WorkShiftStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
import itmo.java.advanced_124_31.service.DriverService;
import itmo.java.advanced_124_31.service.WorkShiftService;
import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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
		if (workShift.getDriver() != null) {
			throw new CustomException(
					String.format("Work shifts (id = %d) field {driver} is not empty",
							idWorkShift), HttpStatus.BAD_REQUEST);
		}
		Car car = driver.getCars().stream().filter(c -> c.getId().equals(idCar))
				.findFirst().orElseThrow(() -> {
					throw new CustomException(String.format(
							"Car with ID number: %d can`t be used by driver with ID: %d",
							idCar, idDriver), HttpStatus.BAD_REQUEST);
				});
		workShift.setGrade(getWorkShiftGrade(driver, car));
		driver.getWorkShifts().add(workShift);
		updateStatus(driver, DriverStatus.UPDATED);
		workShift.setCar(car);
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
		if (driver == null) {
			throw new CustomException(String.format(
					"Work shifts (id = %d) field " + "{driver} is already cleared",
					idWorkShift), HttpStatus.BAD_REQUEST);
		}
		driver.getWorkShifts().remove(workShift);
		updateStatus(driver, DriverStatus.UPDATED);
		workShift.setDriver(null);
		workShift.setCar(null);
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

	private WorkShiftGrade getWorkShiftGrade(Driver driver, Car car) {

		//driver experience
		int exp = getGradeByDriverExp(driver).getGrade();
		log.info(String.format("Experience of driver marked as %d", exp));
		//car class
		int rang = getGradeByCarClass(car).getGrade();
		log.info(String.format("Car class marked as %d", rang));
		//car age
		int age = getGradeByCarAge(car).getGrade();
		log.info(String.format("Car age marked as %d", age));

		Integer min = Collections.min(List.of(exp, rang, age));
		for (WorkShiftGrade e : WorkShiftGrade.values()) {
			if (min == e.getGrade()) {
				return e;
			}
		}
		return null;
	}

	private WorkShiftGrade getGradeByDriverExp(Driver driver) {
		int exp = LocalDate.now().getYear() -
				driver.getLicense().getReceivedAt().getYear();
		switch (exp) {
			case 0:
			case 1:
			case 2:
				return WorkShiftGrade.ECO;
			case 3:
			case 4:
				return WorkShiftGrade.COMFORT_PLUS;
			case 5:
				return WorkShiftGrade.PREMIUM;
			default:
				return WorkShiftGrade.BUSINESS;
		}
	}

	private WorkShiftGrade getGradeByCarClass(Car car) {
		CarClass carClass = car.getCarClass();
		switch (carClass) {
			case ECONOMIC:
				return WorkShiftGrade.ECO;
			case COMFORT:
				return WorkShiftGrade.COMFORT_PLUS;
			case PREMIUM:
				return WorkShiftGrade.PREMIUM;
			default:
				return WorkShiftGrade.BUSINESS;
		}
	}

	private WorkShiftGrade getGradeByCarAge(Car car) {

		int carAge = LocalDate.now().getYear() - car.getVehicleYear();
		switch (carAge) {
			case 0:
			case 1:
				return WorkShiftGrade.BUSINESS;
			case 2:
				return WorkShiftGrade.PREMIUM;
			case 3:
			case 4:
				return WorkShiftGrade.COMFORT_PLUS;
			case 5:
				return WorkShiftGrade.COMFORT;
			default:
				return WorkShiftGrade.ECO;
		}
	}
}
