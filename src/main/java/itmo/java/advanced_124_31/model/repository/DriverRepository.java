package itmo.java.advanced_124_31.model.repository;

import itmo.java.advanced_124_31.model.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DriverRepository extends JpaRepository {
	Driver find(Long id);

	void save(Driver driver);

	void delete(Long id);

	List<Driver> getDrivers();
}
