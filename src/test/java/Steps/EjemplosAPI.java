package Steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Base64;

import static io.restassured.RestAssured.*;
public class EjemplosAPI {
    public void GETRequest(){
        given()
                .baseUri("https://api.github.com")
                .when()
                .get("/users/TheFreeRangeTester/repos")
                .getBody().toString();
    }
    public void POSTRequest(){
        given()
                .baseUri("https://api.blogEjemplo.com")
                .when()
                .post("/posts", "Title:Text");
    }
    public void PUTRequest(){
        given()
                .baseUri("")
                .when()
                .put("", "");
    }
    public void DELETERequest(){
        given()
                .baseUri("https://api.blogEjemplo.com/posts/Text") //intenta borrar la data creada en el post
                .when()
                .delete();
    }
    //Basic Auth
    public void basicAuth(String userName, String password){
        given()
                .auth()
                .basic(userName, password)
                .when()
                .get("AUTH_ENDPOINT")
                .then()
                .assertThat().statusCode(200);
    }

    //Auth by form
    public void formAuth(String userName, String password){
        given()
                .auth().form(userName, password)
                .when()
                .get("SERVICE")
                .then()
                .assertThat().statusCode(200);
    }

    //OAuth: the one used when using external service to log in, i.e., using Facebook, Twitter or Google Account
    //credentials. To do this we follow the next steps:
    /*
    1- Obtener el código del servicio original para obtener el token.
    2- Obtener el token, intercambiando el código que obtuvimos
    3- Acceder al recurso protegido, con nuestro token.
    */

    public static String clientId = "";
    public static String redirectUri = "";
    public static String scope = "";
    public static String userName = "";
    public static String password = "";
    public static String grantType = "";
    public static String accessToken = "";

    //Esto nos va a estar devolviendo el user y el pass decodificados
    public static String encode(String str1, String str2){
        return new String(Base64.getEncoder().encode((str1+ ":" + str2).getBytes()));
    }
    public static Response getCode(){//parseo para obterner el código
        String authorization = encode(userName, password);
        return
                given()
                        .header("authorization", "Basic" + authorization)
                        .contentType(ContentType.URLENC)
                        .formParam("response_type", "code")
                        .queryParam("clientId", clientId)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("scope", scope)
                        .post("/oauth/authorize")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
    }

    //ahora necesitamos devolver el code, para ello hacemos la siguiente función
    //con jsonPath, deserializamos el contenido
    public static String parseForOAuthCode(Response response){
        return response.jsonPath().getString("code");
    }

    //Ahora intercambiamos el código por el token (3er paso)
    public static Response getToken(String authCode){
        String authorization = encode(userName, password);

        return
                given()
                        .header("authorization", "Basic" + authorization)
                        .contentType(ContentType.URLENC)
                        .formParam("response_type", authCode)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("grant_type", grantType) //el tipo del token
                        .post("/oauth/authorize")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
    }

    //Ya tenemos el token en la respuesta, ahora necesitamos "destripar" toda la respuesta,  para obtener el token
    public static String parseForToken(Response loginResponse){
        return loginResponse.jsonPath().getString("access_token");
    } //Ahora con este token ya podremos autenticarnos

    public static void getFinalService(){//esto a modo de ejemplo, cuando sea del mundo real hay que adaptarlo
        given().auth()
                .oauth2(accessToken)
                //.header("Authorization", "Bearer", accessToken) //otra forma de hacer este oauth2
                .when()
                .get("/service")
                .then()
                .statusCode(200);
        //esta última llamada que hicimos es la que va a estar resumiendo o haciendo uso de lo que hicimos previamente
    }

}
