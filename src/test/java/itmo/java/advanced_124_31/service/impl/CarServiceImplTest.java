package itmo.java.advanced_124_31.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.dto.CarDTOResponse;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.CarRepository;
import itmo.java.advanced_124_31.model.repository.DriverRepository;
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
import org.springframework.ui.ModelMap;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

	@InjectMocks
	private CarServiceImpl carService;
	@Mock
	private DriverServiceImpl driverService;
	@Mock
	private DriverRepository driverRepository;
	@Mock
	private CarRepository carRepository;
	@Spy
	private ObjectMapper mapper;

	@Test
	public void create() {
		CarDTORequest carDTORequest = new CarDTORequest();
		carDTORequest.setName("GAZ");
		carDTORequest.setStateNumber("А111АА111");
		when(carRepository.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);
		CarDTORequest result = carService.create(carDTORequest);
		assertEquals(carDTORequest.getName(), result.getName());
	}

	@Test(expected = CustomException.class)
	public void create_stateMissing() {
		CarDTORequest carDTORequest = new CarDTORequest();
		Mockito.lenient()
				.when(carRepository.save(any(Car.class)))
				.thenAnswer(i -> i.getArguments()[0]);
		carService.create(carDTORequest);
	}

	@Test(expected = CustomException.class)
	public void create_wrongStateFormat() {
		CarDTORequest carDTORequest = new CarDTORequest();
		carDTORequest.setStateNumber("123");
		lenient().when(carRepository.save(any(Car.class)))
				.thenAnswer(i -> i.getArguments()[0]);
		carService.create(carDTORequest);
	}

	@Test(expected = CustomException.class)
	public void create_alreadyExisted() {
		Car car = new Car();
		car.setStateNumber("А111АА111");
		CarDTORequest carDTORequest = new CarDTORequest();
		carDTORequest.setStateNumber("А111АА111");
		when(carRepository.findByStateNumberIgnoreCase(car.getStateNumber())).thenReturn(
				Optional.of(car));
		carService.create(carDTORequest);
	}

	@Test
	public void get() {
		Car car = new Car();
		car.setId(1L);
		car.setName("Gaz");
		when(carRepository.findById(1L)).thenReturn(Optional.of(car));

		CarDTORequest result = carService.get(1L);
		assertEquals(car.getName(), result.getName());
	}

	@Test
	public void update() {
		CarDTORequest update = new CarDTORequest();
		update.setStateNumber("А111АА111");
		update.setName("Volvo");
		Car car = mapper.convertValue(update, Car.class);
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		when(carRepository.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);
		CarDTORequest result = carService.update(1L, update);
		assertEquals(car.getName(), result.getName());
	}

	@Test(expected = CustomException.class)
	public void update_notFound() {
		carService.update(1L, new CarDTORequest());
	}

	@Test(expected = CustomException.class)
	public void update_idNull() {
		carService.update(null, new CarDTORequest());
	}

	@Test
	public void delete() {
		Car car = new Car();
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		Mockito.lenient()
				.when(carRepository.save(any(Car.class)))
				.thenAnswer(i -> i.getArguments()[0]);
		carService.delete(1L);
		verify(carRepository, times(1)).save(car);
	}

	@Test
	public void getCars() {
		//initialize params
		Integer page = 2;
		Integer perPage = 10;
		String sort = "name";
		Sort.Direction order = Sort.Direction.DESC;
		//create obj Car
		Car car = new Car();
		car.setName("Lada");
		//add to Collection
		List<Car> cars = Collections.singletonList(car);
		//add mocked Page
		Page<Car> pageResult = mock(Page.class);

		when(carRepository.findAll(any(Pageable.class))).thenReturn(pageResult);
		when(pageResult.getContent()).thenReturn(cars);
		//call serviceImpl method
		ModelMap result = carService.getCars(page, perPage, sort, order);
		List<CarDTORequest> carDTORequests = (List<CarDTORequest>) (result.get(
				"content"));
		assertEquals(car.getName(), carDTORequests.get(0).getName());
	}

	@Test(expected = CustomException.class)
	public void getCars_perPageZero() {
		Integer page = 2;
		Integer perPage = 0;
		String sort = "name";
		Sort.Direction order = Sort.Direction.DESC;
		carService.getCars(page, perPage, sort, order);
	}

	@Test
	public void addTo() {
		Car car = new Car();
		car.setName("LADA");
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		Driver driver = new Driver();
		driver.setCars(new ArrayList<>());
		lenient().when(driverRepository.findById(anyLong()))
				.thenReturn(Optional.of(driver));
		when(driverService.getDriver(anyLong())).thenReturn(driver);
		driver.getCars().add(car);
		when(carRepository.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);
		CarDTOResponse result = carService.addTo(1L, 1L);
		assertEquals(car.getName(), result.getName());
	}

	@Test(expected = CustomException.class)
	public void addTo_carDriverNotEmpty() {
		Car car = new Car();
		car.setDriver(new Driver());
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		carService.addTo(1L, 1L);
	}

	@Test(expected = CustomException.class)
	public void addTo_idNotFound() {
		Car car = new Car();
		Mockito.lenient()
				.when(carRepository.findById(car.getId()))
				.thenReturn(Optional.of(car));
		carService.addTo(1L, 1L);
	}

	@Test
	public void removeDriverFromCar() {
		//init test entities
		Car car = new Car();
		car.setName("LADA");
		Driver driver = new Driver();
		driver.setCars(new ArrayList<>());
		driver.getCars().add(car);
		car.setDriver(driver);

		//find entities from mock repos
		lenient().when(carRepository.findById(anyLong()))
				.thenReturn(Optional.of(car));
		lenient().when(driverRepository.findById(anyLong()))
				.thenReturn(Optional.of(driver));
		lenient().when(driverService.getDriver(anyLong()))
				.thenReturn(driver);

		//remove & save
		driver.getCars().remove(car);
		when(carRepository.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);

		CarDTORequest result = carService.removeDriverFromCar(1L);
		assertNull(car.getDriver());
		assertEquals(car.getName(), result.getName());
	}

	@Test(expected = CustomException.class)
	public void removeDriverFromCar_isRemoved() {
		Car car = new Car();
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		carService.removeDriverFromCar(1L);
	}
}
