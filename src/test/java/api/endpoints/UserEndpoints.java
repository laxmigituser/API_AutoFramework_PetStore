package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
//To perform CRUD operation on User API
public class UserEndpoints {
	
	public static Response createUser(User user) {
		Response response = given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.body(user)
							.when()
								.post(Routes.post_url);
		return response;
	}
	
	public static Response readUser(String username) {
		Response response = given()
								.pathParam("username", username)
							.when()
								.get(Routes.get_url);
		return response;
	}
	
	public static Response updateUser(String username, User user) {
		Response response = given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.pathParam("username", username)
								.body(user)
							.when()
								.put(Routes.update_url);
		return response;
	}
	
	public static Response deleteUser(String username) {
		Response response = given()
								.pathParam("username", username)
							.when()
								.get(Routes.delete_url);
		return response;
	}
}
