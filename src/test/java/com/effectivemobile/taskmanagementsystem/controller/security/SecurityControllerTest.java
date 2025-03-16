package com.effectivemobile.taskmanagementsystem.controller.security;

import com.effectivemobile.taskmanagementsystem.init.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.DirtiesContext;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.AUTH;
import static com.effectivemobile.taskmanagementsystem.controller.util.RestAssuredUtils.createTestUser;
import static com.effectivemobile.taskmanagementsystem.controller.util.RestAssuredUtils.deleteTestUser;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityControllerTest extends BaseTest {

    private static Long testUserId;

    private static String refreshToken;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        testUserId = Long.valueOf(createTestUser());
    }

    @AfterEach
    public void tearDown() {
        deleteTestUser(testUserId);
    }

    @Test
    @Order(1)
    @DirtiesContext
    void givenCreatedTestUser_whenTryToGetAccessToken_thenCheckIfItIsObtained() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email": "morty_smith@gmail.com",
                            "password": "morty_smith"
                        }""")
                .when()
                .post(APP_PREFIX + AUTH + "/login")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @Order(2)
    @DirtiesContext
    void givenCreatedTestUser_whenTryToGetRefreshToken_thenCheckIfItIsObtained() {
        //register tokens for new user
        refreshToken = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email": "morty_smith@gmail.com",
                            "password": "morty_smith"
                        }""")
                .when()
                .post(APP_PREFIX + AUTH + "/login")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .path("refreshToken");

        //try to get new refreshToken
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "refreshToken": "%s"
                        }"""
                        .formatted(refreshToken))
                .when()
                .post(APP_PREFIX + AUTH + "/refresh-token")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
}
