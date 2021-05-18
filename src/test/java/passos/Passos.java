package passos;

import cucumber.api.java.en.Given;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Passos {

	Response resposta;
	private String pessoa;
	private String baseURL = "http://localhost:8080/";
	private String ddd;
	private String telefone;

	/**********************************************************/
	/***** EFETUA A CONSULTA INFORMANDO DDD + TELEFONE ********/
	/**********************************************************/

	@Dado("^Que meu serviço esteja iniciado$")
	public void queMeuServiçoEstejaIniciado() {
		RestAssured.baseURI = "http://localhost:8080/";
	}

	@Quando("^Informar ddd e telefone celular \"([^\"]*)\" , \"([^\"]*)\"$")
	public void euPassarODddOTelefoneCelular(String ddd, String telefone) throws Throwable {

		this.ddd = ddd;
		this.telefone = telefone;

		RestAssured.given().when().get(baseURL + "pessoas/" + ddd + "/" + telefone);

	}

	@Entao("^O sistema verifica que a pessoa existe e valida o status code \"([^\"]*)\" , \"([^\"]*)\"$")
	public void consultarPessoa(String ddd, String telefone) throws Throwable {

		RestAssured.given().when().get(baseURL + "pessoas/" + ddd + "/" + telefone).then().statusCode(200);

	}

	/**********************************************************/
	/***** EFETUA A CONSULTA INFORMANDO TELEFONE INEXISTENTE ********/
	/**********************************************************/

	/**********************************************/
	/***** EFETUA A INCLUSÃO DE UMA PESSOA ********/
	/**********************************************/

	@Given("^Cadastrar Pessoa$")
	public void InserirPessoa() {
	}

	@Quando("^Eu informo os dados NOME, CPF e DDD \\+ Telefone , \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
	public void euInformoOsDadosNOMECPFEDDDTelefone(String nome, String cpf, String ddd, String telefone)
			throws Throwable {

		pessoa = "{\n" + "\"codigo\": 0,\n" + "\"nome\": \"" + nome + "\",\n" + "\"cpf\": \"" + cpf + "\",\n"
				+ "\"enderecos\": [\n" + "{\n" + "\"logradouro\": \"Rua Alexandre Dumas\",\n" + "\"numero\": 123,\n"
				+ "\"complemento\": \"Empresa\",\n" + "\"bairro\": \"Chacara Santo Antonio\",\n"
				+ "\"cidade\": \"São Paulo\",\n" + "\"estado\": \"SP\"\n" + "}\n" + "],\n" + "\"telefones\": [\n"
				+ "{\n" + "\"ddd\": \"" + ddd + "\",\n" + "\"numero\": \"" + telefone + "\"\n" + "}]\n" + "}";

		RestAssured.given().header("Content-type", "application/json").and().body(pessoa);
	}

	@Entao("^O sistema deve inserir a pessoa$")
	public void oSistemaDeveInserirAPessoa() {

		RestAssured.given().header("Content-type", "application/json").and().body(pessoa).when().post("pessoas").then()
				.statusCode(201);
	}

	/****************************************************************/
	/***** EFETUA A INSERÇÃO DE UMA PESSOA COM CPF EXISTENTE ********/
	/****************************************************************/

	@Given("^Cadastrar Pessoa com o Mesmo CPF$")
	public void InserirPessoaCpfExistente() {
	}

	@Quando("^Eu informo um CPF já existente na base , \"([^\"]*)\"$")
	public void euInformoUmCPFJaExistenteNaBase(String cpf) throws Throwable {

		pessoa = "{\n" + "\"codigo\": 0,\n" + "\"nome\": \"Paulo Fonseca\",\n" + "\"cpf\": \"" + cpf + "\",\n"
				+ "\"enderecos\": [\n" + "{\n" + "\"logradouro\": \"Qd 300 Conjunto 10 casa 19\",\n"
				+ "\"numero\": 19,\n" + "\"complemento\": \"Casa\",\n" + "\"bairro\": \"Recanto das Emas\",\n"
				+ "\"cidade\": \"Distrito Federal\",\n" + "\"estado\": \"DF\"\n" + "}\n" + "],\n" + "\"telefones\": [\n"
				+ "{\n" + "\"ddd\": \"61\",\n" + "\"numero\": \"995455657\"\n" + "}]\n" + "}";

		resposta = RestAssured.given().header("Content-type", "application/json").and().body(pessoa).when()
				.post("pessoas").then().extract().response();

	}

	@Entao("^O sistema NÃO deve inserir a Pessoa com mesmo CPF$")
	public void oSistemaNaoDeveInserirAPessoa() {

		assertEquals("{\"erro\":\"Já existe pessoa cadastrada com o CPF " + JsonPath.from(pessoa).getString("\"cpf\"")
				+ "\"}", resposta.asString());

	}

	/*********************************************************************/
	/***** EFETUA A INSERÇÃO DE UMA PESSOA COM TELEFONE EXISTENTE ********/
	/*********************************************************************/

	@Given("^Cadastrar Pessoa com o Mesmo Telefone$")
	public void InserirPessoaTelefoneExistente() {
	}

	@Quando("^Eu informo um telefone já existente na base , \"([^\"]*)\", \"([^\"]*)\"$")
	public void euInformoUmTelefJaExistenteNaBase(String ddd, String telefone) throws Throwable {

		pessoa = "{\n" + "\"codigo\": 0,\n" + "\"nome\": \"Paulo Fonseca\",\n" + "\"cpf\": \"12345678807\",\n"
				+ "\"enderecos\": [\n" + "{\n" + "\"logradouro\": \"Qd 300 Conjunto 10 casa 19\",\n"
				+ "\"numero\": 19,\n" + "\"complemento\": \"Casa\",\n" + "\"bairro\": \"Recanto das Emas\",\n"
				+ "\"cidade\": \"Distrito Federal\",\n" + "\"estado\": \"DF\"\n" + "}\n" + "],\n" + "\"telefones\": [\n"
				+ "{\n" + "\"ddd\": \"" + ddd + "\",\n" + "\"numero\": \"" + telefone + "\"\n" + "}]\n" + "}";

		resposta = RestAssured.given().header("Content-type", "application/json").and().body(pessoa).when()
				.post("pessoas").then().extract().response();

	}

	@Entao("^O sistema NÃO deve inserir a Pessoa com um mesmo telefone$")
	public void oSistemaNaoDeveInserirAPessoaComUmMesmoTelefone() {

		assertEquals("{\"erro\":\"Já existe pessoa cadastrada com o Telefone ("
				+ JsonPath.from(pessoa).getString("telefones[0].ddd") + ")"
				+ JsonPath.from(pessoa).getString("telefones[0].numero") + "\"}", resposta.asString());

	}

	@Entao("^sistema verificar status code , \"([^\"]*)\"$")
	public void validacaoStatusCode(String statusCode) {

		RestAssured.given().when().get(baseURL + "pessoas/" + this.ddd + "/" + this.telefone).then()
				.statusCode(Integer.parseInt(statusCode));

	}

}
