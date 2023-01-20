package itmo.java.advanced_124_31.data;

import itmo.java.advanced_124_31.model.entity.Car;
import java.util.List;

public interface Carbase {
	Car find(Long id);

	void save(Car car);

	void delete(Long id);

	List<Car> getCars();
}
