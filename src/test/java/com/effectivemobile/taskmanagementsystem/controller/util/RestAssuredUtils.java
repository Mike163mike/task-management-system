package com.effectivemobile.taskmanagementsystem.controller.util;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@UtilityClass
public class RestAssuredUtils {

    public static RequestSpecification getAccessSpecification(String token) {
        return new RequestSpecBuilder()
                .addHeader(AUTHORIZATION, BEARER + token)
                .build();
    }

    public static String getTestUserAccessToken() {
        return getUserAccessToken(MORTY, MORTY_PASSWORD);
    }

    public static String getAdminAccessToken() {
        return getUserAccessToken(RICK, RICK_PASSWORD);
    }

    public static String getUserAccessToken(String email, String password) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email" : "%s",
                            "password" : "%s"
                        }
                        """
                        .formatted(email, password))
                .when()
                .post(APP_PREFIX + AUTH + "/login")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getString("accessToken");
    }

    /**
     * Create user with role ROLE_USER -
     * login: 'morty_smith@gmail.com'
     * password: 'morty_smith'
     */

    public static Integer createTestUser() {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email": "morty_smith@gmail.com",
                            "password": "morty_smith"
                        
                        }""")
                .when()
                .post(APP_PREFIX + USER + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("size()", is(2))
                .body("id", notNullValue())
                .body("email", is("morty_smith@gmail.com"))
                .extract()
                .path("id");
    }

    public static void deleteTestUser(Long testUserId) {
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .pathParam("userId", testUserId)
                .when()
                .delete(APP_PREFIX + USER + "/delete/{userId}")
                .then()
                .assertThat()
                .statusCode(NO_CONTENT.value());
    }

    public static Long createTask(Long testUserId) {
        return ((Number) RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": "Rick's task",
                            "description": "Will create a portal or something ..."
                        }""")
                .when()
                .post(APP_PREFIX + TASK + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("title", is("Rick's task"))
                .body("description", is("Will create a portal or something ..."))
                .extract()
                .path("id"))
                .longValue();
    }

    public static Integer createComment(Long taskId) {
        return RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "text": "Will create a portal or something...",
                            "taskId": "%s"
                        }"""
                        .formatted(taskId))
                .when()
                .post(APP_PREFIX + COMMENT + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("id", notNullValue())
                .body("text", is("Will create a portal or something..."))
                .body("taskId", is(Math.toIntExact(taskId)))
                .extract()
                .path("id");
    }
}
