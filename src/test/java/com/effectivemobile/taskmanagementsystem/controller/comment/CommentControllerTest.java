package com.effectivemobile.taskmanagementsystem.controller.comment;

import com.effectivemobile.taskmanagementsystem.init.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.COMMENT;
import static com.effectivemobile.taskmanagementsystem.controller.util.RestAssuredUtils.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest extends BaseTest {

    private static Long testUserId;

    private static Long taskId;

    private static Long commentId;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        testUserId = Long.valueOf(createTestUser());
        taskId = createTask(testUserId);
        commentId = Long.valueOf(createComment(taskId));
    }

    @AfterEach
    public void tearDown() {
        deleteTestUser(testUserId);
    }

    //    @Test
//    @Order(1)
//    void givenTestUserTask_whenDeleteTask_thenCheckIfTaskIsDeleted() {
//        //create the task for a user with email "rick_sanchez@gmail.com"
//        taskId = ((Number) RestAssured
//                .given()
//                .spec(getAccessSpecification(getAdminAccessToken()))
//                .contentType(ContentType.JSON)
//                .body("""
//                        {
//                            "title": "Rick's task",
//                            "description": "Blah blah blah blah"
//                        }""")
//                .when()
//                .post(APP_PREFIX + TASK + "/create")
//                .then()
//                .assertThat()
//                .statusCode(CREATED.value())
//                .body("title", is("Rick's task"))
//                .extract()
//                .path("id"))
//                .longValue();
//
//        //delete task with ID = taskId
//        RestAssured
//                .given()
//                .spec(getAccessSpecification(getAdminAccessToken()))
//                .contentType(ContentType.JSON)
//                .pathParam("taskId", taskId)
//                .when()
//                .delete(APP_PREFIX + TASK + "/delete/{taskId}")
//                .then()
//                .assertThat()
//                .statusCode(NO_CONTENT.value());
//
//        //check that the task where Rick is the creator with the ID = taskId has been deleted
//        RestAssured
//                .given()
//                .spec(getAccessSpecification(getAdminAccessToken()))
//                .contentType(ContentType.JSON)
//                .queryParam("creatorEmail", "rick_sanchez@gmail.com")
//                .when()
//                .get(APP_PREFIX + TASK + "/tasks")
//                .then()
//                .assertThat()
//                .statusCode(OK.value())
//                .body("id", not(hasItem(Math.toIntExact(taskId))));
//    }
//
    @Test
    @Order(2)
    void givenTestUserTask_whenCreateCommentForIt_thenReturnCreatedComment() {
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "text": "I think this idea is fantastic!",
                            "taskId": "%s"
                        }"""
                        .formatted(taskId))
                .when()
                .post(APP_PREFIX + COMMENT + "/create")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("text", is("I think this idea is fantastic!"))
                .body("taskId", is(Math.toIntExact(taskId)))
                .body("userId", is(1));
    }

    @Test
    @Order(3)
    void givenTaskWithComment_whenGetPageOfComments_thenReturnPageOfComments() {
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .pathParam("taskId", taskId)
                .when()
                .get(APP_PREFIX + COMMENT + "/comments/{taskId}")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("content[0].id", is(Math.toIntExact(commentId)))
                .body("content[0].text", is("Will create a portal or something..."))
                .body("content[0].taskId", is(Math.toIntExact(taskId)))
                .body("content[0].userId", is(1))
                .body("page", notNullValue());
    }
//
//    @Test
//    @Order(3)
//    void givenTwoTasksCreatedByDifferentUser_whenGetTasksBySpecifiedUser_thenCheckThatTask() {
//        //create the task by 'rick_sanchez@gmail.com'
//        RestAssured
//                .given()
//                .spec(getAccessSpecification(getAdminAccessToken()))
//                .contentType(ContentType.JSON)
//                .body("""
//                        {
//                            "title": "Rick's task",
//                            "description": "Blah blah blah blah"
//                        }""")
//                .when()
//                .post(APP_PREFIX + TASK + "/create")
//                .then()
//                .assertThat()
//                .statusCode(CREATED.value())
//                .body("title", is("Rick's task"))
//                .body("creator", is("rick_sanchez@gmail.com"));
//
//        //create the task by 'morty_smith@gmail.com'
//        RestAssured
//                .given()
//                .spec(getAccessSpecification(getTestUserAccessToken()))
//                .contentType(ContentType.JSON)
//                .body("""
//                        {
//                            "title": "Morty's task",
//                            "description": "Blah blah blah blah"
//                        }""")
//                .when()
//                .post(APP_PREFIX + TASK + "/create")
//                .then()
//                .assertThat()
//                .statusCode(CREATED.value())
//                .body("title", is("Morty's task"))
//                .body("creator", is("morty_smith@gmail.com"));
//
//        //get all tasks where Rick is the creator
//        RestAssured
//                .given()
//                .spec(getAccessSpecification(getAdminAccessToken()))
//                .contentType(ContentType.JSON)
//                .queryParam("creatorEmail", "rick_sanchez@gmail.com")
//                .when()
//                .get(APP_PREFIX + TASK + "/tasks")
//                .then()
//                .assertThat()
//                .statusCode(OK.value())
//                .body("content", iterableWithSize(1))
//                .body("content.id", notNullValue())
//                .body("content.creator", contains("rick_sanchez@gmail.com"));
//    }
//
//    @Test
//    @Order(4)
//    void givenTestUserTask_whenUpdateTask_thenReturnUpdatedTask() {
//        //create the task for a user with email "rick_sanchez@gmail.com"
//        taskId = ((Number) RestAssured
//                .given()
//                .spec(getAccessSpecification(getAdminAccessToken()))
//                .contentType(ContentType.JSON)
//                .body("""
//                        {
//                            "title": "Rick's task",
//                            "description": "Blah blah blah blah"
//                        }""")
//                .when()
//                .post(APP_PREFIX + TASK + "/create")
//                .then()
//                .assertThat()
//                .statusCode(CREATED.value())
//                .body("title", is("Rick's task"))
//                .body("description", is("Blah blah blah blah"))
//                .body("creator", is("rick_sanchez@gmail.com"))
//                .extract()
//                .path("id"))
//                .longValue();
//
//        //update this task
//        RestAssured
//                .given()
//                .spec(getAccessSpecification(getAdminAccessToken()))
//                .contentType(ContentType.JSON)
//                .pathParam("taskId", taskId)
//                .body("""
//                        {
//                            "title": "Well, yep, it's still my task",
//                            "description": "I have not changed my mind",
//                            "assignee": "morty_smith@gmail.com"
//                        }""")
//                .when()
//                .patch(APP_PREFIX + TASK + "/update/{taskId}")
//                .then()
//                .assertThat()
//                .statusCode(OK.value())
//                .body("id", is(Math.toIntExact(taskId)))
//                .body("title", is("Well, yep, it's still my task"))
//                .body("description", is("I have not changed my mind"))
//                .body("assignee", is("morty_smith@gmail.com"));
//    }
}
