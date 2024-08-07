
# Escopo

- Microservices (Java e Spring Boot)
  - User (Producer)
  - Email (Consumer)
- Base de Dados por Microservice (PostgreSQL) para aumentar isolamento
- Mensageria por Comandos (RabbitMQ)

# Ferramentas

- Java
- Maven
- Spring
  - Spring Boot
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring AMQP
  - Spring Mail
- PostgreSQL
- RabbitMQ
- Cloud AMPQ
- SMTP Gmail

# RabbitMQ

É composto pela junção das esturuturas Exchange e Queues

- Exchange: Recebe as mensagens, analisa e roteia para as filas.
- Queue: É o local onde as mensagens são armazenadas para consumo.