package itmo.java.advanced_124_31.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import itmo.java.advanced_124_31.model.enums.DriverOrderStatus;
import itmo.java.advanced_124_31.model.enums.DriverStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "drivers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Driver implements Serializable {

	static final long SerialVersionUID = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column
	String name;

	@Column
	String surname;

	@Column(unique = true, nullable = false, name = "phone_number")
	String phoneNumber;

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate birthday;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference(value = "driver_license")
	DriverLicense license;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonManagedReference(value = "driver_workshifts")
	List<WorkShift> workShifts;

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	LocalDateTime createdAt;

	@Column(name = "updated_at")
	LocalDateTime updatedAt;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonManagedReference(value = "driver_cars")
	List<Car> cars;

	@Enumerated(EnumType.STRING)
	DriverStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "driver_order_status")
	DriverOrderStatus driverOrderStatus;

}
