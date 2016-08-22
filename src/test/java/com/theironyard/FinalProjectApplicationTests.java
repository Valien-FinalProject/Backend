package com.theironyard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.command.ChildCommand;
import com.theironyard.entities.Child;
import com.theironyard.entities.Parent;
import com.theironyard.services.*;
import com.theironyard.utilities.PasswordStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FinalProjectApplicationTests {

	@Autowired
	WebApplicationContext wac;

	@Autowired
	ParentRepository parents;

	@Autowired
	ChildRepository children;

	@Autowired
	ChoreRepository chores;

	@Autowired
	RewardRepository rewards;

	@Autowired
	AuthService authService;

	MockMvc mockMvc;

	@Before
	public void before(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void contextLoads() {
	}

	/***** POST & PUT Endpoints *****/

	/***************************
	 	Children Test
	 ***************************/

	/***** POST Endpoints *****/

//	@Test
//	public void getChildToken() throws Exception {
//		Parent parent = new Parent("TestName", "TestUsername", PasswordStorage.createHash("password"));
//		parents.save(parent);
//		authService.getParentFromAuth(parent.getToken());
//		Child child = new Child("TestName", "TestChildUsername", PasswordStorage.createHash("password"));
//		children.save(child);
//		ChildCommand command = new ChildCommand(child.getUsername(), child.getPassword());
//		authService.checkChildLogin(command);
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		String json = objectMapper.writeValueAsString(child);
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.post("/child/token").content(json).contentType("application/json").sessionAttr("child", child)
//		);
//
//	}



}
