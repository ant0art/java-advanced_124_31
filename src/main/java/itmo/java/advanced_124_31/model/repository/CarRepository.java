package itmo.java.advanced_124_31.model.repository;

import itmo.java.advanced_124_31.model.entity.Car;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	@NonNull
	Optional<Car> findById(@NonNull Long id);
}
