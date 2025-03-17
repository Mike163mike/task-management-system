package com.effectivemobile.taskmanagementsystem.init;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;

@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = PostgreSQL.Initializer.class)
public abstract class BaseTest {

    /**
     * Test data
     * <p>
     * User with role ROLE_ADMIN (create by SQL-script in V1_0_2__init_data.sql) -
     * login: 'rick_sanchez@gmail.com'
     * password: 'rick_sanchez'
     * </p>
     */

    @LocalServerPort
    private int port;

    @BeforeAll
    static void initTestContainer() {
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    public void setUp() {
        baseURI = "http://localhost:" + port;
    }
}
