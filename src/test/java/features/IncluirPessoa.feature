# language: pt

@IncluirPessoa
Funcionalidade: Incluir Pessoa
  Cenario: Cadastrar Pessoa
    Dado Que meu serviço esteja iniciado
    Quando Eu informo os dados NOME, CPF e DDD + Telefone , "Rafael Teixeira", "12345678909", "11", "985388877"
    Entao O sistema deve inserir a pessoa

  Cenario: Cadastrar Pessoa com o Mesmo CPF
    Dado Que meu serviço esteja iniciado
    Quando Eu informo um CPF já existente na base , "12345678909"
    Entao O sistema NÃO deve inserir a Pessoa com mesmo CPF
    
  Cenario: Cadastrar Pessoa com o Mesmo Telefone
    Dado Que meu serviço esteja iniciado
    Quando Eu informo um telefone já existente na base , "11", "985388877"
    Entao O sistema NÃO deve inserir a Pessoa com um mesmo telefone