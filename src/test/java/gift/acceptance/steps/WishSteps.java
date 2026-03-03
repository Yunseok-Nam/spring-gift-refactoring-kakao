package gift.acceptance.steps;

import gift.acceptance.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class WishSteps {
    @Autowired
    private ScenarioContext scenarioContext;

    @Given("이메일이 {string}, 비밀번호가 {string}인 다른 회원이 등록되어 있다")
    public void 다른_회원_등록(String email, String password) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", email, "password", password))
            .when()
            .post("/api/members/register");
    }

    @Given("다른 회원이 로그인하여 해당 상품을 위시에 추가했다")
    public void 다른_회원_로그인_위시_추가() {
        var loginResponse = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", "other@test.com", "password", "pass456"))
            .when()
            .post("/api/members/login");
        var otherToken = loginResponse.jsonPath().getString("token");
        scenarioContext.setOtherAuthToken(otherToken);

        var wishResponse = RestAssured.given()
            .header("Authorization", "Bearer " + otherToken)
            .contentType(ContentType.JSON)
            .body(Map.of("productId", scenarioContext.getProductId()))
            .when()
            .post("/api/wishes");
        scenarioContext.setOtherMemberWishId(wishResponse.jsonPath().getLong("id"));
    }

    @When("인증 헤더를 포함하여 해당 상품을 위시에 추가 요청한다")
    public void 위시_추가_요청() {
        var response = RestAssured.given()
            .header("Authorization", "Bearer " + scenarioContext.getAuthToken())
            .contentType(ContentType.JSON)
            .body(Map.of("productId", scenarioContext.getProductId()))
            .when()
            .post("/api/wishes");
        scenarioContext.setLastResponse(response);
    }

    @Given("인증 헤더를 포함하여 해당 상품을 위시에 추가했다")
    public void 위시_추가_완료() {
        RestAssured.given()
            .header("Authorization", "Bearer " + scenarioContext.getAuthToken())
            .contentType(ContentType.JSON)
            .body(Map.of("productId", scenarioContext.getProductId()))
            .when()
            .post("/api/wishes");
    }

    @When("인증 헤더를 포함하여 동일한 상품을 위시에 다시 추가 요청한다")
    public void 위시_중복_추가_요청() {
        var response = RestAssured.given()
            .header("Authorization", "Bearer " + scenarioContext.getAuthToken())
            .contentType(ContentType.JSON)
            .body(Map.of("productId", scenarioContext.getProductId()))
            .when()
            .post("/api/wishes");
        scenarioContext.setLastResponse(response);
    }

    @When("첫 번째 회원의 인증 헤더로 다른 회원의 위시 항목을 삭제 요청한다")
    public void 다른_회원_위시_삭제_요청() {
        var response = RestAssured.given()
            .header("Authorization", "Bearer " + scenarioContext.getAuthToken())
            .when()
            .delete("/api/wishes/" + scenarioContext.getOtherMemberWishId());
        scenarioContext.setLastResponse(response);
    }

    @When("유효하지 않은 토큰으로 해당 상품을 위시에 추가 요청한다")
    public void 유효하지_않은_토큰_위시_추가() {
        var response = RestAssured.given()
            .header("Authorization", "Bearer invalid-token")
            .contentType(ContentType.JSON)
            .body(Map.of("productId", scenarioContext.getProductId()))
            .when()
            .post("/api/wishes");
        scenarioContext.setLastResponse(response);
    }
}
