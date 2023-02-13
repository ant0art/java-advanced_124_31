package itmo.java.advanced_124_31.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.dto.DriverDTOResponse;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.entity.DriverLicense;
import itmo.java.advanced_124_31.model.entity.WorkShift;
import itmo.java.advanced_124_31.model.enums.CarClass;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import itmo.java.advanced_124_31.model.enums.WorkShiftGrade;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceImplTest {

	@Spy
	ObjectMapper mapper;
	@InjectMocks
	private DriverServiceImpl driverService;
	@Mock
	private WorkShiftServiceImpl workShiftService;
	@Mock
	private DriverRepository driverRepository;

	@Test
	public void create() {
		DriverDTORequest dto = new DriverDTORequest();
		dto.setPhoneNumber("123");
		dto.setBirthday("2010-10-10");
		when(driverRepository.save(any(Driver.class))).thenAnswer(
				i -> i.getArguments()[0]);
		DriverDTORequest result = driverService.create(dto);
		assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
	}

	@Test(expected = CustomException.class)
	public void create_phoneNumberIsMissing() {
		DriverDTORequest dto = new DriverDTORequest();
		driverService.create(dto);
	}

	@Test(expected = CustomException.class)
	public void create_driverExists() {
		DriverDTORequest dto = new DriverDTORequest();
		dto.setPhoneNumber("123");
		Driver driver = new Driver();
		driver.setPhoneNumber("123");
		when(driverRepository.findByPhoneNumber(anyString())).thenReturn(
				Optional.of(driver));
		driverService.create(dto);
	}

	@Test(expected = CustomException.class)
	public void create_parseDateException() {
		DriverDTORequest dto = new DriverDTORequest();
		dto.setPhoneNumber("123");
		dto.setBirthday("2010-1-10");
		driverService.create(dto);
	}

	@Test
	public void get() {
		Driver driver = new Driver();
		driver.setName("Alex");
		when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
		DriverDTORequest result = driverService.get(1L);
		assertEquals(driver.getName(), result.getName());
	}

	@Test(expected = CustomException.class)
	public void get_driverNotFound() {
		driverService.get(1L);
	}

	@Test
	public void update() {
		DriverDTORequest dto = new DriverDTORequest();
		dto.setPhoneNumber("123");
		Driver driver = new Driver();
		driver.setPhoneNumber("321");
		when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
		when(driverRepository.save(driver)).thenAnswer(i -> i.getArguments()[0]);
		DriverDTORequest result = driverService.update(1L, dto);
		assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
	}

	@Test(expected = CustomException.class)
	public void update_phoneIsMissing() {
		DriverDTORequest dto = new DriverDTORequest();
		driverService.update(1L, dto);
	}

	@Test
	public void delete() {
		Driver driver = new Driver();
		when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
		when(driverRepository.save(any(Driver.class))).thenReturn(driver);
		driverService.delete(1L);
	}

	@Test
	public void getDrivers() {
		Integer page = 2;
		Integer perPage = 10;
		String sort = "name";
		Sort.Direction order = Sort.Direction.DESC;

		Driver driver = new Driver();
		driver.setName("Alex");
		List<Driver> drivers = Collections.singletonList(driver);
		@SuppressWarnings("unchecked") Page<Driver> pageResult = mock(Page.class);

		when(driverRepository.findAll(any(Pageable.class))).thenReturn(pageResult);
		when(pageResult.getContent()).thenReturn(drivers);
		List<DriverDTORequest> result = driverService.getDrivers(page, perPage, sort,
				order);
		assertEquals(driver.getName(), result.get(0).getName());
	}

	@Test
	public void getDriver() {
		Driver driver = new Driver();
		driver.setName("Alex");
		when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
		Driver result = driverService.getDriver(1L);
		assertEquals(driver.getName(), result.getName());
	}

	@Test(expected = CustomException.class)
	public void getDriver_notFound() {
		driverService.getDriver(1L);
	}

	@Test
	public void updateStatus() {
		Driver driver = new Driver();
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		assertEquals(DriverStatus.UPDATED, driver.getStatus());
	}

	@Test
	public void addToWorkShift() {
		WorkShift workShift = new WorkShift();
		workShift.setGrade(WorkShiftGrade.ECO);

		DriverLicense license = new DriverLicense();
		license.setReceivedAt(LocalDate.of(2018, 10, 10));

		Driver driver = new Driver();
		List<WorkShift> list = new ArrayList<>();
		driver.setWorkShifts(list);
		driver.setLicense(license);
		driver.setName("Alex");

		Car car = new Car();
		car.setCarClass(CarClass.COMFORT);
		car.setVehicleYear(2010);
		car.setId(1L);
		car.setName("Lada");
		List<Car> cars = Collections.singletonList(car);
		driver.setCars(cars);

		when(workShiftService.getWorkShift(anyLong())).thenReturn(workShift);
		Mockito.lenient()
				.when(driverRepository.findById(anyLong()))
				.thenReturn(Optional.of(driver));
		Mockito.lenient()
				.when(mock(Driver.class).getCars())
				.thenReturn(cars);

		driver.getWorkShifts().add(workShift);

		when(driverRepository.save(any(Driver.class))).thenReturn(driver);

		DriverDTOResponse result = driverService.addToWorkShift(1L, 1L, 1L);
		verify(driverRepository, times(1)).save(driver);

		assertEquals(workShift.getGrade(), result.getWorkShift().getGrade());
		assertEquals(workShift.getDriver().getName(),
				result.getWorkShift().getDriver().getName());
		assertEquals(workShift.getCar().getName(),
				result.getWorkShift().getCar().getName());
	}

	@Test(expected = CustomException.class)
	public void addToWorkShift_notEmpty() {
		WorkShift workShift = new WorkShift();
		Driver driver = new Driver();
		workShift.setDriver(driver);
		when(workShiftService.getWorkShift(anyLong())).thenReturn(workShift);
		when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
		driverService.addToWorkShift(1L, 1L, 1L);
	}

	@Test(expected = CustomException.class)
	public void addToWorkShift_wrongCarForDriver() {
		WorkShift workShift = new WorkShift();
		Driver driver = new Driver();
		driver.setCars(Collections.EMPTY_LIST);

		when(workShiftService.getWorkShift(anyLong())).thenReturn(workShift);
		when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
		Mockito.lenient()
				.when(mock(Driver.class).getCars())
				.thenReturn(driver.getCars());

		driverService.addToWorkShift(1L, 1L, 1L);
	}

	@Test
	public void removeDriverFromWorkShift() {
		WorkShift workShift = new WorkShift();
		Driver driver = new Driver();
		workShift.setDriver(driver);
		List<WorkShift> list = new ArrayList<>();
		list.add(workShift);
		driver.setWorkShifts(list);

		Mockito.lenient()
				.when(workShiftService.getWorkShift(anyLong()))
				.thenReturn(workShift);
		Mockito.lenient()
				.when(driverRepository.findById(anyLong()))
				.thenReturn(Optional.of(driver));

		when(driverRepository.save(driver)).thenReturn(driver);
		DriverDTORequest result = driverService.removeDriverFromWorkShift(1L);
		assertEquals(driver.getName(), result.getName());
	}

	@Test(expected = CustomException.class)
	public void removeDriverFromWorkShift_isRemoved() {
		WorkShift workShift = new WorkShift();
		Driver driver = new Driver();

		Mockito.lenient()
				.when(workShiftService.getWorkShift(anyLong()))
				.thenReturn(workShift);
		Mockito.lenient()
				.when(driverRepository.findById(anyLong()))
				.thenReturn(Optional.of(driver));

		driverService.removeDriverFromWorkShift(1L);

	}
}
