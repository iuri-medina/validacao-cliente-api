# Validação de Clientes - API REST

Este projeto é uma API REST desenvolvida com **Spring Boot**, projetada para **gerenciar clientes e validar chaves de autenticação** de um sistema de importação de ofertas. A API permite o cadastro, consulta, atualização e remoção de clientes, garantindo que cada loja tenha uma chave única e não reutilizável.

##  Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3** (Spring Web, Spring Data JPA, Validation)
- **MySQL** (Banco de dados relacional)
- **Docker & Docker Compose**
- **Swagger (Springdoc OpenAPI)** (Documentação interativa da API)
- **Lombok** (Para redução de boilerplate)

##  Funcionalidades

- **Gerenciamento de Clientes:** CRUD completo
- **Geração de chave aleatória** para cada cliente no momento do cadastro
- **Validação de chave** na API para garantir autenticidade
- **Validação de CNPJ** para evitar duplicidade
- **Documentação com Swagger** para facilitar integração
- **Execução via Docker** para facilidade de deploy

##  Como Rodar o Projeto

### 1. Clonar o repositório

```sh
git clone https://github.com/seu-usuario/validacao-clientes-api.git
cd validacao-clientes-api
```

### 2. Buildar o projeto e gerar o arquivo `.jar`

Antes de executar o Docker Compose, é necessário **gerar o arquivo** `.jar` **do projeto e colocá-lo na pasta raiz do repositório.**

```sh
mvn clean package
```

Isso criará um arquivo `.jar` dentro da pasta `target/`. Copie-o para a pasta raiz do projeto e renomeie para `app.jar`:

```sh
cp target/*.jar app.jar
```

### 3. Rodando com Docker Compose

Com o arquivo `app.jar` na pasta raiz, execute:

```sh
docker-compose up --build
```

Isso irá subir dois containers:

- **Banco de Dados MySQL** (porta 3306)
- **API Spring Boot** (porta 8080)

### 4. Acessando a API

A API estará rodando em:

```
http://localhost:8080
```

A documentação Swagger pode ser acessada em:

```
http://localhost:8080/swagger-ui.html
```

##  Melhorias Futuras

- Implementação de autenticação com JWT
- Paginação para listagem de clientes
- Testes unitários com JUnit

##  Autor

[Iuri Medina](https://www.linkedin.com/in/iuri-medina/) - Desenvolvedor Back-end

Gostou do projeto? Dê uma ⭐ no repositório e me siga no GitHub!

