# Sistema de Autores e Livros

📌 Pré-requisitos

Git para clonar o repositório

Java 21 + Maven + Node.js

---


## 🛠 Passo 1 — Clonar o Projeto

```
git clone https://github.com/feliperb/apirest-autor-livro.git
cd apirest-autor-livro

```


## 🛠 Passo 2 - Criar o Banco postgreSQL por fora

```sql
biblioteca_db
```


## 🛠 Passo 3 — Iniciar Spring Boot

Ao iniciar o Spring Boot, ele criará as tabelas e fará uma carga inicial de dados no banco
```
mvnw spring-boot:run
```


## 🛠 Passo 4 — Consultar a documentação Swagger

Iniciado o Spring, estará disponível a Documentação via Swagger

Obs: O Spring Boot se conecta automaticamente ao PostgreSQL e carrega os scripts schema.sql e data.sql.

Swagger UI:
http://localhost:8080/swagger-ui/index.html

API Docs (JSON):
http://localhost:8080/v3/api-docs

Todas as requisições REST podem ser testadas via Swagger ou Postman. Uma Collection de exemplo está disponível em postman/collections.


## 🛠 Passo 5 — Consultar o Banco de Dados

Se quiser verificar dados dentro do container PostgreSQL:

```
docker exec -it postgres_felipe psql -U postgres -d felipeDB
```

Comandos úteis dentro do psql:

```
\dt        -- lista todas as tabelas
SELECT * FROM <nome_da_tabela>;
\q         -- sai do psql
```


## 🛠 Passo 6 — Rodar o Vite para o Frontend em React

```
npm install
npm run dev
```

Acesse: http://localhost:5173/



