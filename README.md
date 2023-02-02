### 사용사항
    - p6spy
    - jdk 17
    - springboot 3.x

### 목표
    - 개발(local,dev)환경에서만 p6spy를 이용한 sql 로깅 구현
    - 운영의 경우 spring 기본 logback 로깅 사용

### 구현
#### 1. 의존성 추가 (p6spy)
    <dependency>
        <groupId>p6spy</groupId>
        <artifactId>p6spy</artifactId>
        <version>3.9.1</version>
    </dependency>
<br>

#### 2. profile 구조
 - local 환경 시 logging-starter에 내장된 로깅으로 동작하도록 설정

<br>

#### 3. P6SpyFormatter 설정 등록 확인
 Formatter를 설정하기 위한 Bean을 생성 후 등록한다.
P6Spy의 경우 P6Proxy가 등록되면서 P6Options의 설정들을 통해 로깅에 관련된 설정을 지정한다.
이 때문에 커스텀된 설정을 등록하기 위해서는 P6Spy 라이브러리가 설정된 후 변경해줘야한다.
현재 JDK 버전이 17인 관계로 Bean 초기화 후 설정을 등록한다.

```java
    @Override
    public void afterPropertiesSet() throws Exception {
        if (P6SpyOptions.getActiveInstance() != null) {
            P6SpyOptions.getActiveInstance().setLogMessageFormat(P6SpySqlFormatter.class.getName());
        }
    }
```

JDK8 이전의 경우 @PostConstruct를 이용한 초기화 진행

```java
    @Test
    void P6SpyConfigureLoaded() {
        P6SpySqlFormatConfigure p6SpySqlFormatConfigure = context.getBean(P6SpySqlFormatConfigure.class);
        assertNotNull(p6SpySqlFormatConfigure);
    }
```

Bean이 정상적으로 등록되는지 확인한다.


<br>

#### 4. P6SpyFormatter 설정 등록 확인
P6Spy는 Formatting의 경우 MessageFormattingStrategy을 구현한 구현체들을 기본적으로 사용한다.
내부적으로는 MultiLineFormat, CommandLineFormat 등이 존재하는 것을 확인하였다.

```java
    public class MultiLineFormat implements MessageFormattingStrategy { 
        ...
```

MessageFormattingStrategy을 구현한 구현체를 P6Options에 등록해주면 커스터마이징된 SQL 로깅을 구현할 수 있다.

<br>

#### 5. SQL 로깅 커스터마이징

```java
    @Override
    public String formatMessage(int connectionId, String now, 
                                  long elapsed, String category, String prepared, String sql, String url) {
      if (ObjectUtils.isEmpty(sql)) {
        return "";
      }

      return new StringBuilder()
        .append(SQL_DATE_FORMAT.format(new Date()) + " | " + elapsedInfo(elapsed))
        .append(formatSql(category, sql))
        .append(NEW_LINE)
        .append(NEW_LINE)
        .append("[Stack Trace Information]")
        .append(stackTraceInfo())
        .toString();
    }
```

formatMessage()을 구현해주면 된다.<br>
전반적인 커스텀 코드는 formatSql(), stackTraceInfo()를 통해 구현하였다.<br>
이중 stackTraceInfo()는 해당 쿼리를 호출한 call stack이 기본적으로 출력되지 않아 추가하였다.<br>
------
- P6Spy: [사이트](https://github.com/p6spy)
- 참고코드: [사이트](https://www.tabnine.com/code/java/methods/com.p6spy.engine.spy.appender.MessageFormattingStrategy/formatMessage)
