package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTO;
import itmo.java.advanced_124_31.model.dto.DriverDTO;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.repository.CarRepository;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
import itmo.java.advanced_124_31.service.AdminService;
import itmo.java.advanced_124_31.service.DriverService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
	private final CarRepository carRepository;
	private final DriverRepository driverRepository;

	private final ObjectMapper mapper;

	/**
	 * Set a new driver to database
	 *
	 * @param driverDTO new driver to add
	 * @return a class object driverDTO if everything is well
	 */
	@Override
	public DriverDTO create(DriverDTO driverDTO) {

		//driverDTO --> driver
		Driver driver = mapper.convertValue(driverDTO, Driver.class);
		driver.setBirthday(LocalDate.parse(driverDTO.getBirthday()));
		driver.setCreatedAt(LocalDateTime.now());

		List<Car> cars = new ArrayList<>();
		driver.setCars(cars);
		Driver savedDriver = driverRepository.save(driver);

		//driver --> driverDTO

		return read(savedDriver.getId());
	}

	/**
	 * Read the entity-Driver id and returns it DTO
	 *
	 * @param id ID of driver in database
	 * @return a class object driverDTO
	 */
	@Override
	public DriverDTO read(Long id) {
		DriverDTO res = new DriverDTO();
		Optional<Driver> optionalDriver = driverRepository.findById(id);
		if(optionalDriver.isPresent()){
			Driver driver = optionalDriver.get();
			res.setName(driver.getName());
			res.setSurname(driver.getName());
			res.setBirthday(String.valueOf(driver.getBirthday()));

			List<CarDTO> carsDTO = Collections.emptyList();
			List<Car> cars = driver.getCars();
			if(!cars.isEmpty()) {
				carsDTO = cars.stream().map(car -> {
					CarDTO carDTO = new CarDTO();
					carDTO.setColor(car.getColor());
					carDTO.setName(car.getName());
					carDTO.setWheels(car.getWheels());
					carDTO.setVehicleYear(car.getVehicleYear());
					log.info(carDTO.toString());
					return carDTO;
				}).collect(Collectors.toList());
			}
			res.setCars(carsDTO);
			return res;
		}
		log.warn(String.format("There are no elements with id: %d", id));
		return null;
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id        ID of object to be updated
	 * @param driverDTO DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@Override
	public DriverDTO update(Long id, DriverDTO driverDTO) {

		Driver updatedDriver = null;
		Optional<Driver> optDriver = driverRepository.findById(id);
		if (optDriver.isEmpty()) {
			log.warn("Nothing to update");
		} else {
			Driver tempDriver = mapper.convertValue(driverDTO, Driver.class);
			Driver driver = optDriver.get();
			driver.setId(id);
			driver.setUpdatedAt(LocalDateTime.now());
			AdminServiceImpl adminService = new AdminServiceImpl();
			adminService.copyPropertiesIgnoreNull(tempDriver,driver);
			updatedDriver = driverRepository.save(driver);
			log.info(String.format("Driver with id: %d is updated", id));
			return read(updatedDriver.getId());
		}
		return driverDTO;
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@Override
	public void delete(Long id) {
		Optional<Driver> optDriver = driverRepository.findById(id);
		if (optDriver.isEmpty()) {
			log.warn("Nothing to delete");
		} else {
			driverRepository.delete(optDriver.get());
			log.info(String.format("Driver with id: %d is deleted", id));
		}
	}

	/**
	 * Get the List of all drivers, included in DB
	 *
	 * @return List of DriversDTO
	 */
	@Override
	public List<DriverDTO> getDrivers() {
		List<DriverDTO> driversDTO = driverRepository.findAll().stream()
				.map(e -> read(e.getId())).collect(Collectors.toList());
		return driversDTO;
	}
}
