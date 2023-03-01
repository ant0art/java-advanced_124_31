package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import itmo.java.advanced_124_31.model.dto.DriverLicenseDTO;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.entity.DriverLicense;
import itmo.java.advanced_124_31.model.enums.DriverLicenseStatus;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.DriverLicenseRepository;
import itmo.java.advanced_124_31.service.DriverLicenseService;
import itmo.java.advanced_124_31.service.DriverService;
import itmo.java.advanced_124_31.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DriverLicenseServiceImpl implements DriverLicenseService {
	
	private final DriverLicenseRepository driverLicenseRepository;
	private final DriverService driverService;
	
	private final ObjectMapper mapper = JsonMapper.builder().addModule(
			new JavaTimeModule()).build();
	
	/**
	 * Returns a {@link DriverLicenseDTO} object after adding new object DriverLicense
	 *
	 * @param driverLicenseDTO new driver license to add
	 *
	 * @return a class object DriverLicenseDTO if everything is well
	 *
	 * @see DriverLicense
	 */
	@Override
	public DriverLicenseDTO create(DriverLicenseDTO driverLicenseDTO) {
		String receivedAt = driverLicenseDTO.getReceivedAt();
		if (receivedAt == null || receivedAt.isEmpty()) {
			throw new CustomException("Receiving date is missing",
					HttpStatus.BAD_REQUEST);
		}
		try {
			LocalDate.parse(receivedAt);
		} catch (DateTimeParseException e) {
			throw new CustomException("Failed to deserialize: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		
		//driverLicenseDTO --> driverLicense
		DriverLicense license = mapper.convertValue(driverLicenseDTO,
				DriverLicense.class);
		license.setStatus(DriverLicenseStatus.CREATED);
		
		//driverLicense --> driverLicenseDTO
		
		return mapper.convertValue(driverLicenseRepository.save(license),
				DriverLicenseDTO.class);
	}
	
	/**
	 * Returns a {@link DriverLicenseDTO} object by the given id if it is found
	 *
	 * @param id driver license ID
	 *
	 * @return the entity DTO with the given id
	 */
	@Override
	public DriverLicenseDTO get(String id) {
		return mapper.convertValue(getDriverLicense(id), DriverLicenseDTO.class);
	}
	
	/**
	 * Returns a {@link DriverLicenseDTO} object after updating fields of its entity
	 *
	 * @param id               ID of object to be updated
	 * @param driverLicenseDTO DriverLicenseDTO with fields to update
	 *
	 * @return a class object DriverLicenseDTO if everything is well
	 *
	 * @see DriverLicense
	 */
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
	
	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@Override
	public void delete(String id) {
		DriverLicense driverLicense = getDriverLicense(id);
		updateStatus(driverLicense, DriverLicenseStatus.DELETED);
		driverLicenseRepository.save(driverLicense);
	}
	
	/**
	 * Returns a list of all objects driver licenses in a limited size list sorted by
	 * chosen parameter
	 *
	 * @param page    serial number of page to show
	 * @param perPage elements on page
	 * @param sort    main parameter of sorting
	 * @param order   ASC or DESC
	 *
	 * @return List<DriverLicenseDTO> of sorted elements
	 *
	 * @see PaginationUtil
	 * @see Pageable
	 * @see Page
	 */
	@Override
	public List<DriverLicenseDTO> getLicenses(Integer page, Integer perPage, String sort,
			Sort.Direction order) {
		Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
		//view 1
		Page<DriverLicense> pageResult = driverLicenseRepository.findAll(pageRequest);
		
		List<DriverLicenseDTO> content = pageResult.getContent().stream().map(
				d -> mapper.convertValue(d, DriverLicenseDTO.class)).collect(
				Collectors.toList());
		return content;
	}
	
	/**
	 * Returns a {@link DriverLicenseDTO} object after adding DriverLicense-entity to
	 * Driver-entity
	 *
	 * @param idLicense ID of {@link itmo.java.advanced_124_31.model.entity.DriverLicense}
	 *                  to be added
	 * @param idDriver  ID of {@link itmo.java.advanced_124_31.model.entity.Driver} to
	 *                  add
	 *
	 * @return a class object DriverLicenseDTO if everything is well
	 *
	 * @see Driver
	 * @see DriverLicense
	 */
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
		return mapper.convertValue(driverLicenseRepository.save(license),
				DriverLicenseDTO.class);
	}
	
	/**
	 * Returns a {@link DriverLicenseDTO} object after cleaning the connection between
	 * DriverLicense and it`s Driver
	 *
	 * @param id ID of {@link DriverLicense} to be removed from its {@link Driver}
	 *
	 * @return {@link DriverLicenseDTO} object if removing ends well
	 */
	@Override
	public DriverLicenseDTO removeDriverLicenseFromDriver(String id) {
		DriverLicense license = getDriverLicense(id);
		Driver driver = license.getDriver();
		if (driver == null) {
			throw new CustomException(String.format(
					"Driver license (id = %s) field {driver} is already cleared", id),
					HttpStatus.BAD_REQUEST);
		}
		license.setDriver(null);
		updateStatus(license, DriverLicenseStatus.UPDATED);
		driver.setLicense(null);
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		return mapper.convertValue(driverLicenseRepository.save(license),
				DriverLicenseDTO.class);
	}
	
	/**
	 * Returns the entity with the given id if it is found
	 *
	 * @param id driver license ID
	 *
	 * @return the entity with the given id
	 */
	@Override
	public DriverLicense getDriverLicense(String id) {
		return driverLicenseRepository.findById(id).orElseThrow(() -> new CustomException(
				String.format("DriverLicense with ID: %s not found", id),
				HttpStatus.NOT_FOUND));
	}
	
	/**
	 * Change the state of {@link DriverLicense}-entity by chosen and set the entity field
	 * updatedAt new local date time
	 *
	 * @param driverLicense driver-entity
	 * @param status        new state of entity
	 *
	 * @see LocalDateTime
	 */
	@Override
	public void updateStatus(DriverLicense driverLicense, DriverLicenseStatus status) {
		driverLicense.setStatus(status);
		driverLicense.setUpdatedAt(LocalDateTime.now());
	}
	
	/**
	 * Copy properties from one object to another field to field (excluding class)
	 * ignoring null
	 *
	 * @param source source object, must not be Null
	 * @param target target object, must not be Null
	 */
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
}
