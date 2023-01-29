package itmo.java.advanced_124_31.service;

import itmo.java.advanced_124_31.model.dto.CarDTORequest;
import itmo.java.advanced_124_31.model.dto.CarDTOResponse;
import java.util.List;

public interface CarService {

	CarDTORequest create(CarDTORequest carDTORequest);

	CarDTORequest get(Long id);

	CarDTORequest update(Long id, CarDTORequest carDTORequest);

	void delete(Long id);

	List<CarDTORequest> getCars();

	CarDTOResponse addTo(Long idCar, Long idDriver);

	CarDTORequest removeDriverFromCar(Long id);

}
