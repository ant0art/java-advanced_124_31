package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.dto.CarDTOResponse;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;

public interface CarService {

	CarDTORequest create(CarDTORequest carDTORequest);

	CarDTORequest get(Long id);

	CarDTORequest update(Long id, CarDTORequest carDTORequest);

	void delete(Long id);

	ModelMap getCars(Integer page, Integer perPage, String sort, Sort.Direction order);

	CarDTOResponse addTo(Long idCar, Long idDriver);

	CarDTORequest removeDriverFromCar(Long id);
}
