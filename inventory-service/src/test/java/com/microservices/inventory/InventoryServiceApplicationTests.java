package com.microservices.inventory;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import static com.microservices.inventory.TestcontainersConfiguration.mysqlContainer;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

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
	void shouldCheckIfInventoryInStock() {
		var response = RestAssured
				.given()
				.queryParam("skuCode","xyz")
				.queryParam("quantity",100)
				.when()
				.get("/api/inventory")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.extract()
				.body().toString();
		assertFalse(Boolean.parseBoolean(response));
	}
}
