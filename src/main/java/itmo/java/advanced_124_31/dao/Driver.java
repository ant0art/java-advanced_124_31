package itmo.java.advanced_124_31.dao;

import java.io.Serializable;
import java.util.Arrays;

public class Driver implements Serializable {

	static final long SerialVersionUID = 1;

	private Long id;
	private String name;
	private String surname;
	private Car[] cars;

	public Driver() {
	}

	public Driver(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public Driver(Long id, String name, String surname, Car[] cars) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.cars = cars;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Car[] getCars() {
		return cars;
	}

	public void setCars(Car[] cars) {
		this.cars = cars;
	}

	@Override
	public String toString() {
		return "Driver{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", cars=" + Arrays.toString(cars) +
				'}';
	}
}
