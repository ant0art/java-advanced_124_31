package itmo.java.advanced_124_31.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itmo.java.advanced_124_31.model.dto.WorkShiftDTO;
import itmo.java.advanced_124_31.service.WorkShiftService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
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
@Tag(name = "Work shifts")
public class WorkShiftController {

	private final WorkShiftService workShiftService;

	/**
	 * Set a new workShift to database
	 *
	 * @param workShiftDTO new workShift to add
	 * @return a class object workShiftDTO if everything is well
	 */
	@PostMapping
	@Operation(summary = "Create a work shift")
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
	@Operation(summary = "Get a work shift")
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
	@Operation(summary = "Update a work shift")
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
	@Operation(summary = "Remove a work shift")
	public ResponseEntity<HttpStatus> delete(@RequestParam Long id) {
		workShiftService.delete(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Returns a list of all objects drivers in a limited size list sorted by
	 * chosen parameter
	 *
	 * @param page    serial number of page to show
	 * @param perPage elements on page
	 * @param sort    main parameter of sorting
	 * @param order   ASC or DESC
	 * @return ModelMap of sorted elements
	 * @see ModelMap
	 */
	@GetMapping("/all")
	@Operation(summary = "Get all work shifts")
	public List<WorkShiftDTO> getWorkShifts(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "1") Integer perPage,
			@RequestParam(required = false, defaultValue = "name") String sort,
			@RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
		return workShiftService.getWorkShifts(page, perPage, sort, order);
	}
}
