package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.dto.CarDTO;
import java.util.List;

public interface CarService {

	CarDTO create(CarDTO carDTO);

	CarDTO read(Long id);

	CarDTO update(Long id, CarDTO carDTO);

	void delete(Long id);

	List<CarDTO> getCars();

	void addTo(Long idCar, Long idDriver);

	void removeDriverFromCar(Long idCar);
}
