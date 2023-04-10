package Steps;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.io.File;

import static io.restassured.RestAssured.given;

public class BraveNewCoinAPISteps {
    private static RequestSpecification request;
    private Response response;
    private ValidatableResponse json;

    @Given("^I have a valid API key for the (.+) URI$")
    public void iSetTheRequestParams(String URI){
        request = given()
                .relaxedHTTPSValidation() //is the method suggested to work without having to validate keys
                .header("x-rapidapi-key", "f2c58a1b0emshdc3dbbf70e352f7p1aea5fjsne021712ff261")
                .header("x-rapidapi-host", "bravenewcoin.p.rapidapi.com")
                .contentType(ContentType.JSON)
                .baseUri(URI)
                .log().all();
    }

    @When("^I send a POST request with a valid (.+) payload to the (.+) endpoint$")
    public void sendPOSTRequest(String payload, String endpoint){
        File requestBody = new File("src/test/resources/Payloads/"+ payload +".json");
        response = request.when().body(requestBody).post(endpoint).prettyPeek();
        //prettyPeek() es como un log all, usado para propósitos de debuggin
    }

    @Then("^I should be able to validate I receive a valid token in the response$")
    public void validateTheToken(){

    }
}
