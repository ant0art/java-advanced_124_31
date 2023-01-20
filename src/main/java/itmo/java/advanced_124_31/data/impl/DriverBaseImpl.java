package itmo.java.advanced_124_31.data.impl;

import itmo.java.advanced_124_31.data.Carbase;
import itmo.java.advanced_124_31.data.DriverBase;
import itmo.java.advanced_124_31.model.entity.Driver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DriverBaseImpl implements DriverBase, Serializable {

	static final long SerialVersionUID = 1;
	private static Carbase carBase = new CarBaseImpl();

	private List<Driver> drivers = new ArrayList<>();
	private static long id = 1;

	@Override
	public Driver find(Long id) {
		Driver driver = null;

		try {
			driver =
					getDrivers().stream().filter(e -> e.getId().equals(id)).findFirst()
							.orElse(null);
		} catch (NullPointerException e) {
			log.error("Error NullPointerException");
			//e.printStackTrace();
		}
		return driver;
	}

	@Override
	public void save(Driver driver) {
		Driver driverById = find(driver.getId());

		if (driverById != null) {
			delete(driver.getId());
		} else {
			log.info(String.format("Driver with id=[%d] not found, new Driver added",
					driver.getId()));
			driver.setId(id++);
		}
		driver.getCars().forEach(e -> carBase.save(e));
		this.drivers.add(driver);
	}

	@Override
	public void delete(Long id) {
		log.info(String.format("Delete method of %s started", this.getClass().getName()));
		Driver driver = find(id);
		if (driver != null) {
			getDrivers().remove(driver);
			log.info(String.format("Object %s by id: %d has been deleted",
					driver.getName(), driver.getId()));
		} else {
			log.error(String.format("Driver with id=[%d] not found", id));
		}
	}

	@Override
	public List<Driver> getDrivers() {
		this.drivers =
				this.drivers.stream().sorted((x, y) -> (int) (x.getId() - y.getId()))
						.collect(
								Collectors.toList());
		return this.drivers;
	}
}
