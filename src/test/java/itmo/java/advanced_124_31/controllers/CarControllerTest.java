package itmo.java.advanced_124_31.controllers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.entity.Car;
import itmo.java.advanced_124_31.model.enums.CarClass;
import itmo.java.advanced_124_31.model.enums.CarStatus;
import itmo.java.advanced_124_31.model.enums.Color;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
public class CarControllerTest {
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
			"target/generated-snippets");
	Car car = new Car();
	//@Autowired
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
		car.setStateNumber("A111AА111");
		car.setSeats(4);
		car.setBabyChair(false);
		car.setCarClass(CarClass.COMFORT);
		car.setStatus(CarStatus.CREATED);
		//car.setCreatedAt(LocalDateTime.now());
		//car.setUpdatedAt(LocalDateTime.now());
	}

	@Before
	public void setUp() {

		ConfigurableMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(
						this.webApplicationContext).alwaysDo(document(" {method-name}/{step}/"))
				.apply(documentationConfiguration(this.restDocumentation));
		this.mockMvc = builder.build();
	}

	@Test
	@Transactional
	//@SneakyThrows
	public void create() throws Exception {

		String content = objectMapper.writeValueAsString(car);
		System.out.println(content);
		String uri = "/cars";

		mockMvc.perform(
						post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
				.andDo(print()).andExpect(status().isCreated()).andDo(print())
				//.andExpect(status().isCreated()).andDo(print())
				.andExpect(jsonPath("$.vehicleYear").value(2010))
				.andExpect(jsonPath("$.name").value("LADA"))
				.andExpect(jsonPath("$.stateNumber").value("A111AА111"))
				.andExpect(jsonPath("$.seats").value("4"))
				.andDo(document(uri.replace("/", "\\")));
	}
}
