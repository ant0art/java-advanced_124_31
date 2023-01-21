package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTO;
import itmo.java.advanced_124_31.model.dto.DriverDTO;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
import itmo.java.advanced_124_31.service.DriverService;
import java.time.LocalDate;
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
public class DriverServiceImpl implements DriverService {
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

		List<Car> cars = driverDTO.getCars().stream().map(c -> {
			Car car = new Car();
			car.setName(c.getName());
			car.setWheels(c.getWheels());
			car.setColor(c.getColor());
			car.setVehicleYear(c.getVehicleYear());
			car.setCreatedAt(LocalDateTime.now());
			return car;
		}).collect(Collectors.toList());

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
		Driver driver = driverRepository.findById(id).get();
		DriverDTO res = mapper.convertValue(driver, DriverDTO.class);
		res.setBirthday(String.valueOf(driver.getBirthday()));

		List<CarDTO> carsDTO = driver.getCars().stream().map(car -> {
			CarDTO carDTO = new CarDTO();
			carDTO.setColor(car.getColor());
			carDTO.setName(car.getName());
			carDTO.setWheels(car.getWheels());
			carDTO.setVehicleYear(car.getVehicleYear());
			return carDTO;
		}).collect(Collectors.toList());

		res.setCars(carsDTO);
		return res;
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
			System.out.println("Nothing to update");
			log.info("Nothing to update");
		} else {
			Driver driver = optDriver.get();
			if(driverDTO.getSurname() != null) {
				driver.setSurname(driverDTO.getSurname());
			}
			if (driverDTO.getName() != null) {
				driver.setName(driverDTO.getName());
			}
			if(driverDTO.getBirthday() != null) {
				driver.setBirthday(LocalDate.parse(driverDTO.getBirthday()));
			}
			driver.setId(id);
			driver.setUpdatedAt(LocalDateTime.now());
			log.info(String.format("Driver info: %s",driver.toString()));

			updatedDriver = driverRepository.save(driver);
			log.info(String.format("Driver with id: %d is updated", id));
			log.info(String.format("Driver info: %s",updatedDriver.toString()));

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
			System.out.println("Nothing to delete");
			log.info("Nothing to delete");
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
