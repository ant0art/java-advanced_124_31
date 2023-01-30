package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.dto.DriverLicenseDTO;
import itmo.java.advanced_124_31.model.entity.DriverLicense;
import itmo.java.advanced_124_31.model.enums.DriverLicenseStatus;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface DriverLicenseService {

	DriverLicenseDTO create(DriverLicenseDTO driverLicenseDTO);

	DriverLicenseDTO get(String id);

	DriverLicenseDTO update(String id, DriverLicenseDTO driverLicenseDTO);

	void delete(String id);

	List<DriverLicenseDTO> getLicenses(Integer page, Integer perPage, String sort,
			Sort.Direction order);

	DriverLicenseDTO addTo(Long idDriver, String idLicense);

	DriverLicenseDTO removeDriverLicenseFromDriver(String id);

	DriverLicense getDriverLicense(String id);

	void updateStatus(DriverLicense driverLicense, DriverLicenseStatus status);
}
