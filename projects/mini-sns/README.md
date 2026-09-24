# Mini SNS

Ruby / Javaで学んだHTTP、REST、認証、DB、Connection Pool、JVMを1つのSpring Bootアプリへ統合するプロジェクト。

## Stack

Java 21 / Spring Boot / Spring MVC / Spring Data JPA / PostgreSQL / Flyway / HikariCP / Spring Security / Lombok

## 起動手順（OrbStack）

1. OrbStackを起動する。
2. PostgreSQLを起動する。

```bash
cd projects/mini-sns
docker compose up -d
```

3. アプリを起動する。

```bash
./mvnw spring-boot:run
# Maven Wrapperがまだ無い環境では
mvn spring-boot:run
```

Flywayが起動時にmigrationを適用する。DB schemaを変更するときはHibernateの自動変更ではなく、`db/migration` に次のmigrationを追加する。

停止:

```bash
docker compose down
```

DBも作り直す場合だけ:

```bash
docker compose down -v
```

## ER図

`docs/er.puml` がDB設計のsource of truth。
