package itmo.java.advanced_124_31.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import itmo.java.advanced_124_31.model.enums.CarClass;
import itmo.java.advanced_124_31.model.enums.CarStatus;
import itmo.java.advanced_124_31.model.enums.Color;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "cars")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Car implements Serializable {

	static final long SerialVersionUID = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column
	String name;

	@Column
	Color color;

	@Enumerated
	@Column(name = "car_class")
	CarClass carClass;

	@Column(name = "vehicle_year")
	Integer vehicleYear;

	@Column(name = "state_number", nullable = false, unique = true, length = 9)
	String stateNumber;

	@Column(name = "baby_chair", columnDefinition = "false")
	Boolean babyChair;

	@Column(columnDefinition = "4")
	Integer seats;

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	LocalDateTime createdAt;

	@Column(name = "updated_at")
	LocalDateTime updatedAt;

	@ManyToOne(cascade = CascadeType.ALL)
	@JsonBackReference(value = "driver_cars")
	Driver driver;

	@Enumerated(EnumType.STRING)
	CarStatus status;
}


