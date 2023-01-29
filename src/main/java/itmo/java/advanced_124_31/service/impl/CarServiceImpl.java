package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.dto.CarDTOResponse;
import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.enums.CarStatus;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.CarRepository;
import itmo.java.advanced_124_31.service.CarService;
import itmo.java.advanced_124_31.service.DriverService;
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
public class CarServiceImpl implements CarService {

	private final CarRepository carRepository;
	private final DriverService driverService;

	private final ObjectMapper mapper;

	/**
	 * Set a new car to database
	 *
	 * @param carDTORequest new car to add
	 * @return a class object carDTO if everything is well
	 */
	@Override
	public CarDTORequest create(CarDTORequest carDTORequest) {
		carRepository.findByStateNumberIgnoreCase(carDTORequest.getStateNumber())
				.ifPresent(c -> {
					throw new CustomException(
							String.format("Car with number: %s already exists",
									c.getStateNumber()), HttpStatus.BAD_REQUEST);
				});

		//carDTO --> car
		Car car = mapper.convertValue(carDTORequest, Car.class);
		car.setStatus(CarStatus.CREATED);
		//car --> carDTO
		CarDTORequest readedDTO = get(carRepository.save(car).getId());
		return readedDTO;
	}

	/**
	 * Read the entity-Car id and returns it DTO
	 *
	 * @param id ID of car in database
	 * @return a class object carDTO
	 */
	@Override
	public CarDTORequest get(Long id) {
		return mapper.convertValue(getCar(id), CarDTORequest.class);
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id            ID of object to be updated
	 * @param carDTORequest DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@Override
	public CarDTORequest update(Long id, CarDTORequest carDTORequest) {

		AtomicReference<CarDTORequest> dto = new AtomicReference<>(new CarDTORequest());
		carRepository.findById(id).ifPresentOrElse(c -> {
			copyPropertiesIgnoreNull(mapper.convertValue(carDTORequest, Car.class), c);
			updateStatus(c, CarStatus.UPDATED);
			dto.set(mapper.convertValue(carRepository.save(c), CarDTORequest.class));
		}, () -> {
			log.warn("Nothing to update");
			dto.set(null);
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
		Car car = getCar(id);
		updateStatus(car, CarStatus.DELETED);
		carRepository.save(car);
	}

	/**
	 * Get the List of all cars, included in DB
	 *
	 * @return List of CarsDTO
	 */
	@Override
	public List<CarDTORequest> getCars() {
		return carRepository.findAll().stream().map(e -> get(e.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * Add existed car to it`s driver
	 *
	 * @param idCar    ID of {@link itmo.java.advanced_124_31.model.entity.Car}
	 * @param idDriver ID od {@link itmo.java.advanced_124_31.model.entity.Driver}
	 * @see Car
	 * @see Driver
	 */
	@Override
	public CarDTOResponse addTo(Long idDriver, Long idCar) {

		Driver driver = driverService.getDriver(idDriver);
		Car car = getCar(idCar);
		driver.getCars().add(car);
		car.setDriver(driver);
		updateStatus(car, CarStatus.UPDATED);
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		CarDTOResponse response = mapper.convertValue(carRepository.save(car),
				CarDTOResponse.class);
		response.setDriverDTORequest(mapper.convertValue(driver, DriverDTORequest.class));
		return response;
	}

	@Override
	public CarDTORequest removeDriverFromCar(Long id) {

		Car car = getCar(id);
		Driver driver = car.getDriver();
		driver.getCars().remove(car);
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		//driverRepository.save(driver);
		car.setDriver(null);
		updateStatus(car, CarStatus.UPDATED);
		CarDTORequest request = mapper.convertValue(carRepository.save(car),
				CarDTORequest.class);
		return request;
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

	private Car getCar(Long id) {
		return carRepository.findById(id).orElseThrow(
				() -> new CustomException(String.format("Car with ID: %d not found", id),
						HttpStatus.NOT_FOUND));
	}

	private void updateStatus(Car car, CarStatus status) {
		car.setStatus(status);
		car.setUpdatedAt(LocalDateTime.now());
	}

}
