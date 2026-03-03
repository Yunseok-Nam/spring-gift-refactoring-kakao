package gift.acceptance;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class ScenarioContext {
    private Response lastResponse;
    private String authToken;
    private Long categoryId;
    private Long productId;
    private Long optionId;
    private Long otherMemberWishId;
    private String otherAuthToken;

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getOtherMemberWishId() {
        return otherMemberWishId;
    }

    public void setOtherMemberWishId(Long otherMemberWishId) {
        this.otherMemberWishId = otherMemberWishId;
    }

    public String getOtherAuthToken() {
        return otherAuthToken;
    }

    public void setOtherAuthToken(String otherAuthToken) {
        this.otherAuthToken = otherAuthToken;
    }
}
