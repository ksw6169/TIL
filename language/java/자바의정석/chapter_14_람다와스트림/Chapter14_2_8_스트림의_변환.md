## 2.8 스트림의 변환
아래는 스트림의 변환에 대해 정리한 표이다.

<br>

## 1. 스트림 → 기본형 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
| `Stream<T>` | `IntStream`<br>`LongStream`<br>`DoubleStream` | `mapToInt(ToIntFunction<T> mapper)` <br>`mapToLong(ToLongFunction<T> mapper)`<br>`mapToDouble(ToDoubleFunction<T> mapper)`|

<br>

## 2. 기본형 스트림 → 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
|`IntStream`<br>`LongStream`<br>`DoubleStream`|`Stream<Integer>`<br>`Stream<Long>`<br>`Stream<Double>`|`boxed()`|
|`IntStream`<br>`LongStream`<br>`DoubleStream`|`Stream<U>`|`mapToObj(DoubleFunction<U> mapper)`|

<br>

## 3. 기본형 스트림 → 기본형 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
|`IntStream`<br>`LongStream`<br>`DoubleStream`|`LongStream`<br>`DoubleStream`|`asLongStream()`<br>`asDoubleStream()`|

<br>

## 4. 스트림 → 부분 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Stream<T>`<br>`IntStream`|`Stream<T>`<br>`IntStream`|`skip(long n)`<br>`limit(long maxSize)`|

<br>

## 5. 두 개의 스트림 → 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Stream<T>, Stream<T>`|`Stream<T>`|`concat(Stream<T> a, Stream<T> b)`|
|`IntStream, IntStream`|`IntStream`|`concat(IntStream a, IntStream b)`|
|`LongStream, LongStream`|`LongStream`|`concat(LongStream a, LongStream b)`|
|`DoubleStream, DoubleStream`|`DoubleStream`|`concat(DoubleStream a, DoubleStream b)`|

<br>

## 6. 스트림의 스트림 → 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Stream<Stream<T>>`|`Stream<T>`|`flatMap(Function mapper)`|
|`Stream<IntStream>`|`IntStream`|`flatMapToInt(Function mapper)`|
|`Stream<LongStream>`|`LongStream`|`flatMapToLong(Function mapper)`|
|`Stream<DoubleStream>`|`DoubleStream`|`flatMapToDouble(Function mapper)`|

<br>

## 7. 스트림 → 병렬 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Stream<T>`<br>`IntStream`<br>`LongStream`<br>`DoubleStream`|`Stream<T>`<br>`IntStream`<br>`LongStream`<br>`DoubleStream`|`parallel()`    // 스트림 → 병렬 스트림<br>`sequential()`  // 병렬 스트림 → 스트림|

<br>

## 8. 스트림 → 컬렉션

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Stream<T>`<br>`IntStream`<br>`LongStream`<br>`DoubleStream`|`Collection<T>`|`collect(Collectors.toCollection(Supplier factory))`|
|`Stream<T>`<br>`IntStream`<br>`LongStream`<br>`DoubleStream`|`List<T>`|`collect(Collectors.toList())`|
|`Stream<T>`<br>`IntStream`<br>`LongStream`<br>`DoubleStream`|`Set<T>`|`collect(Collectors.toSet())`|

<br>

## 9. 컬렉션 → 스트림

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Collection<T>, List<T>, Set<T>`|`Stream<T>`|`stream()`|

<br>

## 10. 스트림 → Map

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Stream<T>`<br>`IntStream`<br>`LongStream`<br>`DoubleStream`|`Map<K,V>`|`collect(Collectors.toMap(Function k, Function v))`<br>`collect(Collectors.toMap(Function k, Function v, BinaryOperator merge))`<br>`collect(Collectors.toMap(Function k, Function v, BinaryOperator merge, Supplier mapSupplier))`|

<br>

## 11. 스트림 → 배열

| from | to | 변환 메소드 |
|--------|--------|--------|
|`Stream<T>`|`Object[]`|`toArray()`|
|`Stream<T>`|`T[]`|`toArray(IntFunction<A[]> generator)`|
|`IntStream`<br>`LongStream`<br>`DoubleStream`|`int[]`<br>`long[]`<br>`double[]`|`toArray()`|

<br>

## 참고 자료
- 자바의 정석 3판