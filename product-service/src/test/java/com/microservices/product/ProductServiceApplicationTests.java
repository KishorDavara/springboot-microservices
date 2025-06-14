package com.microservices.product;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import static com.microservices.product.TestcontainersConfiguration.mongoDbContainer;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @BeforeAll
    static void beforeAll() {
        mongoDbContainer().start();
    }

    @AfterAll
    static void afterAll() {
        mongoDbContainer().stop();
    }

    @Test
    @Order(1)
    void shouldCreateProduct() {
        String requestBody = """
                {
                    "name": "iphone17",
                    "description": "iphone17 mobile",
                    "price": 140
                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/products")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", notNullValue())
                .body("name", Matchers.equalTo("iphone17"))
                .body("description", Matchers.equalTo("iphone17 mobile"))
                .body("price", Matchers.equalTo(140));
    }

    @Test
    @Order(2)
    void shouldGetAllProducts() {
        RestAssured
                .given()
                .when()
                .get("/api/products")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .body("id", everyItem(notNullValue()))
                .body("name", hasItem("iphone17"))
                .body("description", hasItem("iphone17 mobile"));
    }
}
