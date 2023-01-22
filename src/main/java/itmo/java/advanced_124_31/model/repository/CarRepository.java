package itmo.java.advanced_124_31.model.repository;

import itmo.java.advanced_124_31.model.entity.Car;
import java.util.Optional;
import itmo.java.advanced_124_31.model.entity.Driver;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
	@Transactional
	@Modifying
	@Query("update Car c set c.driver = ?1 where c.id = ?2")
	void updateDriverById(Driver driver, Long id);

	@NonNull
	Optional<Car> findById(@NonNull Long id);
}
