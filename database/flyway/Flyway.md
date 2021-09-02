## Flyway 란?

- Flyway는 데이터베이스 스키마 변경에 대한 이력 관리를 위해 사용하는 오픈소스 데이터베이스 마이그레이션 라이브러리이다.

<br>

## 동작 원리

```
1. sql 파일 작성

2. flyway가 sql 파일을 실행하고자 할 때 DB에 *flyway_schema_history 테이블이 존재하는지 
   확인하고 없다면 flyway가 새로 생성한다.

*flyway_schema_history: flyway가 변경 이력을 관리하는 metadata table
  
3. flyway는 마이그레이션을 위해 지정된 파일(sql or java)을 지정된 classpath에서 
   탐색하여 버전 순서대로 실행한다.

*flyway는 마이그레이션 대상 파일의 version을 파일명을 통해서 구분한다.
```

<br>

## 마이그레이션 파일명 작성 규칙

```
-- 기본 형식
{prefix}{version}__{description}.{suffix}

-- 버전 마이그레이션(Version migration)용 파일명
V2__Add_new_table.sql

-- 반복 마이그레이션(Repeatable migration)용 파일명
R__Create_view.sql
```

<br>

**`prefix`**

- V(default)는 버전 마이그레이션, R은 반복 마이그레이션용 접두사이다.
- 반드시 V 또는 R로 시작해야만 flyway가 인식한다.

<br>

**`version`**

- 버전 마이그레이션에서만 사용되며 숫자와 점이나 언더바 조합으로 구성한다.
- 반복 마이그레이션에서 version 명시하면 파일명 제약 위반으로 에러가 발생한다.

<br>

**`separator`**

- 설명 부분을 구분하기 위한 구분자로 반드시 언더바를 2개 써야 한다.

<br>

**`description`**

- 이 부분은 schema_version 테이블에 저장할 때 설명으로 사용된다.

<br>

**`suffix`**

- 확장자로 기본은 .sql 을 사용한다.

<br>

> **반복 마이그레이션(Repeatable migration)**<br>
> 
> - 버전 마이그레이션을 사용하면 하나의 파일은 딱 한 번만 수행되도록 되어 있다. 하지만 반복 마이그레이션을 설정하면 체크섬이 변경될 때마다 마이그레이션 파일이 적용되며, 마이그레이션 파일은 버전별 여러 파일이 아니라 하나의 파일로 관리된다.<br>
> 
> - 참고 자료 : [https://flywaydb.org/documentation/tutorials/repeatable#modifying-the-migration](https://flywaydb.org/documentation/tutorials/repeatable#modifying-the-migration)

<br>

> **Flyway의 checksum**<br>
> - migration script가 DB에 적용된 이후 변경되지 않았는지 확인하는 검증 과정에 사용되는 필드다.<br>
> - Flyway는 CRC32 클래스를 사용하여 체크섬을 계산한다.<br>
> 
> - 참고 자료 : [https://stackoverflow.com/questions/43267202/flyway-the-meaning-of-the-concept-of-checksums](https://stackoverflow.com/questions/43267202/flyway-the-meaning-of-the-concept-of-checksums)

<br>

## SQL Syntax

마이그레이션 대상 파일에 사용할 수 있는 SQL Script 구문은 다음과 같다.

- Single 또는 multi-line 구문 지원
- placeholder 치환 지원
- Single(—) 또는 multi-line(/* */) 주석 지원
- Database 별 SQL 구문 확장 지원(PL/SQL, T-SQL, ...)

```sql
/* Single line comment */
CREATE TABLE test_user (
    name VARCHAR(25) NOT NULL,
    PRIMARY KEY(name)
);

/*
    Multi-line 
    Comment
*/
-- Placeholder
INSERT INTO ${tableName} (name) VALUES ('Mr. T');
```

<br>

## Command(실행 명령)

flyway는 총 6개의 명령을 지원하며 이를 실행하는 방법은 Execution mode에 따라 달라진다.

<br>

**`Migrate`**

- database 마이그레이션

<br>

**`Info`**

- 모든 마이그레이션 상세 정보를 출력한다.

<br>

**`Validate`**

- database에 적용된 마이그레이션 정보의 유효성을 검증한다.

<br>

**`Baseline`**

- 기존 database가 있는 경우 기존 database의 특정 버전을 기준으로 마이그레이션을 수행하기 위한 명령이다.
- 이렇게 할 경우 특정 버전까지의 모든 migration을 무시할 수 있다.

<br>

**`Repair`**

- 실패한 마이그레이션 항목을 제거한다. (DDL 트랜잭션을 지원하지 않는 database에만 해당)
- 적용된 마이그레이션의 체크섬, 설명 및 유형을 사용 가능한 마이그레이션 중 하나로 재정렬한다.
- 누락된 모든 마이그레이션을 삭제된 것으로 표시한다.

<br>

> **MySQL은 DDL 트랜잭션을 지원하지 않는다.**<br>
> 
> 즉, DDL 변경 사항에 대한 트랜잭션을 지원하지 않는다.

<br>

**`clean`**

- database의 schema_version 테이블을 포함한 모든 objects(tables, views, procedures, ...)를 drop 시킨다.
- 운영 환경에서는 절대 실행해서는 안된다.

<br>

## Execution Modes(실행 방법)

총 6가지를 지원한다. 형상 관리를 사용하는 Project에서는 maven, gradle에 flyway를 통합하여 실행하는 것이 Build Phase 별로 Command를 선택 실행할 수 있으므로 이 방식을 추천한다.

- `Command-line` : 콘솔에서 명령을 입력하여 실행하는 방법
- `API` : API를 이용하여 실행
- `Maven` : Maven에 통합하여 실행
- `Gradle` : Gradle에 통합하여 실행
- `Ant` : Ant에 통합하여 실행
- `SBT` : SBT(Scala 빌드 도구)에 통합하여 실행

<br>

## 참고 자료

- [나만 모르고 있던 - Flyway (DB 마이그레이션 Tool)ㅣ심천보님](https://www.popit.kr/%EB%82%98%EB%A7%8C-%EB%AA%A8%EB%A5%B4%EA%B3%A0-%EC%9E%88%EB%8D%98-flyway-db-%EB%A7%88%EC%9D%B4%EA%B7%B8%EB%A0%88%EC%9D%B4%EC%85%98-tool/)
- [Flyway 로 Java 에서 DB schema, seed 관리하기ㅣ강남언니 공식 블로그](https://blog.gangnamunni.com/post/introducing-flyway/)
- [Flyway documentationㅣflywaydb.org](https://flywaydb.org/documentation/database/mysql#sql-script-syntax)
- [Database Migrations with FlywayㅣBaeldung](https://www.baeldung.com/database-migrations-with-flyway)