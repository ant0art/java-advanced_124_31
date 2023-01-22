package itmo.java.advanced_124_31.service;

public interface AdminService {

	/**
	 * Copy properties from one class object to another, excluding every Null-value
	 * field. Both classes fields must have the same fields name.
	 *
	 * @param source source object contains vals to write
	 * @param target target object, that should be rewritten
	 */
	void copyPropertiesIgnoreNull(Object source, Object target);
}
