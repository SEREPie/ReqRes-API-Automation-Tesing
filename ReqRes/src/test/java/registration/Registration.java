package registration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class Registration {
    RequestSpecification req = given().baseUri("https://reqres.in");

    @Test
    public void registerUser(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .contentType(ContentType.JSON)
        .when()
                .post("/api/register")
        .then()
                .statusCode(200).time(lessThan(3000L))
                .body(
                        "",hasKey("id"),
                        "",hasKey("token")
                ).log().all()
        ;
    }

    @Test
    public void registerUserWithInvalidEmail(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"email\": \"eve.holt#reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .assertThat().body("",hasKey("error"))
                .log().all()
        ;
    }

    @Test
    public void registerUserWithEmptyBody(){
        given()
                .spec(req)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .assertThat().body("",hasKey("error"))
                .log().all()
        ;
    }

    @Test
    public void registerUserWithEmailOnly(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .body(
                        "error",containsStringIgnoringCase("Missing password")
                ).log().all()
        ;
    }

    @Test
    public void registerUserWithPasswordOnly(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .body(
                        "error",containsStringIgnoringCase("Missing email")
                ).log().all()
        ;
    }
}
