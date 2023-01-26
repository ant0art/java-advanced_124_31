package itmo.java.advanced_124_31.model.repository;

import itmo.java.advanced_124_31.model.entity.Driver;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

	@NonNull
	Optional<Driver> findById(@NonNull Long id);
}
