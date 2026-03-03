package gift.acceptance.steps;

import gift.acceptance.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class CategorySteps {
    @Autowired
    private ScenarioContext scenarioContext;

    @Given("{string} 카테고리가 존재한다")
    public void 카테고리_존재(String name) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", name,
                "color", "#FF0000",
                "imageUrl", "https://img.example.com/img.jpg",
                "description", "설명"
            ))
            .when()
            .post("/api/categories");
        scenarioContext.setCategoryId(response.jsonPath().getLong("id"));
    }

    @When("이름이 {string}, 색상이 {string}인 카테고리를 생성 요청한다")
    public void 카테고리_생성_요청(String name, String color) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", name,
                "color", color,
                "imageUrl", "https://img.example.com/img.jpg",
                "description", "설명"
            ))
            .when()
            .post("/api/categories");
        scenarioContext.setLastResponse(response);
    }

    @When("카테고리 목록을 조회한다")
    public void 카테고리_목록_조회() {
        var response = RestAssured.given()
            .when()
            .get("/api/categories");
        scenarioContext.setLastResponse(response);
    }
}
