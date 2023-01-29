package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.dto.CarDTOResponse;
import itmo.java.advanced_124_31.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class CarController {

	private final CarService carService;

	/**
	 * Set a new car to database
	 *
	 * @param carDTORequest new car to add
	 * @return a class object carDTO if everything is well
	 */
	@PostMapping
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
	public ResponseEntity<CarDTORequest> removeFrom(@RequestParam Long id) {
		return ResponseEntity.ok(carService.removeDriverFromCar(id));
	}
}
