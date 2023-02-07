package itmo.java.advanced_124_31.controllers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.java.advanced_124_31.model.entity.Car;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CarControllerTest {
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
			"target/generated-snippets");
	MockMvc mockMvc;
	@Autowired
	WebApplicationContext webApplicationContext;
	@Autowired
	ObjectMapper objectMapper;
	Car car = new Car();

	//{
	//	car.setId(2L);
	//	car.setVehicleYear(2010);
	//	car.setName("LADA");
	//	car.setColor(Color.BLACK);
	//	car.setStateNumber("A111AА111");
	//	car.setSeats(4);
	//}

	@Before
	public void setUp() {

		ConfigurableMockMvcBuilder builder =
				MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
						.alwaysDo(document(" {method-name}/{step}/"))
						.apply(documentationConfiguration(this.restDocumentation));
		this.mockMvc = builder.build();
	}

	@Test
	@SneakyThrows
	public void create() {

		String content = objectMapper.writeValueAsString(car);
		System.out.println(content);
		String uri = "/cars";
		mockMvc.perform(
						MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
								.content(content)).andDo(print()).andExpect(status().isCreated())
				.andDo(print()).andExpect(jsonPath("$.vehicleYear").value(2010))
				.andExpect(jsonPath("$.name").value("LADA"))
				.andExpect(jsonPath("$.stateNumber").value("A111AА111"))
				.andExpect(jsonPath("$.seats").value("4"))
				.andDo(document(uri.replace("/", "\\")));
	}
}
