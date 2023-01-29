package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.DriverLicenseDTO;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.entity.DriverLicense;
import itmo.java.advanced_124_31.model.enums.DriverLicenseStatus;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.DriverLicenseRepository;
import itmo.java.advanced_124_31.service.DriverLicenseService;
import itmo.java.advanced_124_31.service.DriverService;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DriverLicenseServiceImpl implements DriverLicenseService {

	private final DriverLicenseRepository driverLicenseRepository;
	private final DriverService driverService;

	private final ObjectMapper mapper;

	@Override
	public DriverLicenseDTO create(DriverLicenseDTO driverLicenseDTO) {
		String receivedAt = driverLicenseDTO.getReceivedAt();
		if (receivedAt == null || receivedAt.isEmpty()) {
			throw new CustomException("Receiving date is missing",
					HttpStatus.BAD_REQUEST);
		}

		//driverLicenseDTO --> driverLicense
		DriverLicense license = mapper.convertValue(driverLicenseDTO,
				DriverLicense.class);
		license.setStatus(DriverLicenseStatus.CREATED);

		//driverLicense --> driverLicenseDTO

		DriverLicenseDTO licenseDTO = mapper.convertValue(
				driverLicenseRepository.save(license), DriverLicenseDTO.class);
		return licenseDTO;
	}

	@Override
	public DriverLicenseDTO get(String id) {
		return mapper.convertValue(getDriverLicense(id), DriverLicenseDTO.class);
	}

	@Override
	public DriverLicenseDTO update(String id, DriverLicenseDTO driverLicenseDTO) {

		AtomicReference<DriverLicenseDTO> dto = new AtomicReference<>(
				new DriverLicenseDTO());
		driverLicenseRepository.findById(id).ifPresentOrElse(d -> {
			copyPropertiesIgnoreNull(
					mapper.convertValue(driverLicenseDTO, DriverLicense.class), d);
			updateStatus(d, DriverLicenseStatus.UPDATED);
			dto.set(mapper.convertValue(driverLicenseRepository.save(d),
					DriverLicenseDTO.class));
		}, () -> {
			throw new CustomException(String.format(
					"Driver license with id: %s not found. Nothing to update", id),
					HttpStatus.NOT_FOUND);
		});
		return dto.get();
	}

	@Override
	public void delete(String id) {
		DriverLicense driverLicense = getDriverLicense(id);
		updateStatus(driverLicense, DriverLicenseStatus.DELETED);
		driverLicenseRepository.save(driverLicense);
	}

	@Override
	public List<DriverLicenseDTO> getLicenses() {
		return driverLicenseRepository.findAll().stream().map(e -> get(e.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public DriverLicenseDTO addTo(Long idDriver, String idLicense) {

		DriverLicense license = getDriverLicense(idLicense);
		if (license.getDriver() != null) {
			throw new CustomException(
					String.format("Driver license (id = %s) field {driver} is not empty",
							idLicense), HttpStatus.BAD_REQUEST);
		}
		Driver driver = driverService.getDriver(idDriver);
		license.setDriver(driver);
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		driver.setLicense(license);
		updateStatus(license, DriverLicenseStatus.UPDATED);
		DriverLicenseDTO driverLicenseDTO = mapper.convertValue(
				driverLicenseRepository.save(license), DriverLicenseDTO.class);
		return driverLicenseDTO;
	}

	@Override
	public DriverLicenseDTO removeDriverLicenseFromDriver(String id) {
		DriverLicense license = getDriverLicense(id);
		Driver driver = license.getDriver();
		if (driver == null) {
			throw new CustomException(String.format(
					"Driver license (id = %s) field " + "{driver} is already cleared",
					id), HttpStatus.BAD_REQUEST);
		}
		license.setDriver(null);
		updateStatus(license, DriverLicenseStatus.UPDATED);
		driver.setLicense(null);
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		DriverLicenseDTO driverLicenseDTO = mapper.convertValue(
				driverLicenseRepository.save(license), DriverLicenseDTO.class);
		return driverLicenseDTO;
	}

	private void copyPropertiesIgnoreNull(Object source, Object target) {
		BeanWrapper src = new BeanWrapperImpl(source);
		BeanWrapper trg = new BeanWrapperImpl(target);

		for (PropertyDescriptor descriptor : src.getPropertyDescriptors()) {
			String propertyName = descriptor.getName();
			if (propertyName.equals("class")) {
				continue;
			}
			Object propertyValue = src.getPropertyValue(propertyName);
			if (propertyValue != null) {
				trg.setPropertyValue(propertyName, propertyValue);
			}
		}
	}

	@Override
	public DriverLicense getDriverLicense(String id) {
		return driverLicenseRepository.findById(id).orElseThrow(() -> new CustomException(
				String.format("DriverLicense with ID: %s not found", id),
				HttpStatus.NOT_FOUND));
	}

	@Override
	public void updateStatus(DriverLicense driverLicense, DriverLicenseStatus status) {
		driverLicense.setStatus(status);
		driverLicense.setUpdatedAt(LocalDateTime.now());
	}
}
