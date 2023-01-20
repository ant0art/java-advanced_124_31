package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.entity.Driver;
import java.util.List;

public interface DriverService {
	Driver create(Driver driver);

	Driver read(Long id);

	Driver update(Driver driver);

	void delete(Long id);

	List<Driver> getDrivers();
}
