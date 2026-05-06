# Complex Type Converter

A Spring Boot framework for converting between complex Java types. It ships with a registry-based conversion engine and a set of built-in converters for JSON, XML, CSV, and temporal types. A Thymeleaf playground lets you try conversions directly in the browser.

## Tech stack

- Java 21
- Spring Boot 3.4.5
- Thymeleaf + Bootstrap 5
- Jackson (JSON + XML via `jackson-dataformat-xml`)
- Spring Validation
- JUnit 5 + MockMvc

## How it works

The framework is built around three core interfaces:

- **`Converter<S, T>`** — converts a value from type `S` to type `T`
- **`ConverterRegistry`** — stores and looks up converters by `TypePair(sourceType, targetType)`
- **`ConversionService`** — entry point that delegates to the registry

To avoid key collisions between formats, the built-in serializers use typed string wrappers: `JsonString`, `XmlString`, and `CsvString`. This means `Person → JsonString` and `Person → XmlString` are distinct type pairs.

## Built-in converters

| Source | Target | Description |
|--------|--------|-------------|
| `Person` / `Address` / `Product` / `Order` / `OrderItem` | `JsonString` | Serialize domain object to JSON |
| `JsonString` | domain object | Deserialize JSON to domain object |
| domain object | `XmlString` | Serialize domain object to XML |
| `XmlString` | domain object | Deserialize XML to domain object |
| `CsvString` | `List` | Parse CSV rows to `List<Map<String, String>>` |
| `List` | `CsvString` | Render `List<Map<String, String>>` to CSV |
| `String` | `LocalDate` | Parse ISO date string (yyyy-MM-dd) |
| `LocalDate` | `String` | Format date to ISO string |
| `String` | `LocalDateTime` | Parse ISO datetime string |

## REST API

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/converters` | List all registered converter pairs |
| `GET` | `/api/types` | List all known type names |
| `POST` | `/api/convert` | Perform a conversion |

**POST /api/convert** request body:
```json
{
  "sourceType": "String",
  "targetType": "LocalDate",
  "value": "2024-06-15"
}
```

## How to run

```bash
./mvnw spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) to use the conversion playground.

To run tests:

```bash
./mvnw test
```
