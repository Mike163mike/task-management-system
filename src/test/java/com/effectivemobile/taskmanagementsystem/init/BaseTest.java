package com.effectivemobile.taskmanagementsystem.init;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = PostgreSQL.Initializer.class)
public abstract class BaseTest {

    @LocalServerPort
    private int port;

    @BeforeAll
    static void initTestContainer() {
        PostgreSQL.container.start();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterAll
    static void stopContainer() {
        PostgreSQL.container.stop();
    }

    @BeforeEach
    void setUpBasePath() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

//    protected String getHost() {
//        return "http://localhost:" + port;
//    }

}
