package com.microservices.product;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
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
import static org.hamcrest.Matchers.equalTo;
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
        //below 2 lines required because delete api returns text response. if all the methods returns json response then this won't be required.
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.registerParser("text/plain", Parser.TEXT); // handles plain text responses
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
                .body("name", equalTo("iphone17"))
                .body("description", equalTo("iphone17 mobile"))
                .body("price", equalTo(140));
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

    @Test
    @Order(3)
    void shouldReturnCorrectStatusForDeletingProductWithInvalidId() {
        RestAssured
                .given()
                .when()
                .delete("/api/products?id=xyz123")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("Product with the id xyz123 not exist."));
    }
}
