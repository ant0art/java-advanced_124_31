package itmo.java.advanced_124_31.service.impl;

import itmo.java.advanced_124_31.dao.Driver;
import itmo.java.advanced_124_31.data.DriverBase;
import itmo.java.advanced_124_31.data.impl.DriverBaseImpl;
import itmo.java.advanced_124_31.service.DriverService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
	private DriverBase driverBase = new DriverBaseImpl();

	/**
	 * Create new driver and save it to driver database
	 *
	 * @param driver new Driver for database to add
	 * @return Driver
	 */
	@Override
	public Driver create(Driver driver) {
		this.driverBase.save(driver);
		return driver;
	}

	/**
	 * Find a Driver by it`s ID
	 *
	 * @param id id of Driver
	 * @return Driver
	 */
	@Override
	public Driver read(Long id) {
		return this.driverBase.find(id);
	}

	/**
	 * Update driver in driver database. It rewrites fields of current object Driver or
	 * add new object Driver if it isn't contained earlier
	 *
	 * @param driver object to update or add to database
	 * @return Driver
	 */
	@Override
	public Driver update(Driver driver) {
		this.driverBase.save(driver);
		return driver;
	}

	/**
	 * Delete object from driver database by its ID
	 *
	 * @param id id of object to delete
	 */
	@Override
	public void delete(Long id) {
		this.driverBase.delete(id);
	}

	@Override
	public List<Driver> getDrivers() {
		return this.driverBase.getDrivers();
	}
}
