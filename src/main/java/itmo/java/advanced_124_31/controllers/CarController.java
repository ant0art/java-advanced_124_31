package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.model.dto.CarDTO;
import itmo.java.advanced_124_31.model.dto.DriverDTO;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.service.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

	private final CarService carService;


	/**
	 * Get the List of all cars, included in DB
	 *
	 * @return List of CarsDTO
	 */
	@GetMapping
	public List<CarDTO> getCars() {
		return carService.getCars();
	}

	/**
	 * Set a new car to database
	 *
	 * @param carDTO new car to add
	 * @return a class object carDTO if everything is well
	 */
	@PostMapping
	public CarDTO create(@RequestBody CarDTO carDTO) {
		return carService.create(carDTO);
	}

	/**
	 * Read the entity-Car id and returns it DTO
	 *
	 * @param id ID of car in database
	 * @return a class object carDTO
	 */
	@GetMapping("/{id}")
	public CarDTO read(@PathVariable("id") Long id) {
		return carService.read(id);
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id     ID of object to be updated
	 * @param carDTO DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@PutMapping("/{id}")
	public CarDTO update(@PathVariable Long id, @RequestBody CarDTO carDTO) {
		return carService.update(id, carDTO);
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		carService.delete(id);
	}


	/**
	 * Add existed car to it`s driver
	 *
	 * @param idCar    ID of Car
	 * @param idDriver ID od Driver
	 */
	@PutMapping("/add/{idDriver}/{idCar}")
	public void addTo(@PathVariable("idDriver") Long idDriver,
			@PathVariable("idCar") Long idCar) {
		carService.addTo(idDriver, idCar);
	}
}
