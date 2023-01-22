package itmo.java.advanced_124_31.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.entity.Car;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

public class Test {
	public static void main(String[] args)
			throws NoSuchFieldException, NoSuchMethodException {
		Car car = new Car();
		car.setName("Toy");
		car.setId(1L);
		Car car2 = new Car();
		car2.setWheels(4);
		car2.setName("KIA");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		//Car car3 = mapper.convertValue(car, Car.class);
		//System.out.println(car3);
		Car car3 = new Car();

		System.out.println("Car1: " + car);
		System.out.println("Car2: " + car2);
		car3.setId(ObjectUtils.defaultIfNull(car2.getId(), car.getId()));
		car3.setColor(ObjectUtils.defaultIfNull(car2.getColor(), car.getColor()));
		car3.setVehicleYear(
				ObjectUtils.defaultIfNull(car2.getVehicleYear(), car.getVehicleYear()));
		car3.setWheels(ObjectUtils.defaultIfNull(car2.getWheels(), car.getWheels()));
		car3.setName(ObjectUtils.defaultIfNull(car2.getName(), car.getName()));
		System.out.println("Car3: " + car3);

		copyPropertiesIgnoreNull(car2, car);
		System.out.println("Car1: " + car);


	}

	public static void copyPropertiesIgnoreNull(Object source, Object target) {
		BeanWrapper src = new BeanWrapperImpl(source);
		BeanWrapper trg = new BeanWrapperImpl(target);

		for (PropertyDescriptor descriptor : src.getPropertyDescriptors()) {
			String propertyName = descriptor.getName();
			if (propertyName.equals("class")) {
				continue;
			}
			Object propertyValue = src.getPropertyValue(propertyName);
			if (propertyValue != null) {
				trg.setPropertyValue(propertyName, propertyValue);
			}
		}
	}
}
