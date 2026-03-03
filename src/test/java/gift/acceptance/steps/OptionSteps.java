package gift.acceptance.steps;

import gift.acceptance.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class OptionSteps {
    @Autowired
    private ScenarioContext scenarioContext;

    @Given("해당 상품에 수량이 {int}인 {string} 옵션이 존재한다")
    public void 수량_포함_옵션_존재(int quantity, String optionName) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("name", optionName, "quantity", quantity))
            .when()
            .post("/api/products/" + scenarioContext.getProductId() + "/options");
        scenarioContext.setOptionId(response.jsonPath().getLong("id"));
    }

    @Given("해당 상품에 {string} 옵션이 존재한다")
    public void 옵션_존재(String optionName) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("name", optionName, "quantity", 10))
            .when()
            .post("/api/products/" + scenarioContext.getProductId() + "/options");
        scenarioContext.setOptionId(response.jsonPath().getLong("id"));
    }

    @Given("해당 상품에 {string} 옵션만 존재한다")
    public void 옵션만_존재(String optionName) {
        // 옵션 1개 생성 (이 시점에서 상품에 옵션이 없으므로 1개만 추가)
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("name", optionName, "quantity", 10))
            .when()
            .post("/api/products/" + scenarioContext.getProductId() + "/options");
        scenarioContext.setOptionId(response.jsonPath().getLong("id"));
    }

    @When("해당 상품에 이름이 {string}, 수량이 {int}인 옵션을 추가 요청한다")
    public void 옵션_추가_요청(String name, int quantity) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of("name", name, "quantity", quantity))
            .when()
            .post("/api/products/" + scenarioContext.getProductId() + "/options");
        scenarioContext.setLastResponse(response);
    }

    @When("{string} 옵션을 삭제 요청한다")
    public void 옵션_삭제_요청(String optionName) {
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
            .when()
            .delete("/api/products/" + scenarioContext.getProductId() + "/options/" + optionId);
        scenarioContext.setLastResponse(response);
    }
}
