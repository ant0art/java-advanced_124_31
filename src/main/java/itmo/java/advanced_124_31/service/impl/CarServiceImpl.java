package itmo.java.advanced_124_31.service.impl;

import itmo.java.advanced_124_31.dao.Car;
import itmo.java.advanced_124_31.data.Carbase;
import itmo.java.advanced_124_31.data.impl.CarBaseImpl;
import itmo.java.advanced_124_31.service.CarService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

	private static Carbase carBase = new CarBaseImpl();

	/**
	 * Create new car and save it to car database
	 *
	 * @param car new Car for database to add
	 * @return Car
	 */
	@Override
	public Car create(Car car) {
		carBase.save(car);
		return car;
	}

	/**
	 * Find a Car by it`s ID
	 *
	 * @param id id of Car
	 * @return Car
	 */
	@Override
	public Car read(Long id) {
		return carBase.find(id);
	}

	/**
	 * Update car in car database. It rewrites fields of current object Car or add new
	 * object Car if it isn't contained earlier
	 *
	 * @param car object to update or add to database
	 * @return Car
	 */
	@Override
	public Car update(Car car) {
		carBase.save(car);
		return car;
	}

	/**
	 * Delete object from car database by its ID
	 *
	 * @param id id of object to delete
	 */
	@Override
	public void delete(Long id) {
		carBase.delete(id);
	}

	@Override
	public List<Car> getCars() {
		return carBase.getCars();
	}
}
