package itmo.java.advanced_124_31.model.repository;

import itmo.java.advanced_124_31.model.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, String> {
}
