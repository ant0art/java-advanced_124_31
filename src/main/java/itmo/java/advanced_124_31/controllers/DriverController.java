package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.model.dto.DriverDTO;
import itmo.java.advanced_124_31.service.DriverService;
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
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
	private final DriverService driverService;


	/**
	 * Get the List of all drivers, included in DB
	 *
	 * @return List of DriversDTO
	 */
	@GetMapping
	public List<DriverDTO> getDrivers() {
		return driverService.getDrivers();
	}

	/**
	 * Set a new driver to database
	 *
	 * @param driverDTO new driver to add
	 * @return a class object driverDTO if everything is well
	 */
	@PostMapping
	public DriverDTO create(@RequestBody DriverDTO driverDTO) {
		return driverService.create(driverDTO);
	}

	/**
	 * Read the entity-Driver id and returns it DTO
	 *
	 * @param id ID of driver in database
	 * @return a class object driverDTO
	 */
	@GetMapping("/{id}")
	public DriverDTO read(@PathVariable("id") Long id) {
		return driverService.read(id);
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id        ID of object to be updated
	 * @param driverDTO DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@PutMapping("/{id}")
	public DriverDTO update(@PathVariable Long id, @RequestBody DriverDTO driverDTO) {
		return driverService.update(id, driverDTO);
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		driverService.delete(id);
	}
}
