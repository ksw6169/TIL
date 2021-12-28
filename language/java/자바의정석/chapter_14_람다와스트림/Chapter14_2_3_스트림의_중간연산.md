## 2.3 스트림의 중간 연산

### 스트림 자르기 - skip(), limit()

skip() 과 limit() 은 스트림의 일부를 잘라낼 때 사용한다. skip(3)은 처음 3개의 요소를 건너뛰고, limit(5)는 스트림의 요소를 처음 5개로 제한한다. 

```java
// 기본형 스트림에도 skip과 limit이 정의되어 있다.
Stream<T> skip(long n)
Stream<T> limit(long maxSize)
```

```java
IntStream.rangeClosed(1,10).skip(3).forEach(System.out::println);  // 4 ~ 10
IntStream.rangeClosed(1,10).limit(3).forEach(System.out::println); // 1 ~ 3
```

<br>

### 스트림의 요소 걸러내기 - filter(), distinct()

distinct() 는 스트림에서 중복된 요소들을 제거하고, filter() 는 주어진 조건(Predicate)에 맞지 않는 요소를 걸러낸다. 필요하다면 filter() 를 다른 조건으로 여러 번 사용할 수도 있다.

```java
Stream<T> filter(Predicate<? super T> predicate)
Stream<T> distinct()
```

```java
IntStream.of(1, 2, 2, 3, 3).distinct().forEach(System.out::println);            // 1,2,3
IntStream.rangeClosed(1, 10).filter(i -> i%2==0).forEach(System.out::println);  // 2,4,6,8,10
```

<br>

### 정렬 - sorted()

스트림을 정렬할 때는 sorted() 를 사용하면 된다. 

```java
Stream<T> sorted()
Stream<T> sorted(Comparator<? super T> comparator)
```

sorted() 는 지정된 Comparator로 스트림을 정렬하는데, Comparator 대신 int 값을 반환하는 람다식을 사용하는 것도 가능하다. Comparator를 지정하지 않으면 스트림 요소의 기본 정렬 기준(Comparable)으로 정렬한다. (단, 스트림의 요소가 Comparable을 구현한 클래스가 아니라면 예외가 발생한다.)

```java
// CC, aaa, b, cc, dd
Arrays.asList("dd", "aaa", "CC", "cc", "b").stream().sorted().forEach(System.out::println);
```

JDK 1.8부터 Comparator 인터페이스에 static 메소드와 default 메소드가 많이 추가되었는데 이 메소드들을 사용하면 정렬이 쉬워진다. 이 메소드들은 모두 `Comparator<T>` 를 반환한다.

```java
// Comparator의 default 메소드
reversed()
thenComparing(Comparator<T> other)
...

// Comparator의 static 메소드
naturalOrder()
reverseOrder()
...
```

정렬에 사용되는 메소드의 개수가 많지만 가장 기본적인 메소드는 comparing() 이다. <br>
스트림의 요소가 Comparable을 구현한 경우 매개변수 하나 짜리를 사용하면 되고 그렇지 않은 경우 추가적인 매개변수로 정렬 기준(Comparator) 을 따로 지정해줘야 한다.

```java
comparing(Function<T, U> keyExtractor)
comparing(Function<T, U> keyExtractor, Comparator<U> keyComparator)
```

비교 대상이 기본형인 경우, comparing() 대신 아래 메소드들을 사용하면 오토박싱/언박싱 과정이 없어 더 효율적이다. 

```java
comparingInt(ToIntFunction<T> keyExtractor)
comparingLong(ToLongFunction<T> keyExtractor)
comparingDouble(ToDoubleFunction<T> keyExtractor)
```

그리고 정렬 조건을 추가할 때는 thenComparing() 을 사용한다. 앞에서 동일할 경우 thenComparing() 으로 지정한 비교 구문이 수행된다.

```java
thenComparing(Comparator<T> other)
thenComparing(Function<T, U> keyExtractor)
thenComparing(Function<T, U> keyExtractor, Comparator<U> keyComp)
```

예를 들어 학생 스트림을 반별, 성적순, 이름순으로 정렬하여 출력하려면 다음과 같이 한다.

```java
@AllArgsContructor
@Getter
@ToString
private static class Student {
    private double totalScore;
    private int ban;
    private String name;
}

public static void main(String[] args) {
    Stream<Student> studentStream = Stream.of(
            new Student("이자바", 3, 300),
            new Student("김자바", 1, 200),
            new Student("안자바", 2, 100),
            new Student("박자바", 2, 150),
            new Student("소자바", 1, 200),
            new Student("나자바", 3, 290),
            new Student("감자바", 3, 180));

    // 김-소-안-박-감-나-이
    studentStream.sorted(Comparator.comparing(Student::getBan)  // 반별 내림차순 정렬
            .thenComparing(Student::getTotalScore)              // 총점 내림차순 정렬(반이 같다면 수행됨)
            .thenComparing(Student::getName))                   // 이름 내림차순 정렬(반, 총점이 같다면 수행됨)
            .forEach(System.out::println);
}
```

<br>

### 변환 - map()

스트림의 요소에 저장된 값 중에서 원하는 필드만 뽑아내거나 특정 형태로 변환해야 할 때 map() 을 사용한다. 

```java
// 매개변수로 T 타입을 R 타입으로 변환해서 반환하는 함수를 지정해야 한다.
Stream<R> map(Function<? super T, ? extends R> mapper)
```

