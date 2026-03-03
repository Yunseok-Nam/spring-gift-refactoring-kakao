package gift.acceptance.steps;

import gift.acceptance.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class OrderSteps {
    @Autowired
    private ScenarioContext scenarioContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Given("포인트가 {int}인 {string} 회원이 등록되어 있다")
    public void 포인트_포함_회원_등록(int point, String email) {
        var registerResponse = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", email, "password", "password123"))
            .when()
            .post("/api/members/register");
        scenarioContext.setLastResponse(registerResponse);
        scenarioContext.setAuthToken(registerResponse.jsonPath().getString("token"));

        // 포인트 충전: JDBC로 직접 업데이트 (HTTP 서버와 동일한 DB에 반영)
        jdbcTemplate.update("UPDATE member SET point = ? WHERE email = ?", point, email);
    }

    @Given("해당 회원으로 로그인하여 토큰을 갱신한다")
    public void 토큰_갱신() {
        var token = scenarioContext.getLastResponse().jsonPath().getString("token");
        scenarioContext.setAuthToken(token);
    }

    @When("인증 헤더를 포함하여 {string} 옵션 {int}개를 주문 요청한다")
    public void 주문_요청(String optionName, int quantity) {
        // 옵션 목록에서 ID 찾기
        var optionsResponse = RestAssured.given()
            .when()
            .get("/api/products/" + scenarioContext.getProductId() + "/options");
        List<Map<String, Object>> options = optionsResponse.jsonPath().getList("");
        var targetOption = options.stream()
            .filter(o -> optionName.equals(o.get("name")))
            .findFirst()
            .orElseThrow();
        var optionId = ((Number) targetOption.get("id")).longValue();

        var response = RestAssured.given()
            .header("Authorization", "Bearer " + scenarioContext.getAuthToken())
            .contentType(ContentType.JSON)
            .body(Map.of(
                "optionId", optionId,
                "quantity", quantity,
                "message", "선물입니다"
            ))
            .when()
            .post("/api/orders");
        scenarioContext.setLastResponse(response);
    }

    @When("유효하지 않은 토큰으로 주문 요청한다")
    public void 유효하지_않은_토큰_주문() {
        // 아무 옵션 ID 사용 (인증 실패 테스트이므로 옵션 유효성은 중요하지 않음)
        var optionsResponse = RestAssured.given()
            .when()
            .get("/api/products/" + scenarioContext.getProductId() + "/options");
        List<Map<String, Object>> options = optionsResponse.jsonPath().getList("");
        var optionId = ((Number) options.get(0).get("id")).longValue();

        var response = RestAssured.given()
            .header("Authorization", "Bearer invalid-token")
            .contentType(ContentType.JSON)
            .body(Map.of(
                "optionId", optionId,
                "quantity", 1,
                "message", "테스트"
            ))
            .when()
            .post("/api/orders");
        scenarioContext.setLastResponse(response);
    }
}
