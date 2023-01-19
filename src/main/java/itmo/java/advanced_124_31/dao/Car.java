package itmo.java.advanced_124_31.dao;

import itmo.java.advanced_124_31.data.Carbase;
import itmo.java.advanced_124_31.data.impl.CarBaseImpl;
import itmo.java.advanced_124_31.service.Color;
import java.io.Serializable;

public class Car implements Serializable {

	static final long SerialVersionUID = 1;

	private Long id;
	private String name;
	private Integer wheels;
	private Color color;

	private static Carbase carBase = new CarBaseImpl();

	public Car() {
	}

	public Car(Long id, String name, Integer wheels, Color color) {
		this.id = id;
		this.name = name;
		this.wheels = wheels;
		this.color = color;
		carBase.save(this);
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

	public Integer getWheels() {
		return wheels;
	}

	public void setWheels(Integer wheels) {
		this.wheels = wheels;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Car{" +
				"id=" + id +
				", name='" + name + '\'' +
				", wheels=" + wheels +
				", color=" + color +
				'}';
	}
}
