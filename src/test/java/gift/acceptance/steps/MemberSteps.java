package gift.acceptance.steps;

import gift.acceptance.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class MemberSteps {
    @Autowired
    private ScenarioContext scenarioContext;

    @Given("이메일이 {string}, 비밀번호가 {string}인 회원이 등록되어 있다")
    public void 회원_등록되어_있다(String email, String password) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", email, "password", password))
            .when()
            .post("/api/members/register");
    }

    @When("이메일이 {string}, 비밀번호가 {string}인 회원 등록을 요청한다")
    public void 회원_등록_요청(String email, String password) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", email, "password", password))
            .when()
            .post("/api/members/register");
        scenarioContext.setLastResponse(response);
    }

    @When("이메일이 {string}, 비밀번호가 {string}으로 로그인을 요청한다")
    public void 로그인_요청(String email, String password) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", email, "password", password))
            .when()
            .post("/api/members/login");
        scenarioContext.setLastResponse(response);
    }

    @Given("해당 회원으로 로그인하여 토큰을 획득한다")
    public void 로그인하여_토큰_획득() {
        var token = scenarioContext.getLastResponse().jsonPath().getString("token");
        scenarioContext.setAuthToken(token);
    }
}
