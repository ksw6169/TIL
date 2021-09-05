## 2.4 `Optional<T>`와 `OptionalInt`

**`Optional<T>`은 제네릭 클래스로 T 타입의 객체를 감싸는 래퍼 클래스**이다. 그래서 `Optional` 타입의 객체에는 모든 타입의 참조변수를 담을 수 있다. 참고로 `java.util.Optional` 은 JDK 1.8부터 추가되었다.

```java
public final class Optional<T> {
    private final T value;  // T 타입의 참조 변수
    ...
}
```

스트림에서 최종 연산의 결과 타입이 Optional인 경우가 있는데 최종 연산의 결과를 그냥 반환하는 게 아니라 Optional 객체에 담아서 반환한다. 이처럼 객체에 담아서 반환을 하면 반환된 결과가 null인지 매번 if 문으로 체크하는 대신 Optional에 정의된 메소드를 통해 간단히 처리할 수 있다. 

<br>

> **참고 사항**<br>
>
> Objects 클래스에 isNull(), nonNull(), requireNonNull() 과 같은 메소드가 있는 것도 null 체크를 위한 if 문을 메소드 안으로 넣어서 코드의 복잡도를 낮추기 위한 것이다.

<br>

### Optional 객체 생성하기

Optional 객체를 생성할 때는 of() 또는 ofNullable() 을 사용한다.

```java
String str = "abc";
Optional<String> optVal = Optional.of(str);
Optional<String> optVal = Optional.of("abc");
Optional<String> optVal = Optional.of(new String("abc"));
```

만일 참조 변수의 값이 null 일 가능성이 있으면 of() 대신 ofNullable() 을 사용해야 한다. of() 는 매개변수의 값이 Null이면 NPE가 발생하기 때문이다.

```java
Optional<String> optVal = Optional.of(null);         // NPE 발생
Optional<String> optVal = Optional.ofNullable(null); // OK
```

`Optional<T>` 타입의 참조 변수를 기본값으로 초기화할 때는 empty() 를 사용한다. null로 초기화하는 것이 가능하지만 empty() 로 초기화하는 것이 바람직하다.

```java
Optional<String> optVal = null;             // null로 초기화
Optional<String> optVal = Optional.empty(); // 빈 객체로 초기화
```

<br>

### Optional 객체의 값 가져오기

Optional 객체에 저장된 값을 가져올 때는 get() 을 사용한다. 값이 null일 때는 NoSuchElementException이 발생하며, 이를 대비해서 orElse() 로 대체할 값을 지정할 수 있다.

```java
Optional<String> optVal = Optional.of("abc");
String str1 = optVal.get();             // optVal에 저장된 값을 반환, null이면 예외 발생
String str2 = optVal.orElse("");  // optVal에 저장된 값이 null일 때는 "" 반환
```

orElse() 의 변형으로는 null을 대체할 값을 반환하는 람다식을 지정할 수 있는 orElseGet() 과 null일 때 지정된 예외를 발생시키는 orElseThrow() 가 있다.

```java
T orElseGet(Supplier<? extends T> other)
T orElseThrow(Supplier<? extends X> exceptionSupplier)
```

```java
Optional<String> optVal2 = Optional.of("data");

String str3 = optVal2.orElseGet(String::new);                   // () -> new String() 과 동일
String str4 = optVal2.orElseThrow(NullPointerException::new);   // null이면 예외 발생
```

Stream처럼 Optional 객체에도 filter(), map(), flatMap() 을 사용할 수 있다. map()의 연산 결과가 `Optional<Optional<T>>` 일 때, flatMap() 을 사용하면 `Optional<T>` 를 결과로 얻는다. 만일 Optional 객체의 값이 null이면 이 메소드들은 아무 일도 하지 않는다.

```java
// result = 123
int result = Optional.of("123")
                     .filter(x -> x.length() > 0)
                     .map(Integer::parseInt)
                     .orElse(-1);

// result = -1
result = Optional.of("")
                 .filter(x -> x.length() > 0)
                 .map(Integer::parseInt)
                 .orElse(-1);
```

<br>

> **isPresent(), ifPresent(Consumer<T> block)**<br>
> 
> - isPresent() 는 Optional 객체의 값이 null이면 false를, 아니면 true를 반환한다.
> - ifPresent(`Consumer<T> block`) 은 값이 있으면 주어진 람다식을 실행하고 없으면 아무 일도 하지 않는다.

```java
String str = null;

// 이 구문은 Optional.ofNullable(str).ifPresent(System.out::println); 로 대체할 수 있다.
if (Optional.ofNullable(str).isPresent())
    System.out.println(str);
```

<br>

### OptionalInt, OptionalLong, OptionalDouble

IntStream과 같은 기본형 스트림에는 기본형을 값으로 하는 OptionalInt, OptionalLong, OptionalDouble을 반환하는 메소드들이 존재한다. 

```java
// IntStream에서 OptionalInt를 반환하는 메소드들
OptionalInt findAny()
OptionalInt findFirst()
OptionalInt reduce(IntBinaryOperator op)
OptionalInt max() 
OptionalInt min()
OptionalDouble average()
```

참고로 Optional에 저장된 값을 꺼낼 때 사용하는 메소드들의 이름이 조금씩 다르다.

```java
T get()              // Optional<T>
int getAsInt()       // OptionalInt
long getAsLong()     // OptionalLong
double getAsDouble() // OptionalDouble
```

<br>

## 참고 자료
- 자바의 정석 3판