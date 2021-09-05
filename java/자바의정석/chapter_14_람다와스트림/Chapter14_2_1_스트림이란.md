# Chapter 14. 람다와 스트림 - 2. 스트림(Stream)

## 다수의 데이터를 다루는 기존 방식의 문제점

- 다수의 데이터를 다룰 때 컬렉션이나 배열에 데이터를 담고 for문과 Iterator를 사용해왔다.
- 허나 이 방식은 코드가 너무 길고 알아보기 어려우며 재사용성도 떨어진다.
- 또 다른 문제는 데이터 소스마다 다른 방식으로 다뤄야 한다는 것이다.<br>
  (정렬을 예로 들면 Collection에서는 Collections.sort() 를 사용하고 Array에서는 Arrays.sort() 를 사용해야 한다.)
- 이러한 문제점을 해결하기 위해 만든 것이 스트림이다.

<br>

## 2.1 스트림이란?

**스트림이란 데이터 소스를 추상화하여 데이터의 종류에 상관없이 일관된 방식으로 데이터를 다룰 수 있게 해주는 기능**으로 데이터를 다루는 데 자주 사용되는 메소드들을 정의해놓았다. 스트림을 사용한 코드는 간결하고 이해하기 쉬우며 재사용성도 높아진다.

```java
/**
 * 스트림을 이용하면 배열이나 컬렉션 뿐만 아니라 
 * 파일에 저장된 데이터도 모두 같은 방식으로 다룰 수 있다.
 */
String[] strArr = { "aaa", "ddd", "ccc" };
List<String> strList = Arrays.asList(strArr);

Arrays.stream(strArr).sorted().forEach(System.out::println);
strList.stream.sorted().forEach(System.out::println);
```

<br>

### 스트림의 특징

### 1. 스트림은 데이터 소스를 변경하지 않는다.

스트림은 데이터 소스로부터 읽기만할 뿐, 데이터 소스를 변경하지 않는다.

<br>

### 2. 스트림은 일회용이다.

Iterator로 컬렉션의 요소를 모두 읽고 나면 다시 사용할 수 없는 것처럼 스트림도 한번 사용하면 닫혀서 다시 사용할 수 없다. 필요하다면 스트림을 다시 생성해야 한다. 

```java
strStream1.sorted().forEach(System.out::println);
int numOfStr = strStream1.count();  // ERROR. 스트림이 이미 닫혔음
```

<br>

### 3. 스트림은 작업을 내부 반복으로 처리한다.

스트림을 이용한 작업이 간결할 수 있는 비결 중의 하나가 바로 '내부 반복'이다. 내부 반복이란 반복문을 메소드의 내부에 숨긴다는 것을 말한다. forEach() 는 스트림에 정의된 메소드 중의 하나로 매개변수에 대입된 람다식을 데이터 소스의 모든 요소에 적용한다.

```java
// forEach() 는 메소드 안으로 for문을 넣어 내부 반복으로 처리한다. 수행할 작업은 매개변수로 받는다.
void forEach(Consumer<? super T> action) {
    Objects.requireNonNull(action);  // 매개변수의 NULL 체크
    
    for (T t : src) {  // 내부 반복
        action.accept(t);
    }
}
```

<br>

### 4. 스트림의 연산

스트림은 다양한 연산을 제공해 복잡한 작업들을 간단히 처리할 수 있게 해준다. (마치 데이터베이스에 SELECT문으로 질의하는 것과 같은 느낌이다.)

<br>

> **연산(Operation)**<br>
> 
> 스트림에 정의된 메소드 중에서 데이터 소스를 다루는 작업을 수행하는 것을 연산이라고 한다.

<br>

**중간 연산과 최종 연산**

스트림이 제공하는 연산은 중간 연산과 최종 연산으로 분류할 수 있다.

- **중간 연산** : 연산 결과를 스트림으로 반환하여 중간 연산을 연속해서 연결할 수 있다.
- **최종 연산** : 연산 결과가 스트림이 아닌 연산. 스트림의 요소를 소모하므로 단 한번만 가능

```java
/**
 * 중간 연산 : distinct(), limit(), sorted()
 * 최종 연산 : forEach()
 */
stream.distinct().limit(5).sorted().forEach(System.out::println)
```

<br>

**Stream 클래스에 정의된 중간 연산(일부 생략)**