```java
Stream<File> fileStream = Stream.of(
        new File("Ex1.java"),
        new File("Ex1"),
        new File("Ex1.bak"));

fileStream.map(File::getName)                       // Stream<File> -> Stream<String>
        .filter(s -> s.indexOf('.') != -1)          // 확장자가 없는 것은 제외
        .map(s -> s.substring(s.indexOf('.')+1))   // Stream<String> -> Stream<String>
        .map(String::toUpperCase)                  // 대문자로 변환
        .distinct()                                // 중복 제거
        .forEach(System.out::print);               // 출력
```

<br>

### 조회 - peek()

peek() 은 filter() 나 map() 의 결과를 확인할 때 유용하게 사용될 수 있다. <br>
forEach() 와 달리 스트림의 요소를 소모하지 않으므로 연산 사이에 여러 번 끼워 넣어도 문제가 되지 않는다.

```java
fileStream.map(File::getName)
        .filter(s -> s.indexOf('.') != -1)                  // 확장자가 없는 것은 제외
        .peek(s -> System.out.printf("filename=%s%n", s))   // 파일명 출력
        .map(s -> s.substring(s.indexOf('.')+1))           // 확장자만 추출
        .peek(s -> System.out.printf("extension=%s%n", s)) // 확장자 출력
        .forEach(System.out::println);
```

<br>

### mapToInt(), mapToLong(), mapToDouble()

map() 은 연산의 결과로 Stream<T> 타입을 반환하는데, 스트림의 요소를 숫자로 변환하는 경우 IntStream 과 같은 기본형 스트림으로 변환하는 것이 더 유용할 수 있다. 

```java
// Stream<T> 타입의 스트림을 기본형 스트림으로 변환할 때 사용하는 메소드
DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper)
IntStream mapToInt(ToIntFunction<? super T> mapper)
LongStream mapToLong(ToLongFunction<? super T> mapper)
```

```java
// mapToInt를 통해 얻은 IntStream을 이용하면 별도의 형변환이 필요하지 않다.
IntStream studentScoreStream = studentStream.mapToInt(Student::getTotalScore);
int allTotalScore = studentScoreStream.sum(); // int sum();
```

count() 만 지원하는 `Stream<T>` 와 달리 기본형 스트림은 아래와 같이 숫자를 다루는데 편리한 메소드들을 제공한다.

```java
int sum()                 // 스트림의 모든 요소의 총합
OptionalDouble average()  // sum() / (double) count()
OptionalInt max()         // 스트림의 요소 중 제일 큰 값
OptionalInt min()         // 스트림의 요소 중 제일 작은 값
```

스트림의 요소가 하나도 없을 때 sum() 은 0을 반환하면 되지만, average() 와 같은 메소드에서는 단순히 0을 반환할 수 없으므로 이를 구분하기 위해 내부에 double, int 타입의 값을 가지는 래퍼 클래스인 OptionalDouble, OptionalInt 와 같은 타입의 객체를 반환한다.

<br>

그리고 이 메소드들은 최종 연산이므로 호출 후에 스트림이 닫힌다는 점에 주의해야 한다. 따라서 sum() 과 average() 와 같은 메소드들을 연속해서 사용할 수 없게 되는데, 이를 위해 summaryStatistics() 라는 메소드가 따로 제공된다.

```java
IntSummaryStatistics stat = scoreStream.summaryStatistics();
long totalCount = stat.getCount();
long totalScore = stat.getSum();
double avgScore = stat.getAverage();
int minScore = stat.getMin();
int maxScore = stat.getMax();
```

기본형 스트림 LongStream과 DoubleStream도 IntStream과 같은 연산을 지원한다. <br>
반대로 IntStream을 `Stream<T>` 로 변환할 때는 mapToObj() 를, `Stream<Integer>`로 변환할 때는 `boxed()` 를 사용한다. 

```java
// 기본형 스트림 -> Stream<T> 로 변환 (타입 T는 결정할 수 있음)
Stream<U> mapToObj(IntFunction<? extends U> mapper)

// IntStream -> Stream<Integer>
Stream<Integer> boxed()
```

```java
IntStream stream1 = new Random().ints(1, 46);
IntStream stream2 = new Random().ints(1, 46);

// IntStream to Stream<String>
Stream<String> lottoStream = stream1.distinct().limit(6).sorted().mapToObj(i -> i + ",");

// IntStream to Stream<Int>
Stream<Integer> intStream = stream2.boxed().limit(6);
```

<br>

### flatMap() - `Stream<T[]>`를 `Stream<T>`로 변환

스트림의 요소가 배열이거나 map() 의 연산 결과가 배열인 경우 `Stream<T>`로 다루는 것이 더 편리할 때가 있다. 그럴 때는 `map()` 대신 `flatMap()`을 사용하면 된다.

```java
/**
 * 각 요소의 문자열들을 합쳐서 문자열이 요소인 스트림을 만드는 예제
 */
Stream<String[]> strArrStream = Stream.of(
        new String[]{"abc", "def", "ghi"},
        new String[]{"ABC", "GHI", "JKLMN"}
);

// flatMap을 사용해 Stream<Stream<String>>이 아닌 Stream<String> 을 만들 수 있다.
Stream<String> strStream = strArrStream.flatMap(Arrays::stream);
strStream.forEach(System.out::println);
```

map() 과 flatMap() 의 차이를 간단히 정리하면 다음과 같다.

```java
Stream<String> -> map(s -> Stream.of(s.split(" +")) -> Stream<Stream<String>>
Stream<String> -> flatMap(s -> Stream.of(s.split(" +")) -> Stream<String>
```

<br>

## 참고 자료
- 자바의 정석 3판