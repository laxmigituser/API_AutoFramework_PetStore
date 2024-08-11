package api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTest {
	
	Faker faker;
	User user;
	
	@BeforeClass
	public void setUpData(){
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
	
	@Test(priority = 1)
	public void testPostUser() {
		Response response = UserEndpoints2.createUser(user);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority = 2)
	public void testGetUserByName() {
		Response response = UserEndpoints2.readUser(this.user.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority = 3)
	public void testUpdateUserByName() {
		user.setFirstname(faker.name().firstName());
		user.setLastname(faker.name().lastName());
		user.setEmail(faker.internet().emailAddress());
		
		Response response = UserEndpoints2.updateUser(this.user.getUsername(),user);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//check data after update
		testGetUserByName();
	}
	
	@Test(priority = 4)
	public void testdeleteUserByName() {
		Response response = UserEndpoints2.deleteUser(this.user.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		//after delete
		Response getresponse = UserEndpoints2.readUser(this.user.getUsername());
//		response.then().log().all();
		Assert.assertEquals(getresponse.getStatusCode(), 200);
	}

}
