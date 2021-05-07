import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BrasilPrevChallenge {

    private String pessoa = "{\n" +
            "\"codigo\": 0,\n" +
            "\"nome\": \"Rafael Teixeira\",\n" +
            "\"cpf\": \"12345678909\",\n" +
            "\"enderecos\": [\n" +
            "{\n" +
            "\"logradouro\": \"Rua Alexandre Dumas\",\n" +
            "\"numero\": 123,\n" +
            "\"complemento\": \"Empresa\",\n" +
            "\"bairro\": \"Chacara Santo Antonio\",\n" +
            "\"cidade\": \"São Paulo\",\n" +
            "\"estado\": \"SP\"\n" +
            "}\n" +
            "],\n" +
            "\"telefones\": [\n" +
            "{\n" +
            "\"ddd\": \"11\",\n" +
            "\"numero\": \"985388877\"\n" +
            "}]\n" +
            "}";

    private String pessoaComMesmoTelefone = "{\n" +
            "\"codigo\": 0,\n" +
            "\"nome\": \"Rafael Teixeira\",\n" +
            "\"cpf\": \"39204028041\",\n" +
            "\"enderecos\": [\n" +
            "{\n" +
            "\"logradouro\": \"Rua Alexandre Dumas\",\n" +
            "\"numero\": 123,\n" +
            "\"complemento\": \"Empresa\",\n" +
            "\"bairro\": \"Chacara Santo Antonio\",\n" +
            "\"cidade\": \"São Paulo\",\n" +
            "\"estado\": \"SP\"\n" +
            "}\n" +
            "],\n" +
            "\"telefones\": [\n" +
            "{\n" +
            "\"ddd\": \"11\",\n" +
            "\"numero\": \"985388877\"\n" +
            "}]\n" +
            "}";

    private Response response;
    private String baseURL = "http://localhost:8080/";


    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/";
    }

    @Test
    public void cenario01inserirPessoa() {

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(pessoa)
                .when()
                .post("pessoas")
                .then()
                .statusCode(201)
                .extract().response();

        Assert.assertTrue(response.jsonPath().getString("nome").equals("Rafael Teixeira"));

    }

    @Test //Cenário pesquisa a pessoa cadastrada e também efetua uma busca por DDD + Telefone.
    public void cenario02PesquisarPessoaCadastrada() {

        RestAssured.given()
                   .when()
                   .get(baseURL + "pessoas/11/985388877")
                   .then()
                   .statusCode(200)
                   .body("telefones[0].numero", is("985388877")); //Efetua a validação se cadastrou o usuário com o numero telefone == 985388877

    }

    @Test //Cenário pesquisa a pessoa cadastrada e também efetua uma busca por DDD + Telefone.
    public void cenario03RetornaErroQuandoTelefoneEhInexistente() {

        RestAssured.given()
                   .when()
                   .get(baseURL + "pessoas/11/985388874")
                   .then()
                   .body(containsString("Não existe pessoa"))
                   .statusCode(404);

    }

    @Test
    public void cenario01inserirPessoaComMesmoCPF() {

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(pessoa)
                .when()
                .post("pessoas")
                .then()
                .extract().response(); // inserindo a mesma pessoa com numero de  cpf

        JsonPath jsonPath = new JsonPath(response.asString());

        assertEquals("{\"erro\":\"Já existe pessoa cadastrada com o CPF " + JsonPath.from(pessoa).getString("cpf") + "\"}", response.asString());
    }

    @Test
    public void cenario01inserirPessoaComMesmoTelefone() {

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(pessoaComMesmoTelefone)
                .when()
                .post("pessoas")
                .then()
                .extract().response(); // inserindo a mesma pessoa com numero de telefone

        JsonPath jsonPath = new JsonPath(response.asString());

        assertEquals("{\"erro\":\"Já existe pessoa cadastrada com o Telefone (" + JsonPath.from(pessoaComMesmoTelefone).getString("telefones[0].ddd") + ")" + JsonPath.from(pessoaComMesmoTelefone).getString("telefones[0].numero") + "\"}", response.asString());
    }


}
