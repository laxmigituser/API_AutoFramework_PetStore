package api.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.payload.User;
import api.utilities.Dataproviders;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class DatadrivenUserTest {

	User user;

	@Test(dataProvider = "Data", dataProviderClass = Dataproviders.class, priority = 1,enabled = false)
	public void testPostUser(String userID, String userName, String fname, String lName, String email, String pwd,
			String ph) {
		
		user = new User();
		user.setId(Integer.parseInt(userID));
		user.setUsername(userName);
		user.setFirstname(fname);
		user.setLastname(lName);
		user.setEmail(email);
		user.setPassword(pwd);
		user.setPhone(ph);
		
		Response response = UserEndpoints.createUser(user);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority = 2, dataProvider = "UserNames",dataProviderClass = Dataproviders.class,enabled = false)
	public void testDeleteuserByName(String userName) {
		Response response = UserEndpoints.deleteUser(userName);
		response.then().log().all();
		Map<String, String> cookies = response.getCookies();
		Headers headers = response.getHeaders();
		String contentType = response.getContentType();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	
}
