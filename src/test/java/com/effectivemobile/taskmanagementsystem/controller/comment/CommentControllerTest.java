package com.effectivemobile.taskmanagementsystem.controller.comment;

import com.effectivemobile.taskmanagementsystem.init.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.COMMENT;
import static com.effectivemobile.taskmanagementsystem.controller.util.RestAssuredUtils.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

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

    @Test
    @Order(1)
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
    @Order(2)
    void givenTestUserTaskWithComment_whenGetPageOfCommentsByTaskId_thenReturnPageOfComments() {
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

    @Test
    @Order(3)
    void givenTestUserTaskWithComment_whenUpdateComment_thenCheckIfItUpdated() {
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .pathParam("commentId", commentId)
                .body("""
                        {
                            "text": "Yes, I change my mind finally."
                        }""")
                .when()
                .patch(APP_PREFIX + COMMENT + "/update/{commentId}")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("id", is(Math.toIntExact(commentId)))
                .body("text", is("Yes, I change my mind finally."))
                .body("taskId", is(Math.toIntExact(taskId)))
                .body("userId", is(1));
    }

    @Test
    @Order(4)
    void givenTestUserTaskWithComment_whenDeleteComment_thenCheckIfItDeleted() {
        //delete comment with ID = commentId
        RestAssured
                .given()
                .spec(getAccessSpecification(getAdminAccessToken()))
                .contentType(ContentType.JSON)
                .pathParam("commentId", commentId)
                .when()
                .delete(APP_PREFIX + COMMENT + "/delete/{commentId}")
                .then()
                .assertThat()
                .statusCode(NO_CONTENT.value());

        //check that comment with ID = commentId is not exist
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
                .body("content", empty())
                .body("page", notNullValue());
    }
}
