package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.dto.DriverDTOResponse;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import java.util.List;

public interface DriverService {

	DriverDTORequest create(DriverDTORequest driverDTORequest);

	DriverDTORequest get(Long id);

	DriverDTORequest update(Long id, DriverDTORequest driverDTORequest);

	void delete(Long id);

	List<DriverDTORequest> getDrivers();

	Driver getDriver(Long id);

	void updateStatus(Driver driver, DriverStatus status);

	DriverDTOResponse addToWorkShift(Long idWorkShift, Long idDriver, Long idCar);

	DriverDTORequest removeDriverFromWorkShift(Long idWorkShift);
}
