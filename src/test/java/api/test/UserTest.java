package api.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.payload.User;
import api.utilities.Dataproviders;
import api.utilities.XLUtility;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserTest {
	
	Faker faker;
	User user;
	org.apache.logging.log4j.Logger log=LogManager.getLogger(UserTest.class);

	@BeforeClass
	public void setUpData(){
		log=LogManager.getLogger(UserTest.class);
		log.info("Preparing test data using faker");
		faker=new Faker();
		user = new User();
		user.setId(faker.idNumber().hashCode());
		user.setUsername(faker.name().username());
		user.setFirstname(faker.name().firstName());
		user.setLastname(faker.name().lastName());
		user.setEmail(faker.internet().emailAddress());
		user.setPassword(faker.internet().password(5, 10));
		user.setPhone(faker.phoneNumber().cellPhone());		
	}
	
	@Test(description = "TC_001",priority = 1,enabled = false)
	public void testPostUser() {
		log.info("Inside create user test");
		
		Response response = UserEndpoints.createUser(user);
		//response.then().log().all();
		
		//status code
		Assert.assertEquals(response.getStatusCode(), 200);
		//response jsonschema validate
		response.then().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir")+"/src/test/resources/schemas/user/createuserschema.json")));
		//response body validation
		JsonPath jsonPath = response.getBody().jsonPath();
		int code = jsonPath.get("code");
		Assert.assertEquals(code, 200);
		Assert.assertEquals(response.getBody().jsonPath().get("type").toString(), "unknown");
		String message = jsonPath.get("message");
		Assert.assertTrue(message.matches("[0-9]+"), "Expected and Actual message are not same");
		//response header
		//response.then().log().headers();
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
	}
	
	@Test(description = "TC_002",priority = 2,enabled = false)
	public void testGetUserByName() {
		log.info("Inside get user");
		Response response = UserEndpoints.readUser(this.user.getUsername());
//		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		response.then().log().body();
		response.then().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir")+"/src/test/resources/schemas/user/getuserschema.json")));
		//validate response data
		JsonPath jsonPath = response.getBody().jsonPath();
		int id = jsonPath.get("id");
		Assert.assertEquals(id, user.getId());
		Assert.assertEquals(jsonPath.get("username"), user.getUsername());
//		Assert.assertEquals(jsonPath.get("firstName"), user.getFirstname());
//		Assert.assertEquals(jsonPath.get("lastName"), user.getLastname());
		Assert.assertEquals(jsonPath.get("email"), user.getEmail());
		Assert.assertEquals(jsonPath.get("password"), user.getPassword());
		Assert.assertEquals(jsonPath.get("phone"), user.getPhone());
		int userStatus = jsonPath.get("userStatus");
		Assert.assertEquals(userStatus, user.getUserStatus());
	}
	
	@Test(description = "TC_003",priority = 3,enabled = false)
	public void testUpdateUserByName() {
		user.setFirstname(faker.name().firstName());
		user.setLastname(faker.name().lastName());
		user.setEmail(faker.internet().emailAddress());
		
		Response response = UserEndpoints.updateUser(this.user.getUsername(),user);
//		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		JsonPath jsonPath = response.getBody().jsonPath();
		int code = jsonPath.get("code");
		Assert.assertEquals(code, 200);
		Assert.assertEquals(jsonPath.get("type"), "unknown");
		//check data after update
		testGetUserByName();
	}
	
	@Test(description = "TC_004",priority = 4,enabled = false)
	public void testdeleteUserByName() throws InterruptedException {
		Response response = UserEndpoints.deleteUser(this.user.getUsername());
		response.then().log().body();
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBody().jsonPath().get("type"), "unknown");
		Assert.assertEquals(response.getBody().jsonPath().get("message"), this.user.getUsername());
		Thread.sleep(2000);
		//after delete
		Response getresponse = UserEndpoints.readUser(this.user.getUsername());
		Assert.assertEquals(getresponse.getStatusCode(), 404);
		Assert.assertEquals(getresponse.getBody().jsonPath().get("type"), "error");
		Assert.assertEquals(getresponse.getBody().jsonPath().get("message"), "User not found");
	}
	
	@Test(description = "TC_005",dataProvider = "Data", dataProviderClass = Dataproviders.class, priority = 1,enabled = false)
	public void testCrudUser(String userID, String userName, String fname, String lName, String email, String pwd,
			String ph) throws InterruptedException {
		
		user = new User();
		user.setId(Integer.parseInt(userID));
		user.setUsername(userName);
		user.setFirstname(fname);
		user.setLastname(lName);
		user.setEmail(email);
		user.setPassword(pwd);
		user.setPhone(ph);
		
		testPostUser();
		testGetUserByName();
		testUpdateUserByName();
		testdeleteUserByName();
	}
	@Test
	public void createUserWithList() throws IOException {
		
		List<User> userList = new ArrayList<>();
		String[][] allData = new Dataproviders().getAllUserData();
			for(int row=0;row<allData.length;row++) {
				user = new User();
				int col=0;
				user.setId(Integer.valueOf(allData[row][col++]));
				user.setUsername(allData[row][col++]);
				user.setFirstname(allData[row][col++]);
				user.setLastname(allData[row][col++]);
				user.setEmail(allData[row][col++]);
				user.setPassword(allData[row][col++]);
				user.setPhone(allData[row][col++]);
				userList.add(user);
			}
			
		Response response = UserEndpoints.createUserWithList(userList);
		//response.then().log().body();		
		//validate response
		JsonPath jsonPath = response.getBody().jsonPath();
		int code = jsonPath.get("code");
		Assert.assertEquals(code, 200);
		Assert.assertEquals(jsonPath.get("type"), "unknown");
		Assert.assertEquals(jsonPath.get("message"), "ok");
		
		//we compare the data by calling get api
		for(User u:userList) {
			user=u;
			testGetUserByName();
		}
		
	}
	


}
