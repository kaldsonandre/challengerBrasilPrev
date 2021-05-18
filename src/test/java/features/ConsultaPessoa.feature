# language: pt

@ConsultarPessoa
Funcionalidade: Consulta de uma pessoa na base
  Cenario: Pesquisar uma pessoa dado o numero ddd e telefone celular
    Dado Que meu serviço esteja iniciado
    Quando Informar ddd e telefone celular "11" , "985388877"
    Entao O sistema verifica que a pessoa existe e valida o status code "11" , "985388877"
    
   Cenario: Pesquisar uma pessoa com telefone inexistente
    Dado Que meu serviço esteja iniciado
    Quando Informar ddd e telefone celular "31" , "988387877"
    Entao sistema verificar status code , "404"
   
