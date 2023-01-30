package itmo.java.advanced_124_31.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itmo.java.advanced_124_31.model.dto.DriverLicenseDTO;
import itmo.java.advanced_124_31.service.DriverLicenseService;
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
@RequestMapping("/licenses")
@RequiredArgsConstructor
@Tag(name = "Driver licenses")
public class DriverLicenseController {

	private final DriverLicenseService driverLicenseService;


	/**
	 * Set a new driverLicense to database
	 *
	 * @param driverLicenseDTO new driverLicense to add
	 * @return a class object driverLicenseDTO if everything is well
	 */
	@PostMapping
	@Operation(summary = "Create a driver license")
	public ResponseEntity<DriverLicenseDTO> create(
			@RequestBody DriverLicenseDTO driverLicenseDTO) {
		return ResponseEntity.ok(driverLicenseService.create(driverLicenseDTO));
	}

	/**
	 * Read the entity-DriverLicense id and returns it DTO
	 *
	 * @param id ID of driverLicense in database
	 * @return a class object driverLicenseDTO
	 */
	@GetMapping()
	@Operation(summary = "Get a driver license")
	public ResponseEntity<DriverLicenseDTO> read(@RequestParam String id) {
		return ResponseEntity.ok(driverLicenseService.get(id));
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id               ID of object to be updated
	 * @param driverLicenseDTO DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@PutMapping()
	@Operation(summary = "Update a driver license")
	public ResponseEntity<DriverLicenseDTO> update(@RequestParam String id,
			@RequestBody DriverLicenseDTO driverLicenseDTO) {
		return ResponseEntity.ok(driverLicenseService.update(id, driverLicenseDTO));
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@DeleteMapping()
	@Operation(summary = "Remove a driver license")
	public ResponseEntity<HttpStatus> delete(@RequestParam String id) {
		driverLicenseService.delete(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Add existed driverLicense to it`s driver
	 *
	 * @param idLicense ID of DriverLicense
	 * @param idDriver  ID od Driver
	 */
	@PutMapping("/addToDriver")
	@Operation(summary = "Place a driver license to its driver")
	public ResponseEntity<DriverLicenseDTO> addTo(@RequestParam Long idDriver,
			@RequestParam String idLicense) {
		return ResponseEntity.ok(driverLicenseService.addTo(idDriver, idLicense));
	}

	/**
	 * Clear connection of DriverLicense and its Driver
	 *
	 * @param id ID of DriverLicense to be removed from its Driver
	 */
	@PutMapping("/removeFromDriver")
	@Operation(summary = "Remove a connection between driver license and its driver")
	public ResponseEntity<DriverLicenseDTO> removeFrom(@RequestParam String id) {
		return ResponseEntity.ok(driverLicenseService.removeDriverLicenseFromDriver(id));
	}

	/**
	 * Returns a list of all objects driver licenses in a limited size list
	 * sorted by chosen parameter
	 *
	 * @param page    serial number of page to show
	 * @param perPage elements on page
	 * @param sort    main parameter of sorting
	 * @param order   ASC or DESC
	 * @return ModelMap of sorted elements
	 * @see ModelMap
	 */
	@GetMapping("/all")
	@Operation(summary = "Get all driver licenses")
	public List<DriverLicenseDTO> getAllLicenses(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "1") Integer perPage,
			@RequestParam(required = false, defaultValue = "name") String sort,
			@RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
		return driverLicenseService.getLicenses(page, perPage, sort, order);
	}
}
