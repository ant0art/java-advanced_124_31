package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.dao.Car;
import java.util.List;

public interface CarService {
	Car create(Car car);

	Car read(Long id);

	Car update(Car car);

	void delete(Long id);

	List<Car> getCars();
}
