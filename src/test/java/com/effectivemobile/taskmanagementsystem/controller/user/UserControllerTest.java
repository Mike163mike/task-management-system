package com.effectivemobile.taskmanagementsystem.controller.user;

import com.effectivemobile.taskmanagementsystem.init.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.USER;
import static com.effectivemobile.taskmanagementsystem.controller.util.RestAssuredUtils.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends BaseTest {

    private static Long testUserId;

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
    void whenCreateUser_thenReturnCreatedUser() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email": "gerald_fitzgerald@gmail.com",
                            "password": "gerald_fitzgerald"
                        
                        }""")
                .when()
                .post(APP_PREFIX + USER + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("size()", is(2))
                .body("id", notNullValue())
                .body("email", is("gerald_fitzgerald@gmail.com"));
    }

    @Test
    @Order(2)
    void whenGetAllUsers_thenReturnThreeUsers() {
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .when()
                .get(APP_PREFIX + USER + "/users")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("content", iterableWithSize(3))
                .body("content.id", everyItem(notNullValue()))
                .body("content.email", containsInAnyOrder(
                        "rick_sanchez@gmail.com",
                        "gerald_fitzgerald@gmail.com",
                        "morty_smith@gmail.com"));
    }

    @Test
    @Order(3)
    void whenUpdateUser_thenReturnUpdatedUser() {
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .pathParam("userId", testUserId)
                .body("""
                        {
                            "email": "bender_rodriguez@gmail.com",
                            "password": "bender_rodriguez"
                        
                        }""")
                .when()
                .patch(APP_PREFIX + USER + "/update/{userId}")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("id", is(Math.toIntExact(testUserId)))
                .body("email", is("bender_rodriguez@gmail.com"));
    }

    @Test
    @Order(4)
    void givenTestUser_whenDeleteUser_thenCheckIfUserIsDeleted() {
        //delete user with ID = testUserId
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

        //check that the user with the ID = testUserId has been deleted
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .when()
                .get(APP_PREFIX + USER + "/users")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("content.id", not(hasItem(Math.toIntExact(testUserId))));
    }
}
