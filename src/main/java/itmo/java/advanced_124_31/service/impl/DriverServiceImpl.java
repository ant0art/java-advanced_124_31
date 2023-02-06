package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import itmo.java.advanced_124_31.utils.PaginationUtil;
import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
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
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
	private final DriverRepository driverRepository;

	private final WorkShiftService workShiftService;

	private final ObjectMapper mapper = JsonMapper.builder()
			.addModule(new JavaTimeModule()).build();

	/**
	 * Returns a {@link DriverDTORequest} object after adding new object Driver
	 *
	 * @param driverDTORequest new driver to add
	 * @return a class object {@link DriverDTORequest} if everything is well
	 * @see Driver
	 */
	@Override
	public DriverDTORequest create(DriverDTORequest driverDTORequest) {
		checkPhoneNumber(driverDTORequest.getPhoneNumber());
		try {
			LocalDate.parse(driverDTORequest.getBirthday());
		} catch (DateTimeParseException e) {
			throw new CustomException("Failed to deserialize: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		//driverDTO --> driver
		Driver driver = mapper.convertValue(driverDTORequest, Driver.class);
		driver.setStatus(DriverStatus.CREATED);

		//driver --> driverDTO
		return mapper.convertValue(driverRepository.save(driver), DriverDTORequest.class);
	}

	/**
	 * Returns a {@link DriverDTORequest} object by the given id if it is found
	 *
	 * @param id driver ID
	 * @return the entity DTO with the given id
	 */
	@Override
	public DriverDTORequest get(Long id) {
		return mapper.convertValue(getDriver(id), DriverDTORequest.class);
	}

	/**
	 * Returns a {@link DriverDTORequest} object after updating fields of its entity
	 *
	 * @param id               ID of object to be updated
	 * @param driverDTORequest DriverDTORequest with fields to update
	 * @return a class object {@link DriverDTORequest} if everything is well
	 * @see Driver
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
	 * Returns a list of all objects drivers in a limited size list sorted by
	 * chosen parameter
	 *
	 * @param page    serial number of page to show
	 * @param perPage elements on page
	 * @param sort    main parameter of sorting
	 * @param order   ASC or DESC
	 * @return List<DriverDTORequest> of sorted elements
	 * @see PaginationUtil
	 * @see Pageable
	 * @see Page
	 */
	@Override
	public List<DriverDTORequest> getDrivers(Integer page, Integer perPage, String sort,
			Sort.Direction order) {
		Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
		Page<Driver> pageResult = driverRepository.findAll(pageRequest);

		List<DriverDTORequest> content = pageResult.getContent().stream()
				.map(d -> mapper.convertValue(d, DriverDTORequest.class))
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
	 * @param id driver ID
	 * @return the entity with the given id
	 * @see Driver
	 */
	@Override
	public Driver getDriver(Long id) {
		return driverRepository.findById(id).orElseThrow(() -> new CustomException(
				String.format("Driver with ID: %d not found", id), HttpStatus.NOT_FOUND));
	}

	/**
	 * Change the state of {@link Driver}-entity by chosen and set the entity field
	 * updatedAt new local date time
	 *
	 * @param driver driver-entity
	 * @param status new state of entity
	 * @see LocalDateTime
	 */
	@Override
	public void updateStatus(Driver driver, DriverStatus status) {
		driver.setStatus(status);
		driver.setUpdatedAt(LocalDateTime.now());
	}

	/**
	 * Returns a {@link DriverDTOResponse} object after adding Driver-entity, Car-entity to
	 * WorkShift-entity
	 *
	 * @param idWorkShift ID of
	 *                    {@link itmo.java.advanced_124_31.model.entity.WorkShift} to add
	 * @param idCar       ID of {@link itmo.java.advanced_124_31.model.entity.Car}
	 *                    to be added
	 * @param idDriver    ID of {@link itmo.java.advanced_124_31.model.entity.Driver} to be added
	 * @return a class object {@link DriverDTOResponse if everything is well
	 * @see WorkShift
	 * @see Car
	 * @see Driver
	 */
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
		DriverDTOResponse response = mapper.convertValue(driverRepository.save(driver),
				DriverDTOResponse.class);
		response.setWorkShift(workShift);
		return response;
	}

	/**
	 * Returns a {@link DriverDTORequest} object after cleaning the connection between
	 * WorkShift and it`s Driver
	 *
	 * @param idWorkShift ID of WorkShift to remove its Driver
	 * @return {@link DriverDTORequest} object if removing ends well
	 */
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
		return mapper.convertValue(driverRepository.save(driver), DriverDTORequest.class);
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

	/**
	 * Returns the grade by checking input val properties
	 *
	 * @param driver driver-entity
	 * @param car    car-entity
	 * @return the ex of enum {@link WorkShiftGrade}
	 */
	private WorkShiftGrade getWorkShiftGrade(Driver driver, Car car) {

		WorkShiftGrade workShiftGrade = null;
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
				workShiftGrade = e;
				break;
			}
		}
		return workShiftGrade;
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
