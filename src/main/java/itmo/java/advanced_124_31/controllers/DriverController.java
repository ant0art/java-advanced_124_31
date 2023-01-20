package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
	private final DriverService driverService;


	@GetMapping
	public List<Driver> getDrivers() {
		return driverService.getDrivers();
	}

	//post
	//@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Driver create(@RequestBody Driver driver) {
		return driverService.create(driver);
	}

	//get
	@GetMapping("/{id}")
	public Driver read(@PathVariable("id") Long id) {
		return driverService.read(id);
	}

	//put
	@PutMapping
	public Driver update(@RequestBody Driver driver) {
		return driverService.update(driver);
	}

	//delete
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		driverService.delete(id);
	}
}
