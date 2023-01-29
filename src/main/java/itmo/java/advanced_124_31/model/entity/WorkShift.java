package itmo.java.advanced_124_31.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import itmo.java.advanced_124_31.model.enums.WorkShiftGrade;
import itmo.java.advanced_124_31.model.enums.WorkShiftStatus;
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
@Table(name = "work_shifts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkShift implements Serializable {

	static final long SerialVersionUID = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JsonBackReference(value = "driver_workshifts")
	Driver driver;

	@ManyToOne(cascade = CascadeType.ALL)
	@JsonBackReference(value = "car_workshifts")
	Car car;

	@Column(name = "grade")
	WorkShiftGrade grade;

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	LocalDateTime createdAt;

	@Column(name = "updated_at")
	LocalDateTime updatedAt;

	@Column(name = "closed_at")
	LocalDateTime closedAt;

	@Enumerated(EnumType.STRING)
	WorkShiftStatus status;
}
