package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.dto.CarDTOResponse;
import itmo.java.advanced_124_31.model.dto.DriverDTORequest;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import itmo.java.advanced_124_31.model.enums.CarStatus;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.CarRepository;
import itmo.java.advanced_124_31.service.CarService;
import itmo.java.advanced_124_31.service.DriverService;
import itmo.java.advanced_124_31.utils.PaginationUtil;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

	private final CarRepository carRepository;
	private final DriverService driverService;

	private final ObjectMapper mapper;

	/**
	 * Set a new car to database
	 *
	 * @param carDTORequest new car to add
	 * @return a class object carDTO if everything is well
	 */
	@Override
	public CarDTORequest create(CarDTORequest carDTORequest) {
		checkStateNumber(carDTORequest.getStateNumber());

		//carDTO --> car
		Car car = mapper.convertValue(carDTORequest, Car.class);
		car.setStatus(CarStatus.CREATED);
		//car --> carDTO
		CarDTORequest readedDTO = get(carRepository.save(car).getId());
		return readedDTO;
	}

	/**
	 * Read the entity-Car id and returns it DTO
	 *
	 * @param id ID of car in database
	 * @return a class object carDTO
	 */
	@Override
	public CarDTORequest get(Long id) {
		return mapper.convertValue(getCar(id), CarDTORequest.class);
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id            ID of object to be updated
	 * @param carDTORequest DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@Override
	public CarDTORequest update(Long id, CarDTORequest carDTORequest) {

		AtomicReference<CarDTORequest> dto = new AtomicReference<>(new CarDTORequest());
		carRepository.findById(id).ifPresentOrElse(c -> {
			if (carDTORequest.getStateNumber() != null &&
					!carDTORequest.getStateNumber().isEmpty()) {
				checkStateNumber(carDTORequest.getStateNumber());
			}
			copyPropertiesIgnoreNull(mapper.convertValue(carDTORequest, Car.class), c);
			updateStatus(c, CarStatus.UPDATED);
			dto.set(mapper.convertValue(carRepository.save(c), CarDTORequest.class));
		}, () -> {
			throw new CustomException(
					String.format("Car with id: %d not found. Nothing to update", id),
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
	public void delete(Long id) {
		Car car = getCar(id);
		updateStatus(car, CarStatus.DELETED);
		carRepository.save(car);
	}

	/**
	 * Returns a model map of all objects cars in a limited size list sorted by chosen
	 * parameter
	 *
	 * @param page    serial number of page to show
	 * @param perPage elements on page
	 * @param sort    main parameter of sorting
	 * @param order   ASC or DESC
	 * @return ModelMap of sorted elements
	 * @see ModelMap
	 */
	@Override
	public ModelMap getCars(Integer page, Integer perPage, String sort,
			Sort.Direction order) {
		if (perPage == 0) {
			throw new CustomException("Page size must not be less than one",
					HttpStatus.BAD_REQUEST);
		}

		Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

		//view 1
		Page<Car> pageResult = carRepository.findAll(pageRequest);

		List<CarDTORequest> content = pageResult.getContent().stream()
				.map(c -> mapper.convertValue(c, CarDTORequest.class))
				.collect(Collectors.toList());
		//view 2
		PagedListHolder<CarDTORequest> result = new PagedListHolder<>(content);
		result.setPage(page);
		result.setPageSize(perPage);

		ModelMap map = new ModelMap();
		map.addAttribute("content", result.getPageList());
		map.addAttribute("pageNumber", page);
		map.addAttribute("PageSize", result.getPageSize());
		map.addAttribute("totalPages", result.getPageCount()); //возвращает единицу

		return map;
	}

	/**
	 * Add existed car to its driver
	 *
	 * @param idCar    ID of {@link itmo.java.advanced_124_31.model.entity.Car}
	 * @param idDriver ID of {@link itmo.java.advanced_124_31.model.entity.Driver}
	 * @see Car
	 * @see Driver
	 */
	@Override
	public CarDTOResponse addTo(Long idDriver, Long idCar) {

		Car car = getCar(idCar);
		if (car.getDriver() != null) {
			throw new CustomException(
					String.format("Cars (id = %d) field {driver} is not empty", idCar),
					HttpStatus.BAD_REQUEST);
		}
		Driver driver = driverService.getDriver(idDriver);
		driver.getCars().add(car);
		car.setDriver(driver);
		updateStatus(car, CarStatus.UPDATED);
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		CarDTOResponse response = mapper.convertValue(carRepository.save(car),
				CarDTOResponse.class);
		response.setDriverDTORequest(mapper.convertValue(driver, DriverDTORequest.class));
		return response;
	}

	@Override
	public CarDTORequest removeDriverFromCar(Long id) {

		Car car = getCar(id);
		Driver driver = car.getDriver();
		if (driver == null) {
			throw new CustomException(
					String.format("Car (id = %d) field " + "{driver} is already cleared",
							id), HttpStatus.BAD_REQUEST);
		}
		driver.getCars().remove(car);
		driverService.updateStatus(driver, DriverStatus.UPDATED);
		//driverRepository.save(driver);
		car.setDriver(null);
		updateStatus(car, CarStatus.UPDATED);
		CarDTORequest request = mapper.convertValue(carRepository.save(car),
				CarDTORequest.class);
		return request;
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

	private Car getCar(Long id) {
		return carRepository.findById(id).orElseThrow(
				() -> new CustomException(String.format("Car with ID: %d not found", id),
						HttpStatus.NOT_FOUND));
	}

	private void updateStatus(Car car, CarStatus status) {
		car.setStatus(status);
		car.setUpdatedAt(LocalDateTime.now());
	}

	/**
	 * Checks whether input state number is correct or not
	 *
	 * @param stateNumber input state number
	 */
	private void checkStateNumber(String stateNumber) throws CustomException {
		if (stateNumber == null || stateNumber.isEmpty()) {
			throw new CustomException("State number is missing", HttpStatus.BAD_REQUEST);
		}
		if (!stateNumber.matches(
				"^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2," + "3}$")) {
			throw new CustomException("Wrong state number format",
					HttpStatus.BAD_REQUEST);
		}
		carRepository.findByStateNumberIgnoreCase(stateNumber).ifPresent(c -> {
			throw new CustomException(String.format("Car with number: %s already exists",
					c.getStateNumber()), HttpStatus.BAD_REQUEST);
		});
	}
}
