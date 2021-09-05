## 2.6 collect()

- collect() 는 스트림의 요소를 수집하는 최종 연산으로 reducing 과 유사하다.
- collect() 가 스트림의 요소를 수집하려면 어떻게 수집할 것인가에 대한 방법이 정의되어 있어야 하는데 이 방법을 정의한 것이 바로 컬렉터(collector) 이다.
- 컬렉터는 Collector 인터페이스를 구현한 것으로 직접 구현할 수도 있고 미리 작성된 것을 사용할 수도 있다.
- collect() 는 매개변수로 Collector 타입의 객체를 받아 Collector에 정의된 방식대로 스트림의 요소를 수집한다.

```java
Object collect(Collector collector)
Object collect(Supplier supplier, BiConsumer accmulator, BiConsumer combiner)
```

<br>

## Stream을 Collection과 Array로 변환

### Stream → List, Set

- 스트림의 모든 요소를 컬렉션에 수집하려면 Collections.toList() 를 사용하면 된다.
- List나 Set이 아닌 특정 컬렉션을 지정하려면 toCollection() 에 해당되는 컬렉션의 생성자 참조를 매개변수로 넣어주면 된다.

```java
List<String> names = studentStream.map(Student::getName)
        .collect(Collectors.toList());

List<String> names = studentStream.map(Student::getName)
        .collect(Collectors.toCollection(ArrayList::new));
```

<br>

### Stream → Map

Map은 키와 값의 쌍을 저장해야 하므로 객체의 어떤 값을 키와 값으로 사용할지 지정해야 한다.

```java
Map<Long, String> map = studentStream.collect(
        Collectors.toMap(s -> s.getId(), s -> s.getName()));
```

> **참고 사항**<br>
> 
> 항등 함수를 의미하는 람다식 'p → p' 대신 Function.identity() 를 사용해도 된다.

<br>

### Stream → Array

- Stream에 저장된 요소들을 T[] 타입의 배열로 변환하려면 toArray() 를 사용하면 된다.
- 단, 해당 타입의 생성자 참조를 매개변수로 지정해줘야 한다. (매개변수를 지정하지 않을 경우 반환되는 배열의 타입은 Object[] 다.)

```java
Student[] students = studentStream.toArray(Student[]::new);  // OK
Student[] students = studentStream.toArray();                // ERROR
Object[] students = studentStream.toArray();                 // Object[] 가 반환된다.
```

<br>

### 통계

최종 연산들이 제공하는 통계 정보를 collect() 로 똑같이 얻을 수 있다. (굳이 collect()를 사용하지 않아도 된다.)

```java
// count
long count = stuStream.count();
long count2 = stuStream.collect(Collectors.counting());

// sum
long totalScore = stuStream.mapToInt(Student::getTotalScore).sum();
long totalScore2 = stuStream.collect(Collectors.summingInt(Student::getTotalScore));

// max
OptionalInt topScore = stuStream.mapToInt(Student::getTotalScore).max();
Optional<Student> topStudent1 = stuStream.max(Comparator.comparingInt(Student::getTotalScore));
Optional<Student> topStudent2 = stuStream.collect(Collectors.maxBy(Comparator.comparingInt(Student::getTotalScore)));

// statistics
IntSummaryStatistics stat1 = stuStream.mapToInt(Student::getTotalScore).summaryStatistics();
IntSummaryStatistics stat2 = stuStream.collect(Collectors.summarizingInt(Student::getTotalScore));
```

<br>

### 리듀싱

리듀싱 역시 collect() 로 가능하다.

```java
IntStream intStream = new Random().ints(1, 46).distinct().limit(6);

OptionalInt max = intStream.reduce(Integer::max);
Optional<Integer> max = intStream.boxed().collect(Collectors.reducing(Integer::max));
```

<br>

### 문자열 결합 - joining()

문자열 스트림의 모든 요소를 하나의 문자열로 연결해서 반환한다. (구분자, 접두사, 접미사 지정이 가능하다.)

```java
// studentNames = name1name2
String studentNames = stuStream.map(Student::getName).collect(Collectors.joining());

// studentNames = name1,name2
String studentNames = stuStream.map(Student::getName).collect(Collectors.joining(","));
```

참고로 객체 스트림에 map() 없이 바로 joining() 을 할 경우 toString() 을 호출한 결과를 결합한다.

```java
String result = (String) stuStream.collect(Collectors.joining(","));
```

<br>

### 그룹화와 분할 - groupingBy(), partitioningBy()

