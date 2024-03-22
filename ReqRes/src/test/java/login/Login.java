package login;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Login {

    RequestSpecification req = given().baseUri("https://reqres.in");

    @Test
    public void loginUser(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .then()
                .statusCode(200).time(lessThan(3000L))
                .body(
                        "",hasKey("token")
                ).log().all()
        ;
    }

    @Test
    public void loginUserWithInvalidEmail(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"email\": \"eve.holt#reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .assertThat().body("",hasKey("error"))
                .log().all()
        ;
    }

    @Test
    public void loginUserWithEmptyBody(){
        given()
                .spec(req)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .assertThat().body("",hasKey("error"))
                .log().all()
        ;
    }

    @Test
    public void loginUserWithEmailOnly(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .body(
                        "error",containsStringIgnoringCase("Missing password")
                ).log().all()
        ;
    }

    @Test
    public void loginUserWithPasswordOnly(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .then()
                .statusCode(400).time(lessThan(3000L))
                .body(
                        "error",containsStringIgnoringCase("Missing email")
                ).log().all()
        ;
    }
}
