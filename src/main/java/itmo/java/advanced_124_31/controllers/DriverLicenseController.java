package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.model.dto.DriverLicenseDTO;
import itmo.java.advanced_124_31.service.DriverLicenseService;
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
@RequestMapping("/licenses")
@RequiredArgsConstructor
public class DriverLicenseController {

	private final DriverLicenseService driverLicenseService;


	/**
	 * Set a new driverLicense to database
	 *
	 * @param driverLicenseDTO new driverLicense to add
	 * @return a class object driverLicenseDTO if everything is well
	 */
	@PostMapping
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
	public ResponseEntity<DriverLicenseDTO> addTo(@RequestParam Long idDriver,
			@RequestParam String idLicense) {
		return ResponseEntity.ok(driverLicenseService.addTo(idDriver, idLicense));
	}

	/**
	 * Clear connection of DriverLicense and it`s Driver
	 *
	 * @param id ID of DriverLicense to be removed from it`s Driver
	 */
	@PutMapping("/removeFromDriver")
	public ResponseEntity<DriverLicenseDTO> removeFrom(@RequestParam String id) {
		return ResponseEntity.ok(driverLicenseService.removeDriverLicenseFromDriver(id));
	}
}
