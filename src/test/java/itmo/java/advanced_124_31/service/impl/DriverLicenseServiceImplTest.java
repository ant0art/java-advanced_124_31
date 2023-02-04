package itmo.java.advanced_124_31.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.DriverLicenseDTO;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.entity.DriverLicense;
import itmo.java.advanced_124_31.model.enums.DriverLicenseCategory;
import itmo.java.advanced_124_31.model.enums.DriverLicenseStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.DriverLicenseRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)
public class DriverLicenseServiceImplTest {

	@InjectMocks
	private DriverLicenseServiceImpl driverLicenseService;
	@Mock
	private DriverServiceImpl driverService;
	@Mock
	private DriverLicenseRepository driverLicenseRepository;
	@Spy
	private ObjectMapper mapper;

	@Test
	public void create() {
		DriverLicenseDTO dto = new DriverLicenseDTO();
		dto.setReceivedAt("2010-10-10");
		when(driverLicenseRepository.save(any(DriverLicense.class))).thenAnswer(
				i -> i.getArguments()[0]);
		DriverLicenseDTO result = driverLicenseService.create(dto);
		assertEquals(dto.getReceivedAt(), result.getReceivedAt());
	}

	@Test(expected = CustomException.class)
	public void create_receivingDateMissing() {
		DriverLicenseDTO dto = new DriverLicenseDTO();
		lenient().when(driverLicenseRepository.save(any(DriverLicense.class)))
				.thenAnswer(i -> i.getArguments()[0]);
		driverLicenseService.create(dto);
	}

	@Test(expected = CustomException.class)
	public void create_parseException() {
		DriverLicenseDTO dto = new DriverLicenseDTO();
		dto.setReceivedAt("2010-1-10");
		lenient().when(driverLicenseRepository.save(any(DriverLicense.class)))
				.thenAnswer(i -> i.getArguments()[0]);
		driverLicenseService.create(dto);
	}

	@Test
	public void get() {
		List<DriverLicenseCategory> categories = Collections.singletonList(
				DriverLicenseCategory.A);
		DriverLicense license = new DriverLicense();
		license.setCategories(categories);
		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		DriverLicenseDTO result = driverLicenseService.get("smth");
		assertEquals(license.getCategories(), result.getCategories());
	}

	@Test(expected = CustomException.class)
	public void get_notFound() {
		driverLicenseService.get("smth");
	}

	@Test
	public void update() {
		DriverLicense license = new DriverLicense();

		DriverLicenseDTO dto = new DriverLicenseDTO();
		List<DriverLicenseCategory> categories = Collections.singletonList(
				DriverLicenseCategory.A);
		dto.setCategories(categories);

		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		license.setCategories(dto.getCategories());
		when(driverLicenseRepository.save(license)).thenAnswer(i -> i.getArguments()[0]);

		DriverLicenseDTO result = driverLicenseService.update("smth", dto);
		assertEquals(license.getCategories(), result.getCategories());
	}

	@Test(expected = CustomException.class)
	public void update_notFound() {
		DriverLicenseDTO dto = new DriverLicenseDTO();
		driverLicenseService.update("smth", dto);
	}

	@Test
	public void delete() {
		DriverLicense license = new DriverLicense();
		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		when(driverLicenseRepository.save(license)).thenAnswer(i -> i.getArguments()[0]);
		driverLicenseService.delete("1");
		verify(driverLicenseRepository, times(1)).save(license);
	}

	@Test
	public void getLicenses() {
		Integer page = 2;
		Integer perPage = 10;
		String sort = "name";
		Sort.Direction order = Sort.Direction.DESC;

		DriverLicense license = new DriverLicense();
		List<DriverLicenseCategory> categories = Collections.singletonList(
				DriverLicenseCategory.A);
		license.setCategories(categories);

		List<DriverLicense> content = Collections.singletonList(license);

		Page<DriverLicense> pageResult = mock(Page.class);

		when(driverLicenseRepository.findAll(any(PageRequest.class))).thenReturn(
				pageResult);
		when(pageResult.getContent()).thenReturn(content);

		List<DriverLicenseDTO> result = driverLicenseService.getLicenses(page, perPage,
				sort, order);
		assertEquals(license.getCategories(), result.get(0).getCategories());

	}

	@Test
	public void addTo() {
		Driver driver = new Driver();
		DriverLicense license = new DriverLicense();
		license.setId("test");
		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		when(driverService.getDriver(anyLong())).thenReturn(driver);
		when(driverLicenseRepository.save(license)).thenAnswer(i -> i.getArguments()[0]);
		DriverLicenseDTO result = driverLicenseService.addTo(1L, "smth");
		verify(driverLicenseRepository, times(1)).save(license);
		assertEquals(license.getId(), result.getId());
	}

	@Test(expected = CustomException.class)
	public void addTo_notEmpty() {
		Driver driver = new Driver();
		DriverLicense license = new DriverLicense();
		license.setDriver(driver);
		license.setId("test");
		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		driverLicenseService.addTo(1L, "smth");
	}

	@Test
	public void removeDriverLicenseFromDriver() {
		DriverLicense license = new DriverLicense();
		license.setId("test");
		Driver driver = new Driver();
		license.setDriver(driver);
		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		Mockito.lenient().when(driverService.getDriver(anyLong())).thenReturn(driver);
		when(driverLicenseRepository.save(any(DriverLicense.class))).thenAnswer(
				i -> i.getArguments()[0]);
		DriverLicenseDTO result = driverLicenseService.removeDriverLicenseFromDriver(
				"smth");
		assertEquals(license.getId(), result.getId());
	}

	@Test(expected = CustomException.class)
	public void removeDriverLicenseFromDriver_isRemoved() {
		DriverLicense license = new DriverLicense();
		Driver driver = new Driver();
		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		Mockito.lenient().when(driverService.getDriver(anyLong())).thenReturn(driver);
		driverLicenseService.removeDriverLicenseFromDriver("smth");
	}

	@Test
	public void getDriverLicense() {
		DriverLicense license = new DriverLicense();
		license.setDriver(new Driver());
		when(driverLicenseRepository.findById(anyString())).thenReturn(
				Optional.of(license));
		DriverLicense result = driverLicenseService.getDriverLicense("test");
		assertEquals(license.getDriver(), result.getDriver());
	}

	@Test(expected = CustomException.class)
	public void getDriverLicense_notFound() {
		driverLicenseService.getDriverLicense("test");
	}

	@Test
	public void updateStatus() {
		DriverLicense license = new DriverLicense();
		driverLicenseService.updateStatus(license, DriverLicenseStatus.UPDATED);
		assertEquals(DriverLicenseStatus.UPDATED, license.getStatus());
	}
}
