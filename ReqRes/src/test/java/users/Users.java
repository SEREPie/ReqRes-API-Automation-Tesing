package users;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


public class Users {

    RequestSpecification req = (RequestSpecification) given().baseUri("https://reqres.in");

    @Test
    public void getUsersInPage2(){
        String usersNum = given()
                .spec(req)
                .when()
                .get("/api/users?page=2")
                .then().extract().response().path("per_page").toString();
        ;

        int perPage = Integer.parseInt(usersNum);


        given()
                .spec(req)
        .when()
                .get("/api/users?page=2")
        .then()
                .statusCode(200)
                .time(lessThan(3000L))
                .assertThat().body(
                        "page",equalTo(2),
                "per_page",equalTo(6),
                        "total",equalTo(12),
                        "total_pages",equalTo(2),
                        "data[1]",hasEntry("id",8),
                "data[1].first_name",equalTo("Lindsay"),
                "data[1].email",containsString("lindsay"),
                "data.size()",equalTo(perPage)
                ).log().all()
        ;
    }

    @Test
    public void getSingleUser(){
        given()
                .spec(req)
        .when()
                .get("/api/users/2")
        .then()
                .statusCode(200).time(lessThan(3000L))
                .assertThat().body(
                        "data.first_name" , equalTo("Janet"),
                "data.id",equalTo(2),
                "data.email",containsStringIgnoringCase("Janet"),
                "data.avatar",containsString("reqres.in")
                )
        ;
    }

    @Test
    public void getNonExistingUser(){
        given()
                .spec(req)
                .when()
                .get("/api/users/25")
                .then()
                .statusCode(404).time(lessThan(3000L))
        ;
    }

    @Test
    public void createUser() {
        HashMap<String , String> body = new HashMap<>();
        body.put("name" , "Hossam");
        body.put("job" , "Software Tester");

        given()
                .spec(req)
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post("/api/users")
        .then()
                .statusCode(201)
                .time(lessThan(3000L))
                .assertThat().body(
                        "name", equalTo("Hossam"),
                "job" , equalTo("Software Tester")
                )
                .log().all()
        ;
    }



    @Test
    public void updateUserData(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200).time(lessThan(3000L))
                .assertThat().body(
                        "name",equalTo("morpheus"),
                "job",equalTo("zion resident")
                )

        ;
    }

    @Test
    public void updateUserDataPatch(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/users/2")
                .then()
                .statusCode(200).time(lessThan(3000L))
                .assertThat().body(
                        "name",equalTo("morpheus"),
                        "job",equalTo("zion resident")
                )

        ;
    }

    @Test
    public void deleteUser(){
        given()
                .spec(req)
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204).time(lessThan(3000L))
        ;
    }

}
