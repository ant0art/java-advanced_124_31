package itmo.java.advanced_124_31.model.entity;

import itmo.java.advanced_124_31.model.enums.Color;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cars")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car implements Serializable {

	static final long SerialVersionUID = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column
	String name;
	@Column
	Integer wheels;
	@Column
	@Enumerated(EnumType.STRING)
	Color color;
	@Column(name = "vehicle_year")
	Integer vehicleYear;

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT " +
			"CURRENT_TIMESTAMP", updatable = false)
	LocalDateTime createdAt;

	@Column(name = "updated_at")
	LocalDateTime updatedAt;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "driver_id")
	Driver driver;
}


