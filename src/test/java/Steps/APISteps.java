package Steps;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class APISteps {
    private static RequestSpecification request;
    private static Response response;
    private static ValidatableResponse json;
    @Given("^I sent a GET request to (.+) URI$")
    public void sentGETRequest(String URI) {
        request = given()
                .baseUri(URI)
                .contentType(ContentType.JSON);
    }

    @Then("^I get a (\\d+) status code$")
    public void validateListOfUsers(int expectedStatusCode){
        response = request
                .when()
                .get("/users/TheFreeRangeTester/repos");
        json = response.then().statusCode(expectedStatusCode);
    }
    @Then("^I validate there are (\\d+) items on the (.+) endpoint$")
    public void validateSize(int expectedSize, String endPoint){
        //Build the response in base of the request we are getting
        response = request
                .when()
                .get(endPoint);
        //We are trying to know the amount of users in the response for which what we need to get the size is a List
        List<String> jsonResponse = response.jsonPath().getList("$");
        //$ symbol get all the entries inside the json response we are receiving
        int actualSize = jsonResponse.size();

        assertEquals(expectedSize, actualSize);
    }

    @Then("^I validate there is a value: (.+) in the response at (.+) endpoint$")
    public void validateValueInResponse(String expectedValue, String endPoint){
        response = request
                .when()
                .get(endPoint);

        List<String> jsonResponse = response.jsonPath().getList("username");
        assertTrue("The value "+expectedValue + " is not present in the list", jsonResponse.contains(expectedValue));
        /*boolean nameFound = false;
        for (String name: jsonResponse) {
            if (name.equals(expectedValue)){
                nameFound = true;
                break;
            }
        }
        assertEquals(nameFound, true);*/
    }

    @Then("^I should validate the nested value: (.+) on the response at (.+) endpoint$")
    public void validateNestedValue(String expectedStreet, String endPoint){
        response = request
                .when()
                .get(endPoint);

        String jsonResponse = response.jsonPath().getString("address.street");
        assertTrue("The street " +expectedStreet + "does not belong to any user", jsonResponse.contains(expectedStreet));
    }
}
