package itmo.java.advanced_124_31.service.impl;

import itmo.java.advanced_124_31.service.AdminService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

public class AdminServiceImpl implements AdminService {

	/**
	 * Copy properties from one class object to another, excluding every Null-value
	 * field. Both classes fields must have the same fields name.
	 *
	 * @param source source object contains vals to write
	 * @param target target object, that should be rewritten
	 */
	@Override
	public void copyPropertiesIgnoreNull(Object source, Object target) {
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
