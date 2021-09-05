## 2.5 스트림의 최종 연산

- 최종 연산은 스트림의 연산을 소모해서 결과를 만든다.
- 최종 연산 후에는 스트림이 닫히게 되어 더이상 사용할 수 없다.
- 최종 연산의 결과는 스트림 요소의 합과 같은 단일 값이거나 스트림의 요소가 담긴 배열 또는 컬렉션일 수 있다.

<br>

### forEach()

- forEach()는 스트림의 요소를 소모하는 최종 연산이다. 
- 반환 타입이 void 이므로 스트림의 요소를 출력하는 용도로 많이 사용된다.

```java
void ForEach(Consumer<? super T> action)
```

<br>

### 조건 검사 - allMatch(), anyMatch(), noneMatch(), findFirst(), findAny()

스트림의 요소에 대해 지정된 조건을 모든 요소가 만족하는지, 일부가 일치하는지 등을 확인할 때 사용할 수 있는 메소드들이다. 

```java
boolean allMatch(Predicate<? super T> predicate)
boolean anyMatch(Predicate<? super T> predicate)
boolean noneMatch(Predicate<? super T> predicate)
```

```java
boolean noFailed = stuStream.anyMatch(s -> s.getTotalScore() <= 100)
```

병렬 스트림을 사용하는 경우에는 findFirst() 대신 findAny() 를 사용해야 한다.

```java
Optional<Student> stu = stuStream.filter(s -> s.getTotalScore() <= 100).findFirst();
Optional<Student> stu = parallelStream.filter(s -> s.getTotalScore() <= 100).findAny();
```

findAny()와 findFirst()의 반환 타입은 `Optional<T>` 이며, 스트림의 요소가 없을 때는 비어 있는 Optional 객체를 반환한다. (비어있는 Optional 객체는 내부적으로  null을 저장한다.)

<br>

### 통계 - count(), sum(), average(), max(), min()

기본형 스트림이 아닌 경우에는 통계와 관련된 메소드들이 아래 3개 뿐이다.

```java
long count()
Optional<T> max(Comparator<? super T> comparator)
Optional<T> min(Comparator<? super T> comparator)
```

대부분의 경우 위의 메소드를 사용하기보다 기본형 스트림으로 변환하거나 reduce(), collect() 를 사용해서 통계 정보를 얻는다.

<br>

### 리듀싱 - reduce()

- reduce() 는 스트림의 요소를 줄여나가면서 연산을 수행하고 최종 결과를 반환한다.
- 처음 두 요소를 가지고 연산한 결과를 가지고 그 다음 요소와 연산한다.
- reduce() 는 스트림의 모든 요소를 소모하게 되면 그 결과를 반환한다.

```java
Optional<T> reduce(BinaryOperator<T> accmulator)
```

- 이외에도 연산 결과의 초기값(identity)을 갖는 reduce()도 있다.
- 이 메소드들은 초기값과 스트림의 첫 번째 요소로 연산을 시작한다.
- 스트림의 요소가 하나도 없는 경우 초기값이 반환되므로 반환 타입이 `Optional<T>`가 아닌 `T`이다.

```java
T reduce(T identity, BinaryOperator<T> accmulator)
U reduce(U identity, BinaryOperator<U,T,U> accmulator, BinaryOperator<U> combiner)
```

여기서 combiner는 병렬 스트림에 의해 처리된 결과를 합칠 때 사용한다.

```java
IntStream intStream = IntStream.rangeClosed(1, 10);

int count = intStream.reduce(0, (a, b) -> a + 1);               // count()
int sum = intStream.reduce(0, (a, b) -> a + b);                 // sum()
int max = intStream.reduce(Integer.MIN_VALUE, (a, b) -> a > b ? a : b); // max()
int min = intStream.reduce(Integer.MAX_VALUE, (a, b) -> a < b ? a : b); // min()
```

<br>

## 참고 자료
- 자바의 정석 3판