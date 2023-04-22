package get;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.*;

import java.util.*;

public class PokemonHw1 {

    /*
    1. GET https://pokeapi.co/api/v2/pokemon
    2. Validate you got 20 Pokemons
    3. Get every Pokemons ability and store those in Map<String, List<String>>
     */

    @Test
    public void validateNumberOfPokemon(){

        RestAssured.baseURI="https://pokeapi.co/api/v2/pokemon";
        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get()
                .then().statusCode(200)
                .extract().response();

        PokemonPojo parsedResponse = response.as(PokemonPojo.class);

        int actualNumberOfPokemons=parsedResponse.getResults().size();
        int expectedNumberOfPokemons=20;
        Assert.assertEquals(expectedNumberOfPokemons,actualNumberOfPokemons);
        Map<String,List<String>> pokeNameAndAbilities=new LinkedHashMap<>();
        List<String> pokeAbilities=new ArrayList<>();
        for (int i=0;i<parsedResponse.getResults().size();i++){
            RestAssured.baseURI=parsedResponse.getResults().get(i).getUrl();
            Response response1 = RestAssured.given().accept(ContentType.JSON)
                    .when().get()
                    .then().statusCode(200)
                    .extract().response();

            Pokemondetails deserializationResponse1 = response1.as(Pokemondetails.class);
            pokeAbilities.clear();
            pokeNameAndAbilities.clear();
            for (int j=0;j<deserializationResponse1.getAbilities().size();j++){
                pokeAbilities.add(deserializationResponse1.getAbilities().get(j).getAbility().getName());
            }
            pokeNameAndAbilities.put(parsedResponse.getResults().get(i).getName(),pokeAbilities);
            System.out.println(pokeNameAndAbilities);
        }

    }

    @Test
    public void validateNumberOfPokemonTypeRef(){
        System.out.println();
        System.out.println("=========================================");
        System.out.println();
        RestAssured.baseURI="https://pokeapi.co/api/v2/pokemon";
        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get()
                .then().statusCode(200)
                .extract().response();

        Map<String, Object> deserializationResponse = response.as(new TypeRef<Map<String, Object>>() {
        });

        List<Map<String,String>> results= (List<Map<String, String>>) deserializationResponse.get("results");

        //System.out.println(results.get(0).get("url"));

        for (int i=0; i<results.size();i++){

            RestAssured.baseURI=results.get(i).get("url");
            Response response1 = RestAssured.given().accept(ContentType.JSON)
                    .when().get()
                    .then().statusCode(200)
                    .extract().response();

            Map<String, Object> deserializationResponse1 = response1.as(new TypeRef<Map<String, Object>>() {
            });

            List<Map<String,Map<String,String>>> abilities= (List<Map<String, Map<String, String>>>) deserializationResponse1.get("abilities");
            Map<String,List> namesAndAbilities=new LinkedHashMap<>();
            List<String>abilitiesName=new ArrayList<>();

            abilitiesName.clear();
            namesAndAbilities.clear();
            for (int j=0;j<abilities.size();j++){
                abilitiesName.add(abilities.get(j).get("ability").get("name"));
                namesAndAbilities.put(results.get(i).get("name"),abilitiesName);

            }
            System.out.println(namesAndAbilities);

        }

    }



}
