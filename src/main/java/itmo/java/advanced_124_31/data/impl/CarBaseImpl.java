package itmo.java.advanced_124_31.data.impl;

import itmo.java.advanced_124_31.dao.Car;
import itmo.java.advanced_124_31.data.Carbase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarBaseImpl implements Carbase, Serializable {

	static final long SerialVersionUID = 1;
	private List<Car> cars = new ArrayList<>();
	private static long id = 1;

	@Override
	public Car find(Long id) {
		Car car = null;

		try {
			car = getCars().stream().filter(e -> e.getId().equals(id)).findFirst()
					.orElse(null);
		} catch (NullPointerException e) {
		}
		return car;
	}

	@Override
	public void save(Car car) {
		Car carById = find(car.getId());
		if (carById != null) {
			delete(car.getId());
		} else {
			log.info(String.format("Car with id=[%d] not found, new Car added",
					car.getId()));
			car.setId(id++);
		}
		this.cars.add(car);
	}

	@Override
	public void delete(Long id) {
		log.info(String.format("Delete method of %s started", this.getClass().getName()));
		Car car = find(id);
		if (car != null) {
			getCars().remove(car);
			log.info(String.format("Object %s by id: %d has been deleted",
					car.getName(), car.getId()));
		} else {
			log.error(String.format("Car with id=[%d] not found", id));
		}
	}

	@Override
	public List<Car> getCars() {
		this.cars =
				this.cars.stream().sorted((x, y) -> (int) (x.getId() - y.getId()))
						.collect(
								Collectors.toList());
		return this.cars;
	}
}

