# ============================
# STAGE 1: Build
# ============================
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copia o Maven Wrapper e o pom.xml primeiro para cachear dependências
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Garante permissão de execução no Maven Wrapper
RUN chmod +x mvnw

# Baixa as dependências (camada cacheada enquanto o pom.xml não mudar)
RUN ./mvnw dependency:resolve -B

# Copia o código-fonte
COPY src/ src/

# Compila o projeto (pula testes para agilizar o build)
RUN ./mvnw package -DskipTests -B

# ============================
# STAGE 2: Runtime
# ============================
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Cria um usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia o JAR gerado do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Define o dono do arquivo
RUN chown appuser:appgroup app.jar

# Executa como usuário não-root
USER appuser

# Expõe a porta da aplicação
EXPOSE 8080

# Health check básico
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

# Variáveis de ambiente (devem ser fornecidas no docker run ou docker-compose)
# DB_URL        - URL de conexão com o PostgreSQL (ex: jdbc:postgresql://host:5432/dbname)
# DB_USERNAME   - Usuário do banco de dados (ex: postgres)
# DB_PASSWORD   - Senha do banco de dados

# Inicia a aplicação com otimizações para containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
