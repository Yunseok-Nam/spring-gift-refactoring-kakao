package gift.acceptance.steps;

import gift.acceptance.ScenarioContext;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CommonSteps {
    @Autowired
    private ScenarioContext scenarioContext;

    @Then("응답 상태 코드가 {int}이다")
    public void 응답_상태_코드_검증(int statusCode) {
        scenarioContext.getLastResponse()
            .then()
            .statusCode(statusCode);
    }

    @Then("응답 바디의 {string} 필드 값이 {string}이다")
    public void 응답_바디_필드_값_검증(String field, String expectedValue) {
        scenarioContext.getLastResponse()
            .then()
            .body(field, equalTo(expectedValue));
    }

    @Then("응답 바디에 {string} 필드가 존재한다")
    public void 응답_바디_필드_존재_검증(String field) {
        scenarioContext.getLastResponse()
            .then()
            .body(field, notNullValue());
    }
}
