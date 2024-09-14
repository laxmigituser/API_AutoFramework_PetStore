package api.endpoints;

import static io.restassured.RestAssured.given;

import java.util.ResourceBundle;

import api.payload.Pet;
import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PetEndpoints {

	public static ResourceBundle getUrl() {
		ResourceBundle routes = ResourceBundle.getBundle("routes");// load the routes.properties file
		return routes;
	}

	public static Response createPet(Pet pet) {
		String post_url = getUrl().getString("create_pet");
		Response response = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(pet).when()
				.post(post_url);
		return response;
	}
	public static Response getPet(int id) {
		String get_url = getUrl().getString("get_pet");
		Response response = given().pathParam("id", id)
				.when()
				.post(get_url);
		return response;
	}
	public static Response updatePet(Pet pet) {
		String put_url = getUrl().getString("update_pet");
		Response response = given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.body(pet)
							.when()
								.put(put_url);
		return response;
	}
	public static Response deletePet(int id) {
		String delete_url = getUrl().getString("delete_pet");
		Response response = given()
								.pathParam("id", id)
								.accept(ContentType.JSON)
								.header("api_key","api_key")
							.when()
								.delete(delete_url);
		return response;
	}
}
