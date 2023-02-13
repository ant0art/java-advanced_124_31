package itmo.java.advanced_124_31.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.dto.WorkShiftDTO;
import itmo.java.advanced_124_31.model.entity.WorkShift;
import itmo.java.advanced_124_31.model.enums.WorkShiftGrade;
import itmo.java.advanced_124_31.model.enums.WorkShiftStatus;
import itmo.java.advanced_124_31.model.exceptions.CustomException;
import itmo.java.advanced_124_31.model.repository.WorkShiftRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)
public class WorkShiftServiceImplTest {

	@Spy
	ObjectMapper mapper;
	@InjectMocks
	private WorkShiftServiceImpl workShiftService;
	@Mock
	private WorkShiftRepository workShiftRepository;

	@Test
	public void create() {
		WorkShiftDTO workShiftDTO = new WorkShiftDTO();
		workShiftDTO.setGrade(WorkShiftGrade.COMFORT);
		when(workShiftRepository.save(any(WorkShift.class))).thenAnswer(
				i -> i.getArguments()[0]);
		WorkShiftDTO result = workShiftService.create(workShiftDTO);
		assertEquals(workShiftDTO.getGrade(), result.getGrade());
	}

	@Test
	public void get() {
		WorkShift workShift = new WorkShift();
		workShift.setGrade(WorkShiftGrade.COMFORT);
		when(workShiftRepository.findById(anyLong())).thenReturn(Optional.of(workShift));
		WorkShiftDTO result = workShiftService.get(1L);
		assertEquals(workShift.getGrade(), result.getGrade());
	}

	@Test
	public void update() {
		WorkShift workShift = new WorkShift();
		workShift.setGrade(WorkShiftGrade.BUSINESS);
		WorkShiftDTO workShiftDTO = new WorkShiftDTO();
		workShiftDTO.setGrade(WorkShiftGrade.PREMIUM);
		when(workShiftRepository.findById(anyLong())).thenReturn(Optional.of(workShift));
		when(workShiftRepository.save(workShift)).thenAnswer(i -> i.getArguments()[0]);
		WorkShiftDTO result = workShiftService.update(1L, workShiftDTO);
		assertEquals(workShift.getGrade(), result.getGrade());
	}

	@Test(expected = CustomException.class)
	public void update_notFound() {
		WorkShiftDTO workShiftDTO = new WorkShiftDTO();
		workShiftService.update(1L, workShiftDTO);
	}

	@Test
	public void delete() {
		WorkShift workShift = new WorkShift();
		when(workShiftRepository.findById(anyLong())).thenReturn(Optional.of(workShift));
		when(workShiftRepository.save(workShift)).thenReturn(workShift);
		workShiftService.delete(1L);
		verify(workShiftRepository, times(1)).save(workShift);
		assertEquals(WorkShiftStatus.DELETED, workShift.getStatus());
	}

	@Test
	public void getWorkShifts() {
		Integer page = 2;
		Integer perPage = 10;
		String sort = "name";
		Sort.Direction order = Sort.Direction.DESC;

		WorkShift workShift = new WorkShift();
		workShift.setGrade(WorkShiftGrade.COMFORT);
		List<WorkShift> list = Collections.singletonList(workShift);
		@SuppressWarnings("unchecked") Page<WorkShift> pageResult = mock(Page.class);

		when(workShiftRepository.findAll(any(Pageable.class))).thenReturn(pageResult);
		when(pageResult.getContent()).thenReturn(list);
		List<WorkShiftDTO> result = workShiftService.getWorkShifts(page, perPage, sort,
				order);
		assertEquals(workShift.getGrade(), result.get(0).getGrade());
	}

	@Test
	public void getWorkShift() {
		WorkShift workShift = new WorkShift();
		workShift.setGrade(WorkShiftGrade.BUSINESS);
		when(workShiftRepository.findById(anyLong())).thenReturn(Optional.of(workShift));
		WorkShift result = workShiftService.getWorkShift(1L);
		assertEquals(workShift.getGrade(), result.getGrade());
	}

	@Test(expected = CustomException.class)
	public void getWorkShift_notFound() {
		workShiftService.getWorkShift(1L);
	}

	@Test
	public void updateStatus() {
		WorkShift workShift = new WorkShift();
		workShiftService.updateStatus(workShift, WorkShiftStatus.UPDATED);
		assertEquals(WorkShiftStatus.UPDATED, workShift.getStatus());
	}
}
