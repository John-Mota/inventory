# 📦 Inventory API — Autoflex

API REST para controle de estoque de uma indústria, permitindo gerenciar **produtos**, **matérias-primas**, suas **associações** e consultar **sugestões de produção** com base no estoque disponível.

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.11 |
| Spring Data JPA | Incluído |
| Bean Validation (Jakarta) | Incluído |
| PostgreSQL (Supabase) | — |
| Lombok | — |
| Maven Wrapper | — |

---

## 📐 Arquitetura

O projeto segue o padrão de camadas com separação clara de responsabilidades:

```
src/main/java/com/autoflex/inventory/
├── config/               # Configurações (CORS)
├── controller/           # Endpoints REST
├── dto/
│   ├── request/          # DTOs de entrada (com validação)
│   └── response/         # DTOs de saída
├── entity/               # Entidades JPA
├── exception/            # Tratamento global de erros
├── repository/           # Repositórios Spring Data
└── service/
    └── impl/             # Implementação dos serviços
```

### Entidades e Relacionamentos

```
Product (1) ──── (N) ProductRawMaterial (N) ──── (1) RawMaterial
```

- **Product** → `id`, `name`, `value`
- **RawMaterial** → `id`, `name`, `stockQuantity`
- **ProductRawMaterial** → `id`, `product`, `rawMaterial`, `requiredQuantity`

---

## 🔌 Endpoints da API

> Base URL: `http://localhost:8080/api`

### Produtos — `/products`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/products` | Listar todos os produtos |
| `GET` | `/products/{id}` | Buscar produto por ID |
| `POST` | `/products` | Criar novo produto |
| `PUT` | `/products/{id}` | Atualizar produto |
| `DELETE` | `/products/{id}` | Deletar produto |

**Body (POST/PUT):**
```json
{
  "name": "Cadeira de Escritório",
  "value": 450.00
}
```

### Matérias-Primas — `/raw-materials`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/raw-materials` | Listar todas |
| `GET` | `/raw-materials/{id}` | Buscar por ID |
| `POST` | `/raw-materials` | Criar nova |
| `PUT` | `/raw-materials/{id}` | Atualizar |
| `DELETE` | `/raw-materials/{id}` | Deletar |

**Body (POST/PUT):**
```json
{
  "name": "Madeira MDF",
  "stockQuantity": 500.0
}
```

### Associação Produto ↔ Matéria-Prima

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/products/{id}/raw-materials` | Associar matéria-prima ao produto |
| `DELETE` | `/products/raw-materials/{id}` | Remover associação |

**Body (POST):**
```json
{
  "rawMaterialId": "uuid-da-materia-prima",
  "requiredQuantity": 2.5
}
```

### Sugestão de Produção — `/production`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/production/suggestions` | Consultar produtos que podem ser produzidos |

**Response:**
```json
[
  {
    "productId": "uuid",
    "productName": "Cadeira de Escritório",
    "productValue": 450.00,
    "quantity": 12
  }
]
```

> A lógica prioriza produtos de **maior valor**, simulando o consumo sequencial do estoque em memória para calcular a quantidade máxima de cada produto que pode ser fabricado.

---

## ✅ Validação e Tratamento de Erros

### Validação (Bean Validation)

Os DTOs de entrada possuem validações com `@NotBlank`, `@NotNull`, `@Positive` e `@PositiveOrZero`. Os controllers utilizam `@Valid` para ativar a validação automática.

### Tratamento Global de Erros (`@RestControllerAdvice`)

| Exceção | Status HTTP | Quando ocorre |
|---|---|---|
| `ResourceNotFoundException` | `404` | Produto ou matéria-prima não encontrado |
| `MethodArgumentNotValidException` | `400` | Campos inválidos no body |
| `Exception` | `500` | Erro inesperado |

**Exemplo de resposta de erro (400):**
```json
{
  "timestamp": "2026-02-23T14:30:00",
  "status": 400,
  "error": "Falha na validação",
  "fields": {
    "name": "O nome do produto é obrigatório",
    "value": "O valor do produto deve ser positivo"
  }
}
```

---

## ⚙️ Configuração e Execução

### Pré-requisitos

- **Java 21+**
- **PostgreSQL** (local ou Supabase)

### Variáveis de Ambiente

| Variável | Descrição | Exemplo |
|---|---|---|
| `DB_URL` | URL JDBC do banco PostgreSQL | `jdbc:postgresql://host:5432/autoflex_db` |
| `DB_PASSWORD` | Senha do banco | `sua_senha` |

### Executar localmente

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/inventory.git
cd inventory

# Configure as variáveis de ambiente
export DB_URL=jdbc:postgresql://localhost:5432/autoflex_db
export DB_PASSWORD=postgres

# Execute a aplicação
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080/api`

### CORS

O CORS está configurado para aceitar requisições de qualquer origem, permitindo a integração com o front-end.

---

## 📋 Requisitos Atendidos

| Requisito | Descrição | Status |
|---|---|---|
| RF001 | CRUD de Produtos (back-end) | ✅ |
| RF002 | CRUD de Matérias-Primas (back-end) | ✅ |
| RF003 | Associação Matéria-Prima ↔ Produto (back-end) | ✅ |
| RF004 | Consulta de produção possível (back-end) | ✅ |
| RNF002 | Arquitetura API (back-end separado do front-end) | ✅ |
| RNF004 | Persistência em PostgreSQL | ✅ |
| RNF005 | Framework Spring Boot | ✅ |
| RNF007 | Codificação em inglês (entidades, tabelas, colunas) | ✅ |
