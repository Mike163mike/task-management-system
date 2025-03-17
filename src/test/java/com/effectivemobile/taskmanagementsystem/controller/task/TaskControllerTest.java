package com.effectivemobile.taskmanagementsystem.controller.task;

import com.effectivemobile.taskmanagementsystem.entity.task.PriorityEnum;
import com.effectivemobile.taskmanagementsystem.entity.task.StatusEnum;
import com.effectivemobile.taskmanagementsystem.init.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.TASK;
import static com.effectivemobile.taskmanagementsystem.controller.util.RestAssuredUtils.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerTest extends BaseTest {

    private static Long testUserId;

    private static Long taskId;

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
    void givenTestUserTask_whenDeleteTask_thenCheckIfTaskIsDeleted() {
        //create the task for a user with email "rick_sanchez@gmail.com"
        taskId = ((Number) RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": "Rick's task",
                            "description": "Blah blah blah blah"
                        }""")
                .when()
                .post(APP_PREFIX + TASK + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("title", is("Rick's task"))
                .extract()
                .path("id"))
                .longValue();

        //delete task with ID = taskId
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .pathParam("taskId", taskId)
                .when()
                .delete(APP_PREFIX + TASK + "/delete/{taskId}")
                .then()
                .assertThat()
                .statusCode(NO_CONTENT.value());

        //check that the task where Rick is the creator with the ID = taskId has been deleted
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .queryParam("creatorEmail", "rick_sanchez@gmail.com")
                .when()
                .get(APP_PREFIX + TASK + "/tasks")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("id", not(hasItem(Math.toIntExact(taskId))));
    }

    @Test
    @Order(2)
    void givenTestUserWithRoleUser_whenCreateTaskForHim_thenReturnCreatedTask() {
        RestAssured
                .given()
                .spec(getAccessSpecification(getTestUserAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": "My first task",
                            "description": "I should do a lot of important things"
                        }""")
                .when()
                .post(APP_PREFIX + TASK + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("title", is("My first task"))
                .body("description", is("I should do a lot of important things"))
                .body("status", is(StatusEnum.PENDING.name()))
                .body("priority", is(PriorityEnum.LOW.name()))
                .body("creator", is("morty_smith@gmail.com"));
    }

    @Test
    @Order(3)
    void givenTwoTasksCreatedByDifferentUser_whenGetTasksBySpecifiedUser_thenCheckThatTask() {
        //create the task by 'rick_sanchez@gmail.com'
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": "Rick's task",
                            "description": "Blah blah blah blah"
                        }""")
                .when()
                .post(APP_PREFIX + TASK + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("title", is("Rick's task"))
                .body("creator", is("rick_sanchez@gmail.com"));

        //create the task by 'morty_smith@gmail.com'
        RestAssured
                .given()
                .spec(getAccessSpecification(getTestUserAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": "Morty's task",
                            "description": "Blah blah blah blah"
                        }""")
                .when()
                .post(APP_PREFIX + TASK + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("title", is("Morty's task"))
                .body("creator", is("morty_smith@gmail.com"));

        //get all tasks where Rick is the creator
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .queryParam("creatorEmail", "rick_sanchez@gmail.com")
                .when()
                .get(APP_PREFIX + TASK + "/tasks")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("content.creator", everyItem(equalTo("rick_sanchez@gmail.com")));
    }

    @Test
    @Order(4)
    void givenTestUserTask_whenUpdateTask_thenReturnUpdatedTask() {
        //create the task for a user with email "rick_sanchez@gmail.com"
        taskId = ((Number) RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": "Rick's task",
                            "description": "Blah blah blah blah"
                        }""")
                .when()
                .post(APP_PREFIX + TASK + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("title", is("Rick's task"))
                .body("description", is("Blah blah blah blah"))
                .body("creator", is("rick_sanchez@gmail.com"))
                .extract()
                .path("id"))
                .longValue();

        //update this task
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .pathParam("taskId", taskId)
                .body("""
                        {
                            "title": "Well, yep, it's still my task",
                            "description": "I have not changed my mind",
                            "assignee": "morty_smith@gmail.com"
                        }""")
                .when()
                .patch(APP_PREFIX + TASK + "/update/{taskId}")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("id", is(Math.toIntExact(taskId)))
                .body("title", is("Well, yep, it's still my task"))
                .body("description", is("I have not changed my mind"))
                .body("assignee", is("morty_smith@gmail.com"));
    }
}