- 그룹화는 스트림의 요소를 특정 기준으로 그룹화하는 것을 의미한다.
- 분할은 스트림의 요소를 두 가지, 지정된 조건에 일치하는 그룹과 일치하지 않는 그룹으로 분할하는 것을 의미한다.

```java
// groupingBy는 스트림의 요소를 Function으로 분류한다.
Collector groupingBy(Function classifer)
Collector groupingBy(Function classifer, Collector downstream)
Collector groupingBy(Function classifer, Supplier mapFactory, Collector downstream)

// partitioningBy는 스트림의 요소를 Predicate으로 분류한다.
Collector partitioningBy(Predicate predicate)
Collector partitioningBy(Predicate predicate, Collector downstream)
```

- 스트림을 두 개의 그룹으로 나눠야 한다면 partitioningBy() 로 분할하는 것이 더 빠르다. 그 외에는 groupingBy() 를 쓰면 된다.
- 그룹화와 분할의 결과는 Map에 담겨 반환된다.

<br>

### partitioningBy() 에 의한 분류

```java
// 학생을 성별로 분류
Map<Boolean, List<Student>> stuBySex = stuStream.collect(
    Collectors.partitioningBy(Student::isMale));

List<Student> maleStudents = stuBySex.get(true);
List<Student> femaleStudents = stuBySex.get(false);

// 학생을 성별로 분류하여 카운팅
Map<Boolean, Long> stuNumBySex = stuStream.collect(
    Collectors.partitioningBy(Student::isMale, Collectors.counting()));
```

이중 분류를 하고 싶다면 partitioningBy() 를 두번 사용하면 된다.

```java
// 순차적으로 분류된다.
Map<Boolean, Map<Boolean, List<Student>>> student = stuStream.collect(
        Collectors.partitioningBy(s -> s.getScore() < 150,
                Collectors.partitioningBy(Student::isMale))
);

// 150점 미만인 학생들 중에서 남자인 경우
assertEquals(1, student.get(true).get(true).size());

// 150점 미만인 학생들 중에서 여자인 경우
assertEquals(5, student.get(true).get(false).size());

// 150점 이상인 학생들 중에서 남자인 경우
assertEquals(7, student.get(false).get(true).size());

// 150점 이상인 학생들 중에서 여자인 경우
assertEquals(5, student.get(false).get(false).size());
```

<br>

### groupingBy() 에 의한 분류

```java
// 반 별로 그룹화
Map<Integer, List<Student>> stuByBan = stuStream.collect(
        Collectors.groupingBy(Student::getBan));
```

- groupingBy() 로 그룹화를 하면 기본적으로 `List<T>` 에 담는다.
- 만일 원한다면 toSet() 이나 toCollection(HashSet::new) 를 사용하여 원하는 형태로 변환할 수 있다.

```java
Map<Integer, Set<Student>> stuByBan = stuStream.collect(
    Collectors.groupingBy(Student::getBan, Collectors.toSet()));

Map<Integer, Set<Student>> stuByBan = stuStream.collect(
    Collectors.groupingBy(Student::getBan, Collectors.toCollection(HashSet::new)));
```

```java
// 성적의 등급(Student.Level)을 기준으로 그룹화
Map<Student.Level, Long> student = stuStream.collect(
        Collectors.groupingBy(s -> {
            if (s.getScore() >= 200) return Student.Level.HIGH;
            else if (s.getScore() >= 100) return Student.Level.MID;
            else return Student.Level.LOW;
        }, Collectors.counting()));
```

groupingBy() 를 여러번 사용하면 다수준 그룹화가 가능하다.

```java
// 학년별 그룹화 -> 반별로 그룹화
Map<Integer, Map<Integer, List<Student>>> student = stuStream.collect(
        Collectors.groupingBy(Student::getHak,
                Collectors.groupingBy(Student::getBan)));
```

```java
// 각 반의 1등 출력
Map<Integer, Map<Integer, Student>> topStudentByHakAndBan = stuStream.collect(
    Collectors.groupingBy(Student::getHak,
            Collectors.groupingBy(Student::getBan,
                    Collectors.collectingAndThen(
                            Collectors.maxBy(Comparator.comparingInt(Student::getScore)),
                            Optional::get))));
```

<br>

> **Collectors.collectionAndThen()**<br>
> 
> Collecting을 진행한 후 그 결과로 메소드를 하나 더 호출할 수 있게 한다.

<br>

## 참고 자료
- 자바의 정석 3판