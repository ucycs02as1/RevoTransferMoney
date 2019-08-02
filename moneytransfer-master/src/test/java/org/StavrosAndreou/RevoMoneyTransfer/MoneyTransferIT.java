package org.StavrosAndreou.RevoMoneyTransfer;

import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class MoneyTransferIT {

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @AfterClass
    public static void unconfigureRestAssured() {
        RestAssured.reset();
    }

    @Test
    public void testRetrieveAllAccounts() {
        final int id = get("/api/accounts").then()
        		.assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getInt("find { it.accBalance==30000 }.accID");
        get("/api/accounts/" + id).then()
                .assertThat()
                .statusCode(200)
                .body("accID", equalTo(id))
                .body("accCurr", equalTo("EUR"))
                .body("ownerName", equalTo("Andreas Andreou"));
    }

    @Test
    public void testRetrieveOneAccountPass() {
        get("/api/accounts/0").then()
                .assertThat()
                .statusCode(200)
                .body("accID", equalTo(0))
                .body("accCurr", equalTo("EUR"))
                .body("ownerName", equalTo("Stavros Andreou"));
    }

    @Test
    public void testRetrieveOneAccountFail() {
        get("/api/accounts/999").then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testAddAccountPass() {
        given().body("{\n" +
                "    \"ownerName\": \"Andreas Andreou\",\n" +
                "    \"accBalance\": \"12332\",\n" +
                "    \"accCurr\": \"GBP\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
    }


    @Test
    public void testDeleteOneAccountPass() {
        delete("/api/accounts/0").then()
                .assertThat()
                .statusCode(204);
        get("/api/accounts/0").then()
                .assertThat()
                .statusCode(404);
    }

    
    @Test
    public void testRetrieveAllmoves() {
        final int id = get("/api/moves").then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getInt("find { it.moveAmount==500 }.id");
        get("/api/moves/" + id).then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("moveSourceAccID", equalTo(0))
                .body("moveDestinationAccID", equalTo(1))
                .body("moveAmount", equalTo(500))
                .body("moveCurr", equalTo("EUR"))
                .body("moveComment", equalTo("IKEA"));
    }

    @Test
    public void testRetrieveOneTransferPass() {
        get("/api/moves/0").then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(0))
                .body("moveSourceAccID", equalTo(0))
                .body("moveDestinationAccID", equalTo(1))
                .body("moveAmount", equalTo(500))
                .body("moveCurr", equalTo("EUR"))
                .body("moveComment", equalTo("IKEA"));
    }

   

    @Test
    public void testAddTransferPass() {
        given().body("{\n" +
                "    \"moveSourceAccID\": \"1\",\n" +
                "    \"moveDestinationAccID\": \"0\",\n" +
                "    \"moveAmount\": \"1000\",\n" +
                "    \"moveCurr\": \"USD\",\n" +
                "    \"moveComment\": \"testingtransfer\"\n" +
                "}")
                .when()
                .post("api/moves")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void testAddTransferFail() {
        given().body("{\n" +
                "    \"moveSourceAccID\": \"1\",\n" +
                "    \"moveDestinationAccID\": \"0\",\n" +
                "    \"moveAmount\": \"1000\",\n" +
                "    \"moveCurr\": \"ihhuhu\",\n" +
                "    \"moveComment\": \"testing\"\n" +
                "}")
                .when()
                .post("api/moves")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testUpdateTransferPass() {
        put("api/moves/0")
                .then()
                .assertThat()
                .body("status", equalTo("EXECUTED"));
    }

    @Test
    public void testUpdateTransferFail() {
        put("api/moves/1")
                .then()
                .assertThat()
                .body("status", equalTo("FAILED"));
    }

}
