package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTO;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.repository.CarRepository;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
import itmo.java.advanced_124_31.service.CarService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

		//driver --> driverDTO

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
		CarDTO carDTO = null;
		Optional<Car> optCar = carRepository.findById(id);
		if (optCar.isEmpty()) {
			log.warn(String.format("There are no elements with id: %d", id));
			return carDTO;
		} else {
			Car car = optCar.get();
			return mapper.convertValue(car, CarDTO.class);
		}
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
			Car car = optCar.get();
			if (carDTO.getColor() != null) {
				car.setColor(carDTO.getColor());
			}
			if (carDTO.getName() != null) {
				log.info(String.valueOf(carDTO.getName() == null));
				car.setName(carDTO.getName());
			}
			if (carDTO.getWheels() != null) {
				car.setWheels(carDTO.getWheels());
			}
			if (carDTO.getVehicleYear() != null) {
				car.setVehicleYear(carDTO.getVehicleYear());
			}
			car.setId(id);
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
			System.out.println("Nothing to delete");
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
	public void addTo(Long idCar, Long idDriver) {
		Optional<Car> optionalCar = carRepository.findById(idCar);
		Optional<Driver> optionalDriver = driverRepository.findById(idDriver);
		if (optionalDriver.isPresent() & optionalCar.isPresent()) {
			Car car = optionalCar.get();
			Driver driver = optionalDriver.get();
			driver.getCars().add(car);
			driver.setUpdatedAt(LocalDateTime.now());
			driverRepository.save(driver);
		} else {
			log.warn(optionalDriver.isEmpty() ? String.format(
					"Driver with id: %d not found", idDriver)
					: String.format("Car with id: %d not found", idCar));
		}
	}
}
