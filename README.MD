**Desafio Elabbora**

Executando:
Acredito que um "docker compose up --build" seja o suficiente para executar toda a aplicação, se não falhei no setup da conteinerização.

Caso não funcione sinto muito, não tive muita experiência elaborando containeres, pretendo testar isso a fundo e subir um fix, qualquer coisa

Ainda assim, é possível executar a aplicação da forma tradicional utilizando o IntelliJ para o backend, "ng serve" para o frontend e é necessário criar o banco PostgreSQL

jdbc:postgresql://localhost:5432/elabbora
user: elabbora
password: elabbora

Para conectar no banco de dados de fora do container, utilizar porta 15432.
