package gift.acceptance.steps;

import gift.acceptance.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ProductSteps {
    @Autowired
    private ScenarioContext scenarioContext;

    @Given("해당 카테고리에 {string} 상품이 존재한다")
    public void 상품_존재(String productName) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", productName,
                "price", 5000,
                "imageUrl", "https://img.example.com/product.jpg",
                "categoryId", scenarioContext.getCategoryId()
            ))
            .when()
            .post("/api/products");
        scenarioContext.setProductId(response.jsonPath().getLong("id"));
    }

    @Given("해당 카테고리에 가격이 {int}원인 {string} 상품이 존재한다")
    public void 가격_포함_상품_존재(int price, String productName) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", productName,
                "price", price,
                "imageUrl", "https://img.example.com/product.jpg",
                "categoryId", scenarioContext.getCategoryId()
            ))
            .when()
            .post("/api/products");
        scenarioContext.setProductId(response.jsonPath().getLong("id"));
    }

    @When("이름이 {string}, 가격이 {int}원인 상품을 생성 요청한다")
    public void 상품_생성_요청(String name, int price) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", name,
                "price", price,
                "imageUrl", "https://img.example.com/product.jpg",
                "categoryId", scenarioContext.getCategoryId()
            ))
            .when()
            .post("/api/products");
        scenarioContext.setLastResponse(response);
    }

    @When("이름이 {string}인 상품을 생성 요청한다")
    public void 이름만_상품_생성_요청(String name) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", name,
                "price", 5000,
                "imageUrl", "https://img.example.com/product.jpg",
                "categoryId", scenarioContext.getCategoryId()
            ))
            .when()
            .post("/api/products");
        scenarioContext.setLastResponse(response);
    }

    @When("첫 번째 페이지 상품 목록을 조회한다")
    public void 상품_목록_조회() {
        var response = RestAssured.given()
            .queryParam("page", 0)
            .queryParam("size", 10)
            .when()
            .get("/api/products");
        scenarioContext.setLastResponse(response);
    }

    @When("카테고리 ID가 {long}인 상품을 생성 요청한다")
    public void 존재하지_않는_카테고리_상품_생성(long categoryId) {
        var response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "name", "테스트상품",
                "price", 5000,
                "imageUrl", "https://img.example.com/product.jpg",
                "categoryId", categoryId
            ))
            .when()
            .post("/api/products");
        scenarioContext.setLastResponse(response);
    }
}
