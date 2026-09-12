# Java 17 + Azure AI Vision

O fluxo é propositalmente simples:

```text
Navegador
   |
   v
Spring Boot / Java 17
   |
   v
Azure AI Vision
   |
   v
Descrição + confiança + tags
```

## O que o projeto faz

1. Exibe uma página web simples
2. O usuário informa uma URL pública de imagem
3. O Java recebe a URL em `POST /api/analyze`
4. O backend chama o Azure AI Vision
5. A aplicação mostra:
   - descrição da imagem
   - nível de confiança
   - tags identificadas

O projeto mantém a url e a chave do Azure **somente no backend**, usando variáveis de ambiente

## Pré-requisitos

- Java 17
- Maven 3.9+
- Uma conta Azure
- Um recurso Azure AI Vision / Foundry Tools com endpoint e chave


## Configuração local

Linux/macOS:

```bash
export VISION_ENDPOINT="SEU_ENDPOINT"
export VISION_KEY="SUA_CHAVE"
```

Windows CMD:

```bash
set VISION_ENDPOINT="SEU_ENDPOINT"
set VISION_KEY="SUA_CHAVE"
```

Windows PowerShell:

```powershell
$env:VISION_ENDPOINT="SEU_ENDPOINT"
$env:VISION_KEY="SUA_CHAVE"
```


## Executar

```bash
mvn spring-boot:run
```

Acesse:

```text
http://localhost:8080
```

## Gerar JAR

```bash
mvn clean package
```

Executar:

```bash
java -jar target/java17-cognitive-vision-1.0.0.jar
```

## Testar a API diretamente

Windows CMD:

```bash:
curl.exe -X POST "http://localhost:8080/api/analyze" -H "Content-Type: application/json" -d "{\"url\":\"https://aka.ms/azsdk/image-analysis/sample.jpg\"}"
```

Windows PowerShell:

```bash
curl.exe -X POST "http://localhost:8080/api/analyze" -H "Content-Type: application/json" -d '{\"url\":\"https://aka.ms/azsdk/image-analysis/sample.jpg\"}'
```

Linux/macOS:

```bash
curl -X POST "http://localhost:8080/api/analyze" -H "Content-Type: application/json" -d '{"url":"https://aka.ms/azsdk/image-analysis/sample.jpg"}'
```

Health check:

```bash
curl http://localhost:8080/api/health
```

- Java 17
- Spring Boot
- API REST
- variáveis de ambiente
- Azure AI Vision
- Azure App Service
- deploy de JAR
