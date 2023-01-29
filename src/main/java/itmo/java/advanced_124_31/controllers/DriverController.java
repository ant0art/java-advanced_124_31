package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.dto.DriverDTOResponse;
import itmo.java.advanced_124_31.service.DriverService;
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
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
	private final DriverService driverService;

	/**
	 * Set a new driver to database
	 *
	 * @param driverDTORequest new driver to add
	 * @return a class object driverDTO if everything is well
	 */
	@PostMapping
	public ResponseEntity<DriverDTORequest> create(
			@RequestBody DriverDTORequest driverDTORequest) {
		return ResponseEntity.ok(driverService.create(driverDTORequest));
	}

	/**
	 * Read the entity-Driver id and returns it DTO
	 *
	 * @param id ID of driver in database
	 * @return a class object driverDTO
	 */
	@GetMapping()
	public ResponseEntity<DriverDTORequest> read(@RequestParam Long id) {
		return ResponseEntity.ok(driverService.get(id));
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id               ID of object to be updated
	 * @param driverDTORequest DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@PutMapping()
	public ResponseEntity<DriverDTORequest> update(@RequestParam Long id,
			@RequestBody DriverDTORequest driverDTORequest) {
		return ResponseEntity.ok(driverService.update(id, driverDTORequest));
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@DeleteMapping()
	public ResponseEntity<HttpStatus> delete(@RequestParam Long id) {
		driverService.delete(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/addToWorkShift")
	public ResponseEntity<DriverDTOResponse> addToWorkShift(
			@RequestParam Long idWorkShift, @RequestParam Long idDriver,
			@RequestParam Long idCar) {
		return ResponseEntity.ok(
				driverService.addToWorkShift(idWorkShift, idDriver, idCar));
	}

	@PutMapping("/removeFromWorkShift")
	public ResponseEntity<DriverDTORequest> removeFromWorkShift(
			@RequestParam Long idWorkShift) {
		return ResponseEntity.ok(driverService.removeDriverFromWorkShift(idWorkShift));
	}

}
