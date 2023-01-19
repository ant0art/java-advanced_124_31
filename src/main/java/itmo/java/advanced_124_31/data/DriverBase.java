package itmo.java.advanced_124_31.data;

import itmo.java.advanced_124_31.dao.Driver;
import java.util.List;

public interface DriverBase {

	Driver find(Long id);

	void save(Driver driver);

	void delete(Long id);

	List<Driver> getDrivers();
}

