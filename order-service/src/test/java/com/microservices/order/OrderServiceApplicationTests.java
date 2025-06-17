package com.microservices.order;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import static com.microservices.order.TestcontainersConfiguration.mysqlContainer;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @BeforeAll
    static void beforeAll() {
        mysqlContainer().start();
    }

    @AfterAll
    static void afterAll() {
        mysqlContainer().stop();
    }

    @Test
    void shouldPlaceOrder() {
        String requestBody = """
                {
                    "skuCode": "iphone_15",
                    "price": 1000,
                    "quantity": 2
                }
                """;
        RestAssured
                .given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", notNullValue())
                .body("orderNumber", notNullValue())
                .body("skuCode", equalTo("iphone_15"))
                .body("price", equalTo(1000))
                .body("quantity", equalTo(2));
    }
}
