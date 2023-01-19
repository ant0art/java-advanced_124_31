package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.dao.Driver;
import itmo.java.advanced_124_31.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DriverController {
	private final DriverService driverService;


	@GetMapping("/drivers")
	public List<Driver> getDrivers() {
		return driverService.getDrivers();
	}

	//post
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Driver create(@RequestBody Driver driver) {
		return driverService.create(driver);
	}

	//get
	@GetMapping("/drivers/{id}")
	public Driver read(@PathVariable("id") Long id) {
		return driverService.read(id);
	}

	//put
	@PutMapping
	public Driver update(@RequestBody Driver driver) {
		return driverService.update(driver);
	}

	//delete
	@DeleteMapping("/drivers/{id}")
	public void delete(@PathVariable Long id) {
		driverService.delete(id);
	}
}
