package itmo.java.advanced_124_31.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import itmo.java.advanced_124_31.model.enums.DriverLicenseCategory;
import itmo.java.advanced_124_31.model.enums.DriverLicenseStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "driver_licenses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverLicense implements Serializable {

	static final long SerialVersionUID = 1;

	@Id
	@Column(name = "id", nullable = false, unique = true)
	String id;

	@ElementCollection(targetClass = DriverLicenseCategory.class)
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	List<DriverLicenseCategory> categories;

	@Column(name = "received_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate receivedAt;

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	LocalDateTime createdAt;

	@Column(name = "updated_at")
	LocalDateTime updatedAt;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonBackReference(value = "driver_license")
	Driver driver;

	@Enumerated(EnumType.STRING)
	DriverLicenseStatus status;
}
