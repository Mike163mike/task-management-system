package com.effectivemobile.taskmanagementsystem.controller.user;

import com.effectivemobile.taskmanagementsystem.init.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.USER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;

public class UserControllerTest extends BaseTest {

    @Test
    void whenLogin_thenReturnCurrentUser() {
        RestAssured
                .given()
//                .spec(getAccessSpecification(getTestUserAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email": "gerald_fitzgerald@gmail.com",
                            "password": "gerald_fitzgerald"
                        
                        }""")
                .when()
                .post(APP_PREFIX + USER + "/create")
                .then()
                .log().all()
                .assertThat()
                .statusCode(CREATED.value())
                .body("size()", is(2))
                .body("id", notNullValue())
                .body("email", is("gerald_fitzgerald@gmail.com"));
    }


}
