package controllers;

import models.User;
import models.UserRepository;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class UserController extends Controller {

	private final UserRepository userRepository;

	// We are using constructor injection to receive a repository to support our
	// desire for immutability.
	@Inject
	public UserController(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Result addUser() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("User not created, expecting Json data");
			return badRequest("User not created, expecting Json data");
		}

		// Parse JSON file
		String firstName = json.path("firstName").asText();
		String lastName = json.path("lastName").asText();
		String middleInitial = json.path("middleInitial").asText();
	    String affiliation = json.path("affiliation").asText();
	    String title = json.path("title").asText();
	    String email = json.path("email").asText();
	    String mailingAddress = json.path("mailingAddress").asText();
	    String phoneNumber = json.path("phoneNumber").asText();
	    String faxNumber = json.path("faxNumber").asText();
	    String researchFields = json.path("researchFields").asText();
	    String highestDegree = json.path("highestDegree").asText();

		try {
			User user = new User(firstName, lastName, middleInitial, affiliation, title, email, mailingAddress, phoneNumber, faxNumber, researchFields, highestDegree);	
			userRepository.save(user);
			System.out.println("User saved: " + user.getId());
			return created(new Gson().toJson(user.getId()));
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			System.out.println("User not saved: " + firstName + " " + lastName);
			return badRequest("User not saved: " + firstName + " " + lastName);
		}
	}

	public Result deleteUser(Long id) {
		User deleteUser = userRepository.findOne(id);
		if (deleteUser == null) {
			System.out.println("User not found with id: " + id);
			return notFound("User not found with id: " + id);
		}

		userRepository.delete(deleteUser);
		System.out.println("User is deleted: " + id);
		return ok("User is deleted: " + id);
	}

	public Result updateUser(long id) {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("User not saved, expecting Json data");
			return badRequest("User not saved, expecting Json data");
		}

		// Parse JSON file
		String firstName = json.path("firstName").asText();
		String lastName = json.path("lastName").asText();
		String middleInitial = json.path("middleInitial").asText();
	    String affiliation = json.path("affiliation").asText();
	    String title = json.path("title").asText();
	    String email = json.path("email").asText();
	    String mailingAddress = json.path("mailingAddress").asText();
	    String phoneNumber = json.path("phoneNumber").asText();
	    String faxNumber = json.path("faxNumber").asText();
	    String researchFields = json.path("researchFields").asText();
	    String highestDegree = json.path("highestDegree").asText();
		try {
			User updateUser = userRepository.findOne(id);

			updateUser.setFirstName(firstName);
			updateUser.setLastName(lastName);
			updateUser.setAffiliation(affiliation);
			updateUser.setEmail(email);
			updateUser.setFaxNumber(faxNumber);
			updateUser.setHighestDegree(highestDegree);
			updateUser.setMailingAddress(mailingAddress);
			updateUser.setMiddleInitial(middleInitial);
			updateUser.setPhoneNumber(phoneNumber);
			updateUser.setResearchFields(researchFields);
			updateUser.setTitle(title);

			System.out.println("User updated: " + updateUser.getFirstName()
					+ " " + updateUser.getLastName());
			return created("User updated: " + updateUser.getFirstName() + " "
					+ updateUser.getLastName());
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			System.out.println("User not updated: " + firstName + " "
					+ lastName);
			return badRequest("User not updated: " + firstName + " " + lastName);
		}
	}


	public Result getUser(Long id, String format) {
		if (id == null) {
			System.out.println("User id is null or empty!");
			return badRequest("User id is null or empty!");
		}

		User user = userRepository.findOne(id);

		if (user == null) {
			System.out.println("User not found with with id: " + id);
			return notFound("User not found with with id: " + id);
		}
		String result = new String();
		if (format.equals("json")) {
			result = new Gson().toJson(user);
		}

		return ok(result);
	}

}
