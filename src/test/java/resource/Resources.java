package resource;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Resources {
    RequestSpecification req = (RequestSpecification) given().baseUri("https://reqres.in");

    @Test
    public void getResourcesInPage1(){
        String usersNum = given()
                .spec(req)
                .when()
                .get("/api/unknown")
                .then().extract().response().path("per_page").toString();
        ;

        int perPage = Integer.parseInt(usersNum);

        given()
                .spec(req)
                .when()
                .get("/api/unknown")
                .then()
                .statusCode(200)
                .time(lessThan(3000L))
                .assertThat().body(
                        "page",equalTo(1),
                        "per_page",equalTo(6),
                        "total",equalTo(12),
                        "total_pages",equalTo(2),
                        "data[2]",hasEntry("id",3),
                        "data[2].name",equalTo("true red"),
                        "data[2].color",containsString("#BF1932"),
                        "data.size()",equalTo(perPage)
                ).log().all()
        ;
    }

    @Test
    public void getSingleResource(){
        given()
                .spec(req)
                .when()
                .get("/api/unknown/2")
                .then()
                .statusCode(200).time(lessThan(3000L))
                .assertThat().body(
                        "data.name" , equalTo("fuchsia rose"),
                        "data.id",equalTo(2),
                        "data.color",containsStringIgnoringCase("#C74375"),
                        "data.year",equalTo(2001)
                )
        ;
    }


    @Test
    public void createResource() {
        HashMap<String , String> body = new HashMap<>();
        body.put("name" , "Blue");
        body.put("color" , "#C12345");

        given()
                .baseUri("https://reqres.in/api")
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/unknown")
                .then()
                .statusCode(201)
                .time(lessThan(3000L))
                .assertThat().body(
                        "name", equalTo("Blue"),
                        "color" , equalTo("#C12345")
                )
                .log().all()
        ;
    }



    @Test
    public void updateResourceData(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"name\": \"Blue\",\n" +
                        "    \"color\": \"#C12345\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .put("/api/unknown/2")
                .then()
                .statusCode(200).time(lessThan(3000L))
                .assertThat().body(
                        "name",equalTo("Blue"),
                        "color",equalTo("#C12345")
                ).log().all()

        ;
    }

    @Test
    public void updateUserDataPatch(){
        given()
                .spec(req)
                .body("{\n" +
                        "    \"name\": \"Blue\",\n" +
                        "    \"color\": \"#C12345\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/unknown/2")
                .then()
                .statusCode(200).time(lessThan(3000L))
                .assertThat().body(
                        "name",equalTo("Blue"),
                        "color",equalTo("#C12345")
                ).log().all()

        ;
    }

    @Test
    public void deleteResource(){
        given()
                .spec(req)
                .when()
                .delete("/api/unknown/2")
                .then()
                .statusCode(204).time(lessThan(3000L))
        ;
    }
}
