# spring-kafka-api

Projeto de microsserviços usando Spring Boot 3.x, Kafka, JPA, PostgreSQL e JasperReports

# Requisitos para rodar o projeto
https://www.oracle.com/java/technologies/downloads/

Necessário  baixar o SDK e editar as variáveis de ambiente do sistema
Colocar o caminho do JDK na pasta Ex.: 'JAVA_HOME' - 'C:\Program Files\Java\jdk-21'
Adicionar mais uma variável no path '%JAVA_HOME%\bin'

'java -version' para verificar se está tudo certo, caso não apareça a versão reeinicie o computador

https://community.jaspersoft.com/download-jaspersoft/community-edition/

https://www.docker.com/products/docker-desktop/
# Imagem do Postgres para o Docker
https://hub.docker.com/_/postgres
Caso o Docker de problema necessário acessar 'Ativa ou Dasativar recursos do Windows' e habilitar ho Hyper V
Habilitar Virtualização na BIOS


# Dependências necessárias para o projeto
https://start.spring.io/

Spring WEB
JPA
Lombok
Postgre ou outro banco de dados

# usar para criar imagem dentro do Docker, usar na pasta database do prejeto servicos usando 'cd servicos/database/'
docker compose up -d

# para ver os containers rodando
docker ps

# Usar para realizar conexão sincrona com outros microsserviços
https://spring.io/projects/spring-cloud-openfeign

# Adicionar dependência no pom.xml

# Está disponivel apensa para Spring Boot 3.4.x

## Pedidos (Spring Boot + Kafka)


## Pré-requisitos

## Infra com Docker (Kafka + UI)
Na raiz do projeto:

```powershell
docker compose -f servicos/kafka/compose.yaml up -d
```

- UI: http://localhost:8022
- Zookeeper: localhost:8023
- Broker (externo para apps no host): 127.0.0.1:8021

O compose já está configurado com dois listeners:
- Externo: `PLAINTEXT` em 127.0.0.1:8021 (mapeado para 8021:9092)
- Interno: `PLAINTEXT_INTERNAL` em kafkabroker:9094 (rede Docker)

Isso garante que a aplicação no host use 127.0.0.1 e que os containers (como a UI) usem o host interno `kafkabroker`.

## Banco de Dados (PostgreSQL)
Você pode usar um PostgreSQL local (porta 8020) ou subir um container rápido:

```powershell
docker run --name pedidos-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=icompraspedidos -p 8020:5432 -d postgres:16
```

Configuração padrão do app aponta para: jdbc:postgresql://localhost:8020/icompraspedidos (usuário/senha: postgres/postgres).

## Configuração da Aplicação
Principais ajustes em [pedidos/src/main/resources/application.yaml](pedidos/src/main/resources/application.yaml):

```yaml
server:
	port: 8050

spring:
	datasource:
		url: jdbc:postgresql://localhost:8020/icompraspedidos
		username: postgres
		password: postgres
	kafka:
		bootstrap-servers: 127.0.0.1:8021

icompraspedidos:
	config:
		kafka:
			topics:
				envio: icompras.pedido-enviado
				pagamento: icompras.pedido-pago
				faturamento: icompras.pedido-faturado
```

Alternativa por variável de ambiente (Windows PowerShell):

```powershell
$env:SPRING_KAFKA_BOOTSTRAP_SERVERS="127.0.0.1:8021"
```

## Executando a Aplicação
Na raiz do módulo pedidos:

```powershell
"d:\curso-spring-kafka-main\pedidos\mvnw.cmd" clean package -DskipTests
"d:\curso-spring-kafka-main\pedidos\mvnw.cmd" spring-boot:run -f "d:\curso-spring-kafka-main\pedidos\pom.xml"
```

Ou via jar:

```powershell
java -jar .\pedidos\target\pedidos-0.0.1-SNAPSHOT.jar
```

## Criando Tópicos (opcional)
O broker está com auto-create habilitado, mas você pode criar via UI (Clusters > Topics):
- icompras.pedido-enviado
- icompras.pedido-pago
- icompras.pedido-faturado

## Teste Rápido (callback de pagamento)

```powershell
curl --location "http://localhost:8050/pedido/callback-pagamento" \
	--header "api-key: 4f8b3c2a1d6e9f0b1234567890abcdef" \
	--header "Content-Type: application/json" \
	--data "{\"pedidoId\":57,\"idPagamento\":\"cf92464d-5141-4787-a753-38f407dedf59\",\"status\":true,\"observacoes\":\"Sucesso no pagamento\"}"
```

O serviço publicará no tópico configurado em `icompraspedidos.config.kafka.topics.pagamento`. Você pode ver a mensagem na Kafka UI.

## Endpoints Úteis
- POST /pedido/callback-pagamento – processa o retorno de pagamento e publica evento.

## Solução de Problemas
- Erro “UnknownHostException: kafkabroker” ou "Bootstrap broker ... disconnected":
	- Garanta que o compose em [servicos/kafka/compose.yaml](servicos/kafka/compose.yaml) está rodando.
	- A aplicação deve apontar para `127.0.0.1:8021` (Spring Kafka).
	- O compose anuncia `PLAINTEXT` externo e `PLAINTEXT_INTERNAL` interno — isso evita escolher o host interno no Windows.
- Porta 8050 em uso:
	- Feche o processo ou rode na 8051: `-Dspring-boot.run.arguments="--server.port=8051"`.
- Falha no PostgreSQL:
	- Suba o container (`docker run ...`) ou ajuste `spring.datasource.url` para seu ambiente.
- Verificar portas (Windows):
	- `Test-NetConnection -ComputerName 127.0.0.1 -Port 8021 | Format-List`

## Estrutura relevante
- App principal: [pedidos/src/main/java/io/github/icompras/pedidos/PedidosApplication.java](pedidos/src/main/java/io/github/icompras/pedidos/PedidosApplication.java)
- Publisher Kafka: [pedidos/src/main/java/io/github/icompras/pedidos/publisher/PagamentoPublisher.java](pedidos/src/main/java/io/github/icompras/pedidos/publisher/PagamentoPublisher.java)
- Config Kafka: [pedidos/src/main/java/io/github/icompras/pedidos/config/KafkaConfig.java](pedidos/src/main/java/io/github/icompras/pedidos/config/KafkaConfig.java)
- Compose Kafka: [servicos/kafka/compose.yaml](servicos/kafka/compose.yaml)

---
Com isso, qualquer pessoa (incluindo recrutadores) consegue subir o Kafka, rodar a aplicação e validar o fluxo publicando mensagens no Kafka em poucos minutos.

## Para armazenar objetos está sendo utilizado o MinIO
https://min.io/download#/windows
# Imagem do MinIO para o Docker
https://hub.docker.com/r/minio/minio

# usar para criar imagem dentro do Docker, usar na pasta minio do prejeto servicos usando 'D:\curso-spring-kafka-main\servicos\minio'
docker compose down && docker compose up -d



