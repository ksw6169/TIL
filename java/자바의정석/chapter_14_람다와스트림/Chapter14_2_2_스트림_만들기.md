## 2.2 스트림 만들기

- 스트림의 소스가 될 수 있는 대상은 배열, 컬렉션, 임의의 수 등 다양하다.
- 이러한 소스들로부터 스트림을 생성하는 방법에 대해 알아보자.

<br>

### Collection → Stream

Collection에 정의된 stream() 을 사용하면 해당 컬렉션을 소스로 하는 스트림을 반환한다.

```java
Stream<T> Collection.stream()
```

```java
/**
 * 예제 : Collection -> Stream
 */
List<Integer> list = Arrays.asList(1,2,3,4,5);  // 가변 인자
Stream<Integer> intStream = list.stream();      // list를 소스로 하는 컬렉션 생성

// forEach() 는 지정된 작업을 스트림의 모든 요소에 대해 수행한다.
intStream.forEach(System.out::println);  // 스트림의 모든 요소를 출력한다.
intStream.forEach(System.out::println);  // ERROR. 스트림이 이미 닫혔다.
```

<br>

### Array → Stream

Stream과 Array의 static 메소드를 사용하면 해당 배열을 소스로 하는 스트림을 반환한다.

```java
Stream<T> Stream.of(T... values) // 가변 인자
Stream<T> Stream.of(T[])
Stream<T> Arrays.stream(T[])
Stream<T> Arrays.stream(T[] array, int startInclusive, int endExclusive)
```

```java
/**
 * 예제 : Array -> Stream
 */
Stream<String> strStream = Stream.of("a", "b", "c");
Stream<String> strStream = Stream.of(new String[]{"a", "b", "c"});
Stream<String> strStream = Arrays.stream(new String[]{"a", "b", "c"});
Stream<String> strStream = Arrays.stream(new String[]{"a", "b", "c"}, 0, 3);
```

int, long, double과 같은 기본형 배열을 소스로 하는 스트림을 생성하는 메소드도 있다.

```java
IntStream IntStream.of(int... values)  // Stream이 아닌 IntStream
IntStream IntStream.of(int[])
IntStream Arrays.stream(int[])
IntStream Arrays.stream(int[] array, int startInclusive, int endExclusive)
```

<br>

### 특정 범위의 정수 → Stream

IntStream과 LongStream은 지정된 범위의 연속된 정수를 스트림으로 생성해서 반환하는 `range()` 와 `rangeClosed()` 를 가지고 있다.

```java
IntStream IntStream.range(int begin, int end)        // begin ~ end-1 범위의 정수 반환
IntStream IntStream.rangeClosed(int begin, int end)  // begin ~ end 범위의 정수 반환
```

<br>

### 임의의 수 → Stream

Random 클래스에는 난수들을 담은 스트림을 반환하는 인스턴스 메소드들이 제공된다.

```java
IntStream ints()                     // 무한 스트림
IntStream ints(long streamSize)      // 지정된 개수만 반환
IntStream ints(int begin, int end)   // 지정된 범위의 난수를 발생시키는 스트림(end는 포함되지 않음)
IntStream ints(long streamSize, int begin, int end)

LongStream longs()
LongStream longs(long streamSize)
LongStream longs(long begin, long end)
LongStream longs(long streamSize, long begin, long end)

DoubleStream doubles()
DoubleStream doubles(long streamSize)
DoubleStream doubles(double begin, double end)
DoubleStream doubles(long streamSize, double begin, double end)
```

위 메소드들에 의해 생성된 스트림의 난수는 아래의 범위를 갖는다.

```java
Integer.MIN_VALUE <= ints() <= Integer.MAX_VALUE
Long.MIN_VALUE <= longs() <= Long.MAX_VALUE
0.0 <= doubles() < 1.0
```

사용 예제는 다음과 같다.

```java
IntStream intStream = new Random().ints();       // 무한 스트림
intStream.limit(5).forEach(System.out::println); // 5개의 요소만 출력
```

<br>

### 람다식 → Stream

- Stream 클래스의 `iterate()` 와 `generate()` 는 람다식을 매개변수로 받아서 람다식에 의해 계산되는 값들을 요소로 하는 무한 스트림을 생성한다.

```java
// seed 값부터 시작해서 람다식 f에 의해 계산된 결과를 다시 seed 값으로 해서 계산을 반복한다.
static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)

// iterate와 동일하나 이전 결과를 이용해서 다음 요소를 계산하지 않는다.
static <T> Stream<T> generate(Supplier<T> s) 
```

사용 예제는 다음과 같다.

```java
// 전부 무한 스트림을 반환한다.
Stream<Integer> evenStream = Stream.iterate(0, n->n+2);      // 0, 2, 4, 6, ...
Stream<Double> randomStream = Stream.generate(Math::random); // 난수, 난수, 난수, ... 
Stream<Integer> oneStream = Stream.generate(()->1);          // 1, 1, 1, ...
```

주의할 점은 `iterate()` 와 `generate()` 에 의해 생성된 스트림은 기본형 스트림의 참조 변수로 다룰 수 없다는 점이다.

```java
IntStream evenStream = Stream.iterate(0, n->n+2);         // ERROR
DoubleStream randomStream = Stream.iterate(Math::random); // ERROR

// 굳이 필요하다면 mapToInt() 와 같은 메소드로 변환을 해야 한다.
IntStream evenStream = Stream.iterate(0, n->n+2).mapToInt(Integer::valueOf);

// IntStream -> Stream<Integer>
Stream<Integer> stream = evenStream.boxed();
```

<br>

### 파일 → Stream

`java.nio.file.Files.list()` 는 지정된 디렉토리에 있는 파일의 목록을 소스로 하는 스트림을 생성해 반환한다.

```java
Stream<Path> Files.list(Path dir)
```

파일의 한 행(line)을 요소로 하는 스트림을 생성하는 메소드도 있다.

```java
Stream<String> Files.lines(Path path)
Stream<String> Files.lines(Path path, Charset cs)
Stream<String> lines()  // BufferedReader 클래스의 메소드
```

<br>

### 빈 스트림

요소가 하나도 없는 빈 스트림을 생성할 수도 있다. 스트림에 연산을 수행한 결과가 하나도 없을 때 null 보다는 빈 스트림을 반환하는 것이 낫다.

```java
Stream emptyStream = Stream.empty(); // empty()는 빈 스트림을 생성해서 반환한다.
long count = emptyStream.count();    // count()는 스트림 요소의 개수를 반환한다. (여기서는 0)
```

<br>

### 두 스트림의 연결

`Stream.concat()` 을 사용하면 두 스트림을 하나로 연결할 수 있다. (당연히 연결하려는 두 스트림의 요소는 같은 타입이어야 한다.)

```java
List<String> str1 = Arrays.asList("123", "456", "789");
List<String> str2 = Arrays.asList("ABC", "DEF", "GHI");

// 두 스트림을 하나로 연결(str1 -> str2 순으로 연결된다.)
Stream<String> stream = Stream.concat(str1.stream(), str2.stream());
stream.forEach(System.out::println);
```

<br>

## 참고 자료
- 자바의 정석 3판