| 중간 연산 | 설명 |
|--------|--------|
|`Stream<T> distinct()`|중복을 제거|
|`Stream<T> filter(Predicate<T> predicate)`|조건에 안 맞는 요소 제외|
|`Stream<T> limit(long maxSize)`|스트림의 일부를 잘라낸다.|
|`Stream<T> skip(long n)`|스트림의 일부를 건너뛴다.|
|`Stream<T> peek(Consumer<T> action)`|스트림의 요소에 작업 수행|
|`Stream<T> sorted()`<br>`Stream<T> sorted(Comparator<T> comparator)`|스트림의 요소를 정렬한다.|
|`Stream<R> map(Function<T,R> mapper)`<br>`DoubleStream mapToDouble(ToDoubleFunction<T> mapper)`<br>`Stream<R> flatMap(Function<T,Stream<R>> mapper)`<br>`DoubleStream flatMapToDouble(Function<T,DoubleStream> m)`|스트림의 요소를 변환한다.|

<br>

**Stream 클래스에 정의된 최종 연산(일부 생략)**

| 최종 연산 | 설명 |
|--------|--------|
|`void forEach(Consumer<? super T> action)`<br>`void forEachOrdered(Consumer<? super T> action)`|각 요소에 지정된 작업 수행|
|`long count()`|스트림의 요소의 개수 반환|
|`Optional<T> max(Comparator<? super T> comparator)`<br>`Optional<T> min(Comparator<? super T> comparator)`|스트림의 최대값/최소값을 반환|
|`Optional<T> findAny()` // 아무거나 하나<br>`Optional<T> findFirst()` // 첫 번째 요소|스트림의 요소 하나를 반환|
|`boolean allMatch(Predicate<T> p)` // 모두 만족하는지<br>`boolean anyMatch(Predicate<T> p)` // 하나라도 만족하는지<br>`boolean noneMatch(Predicate<> p)` // 모두 만족하지 않는지|주어진 조건을 모든 요소가 만족하는지, 만족하지 않는지 확인|
|`Object[] toArray()`<br>`A[] toArray(IntFunction<A[]> generator)`|스트림의 모든 요소를 배열로 반환|
|`Optional<T> reduce(BinaryOperator<T> accmulator)`<br>`T reduce(T identity, BinaryOperator<T> accmulator)`<br>`U reduce(T identity, BiFunction<U,T,U> accmulator, BinaryOperator<U> combiner)`|스트림의 요소를 하나씩 줄여가면서(리듀싱) 계산한다.|
|`R collect(Collector<T,A,R> collector)`<br>`R collect(Supplier<R> supplier, BiConsumer<R,T> accmulator, BiConsumer<R,R> combiner)`|스트림의 요소를 수집한다. 주로 요소를 그룹화하거나 분할한 결과를 컬렉션에 담아 반환하는데 사용된다.|

<br>

> **Optional<T> class**<br>
> 
> 일종의 래퍼 클래스로 내부에 하나의 객체를 저장할 수 있다.

<br>

### 5. 지연된 연산

**스트림 연산은 최종 연산이 수행되기 전까지는 중간 연산이 수행되지 않는다. 중간 연산을 호출하는 것은 단지 어떤 작업이 수행되어야 하는지를 지정해주는 것일 뿐이다.** 최종 연산이 수행되어야 비로소 스트림의 요소들이 중간 연산을 거쳐 최종 연산에서 소모된다.

<br>

### 6. `Stream<Integer>`와 `IntStream`

오토박싱/언박싱으로 인한 비효율을 줄이기 위해 데이터 소스의 요소를 기본형으로 다루는 스트림으로 `IntStream`, `LongStream`, `DoubleStream` 이 제공된다. 일반적으로 `Stream<Integer>` 대신 `IntStream` 을 사용하는 것이 더 효율적이고, `IntStream` 에는 int 타입의 값으로 작업하는데 유용한 메소드들이 포함되어 있다.

<br>

### 7. 병렬 스트림

스트림으로 데이터를 다룰 때의 장점 중 하나는 병렬 처리가 쉽다는 것이다. 병렬 스트림은 내부적으로 fork & join 프레임워크를 이용해 자동적으로 연산을 병렬로 수행한다. 연산을 병렬로 수행하기 위해서는 스트림에 `parallel()` 을 호출해서 병렬로 연산을 수행하도록 지시하면 된다. 반대로 병렬 처리를 하지 않게 하려면 `sequential()` 을 호출하면 된다. (스트림은 기본이 병렬 스트림이 아니므로 sequential() 을 호출할 필요가 없다.) 이 메소드는 parallel() 을 호출한 것을 취소할 때만 사용한다.

```java
int sum = strStream.parallel()  // strStream을 병렬 스트림으로 전환
                   .mapToInt(s -> s.length())
                   .sum();
```

<br>

> **참고 사항**<br>
> 
> - parallel() 과 sequential() 은 새로운 스트림을 생성하는 것이 아니라 스트림의 속성을 변경할 뿐이다.
> - 병렬 처리가 항상 빠른 것은 아니다.

<br>

## 참고 자료
- 자바의 정석 3판