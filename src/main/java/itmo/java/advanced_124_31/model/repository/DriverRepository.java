package itmo.java.advanced_124_31.model.repository;

import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.entity.Driver;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
	@Transactional
	@Modifying
	@Query("update Driver d set d.cars = ?1 where d.id = ?2")
	int updateCarsById(Car cars, Long id);

	@NonNull
	Optional <Driver> findById(@NonNull Long id);
}
