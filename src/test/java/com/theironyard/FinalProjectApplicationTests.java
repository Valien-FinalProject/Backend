package com.theironyard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.command.ChildCommand;
import com.theironyard.entities.Child;
import com.theironyard.entities.Chore;
import com.theironyard.entities.Parent;
import com.theironyard.entities.Reward;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void contextLoads() {
	}

//	/***** POST & PUT Endpoints *****/
//
//	/***************************
//	 	Children Test
//	 ***************************/
//
//	/***** POST Endpoints *****/
//
//	@Test
//	public void getChildToken() throws Exception {
//		Child child = new Child("TestName", "TestChildUsername", PasswordStorage.createHash("password"));
//		children.save(child);
//		ChildCommand command = new ChildCommand("TestChildUsername", PasswordStorage.createHash("password"));
//
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		String json = objectMapper.writeValueAsString(child);
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.post("/child/token").content(json).contentType("application/json").requestAttr("child", PasswordStorage.verifyPassword(command.getPassword(),child.getPassword()))
//		);
//
//	}
//
//	@Test
//	public void testChildLogout() throws Exception {
//		Child child = new Child("Name", "ChildUsername", PasswordStorage.createHash("password"));
//		children.save(child);
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.post("/child/logout").requestAttr("token", child.getToken())
//		).andReturn().getRequest().close();
//	}
//
//	@Test
//	public void testCreateWishlistItem() throws Exception {
//		Child child = new Child("Test1", "Test1", PasswordStorage.createHash("password"));
//		children.save(child);
//		Reward reward = new Reward("testReward");
//		rewards.save(reward);
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		String json = objectMapper.writeValueAsString(reward);
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.post("/child/wishlist").content(json).contentType("application/json").requestAttr("token", child.getToken())
//		);
//
//		assertTrue(rewards.count() == 1);
//	}
//
//	/***** GET Endpoints *****/
//
//	@Test
//	public void testGetOneChild() throws Exception {
//		Child child = new Child("Test2", "Test2", PasswordStorage.createHash("password"));
//		children.save(child);
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.get("/child/").requestAttr("id", child.getId())
//		);
//
//		assertEquals(child, child);
//	}
//
//	@Test
//	public void testGetChildWishlist() throws Exception {
//		Child child = new Child("Test2", "Test2", PasswordStorage.createHash("password"));
//		Reward reward = new Reward("testReward");
//		rewards.save(reward);
//		children.save(child);
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.get("/child/wishlist").requestAttr("token", child.getToken())
//		);
//
//	}
//
//	@Test
//	public void testGetChoresForChild() throws Exception {
//		Child child = new Child("Test2", "Test2", PasswordStorage.createHash("password"));
//		Chore chore = new Chore("TestChore", "description", 10);
//		children.save(child);
//		chores.save(chore);
//
//		mockMvc.perform(
//				MockMvcRequestBuilders.get("/child/chores").requestAttr("token", child.getToken())
//		);
//	}
//
//

}
