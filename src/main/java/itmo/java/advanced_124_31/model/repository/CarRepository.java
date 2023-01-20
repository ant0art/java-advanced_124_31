package itmo.java.advanced_124_31.model.repository;

import itmo.java.advanced_124_31.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository {
	Car find(Long id);

	void save(Car car);

	void delete(Long id);

	List<Car> getCars();
}
