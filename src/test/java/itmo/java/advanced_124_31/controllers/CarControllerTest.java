package itmo.java.advanced_124_31.controllers;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.enums.CarClass;
import itmo.java.advanced_124_31.model.enums.CarStatus;
import itmo.java.advanced_124_31.model.enums.Color;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CarControllerTest {
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
			"target/generated-snippets");
	Car car = new Car();
	MockMvc mockMvc;
	@Autowired
	WebApplicationContext webApplicationContext;
	@Autowired
	ObjectMapper objectMapper;

	{
		car.setId(1L);
		car.setVehicleYear(2010);
		car.setName("LADA");
		car.setColor(Color.BLACK);
		car.setStateNumber("А123АА178");
		car.setSeats(4);
		car.setBabyChair(false);
		car.setCarClass(CarClass.COMFORT);
		car.setStatus(CarStatus.CREATED);
	}

	@Before
	public void setUp() {

		ConfigurableMockMvcBuilder<DefaultMockMvcBuilder> builder
				= MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
				.alwaysDo(document(" {method-name}/{step}/"))
				.apply(documentationConfiguration(this.restDocumentation));
		this.mockMvc = builder.build();
	}

	@Test
	@Transactional
	@SneakyThrows
	public void create() {

		String content = objectMapper.writeValueAsString(car);
		System.out.println(content);
		String uri = "/cars";

		mockMvc.perform(
						post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
				.andDo(print()).andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.vehicleYear").value(2010))
				.andExpect(jsonPath("$.name").value("LADA"))
				.andExpect(jsonPath("$.stateNumber").value("А123АА178"))
				.andExpect(jsonPath("$.seats").value("4"))
				.andDo(document(uri.replace("/", "\\")));
	}

	@Test
	@Transactional
	@SneakyThrows
	public void read() {
		String uri = "/cars";

		mockMvc.perform(get(uri).param("id", String.valueOf(1L))).andDo(print())
				.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.name").value("BMW"));
	}

	@Test
	@Transactional
	@SneakyThrows
	public void update() {
		String uri = "/cars";
		String content = objectMapper.writeValueAsString(car);
		System.out.println(content);

		mockMvc.perform(put(uri).param("id", String.valueOf(1L))
						.contentType(MediaType.APPLICATION_JSON).content(content)).andDo(print())
				.andDo(print()).andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.vehicleYear").value(2010))
				.andExpect(jsonPath("$.name").value("LADA"))
				.andExpect(jsonPath("$.stateNumber").value("А123АА178"))
				.andExpect(jsonPath("$.seats").value("4"))
				.andDo(document(uri.replace("/", "\\")));
	}

	@Test
	@Transactional
	@SneakyThrows
	public void delete() {
		String uri = "/cars";

		mockMvc.perform(RestDocumentationRequestBuilders.delete(uri)
						.param("id", String.valueOf(1L))).andDo(print())
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	@Transactional
	@SneakyThrows
	public void addTo() {
		String uri = "/cars/addToDriver";

		mockMvc.perform(put(uri).param("idDriver", String.valueOf(1L))
						.param("idCar", String.valueOf(1L))).andDo(print())
				.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.name").value("BMW"))
				.andExpect(jsonPath("$.color").value("WHITE"))
				.andExpect(jsonPath("$.carClass").value("BUSINESS"))
				.andExpect(jsonPath("$.vehicleYear").value(2012))
				.andExpect(jsonPath("$.stateNumber").value("А565АА498"))
				.andExpect(jsonPath("$.babyChair").value(false))
				.andExpect(jsonPath("$.seats").value(2))
				.andExpect(jsonPath("$.driverDTORequest.name").value("Petr"))
				.andDo(document(uri.replace("/", "\\")));
	}

	@Test
	@Transactional
	@SneakyThrows
	public void removeFromDriver() {
		String uri = "/cars/removeFromDriver";

		mockMvc.perform(put(uri).param("id", String.valueOf(2L))).andDo(print())
				.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.name").value("TOYOTA"))
				.andExpect(jsonPath("$.color").value("RED"))
				.andExpect(jsonPath("$.carClass").value("BUSINESS"))
				.andExpect(jsonPath("$.vehicleYear").value(1997))
				.andExpect(jsonPath("$.stateNumber").value("А551АА235"))
				.andExpect(jsonPath("$.babyChair").value(true))
				.andExpect(jsonPath("$.seats").value(4))
				.andDo(document(uri.replace("/", "\\")));
	}

	@Test
	@Transactional
	@SneakyThrows
	public void getCars() {
		String uri = "/cars/all";

		mockMvc.perform(get(uri).param("page", String.valueOf(0))
						.param("perPage", String.valueOf(10)).param("sort", "name")
						.param("order", "DESC")).andDo(print()).andExpect(status().isOk())
				.andDo(print()).andExpect(ResultMatcher.matchAll(status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON))).andExpect(
						jsonPath("$..name", hasItem(CoreMatchers.containsString("BMW"))))
				.andExpect(jsonPath("$.totalPages").value(1))
				.andExpect(jsonPath("$.[*]", hasSize(4)))
				.andExpect(jsonPath("$.content.[*]", hasSize(2)))
				.andDo(document(uri.replace("/", "\\")));
	}
}
