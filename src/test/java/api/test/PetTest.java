package api.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.PetEndpoints;
import api.endpoints.UserEndpoints;
import api.payload.Pet;
import api.payload.PetCategory;
import api.payload.PetTags;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PetTest {
	
	Faker faker;
	Pet pet; 
	Logger log = LogManager.getLogger(UserTest.class);

	@BeforeClass
	public void setUpData() {
		log = LogManager.getLogger(UserTest.class);
		log.info("Preparing test data using faker");
		faker = new Faker();
		pet = new Pet();
		pet.setId(faker.idNumber().hashCode());
		PetCategory petCategory=new PetCategory();
		petCategory.setId(0);
		petCategory.setName(faker.name().name());
		pet.setCategory(petCategory);
		pet.setName(faker.name().name());
		//generate fake urls
		pet.setPhotoUrls(Arrays.asList(faker.internet().url(),faker.internet().url()));
		List<PetTags> petTags = new ArrayList<>();
		PetTags petTag01 = new PetTags();
		petTag01.setId(faker.idNumber().hashCode());
		petTag01.setName(faker.name().name());
		petTags.add(petTag01);
		PetTags petTag02 = new PetTags();
		petTag02.setId(faker.idNumber().hashCode());
		petTag02.setName(faker.name().name());
		petTags.add(petTag02);
		pet.setStatus("available");
	}
	
	@Test(description = "TC_001",priority = 1,enabled = false)
	public void testCreatePet() {
		log.info("Inside create pet test");
		Response response = PetEndpoints.createPet(pet);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		response.then().log().body();
		//response.then().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir")+"/src/test/resources/schemas/pet/createpetschema.json")));
		//response body validation
		JsonPath jsonPath = response.getBody().jsonPath();
		int id = jsonPath.get("id");
		Assert.assertEquals(id, pet.getId());	
		Assert.assertEquals(response.getBody().jsonPath().get("name").toString(), pet.getName());
		Assert.assertEquals(response.getBody().jsonPath().get("status").toString(), pet.getStatus());
//		//url list validate
//		Object object = response.getBody().jsonPath().get("photoUrls");
//		Assert.assertEquals(response.getBody().jsonPath().get("photoUrls").toString(), pet.getPhotoUrls());
		JSONObject jsonObject =new JSONObject(response.asString());
		JSONObject category_obj = jsonObject.getJSONObject("category");
		Assert.assertEquals(category_obj.get("id"), pet.getCategory().getId());
		Assert.assertEquals(category_obj.get("name"), pet.getCategory().getName());
		//validate tags array
		JSONArray tagsArray = jsonObject.getJSONArray("tags");
		for(int i=0;i<tagsArray.length();i++) {
			 JSONObject tagObject = tagsArray.getJSONObject(i);
			Assert.assertEquals(tagObject.get("id"), pet.getTags().get(i).getId());
			Assert.assertEquals(tagObject.get("name"), pet.getTags().get(i).getName());

		}
		
		
	}
	
	@Test(alwaysRun = true,priority = 1)
	public void getPetTest() {
		PetEndpoints.createPet(pet);
		Response response=PetEndpoints.getPet(pet.getId());
		//response.then().log().all();
	}
	@Test(priority = 2)
	public void PutPetTest() {
		pet.setName("updated");
		Response response = PetEndpoints.updatePet(pet);
		//response.then().log().body();
		JsonPath jsonPath = response.getBody().jsonPath();
		String name = jsonPath.get("name").toString();
		Assert.assertEquals(name, "updated");
//		Response getResponse = PetEndpoints.getPet(pet.getId());
//		Assert.assertEquals(getResponse.jsonPath().get("name"), "updated");
	}
	@Test(priority = 3)
	public void DeletePetTest() {
		Response response = PetEndpoints.deletePet(pet.getId());
		JsonPath jsonPath = response.getBody().jsonPath();
		//response.then().log().all();
		Assert.assertEquals(jsonPath.get("code").toString(), "200");
//		Response getResponse = PetEndpoints.getPet(pet.getId());
//		Assert.assertEquals(getResponse.jsonPath().get("code").toString(), "404");
	}

}
