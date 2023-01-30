package itmo.java.advanced_124_31.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.dto.DriverDTOResponse;
import itmo.java.advanced_124_31.service.DriverService;
import java.util.List;
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
@RequestMapping("/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers")
public class DriverController {
	private final DriverService driverService;

	/**
	 * Set a new driver to database
	 *
	 * @param driverDTORequest new driver to add
	 * @return a class object driverDTO if everything is well
	 */
	@PostMapping
	@Operation(summary = "Create a driver")
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
	@Operation(summary = "Get a driver")
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
	@Operation(summary = "Update a driver")
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
	@Operation(summary = "Remove a driver")
	public ResponseEntity<HttpStatus> delete(@RequestParam Long id) {
		driverService.delete(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/addToWorkShift")
	@Operation(summary = "Place driver and his own car to work shift")
	public ResponseEntity<DriverDTOResponse> addToWorkShift(
			@RequestParam Long idWorkShift, @RequestParam Long idDriver,
			@RequestParam Long idCar) {
		return ResponseEntity.ok(
				driverService.addToWorkShift(idWorkShift, idDriver, idCar));
	}

	@PutMapping("/removeFromWorkShift")
	@Operation(summary = "Remove a connection between driver, his own car and work shift")
	public ResponseEntity<DriverDTORequest> removeFromWorkShift(
			@RequestParam Long idWorkShift) {
		return ResponseEntity.ok(driverService.removeDriverFromWorkShift(idWorkShift));
	}

	/**
	 * Returns a list of all objects drivers in a limited size list sorted by chosen
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
	@Operation(summary = "Get all drivers")
	public List<DriverDTORequest> getAllDrivers(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "1") Integer perPage,
			@RequestParam(required = false, defaultValue = "name") String sort,
			@RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
		return driverService.getDrivers(page, perPage, sort, order);
	}
}
