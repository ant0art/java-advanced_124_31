package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.dto.DriverDTO;
import java.util.List;

public interface DriverService {

	DriverDTO create(DriverDTO driverDTO);

	DriverDTO read(Long id);

	DriverDTO update(Long id, DriverDTO driverDTO);

	void delete(Long id);

	List<DriverDTO> getDrivers();
}
