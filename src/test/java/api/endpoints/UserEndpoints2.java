package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ResourceBundle;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
//To perform CRUD operation on User API
// takes url from properties file
public class UserEndpoints2 {
	
	//method to get url from properties file
	public static ResourceBundle getUrl() {
		ResourceBundle routes = ResourceBundle.getBundle("routes");//load the properties file
		return routes;		
	}
	
	public static Response createUser(User user) {
		
		String post_url = getUrl().getString("post_url");
		Response response = given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.body(user)
							.when()
								.post(post_url);
		return response;
	}
	
	public static Response readUser(String username) {
		Response response = given()
								.pathParam("username", username)
							.when()
								.get(getUrl().getString("get_url"));
		return response;
	}
	
	public static Response updateUser(String username, User user) {
		Response response = given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.pathParam("username", username)
								.body(user)
							.when()
								.put(getUrl().getString("update_url"));
		return response;
	}
	
	public static Response deleteUser(String username) {
		Response response = given()
								.pathParam("username", username)
							.when()
								.get(getUrl().getString("delete_url"));
		return response;
	}
}
