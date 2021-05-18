package executar;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.*;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/IncluirPessoa.feature", tags = "@IncluirPessoa", 
glue = "passos", monochrome = true)

public class IniciarTestesInclusao{

}
