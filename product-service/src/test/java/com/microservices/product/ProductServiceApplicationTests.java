package com.microservices.product;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import static com.microservices.product.TestcontainersConfiguration.mongoDbContainer;

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

    static {
        mongoDbContainer().start();
    }

    @Test
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
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("iphone17"))
				.body("description", Matchers.equalTo("iphone17 mobile"))
				.body("price", Matchers.equalTo(140));
    }

}
