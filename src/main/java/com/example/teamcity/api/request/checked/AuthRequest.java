package com.example.teamcity.api.request.checked;

import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import com.example.teamcity.api.models.User;

public class AuthRequest {
    private User user;
    public AuthRequest(User user){
        this.user = user;
    }
    public String getCsrfToken(){
        return RestAssured
                .given()
                .spec(Specifications.getSpec().authSpec(user))
                .get("/authenticationTest.html?csrf")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
}
