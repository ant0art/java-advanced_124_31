package itmo.java.advanced_124_31.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.enums.CarStatus;
import itmo.java.advanced_124_31.model.repository.CarRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

	private final Map<Integer, Car> testMap = new HashMap<>();
	@InjectMocks
	private CarServiceImpl carService;
	@Mock
	private CarRepository carRepository;
	@Spy
	private ObjectMapper mapper;

	@Before
	public void setDefaults() {
		Car car = new Car();
		car.setId(1L);
		car.setStatus(CarStatus.CREATED);
		car.setName("GAZ");
		car.setStateNumber("А111АА111");
		testMap.put(0, car);
	}

	@Test
	public void create() {
		//create car
		Car car = new Car();
		car.setId(1L);
		car.setStatus(CarStatus.CREATED);
		car.setName("GAZ");
		car.setStateNumber("А111АА111");
		//Car car = testMap.get(0);
		//create result carDTO
		CarDTORequest carDTORequest = new CarDTORequest();
		carDTORequest.setName(car.getName());
		carDTORequest.setStateNumber(car.getStateNumber());
		//save car to repository
		//verify(carRepository, times(1)).save(car);
		when(carRepository.findById(1L)).thenReturn(Optional.of(car));
		when(carRepository.save(car)).thenReturn(car);
		//when(carService.get(1L)).thenReturn(carDTORequest);
		//when(carService.get(anyLong())).thenReturn(carDTORequest);

		CarDTORequest result = carService.create(carDTORequest);
		//verify(carRepository, times(1)).save(car);
		assertEquals(car.getName(), result.getName());
	}

	@Test
	public void get() {
		Car car = new Car();
		car.setId(1L);
		car.setName("Gaz");

		when(carRepository.findById(1L)).thenReturn(Optional.of(car));
		CarDTORequest carDTORequest = carService.get(1L);
		assertEquals(carDTORequest.getName(), car.getName());

	}

	@Test
	public void update() {
	}

	@Test
	public void delete() {
		//when(carService.get(anyLong()))
		//Car car = getCar(id);
		//updateStatus(car, CarStatus.DELETED);
		//carRepository.save(car);
	}

	@Test
	public void getCars() {
	}

	@Test
	public void addTo() {
	}

	@Test
	public void removeDriverFromCar() {
	}
}
