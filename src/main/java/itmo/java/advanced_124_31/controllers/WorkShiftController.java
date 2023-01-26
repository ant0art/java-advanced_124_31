package itmo.java.advanced_124_31.controllers;

import itmo.java.advanced_124_31.model.dto.WorkShiftDTO;
import itmo.java.advanced_124_31.service.WorkShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workshifts")
@RequiredArgsConstructor
public class WorkShiftController {

	private final WorkShiftService workShiftService;


	/**
	 * Set a new workShift to database
	 *
	 * @param workShiftDTO new workShift to add
	 * @return a class object workShiftDTO if everything is well
	 */
	@PostMapping
	public ResponseEntity<WorkShiftDTO> create(@RequestBody WorkShiftDTO workShiftDTO) {
		return ResponseEntity.ok(workShiftService.create(workShiftDTO));
	}

	/**
	 * Read the entity-WorkShift id and returns it DTO
	 *
	 * @param id ID of workShift in database
	 * @return a class object workShiftDTO
	 */
	@GetMapping()
	public ResponseEntity<WorkShiftDTO> read(@RequestParam Long id) {
		return ResponseEntity.ok(workShiftService.get(id));
	}

	/**
	 * Update fields of object by DTO
	 *
	 * @param id           ID of object to be updated
	 * @param workShiftDTO DTO object with definite fields to update
	 * @return DTO object as everything went right
	 */
	@PutMapping()
	public ResponseEntity<WorkShiftDTO> update(@RequestParam Long id,
			@RequestBody WorkShiftDTO workShiftDTO) {
		return ResponseEntity.ok(workShiftService.update(id, workShiftDTO));
	}

	/**
	 * Delete object by ID from DB
	 *
	 * @param id ID of object to delete
	 */
	@DeleteMapping()
	public ResponseEntity<HttpStatus> delete(@RequestParam Long id) {
		workShiftService.delete(id);
		return ResponseEntity.ok().build();
	}
}
