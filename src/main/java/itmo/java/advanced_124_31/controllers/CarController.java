package itmo.java.advanced_124_31.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.dto.CarDTOResponse;
import itmo.java.advanced_124_31.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@Tag(name = "Cars")
public class CarController {

	private final CarService carService;

	/**
	 * Set a new car to database
	 *
	 * @param carDTORequest new car to add
	 * @return a class object carDTO if everything is well
	 */
	@PostMapping
	@Operation(summary = "Create a car")
	public ResponseEntity<CarDTORequest> create(
			@RequestBody CarDTORequest carDTORequest) {
		return ResponseEntity.ok(carService.create(carDTORequest));
	}

	/**
	 * Read the entity-Car id and returns it DTO
	 *
	 * @param id ID of car in database
	 * @return a class object carDTO
	 */
	@GetMapping()
	@Operation(summary = "Get a car")
	public ResponseEntity<CarDTORequest> read(@RequestParam Long id) {
		return ResponseEntity.ok(carService.get(id));
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id            ID of object to be updated
	 * @param carDTORequest DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@PutMapping()
	@Operation(summary = "Update a car")
	public ResponseEntity<CarDTORequest> update(@RequestParam Long id,
			@RequestBody CarDTORequest carDTORequest) {
		return ResponseEntity.ok(carService.update(id, carDTORequest));
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@DeleteMapping()
	@Operation(summary = "Remove a car")
	public ResponseEntity<HttpStatus> delete(@RequestParam Long id) {
		carService.delete(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Add existed car to it`s driver
	 *
	 * @param idCar    ID of Car
	 * @param idDriver ID od Driver
	 */
	@PutMapping("/addToDriver")
	@Operation(summary = "Add a car to a definite driver")
	public ResponseEntity<CarDTOResponse> addTo(@RequestParam Long idDriver,
			@RequestParam Long idCar) {
		return ResponseEntity.ok(carService.addTo(idDriver, idCar));
	}

	/**
	 * Clear connection of Car and it`s Driver
	 *
	 * @param id ID of Car to be removed from it`s Driver
	 */
	@PutMapping("/removeFromDriver")
	@Operation(summary = "Remove a connection between car and its driver")
	public ResponseEntity<CarDTORequest> removeFrom(@RequestParam Long id) {
		return ResponseEntity.ok(carService.removeDriverFromCar(id));
	}

	/**
	 * Returns a model map of all objects cars in a limited size list sorted by chosen
	 * parameter
	 *
	 * @param page    serial number of page to show
	 * @param perPage elements on page
	 * @param sort    main parameter of sorting
	 * @param order   ASC or DESC
	 * @return ModelMap of sorted elements
	 * @see ModelMap
	 */
	@GetMapping("/all")
	@Operation(summary = "Get all cars")
	public ModelMap getCars(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "1") Integer perPage,
			@RequestParam(required = false, defaultValue = "name") String sort,
			@RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
		return carService.getCars(page, perPage, sort, order);
	}
}
