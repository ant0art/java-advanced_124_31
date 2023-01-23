package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTO;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.repository.CarRepository;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
import itmo.java.advanced_124_31.service.AdminService;
import itmo.java.advanced_124_31.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
	private final CarRepository carRepository;
	private final DriverRepository driverRepository;

	private final ObjectMapper mapper;

	/**
	 * Set a new car to database
	 *
	 * @param carDTO new car to add
	 * @return a class object carDTO if everything is well
	 */
	@Override
	public CarDTO create(CarDTO carDTO) {

		//carDTO --> car
		Car car = mapper.convertValue(carDTO, Car.class);
		car.setCreatedAt(LocalDateTime.now());

		Car savedCar = carRepository.save(car);

		//car --> carDTO

		return read(savedCar.getId());
	}

	/**
	 * Read the entity-Car id and returns it DTO
	 *
	 * @param id ID of car in database
	 * @return a class object carDTO
	 */
	@Override
	public CarDTO read(Long id) {
		Car car;
		CarDTO res = new CarDTO();
		Optional<Car> optCar = carRepository.findById(id);
		if (optCar.isPresent()) {
			car = optCar.get();
			res.setName(car.getName());
			res.setColor(car.getColor());
			res.setWheels(car.getWheels());
			res.setVehicleYear(car.getVehicleYear());
			return res;
		}
		log.warn(String.format("There are no elements with id: %d", id));
		return null;
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id     ID of object to be updated
	 * @param carDTO DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@Override
	public CarDTO update(Long id, CarDTO carDTO) {

		Optional<Car> optCar = carRepository.findById(id);
		if (optCar.isEmpty()) {
			log.warn("Nothing to update");
		} else {
			Car tempCar = mapper.convertValue(carDTO, Car.class);
			Car car = optCar.get();
			car.setId(id);
			AdminService adminService = new AdminServiceImpl();
			adminService.copyPropertiesIgnoreNull(tempCar, car);
			car.setUpdatedAt(LocalDateTime.now());
			Car updatedCar = carRepository.save(car);
			log.info(String.format("Car with id: %d is updated", id));
			return read(updatedCar.getId());
		}
		return carDTO;
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@Override
	public void delete(Long id) {
		Optional<Car> optDriver = carRepository.findById(id);
		if (optDriver.isEmpty()) {
			log.info("Nothing to delete");
		} else {
			carRepository.delete(optDriver.get());
			log.info(String.format("Driver with id: %d is deleted", id));
		}
	}

	/**
	 * Get the List of all cars, included in DB
	 *
	 * @return List of CarsDTO
	 */
	@Override
	public List<CarDTO> getCars() {

		List<CarDTO> carsDTO = carRepository.findAll().stream().map(e -> read(e.getId()))
				.collect(Collectors.toList());
		return carsDTO;
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
	public void addTo(Long idDriver, Long idCar) {
		Optional<Car> optionalCar = carRepository.findById(idCar);
		Optional<Driver> optionalDriver = driverRepository.findById(idDriver);
		if (optionalDriver.isPresent() & optionalCar.isPresent()) {
			Car car = optionalCar.get(); //машина из базы
			Driver driver = optionalDriver.get(); //водитель из базы
			List<Car> cars = driver.getCars();
			cars.add(car);
			driver.setCars(cars);
			driver.setUpdatedAt(LocalDateTime.now());
			car.setUpdatedAt(LocalDateTime.now());
			car.setDriver(driver);
			carRepository.save(car);
		} else {
			log.warn(optionalDriver.isEmpty() ? String.format(
					"Driver with id: %d not found", idDriver)
					: String.format("Car with id: %d not found", idCar));
		}
	}

	@Override
	public void removeDriverFromCar(Long idCar) {
		Optional<Car> optionalCar = carRepository.findById(idCar);
		if (optionalCar.isPresent()) {
			Car car = optionalCar.get();
			Driver driver = car.getDriver();
			List<Car> cars = driver.getCars();
			cars.remove(car);
			driver.setCars(cars);
			driverRepository.save(driver);
			car.setDriver(null);
			carRepository.save(car);
		}
	}
}
