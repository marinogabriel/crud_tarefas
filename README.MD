**Desafio Elabbora**

Executando:
Acredito que um "docker compose up --build" seja o suficiente para executar toda a aplicação, se não falhei no setup da conteinerização.

Caso não funcione , é possível executar a aplicação da forma tradicional utilizando o IntelliJ para o backend, "ng serve" para o frontend e é necessário criar o banco PostgreSQL

jdbc:postgresql://localhost:5432/elabbora

user: elabbora

password: elabbora

Para conectar no banco de dados de fora do container, utilizar porta 15432.


Notas:

A interface ficou bastante simples, o Gerenciar categorias era pra ser um placeholder mas acabou ficando por conta do tempo.

No login/registro, faltou limpar os campos ao trocar entre eles + bug visual na mensagem de erro (aparecendo dentro do input).

Também poderia melhorar um pouco as rotas da aplicação, principalmente evitar que o usuário logado acesse /login.
