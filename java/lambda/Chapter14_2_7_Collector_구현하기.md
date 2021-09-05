## 2.7 Collector 구현하기

Collector 클래스가 제공하는 메소드 외에 Collector 인터페이스를 구현하여 직접 Collector 클래스를 만들어 사용할 수도 있다.

```java
public interface Collector<T, A, R> {
    Supplier<A> supplier();
    BiConsumer<A, T> accmulator();
    BinaryOperator<A> combiner();
    Function<A, R> finisher();

    Set<Characteristics> characteristics(); // 컬렉션의 특성이 담긴 Set을 반환
    ...
}
```

직접 구현해야 하는 것은 위 5개 메소드로 characteristics() 를 제외하면 반환형이 모두 함수형 인터페이스다.

```
supplier()        작업 결과를 저장할 공간을 제공
accmulator()      스트림의 요소를 수집(collect) 할 방법을 제공
combiner()        두 저장공간을 병합할 방법을 제공(병렬 스트림)
finisher()         결과를 최종적으로 변환할 방법을 제공
characteristics() 컬렉터가 수행하는 작업의 속성에 대한 정보 제공 
```

characteristics() 는 컬렉터가 수행하는 작업의 속성에 대한 정보를 제공하기 위한 것으로 아래의 3가지 속성 중에서 해당하는 것을 다음과 같이 Set에 담아서 반환하도록 구현하면 된다.

```
Characteristics.CONCURRENT      병렬로 처리할 수 있는 작업
Characteristics.UNORDERED       스트림의 요소가 순서가 유지될 필요가 없는 작업
Characteristics.IDENTITY_FINISH finisher() 가 항등 함수인 작업
```

```java
@Override
public Set<Characteristics> characteristics() {
    // 아무런 속성도 지정하고 싶지 않다면 Collections.emptySet() 을 사용하면 된다.
    return Collections.unmodifiableSet(EnumSet.of(
            Characteristics.CONCURRENT,
            Characteristics.UNORDERED
    ));
}
```

- Collector 인터페이스를 구현한 Custom Collector 예제는 다음과 같다.
- Collector를 제공하면 collect() 시에 Collector의 supplier() -> accmulator() -> combiner() -> finisher() 순으로 메소드를 호출한다.

```java
public class ConcatCollector implements Collector<String, StringBuilder, String> {

    public static void main(String[] args) {
        // result = aaabbbccc
        String result = Stream.of("aaa", "bbb", "ccc")
                .collect(new ConcatCollector());
    }

    @Override
    public Supplier<StringBuilder> supplier() {
        return () -> new StringBuilder();
    }

    @Override
    public BiConsumer<StringBuilder, String> accumulator() {
        return (sb, s) -> sb.append(s);
    }

    @Override
    public BinaryOperator<StringBuilder> combiner() {
        return (sb, sb2) -> sb.append(sb2);
    }

    @Override
    public Function<StringBuilder, String> finisher() {
        return sb -> sb.toString();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
```

<br>

### reduce(), collect() 의 차이점

- 기본적으로 하는 일은 같다.
- collect는 그룹화와 분할, 집계 등에 주로 쓰이고 병렬화에 있어서 reduce보다 유리하다.

<br>

## 참고 자료
- 자바의 정석 3판