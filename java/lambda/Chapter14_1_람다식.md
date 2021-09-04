# Chapter 14. 람다와 스트림 - 1. 람다식(Lambda expression)

## 1.1 람다식이란?

- **람다식은 간단히 말해서 메소드를 하나의 식(expression) 으로 표현한 것**이다.
- 람다식은 함수를 간략하면서도 명확한 식으로 표현할 수 있게 해준다.
- 메소드를 람다식으로 표현하면 메소드의 이름과 반환값이 없어지므로 람다식을 익명 함수라고도 한다.

```java
int[] arr = new int[5];
Arrays.setAll(arr, (i) -> (int)(Math.random()*5)+1);
```

위 코드에서 사용된 람다식을 메소드로 표현하면 다음과 같다.

```java
int method() {
    return (int) (Math.random() * 5) + 1;
}
```

모든 메소드는 객체를 생성해야만 호출할 수 있었으나 람다식은 오직 람다식 자체만으로도 이 메소드의 역할을 대신할 수 있다. 게다가 람다식은 메소드의 매개변수로 전달하는 것이 가능하고 메소드의 결과로 반환할 수도 있다. 람다식으로 인해 메소드를 변수처럼 다루는 것이 가능해진 것이다.

<br>

## 1.2 람다식 작성하기

람다식은 익명 함수답게 메소드에서 이름과 반환타입을 제거하고 매개변수 선언부와 몸통 사이에 '→' 를 추가한다.

```java
// 메소드
반환타입 메소드이름(매개변수 선언) {
    문장들
}

// 람다식
(매개변수 선언) -> {
    문장들
}
```

예를 들어 두 값 중에서 큰 값을 반환하는 메소드 max를 람다식으로 변환하면 다음과 같다.

```java
// 메소드
int max(int a, int b) {
    return a > b ? a : b;
}

// 람다식
(int a, int b) -> {
    return a > b ? a : b;
}
```

반환 값이 있는 메소드의 경우 return문 대신 '식(expression)' 으로 대신 할 수 있다. 식의 연산 결과가 자동적으로 반환값이 된다. 이 때는 문장(statement)이 아닌 식이므로 끝에 세미콜론을 붙이지 않는다.

```java
(int a, int b) -> a > b ? a : b

// 중괄호, return, 세미콜론 제거
(int a, int b) -> a > b ? a : b
```

람다식에 선언된 매개변수의 타입은 추론이 가능한 경우 생략할 수 있다. 람다식에 반환 타입이 없는 이유도 항상 추론이 가능하기 때문이다. 두 매개변수 중 어느 하나의 타입만 생략하는 것은 허용되지 않으니 주의해야 한다.

```java
(int a, int b) -> a > b ? a : b

// 타입 추론이 가능하게 변경
(a, b) -> a > b ? a : b
```

매개변수가 하나뿐인 경우에는 괄호('()')를 생략할 수 있다. 단, 매개변수의 타입이 있으면 괄호를 생략할 수 없다.

```java
(a) -> a * a
(int a) -> a * a

// 괄호 생략
a -> a * a        // OK
int a -> a * a    // ERROR
```

괄호('{}') 안의 문장이 하나일 때는 괄호를 생략할 수 있다. 이 때 문장의 끝에 세미콜론을 붙이지 않아야 한다.

```java
(String name, int i) -> {
    System.out.println(name + "=" + i)~~;~~
}

// 괄호, 세미콜론 제거
(String name, int i) -> 
    System.out.println(name + "=" + i)
```

그러나 괄호('{}') 안의 문장이 return문일 경우 괄호를 생략할 수 없다.

```java
(int a, int b) -> { return a > b ? a : b; }  // OK
(int a, int b) -> return a > b ? a : b       // ERROR
```

<br>

### 예제 - 메소드를 람다식으로 변환해보자.

```java
// Case 1
int max(int a, int b) {
    return a > b ? a : b;
}

// Case 1 - 람다식으로 변환
(int a, int b) -> { return a > b ? a : b }
(int a, int b) -> a > b ? a : b
(a, b) -> a > b ? a : b

// Case 2
void printVar(String name, int i) {
    System.out.println(name + "=" + i);
}

// Case 2 - 람다식으로 변환
(String name, int i) -> { System.out.println(name + "=" + i); }
(name, i) -> { System.out.println(name + "=" + i) }
(name, i) -> System.out.println(name + "=" + i)

// Case 3
int square(int x ) {
    return x * x;
}

// Case 3 - 람다식으로 변환
(int x) -> x * x;
(x) -> x * x
x -> x * x

// Case 4
int roll() {
    return (int) (Math.random() * 6);
}

// Case 4 - 람다식으로 변환
() -> { return (int) (Math.random() * 6); }
() -> (int) (Math.random() * 6)

// Case 5.
int sumArr(int[] arr) {
    int sum = 0;
    for (int i : arr)
        sum += i;
    return sum;
}

// Case 5 - 람다식으로 변환
(int[] arr) -> {
    int sum = 0;
    for (int i : arr)
        sum += i;
    return sum;
}
```

<br>

## 1.3 함수형 인터페이스(Functional Interface)

지금까지는 람다식이 메소드와 동등한 것처럼 설명했지만, 람다식은 사실 익명 클래스의 객체와 동등하다.

```java
// 람다식
(int a, int b) -> a > b ? a : b

// 람다식은 사실 익명 클래스의 객체와 동등하다.
new Object() {
    int max(int a, int b) {
        return a > b ? a : b;
    }
}
```

참조변수가 있어야 객체의 메소드를 호출할 수 있으니 람다식으로 정의된 익명 객체의 메소드를 호출하기 위해서 일단 익명 객체의 주소를 f 라는 참조 변수에 저장해보자.

```java
타입 f = (int a, int b) -> a > b ? a : b;
```

참조형이니 타입으로는 클래스 또는 인터페이스가 가능하다. 그리고 람다식과 동등한 메소드가 정의되어 있는 것이어야 한다. 그래야만 참조변수로 익명 객체(람다식)의 메소드를 호출할 수 있기 때문이다. 

예를 들어 max() 라는 메소드가 정의된 MyFunction 인터페이스가 정의되어 있다고 가정하면 이 인터페이스를 구현한 익명 클래스의 객체는 다음과 같이 생성할 수 있다.

```java
MyFunction f = new MyFunction() {
    public int max(int a, int b) {
        return a > b ? a : b;
    }
}

int big = f.max(5, 3);  // 익명 객체의 메소드를 호출
```

위 코드의 익명 객체를 람다식으로 아래와 같이 대체할 수 있다.

```java
MyFunction f = (int a, int b) -> a > b ? a : b;  // 익명 객체를 람다식으로 대체
int big = f.max(5,3);  // 익명 객체의 메소드를 호출
```

이처럼 MyFunction 인터페이스를 구현한 익명 객체를 람다식으로 대체가 가능한 이유는 람다식도 실제로는 익명 객체이고, MyFunction 인터페이스를 구현한 익명 객체의 메소드 max() 와 람다식의 매개변수의 타입과 개수 그리고 반환값이 일치하기 때문이다.

<br>

하나의 메소드가 선언된 인터페이스를 정의해서 람다식으로 다루는 것은 기존의 자바의 규칙들을 어기지 않으면서도 자연스럽다. 그래서 인터페이스를 통해 람다식을 다루기로 결정되었으며, **람다식을 다루기 위한 인터페이스를 함수형 인터페이스**라고 부르기로 했다.

```java
// 함수형 인터페이스 MyFunction을 정의
@FunctionalInterface
interface MyFunction {
    public abstract int max(int a, int b);
}
```

**단, 함수형 인터페이스에는 오직 하나의 추상 메소드만 정의되어 있어야 한다는 제약**이 있다. 그래야 람다식과 인터페이스의 메소드가 1:1로 연결될 수 있기 대문이다. 반면에 static 메소드와 default 메소드의 개수에는 제약이 없다.

<br>

> **@FunctionalInterface**<br>
> 
> 이 어노테이션을 붙이면 컴파일러가 함수형 인터페이스를 올바르게 정의하였는지 확인해주므로 꼭 붙이는 것이 좋다.

<br>

### 함수형 인터페이스 타입의 매개변수와 반환 타입

함수형 인터페이스는 매개변수로 받을 수 있고, 반환 타입으로 지정할 수도 있다.

```java
@FunctionalInterface
interface MyFunction {
    void myMethod();
}

// 매개변수로 함수형 인터페이스를 받아 처리
void aMethod(MyFunction f) {
    f.myMethod();
}

// 참조 변수로 함수형 인터페이스를 전달
MyFunction f = () -> System.out.println("myMethod()");
aMethod(f);

// 참조 변수 없이 람다식을 직접 매개변수로 지정하는 것도 가능하다. 
aMethod(() -> System.out.println("myMethod()");
```

메소드의 반환 타입이 함수형 인터페이스라면 함수형 인터페이스의 추상 메소드와 동등한 람다식을 가리키는 참조 변수를 반환하거나 람다식을 직접 반환할 수 있다.

```java
MyFunction myMethod() {
    // 이 코드는 return () -> {}; 로 줄일 수 있다.
    MyFunction f = () -> {};
    return f;
}
```

<br>

### 람다식의 타입과 형변환

함수형 인터페이스로 람다식을 참조할 수 있는 것일 뿐, 람다식의 타입이 함수형 인터페이스의 타입과 일치하는 것은 아니다. 람다식은 익명 객체이고 익명 객체는 타입이 없다. (정확히는 타입은 있지만 컴파일러가 임의로 이름을 정하기 때문에 알 수 없는 것이다.)

```java
// 컴파일러가 임의로 정한 람다식의 타입
LambdaEx2$$Lambda$1
LambdaEx2$$Lambda$2
```

그래서 대입 연산자의 양변을 일치시키기 위해 아래와 같이 형변환이 필요하다.

```java
MyFunction f = (MyFunction) (() -> {});  // 양변의 타입이 다르므로 형변환이 필요
```

람다식은 MyFunction 인터페이스를 직접 구현하지 않았지만, 이 인터페이스를 구현한 클래스의 객체와 완전히 동일하기 때문에 위와 같은 형변환을 허용한다. 그리고 이 형변환은 생략 가능하다.

<br>

람다식은 이름이 없는 객체지만 아래와 같이 Object 타입으로는 형변환할 수 없다. 람다식은 오직 함수형 인터페이스로만 형변환이 가능하다.

```java
Object obj = (Object) (() -> {});  // ERROR. 함수형 인터페이스로만 형변환 가능
```

굳이 Object 타입으로 형변환하려면 먼저 함수형 인터페이스로 변환해야 한다.

```java
Object obj = (Object)(MyFunction)(() -> {});
```

<br>

### 외부 변수를 참조하는 람다식

람다식도 익명 클래스의 인스턴스(익명 객체) 이므로 외부에 선언된 변수에 접근할 수 있다.

```java
class Outer {
    int val = 10;  // Outer.this.val

    class Inner {
        int val = 20;  // this.val

        void method(int i) {
            int val = 30;  // final int val = 30;
 
            MyFunction f = () -> {
                System.out.println("i : " + i);
                System.out.println("val : " + val);
                System.out.println("this.val : " + ++this.val);
                System.out.println("Outer.this.val : " + Outer.this.val);
            }

            f.myMethod();
        }
    }
}
```

람다식 내에서 참조하는 지역변수는 final이 붙지 않았어도 상수로 간주된다. 람다식 내에서 지역변수 i와 val을 참조하고 있으므로 람다식 내에서나 다른 어느 곳에서도 이 변수들의 값을 변경하는 일은 허용되지 않는다. (반면에 this.val, Outer.this.val은 상수로 간주되지 않으므로 값을 변경해도 된다.) 

<br>

그리고 외부 지역변수와 같은 이름의 람다식 매개변수는 허용되지 않는다.

```java
void method(int i) {
    i = 10;  // 에러 1. 상수의 값을 변경할 수 없음

    MyFunction f = (i) -> {  // 에러 2. 외부 지역변수와 이름이 중복됨
        ...
    }
}
```

<br>

## 1.4 java.util.function package

`java.util.function` package에 일반적으로 자주 쓰이는 형식의 메소드를 함수형 인터페이스로 미리 정의되어 있다. 가급적이면 매번 새로운 함수형 인터페이스를 정의하지 말고 이 패키지의 인터페이스를 활용하는 것이 좋다. 그래야 함수형 인터페이스에 정의된 메소드 이름도 통일되고, 재사용성이나 유지보수 측면에서도 좋기 때문이다. 

<br>

### 자주 쓰이는 기본적인 함수형 인터페이스(java.util.function package)

매개변수와 반환값의 유무에 따라 4개의 함수형 인터페이스가 정의되어 있고,  Function의 변형으로 Predicate이 있는데 반환값이 boolean 이라는 것만 제외하면 Function과 동일하다.  

- `java.lang.Runnable` / `void run()` : 매개변수도 없고 반환값도 없음
- `Supplier<T>` / `T get()` : 매개변수는 없고 반환값만 있음
- `Consumer<T>` / `void accept(T t)` : Supplier와 반대로 매개변수만 있고 반환값이 없음
- `Function<T, R>` / `R apply(T t)` :  일반적인 함수. 하나의 매개변수를 받아 결과를 반환
- `Predicate<T>` / `boolean test(T t)` : 조건식을 함수로 표현하는 데 사용

<br>

> **타입 매개변수 T, R**<br>
>
> T는 Type을, R은 Return Type을 의미한다.

<br>

### 조건식의 표현에 사용되는 Predicate

Predicate은 Function의 변형으로 반환 타입이 boolean 이라는 것만 다르다. Predicate은 조건식을 람다식으로 표현하는데 사용된다.

```java
Predicate<String> isEmptyStr = s -> s.length() == 0;
String s = "";

if (isEmptyStr.test(s))  // if(s.length() == 0)
    System.out.println("This is an empty String.");
```

<br>

### 매개변수가 두 개인 함수형 인터페이스

매개변수가 두 개인 함수형 인터페이스는 이름 앞에 접두사 Bi 가 붙는다.

- `BiConsumer<T, U>` / `void accept(T t, U u)` : 매개변수만 있고 반환값이 없음
- `BiPredicate<T, U>` / `boolean test(T t, U u)` : 조건식 표현에 사용
- `BiFunction<T, U, R>` / `R apply(T t, U u)` : 두 개의 매개변수를 받아 하나의 결과를 반환

<br>

> **BiSupplier 인터페이스가 없는 이유**<br>
>
> Supplier는 매개변수는 없고 반환값만 존재하는데, 메소드는 두 개의 값을 반환할 수 없으므로 BiSupplier가 없는 것이다.

<br>

### UnaryOperator와 BinaryOperator

Function의 변형으로 매개변수 타입과 반환 타입이 모두 일치한다는 점만 제외하고는 Function과 같다.

- `UnaryOperator<T>` / `T apply(T t)` : Function의 자손으로 Function과 달리 매개변수와 결과의 타입이 같다.
- `BinaryOperator<T>` / `T apply(T t, T t)` : BiFunction의 자손으로 BiFunction과 달리 매개변수와 결과의 타입이 같다.

<br>

### 컬렉션 프레임워크와 함수형 인터페이스

컬렉션 프레임워크의 인터페이스에 다수의 디폴트 메소드가 추가되었는데 그 중 일부는 함수형 인터페이스를 사용한다. 다음은 그 메소드들의 목록이다.

| 인터페이스 | 메소드 | 설명 |
|--------|--------|--------|
|Collection| boolean removeIf(Predicate<E> filter) | 조건에 맞는 요소 삭제 |
|List| void replaceAll(UnaryOperator<E> operator) | 모든 요소를 변환하여 대체 |
|Iterable| void forEach(Consumer<T> action) | 모든 요소에 작업 action을 수행 |
|Map| V compute(K key, BiFunction<K,V,V> f) | 지정된 키의 값에 작업 f를 수행|
|Map| V computeIfAbsent(K key, Function<K,V> f) | 키가 없으면 작업 f 수행 후 추가 |
|Map| V computeIfPresent(K key, BiFunction<K,V,V> f) | 지정된 키가 있을 때 작업 f 수행 |
|Map| V merge(K key, V value, BiFunction<V,V,V> f) | 모든 요소에 병합작업 f를 수행 |
|Map| void forEach(BiConsumer<K,V> action) | 모드 요소에 작업 action을 수행 |
|Map| void replaceAll(BiFunction<K,V,V> f) | 모든 요소에 치환작업 f를 수행 |

<br>

다음은 함수형 인터페이스들을 사용하는 예제다.

```java
public static void main(String[] args) {
    Supplier<Integer> s = () -> (int)(Math.random() * 100) + 1;
    Consumer<Integer> c = i -> System.out.print(i + ", ");
    Predicate<Integer> p = i -> i % 2 == 0;
    Function<Integer, Integer> f = i -> i/10*10;

    List<Integer> list = new ArrayList<>();
    makeRandomList(s, list);
    System.out.println(list);

    printEvenNum(p, c, list);

    List<Integer> newList = doSomething(f, list);
    System.out.println(newList);
}

// list의 원소를 Function을 수행해 변환한다.
static <T> List<T> doSomething(Function<T, T> f, List<T> list) {
    List<T> newList = new ArrayList<>(list.size());

    for (T i : list) {
        newList.add(f.apply(i));
    }

    return newList;
}

// list의 원소 중 Predicate을 만족하는 원소를 Consumer에 전달해 출력한다.
static <T> void printEvenNum(Predicate<T> p, Consumer<T> c, List<T> list) {
    System.out.print("[");

    for (T i : list) {
        if (p.test(i))
            c.accept(i);
    }

    System.out.println("]");
}

// Supplier에서 제공하는 난수를 list에 추가한다.
static <T> void makeRandomList(Supplier<T> s, List<T> list) {
    for (int i=0; i<10; i++)
        list.add(s.get());
}
}
```

```java
public static void main(String[] args) {
    List<Integer> list = new ArrayList<>();
    for (int i=0; i<10; i++)
        list.add(i);

    // list의 모든 요소를 출력
    list.forEach(i -> System.out.print(i + ","));
    System.out.println();

    // list에서 2 또는 3의 배수를 제거한다.
    list.removeIf(i -> i % 2 == 0 || i % 3 == 0);
    System.out.println(list);

    // list의 각 요소에 10을 곱한다.
    list.replaceAll(i -> i * 10);
    System.out.println(list);

    Map<String, String> map  = new HashMap<>();
    map.put("1", "1");
    map.put("2", "2");
    map.put("3", "3");
    map.put("4", "4");

    // map의 모든 요소를 {k,v}의 형식으로 출력한다.
    map.forEach((k, v) -> System.out.println("{" + k + ", " + v + "}, "));
    System.out.println();
}
```

<br>

> **참고**<br>
>
> Map 인터페이스의 compute로 시작하는 메소드들은 map의 value를 변환하는 일을 하고 merge()는 map을 병합하는 일을 한다.

<br>

### 기본형을 사용하는 함수형 인터페이스

래퍼 클래스가 아닌 기본형을 사용하는 함수형 인터페이스도 제공된다. 래퍼 클래스를 사용하는 경우 오토박싱, 언박싱 과정이 수반되므로 성능 저하가 발생할 수 있다. 이를 방지하기 위해 기본형을 지원하는 함수형 인터페이스로 대체가 가능한 경우에는 가급적 기본형을 지원하는 함수형 인터페이스를 사용하자. 

- `DoubleToIntFunction` / `int applyAsInt(double d)`
- `ToIntFunction<T>` / `int applyAsInt(T value)`
- `IntFunction<R>` / `R apply(T t, U u)`
- `ObjIntConsumer<T>` / `void accept(T t, U u)`

<br>

> **참고**<br>
> 
> 다른 기본형 타입으로도 함수형 인터페이스가 제공된다. (ex. IntToDoubleFunction)
> IntFunction, ToIntFunction, IntToLongFunction은 있어도 IntToIntFunction은 없는데, 그 이유는 IntUnaryOperator가 그 역할을 하기 때문이다. 매개변수의 타입과 반환 타입이 일치할 때는 Function 대신 UnaryOperator를 사용하자.

<br>

## 1.5 Function의 합성과 Predicate의 결합

`java.util.function` 패키지의 함수형 인터페이스에는 추상 메소드 외에도 Function의 합성과 Predicate의 결합을 지원하는 default, static 메소드가 정의되어 있다. 

```
Function에 정의된 메소드
default <V> Function<T, V> andThen(Function<? super R, ? extends V) after)
default <V> Function<V, R> compose(Function<? super V, ? extends T) before)
static <T> Function<T, T> identity()

Predicate에 정의된 메소드
default Predicate<T> and(Predicate<? super T> other)
default Predicate<T> or(Predicate<? super T> other)
default Predicate<T> negate()
static <T> Predicate<T> isEqual(Object targetRef)
```

<br>

### Function의 합성

수학에서 두 함수를 합성해 하나의 새로운 함수를 만들어낼 수 있는 것처럼, 두 람다식을 합성해서 새로운 람다식을 만들 수 있다. 두 함수의 합성은 어느 함수를 먼저 적용하느냐에 따라 달라질 수 있다. 예를 들어 f.andThen(g)는 함수 f를 먼저 적용하고, 그 다음에 함수 g를 적용한다. 그리고 f.compose(g)는 반대로 g를 먼저 적용하고 f를 적용한다.

```java
// andThen()은 함수 f를 먼저 적용하고 g를 적용한다.
default <V> Function<T, V> andThen(Function<? super R, ? extends V) after)

// 입력 T를 받아 V를 반환하도록 2개의 Function을 합성한다.
T -> Function<T, R> apply() -> R -> Function<R, V> apply() -> V

// compose()는 함수 g를 먼저 적용하고 f를 적용한다.
default <V> Function<V, R> compose(Function<? super V, ? extends T) before)

// 입력 V를 받아 R을 반환하도록 2개의 Function을 합성한다.
V -> Function<V, T> apply() -> T -> Function<T, R> apply() -> R
```

<br>

예를 들어 문자열을 숫자로 변환하는 함수 f와 숫자를 2진 문자열로 변환하는 함수 g를 andThen() 으로 합성하여 새로운 함수 h를 만들어낼 수 있다.

```java
Function<String, Integer> f = (s) -> Integer.parseInt(s, 16);
Function<Integer, String> g = (i) -> Integer.toBinaryString(i);
Function<String, String> h = f.andThen(g);
```

그리고 identity() 는 함수를 적용하기 이전과 이후가 동일한 '항등 함수'가 필요할 때 사용한다. 

```java
Function<String, String> f = x -> x;
// Function<String, String> f = Function.identity();  // 위 문장과 동일
System.out.println(f.apply("AAA"));  // AAA가 그대로 출력됨
```

항등 함수는 잘 사용되지 않는 편이며, 나중에 배울 map()으로 변환 작업을 할 때 변환없이 그대로 처리하고자 할 때 사용된다.

<br>

### Predicate의 결합

여러 조건식을 논리 연산자인 &&, ||, ! 으로 연결해서 하나의 식을 구성할 수 있는 것처럼 여러 Predicate을 and(), or(), negate() 로 연결해서 하나의 새로운 Predicate으로 결합할 수 있다.

```java
Predicate<Integer> p = i -> i < 100;
Predicate<Integer> q = i -> i < 200;
Predicate<Integer> r = i -> i % 2 == 0;
Predicate<Integer> notP = p.negate();   // i >= 100

// i >= 100 && (i < 200 || i % 2 == 0)
Predicate<Integer> all = notP.and(q.or(r));
System.out.println(all.test(150));  // true
```

그리고 static 메소드인 isEqual() 은 두 대상을 비교하는 Predicate을 만들 때 사용한다. 먼저 isEquals() 의 매개변수로 비교 대상을 하나 지정하고, 또 다른 비교 대상은 test() 의 매개변수로 지정한다.

```java
Predicate<String> p = Predicate.isEqual(str1);
boolean result = p.test(str2);  // str1과 str2가 같은지 비교하여 결과를 반환
```

<br>

## 1.6 메소드 참조(Method reference)

항상 사용할 수 있는 것은 아니지만 람다식을 더욱 간결하게 표현할 수 있는 방법이 있다. **람다식이 하나의 메소드만 호출하는 경우에는 메소드 참조를 사용해 람다식을 간략히 할 수 있다.** 예를 들어 문자열을 정수로 변환하는 람다식은 아래와 같이 작성할 수 있다.

```java
Function<String, Integer> f = (String s) -> Integer.parseInt(s);
```

이 람다식을 메소드 참조로 표현하면 아래와 같다.

```java
Integer wrapper(String s) {
    return Integer.parseInt(s);
}
```

Integer.parseInt() 만 호출하고 있으므로 wrapper method를 사용할 것이 아니라 람다식에서 이 메소드를 직접 호출하도록 메소드 참조를 사용할 수 있다.

```java
// 일반 람다식
Function<String, Integer> f = (String s) -> Integer.parseInt(s);

// 메소드 참조(컴파일러는 좌변의 Function 인터페이스에 지정된 제네릭 타입으로부터 타입을 추론해서 사용할 수 있게 해줌)
Function<String, Integer> f = Integer::parseInt;
```

타입 추론이 가능한 상황이라면 더 생략해서 람다식의 매개변수들을 없앨 수도 있다.

```java
// 일반 람다식
BiFunction<String, String, Boolean> f = (s1, s2) -> s1.equals(s2);

// 메소드 참조(두 개의 String 매개변수를 받는 것을 알 수 있으므로 람다식의 매개변수 제거 가능)
BiFunction<String, String, Boolean> f = String::equals();
```

Boolean을 반환하는 equals 메소드는 다른 클래스에도 존재할 수 있기 때문에 equals 앞에 클래스 이름은 반드시 필요하다. 추가로 이미 생성된 객체의 메소드를 람다식에서 사용한 경우에는 클래스 이름 대신 그 객체의 참조 변수를 적어줘야 한다.

```java
MyClass obj = new MyClass();
Function<String, Boolean> f = (x) -> obj.equals(x);  // 람다식
Function<String, Boolean> f2 = obj::equals;          // 메소드 참조
```

<br>

### 람다식을 메소드 참조로 변환하는 방법
```
1. static 메소드 참조
(x) -> ClassName.method(x)  // 람다식
ClassName::method           // 메소드 참조

2. 인스턴스 메소드 참조
(obj, x) -> obj.method(x)  // 람다식
ClassName::method          // 메소드 참조

3. 특정 객체 인스턴스 메소드 참조
(x) -> obj.method(x)
obj::method  // 람다식
```

<br>

### 생성자의 메소드 참조

생성자를 호출하는 람다식도 메소드 참조로 변환할 수 있다.

```java
Supplier<MyClass> s = () -> new MyClass();  // 람다식
Supplier<MyClass> s = MyClass::new;         // 메소드 참조
```

매개변수가 있는 생성자라면 매개변수의 개수에 따라 알맞은 함수형 인터페이스를 사용하면 된다. 필요하다면 함수형 인터페이스를 새로 정의해야 한다.

```java
Function<Integer, MyClass> f = (i) -> new MyClass(i);  // 람다식
Function<Integer, MyClass> f2 = MyClass::new;          // 메소드 참조

BiFunction<Integer, String, MyClass> bf = (i, s) -> new MyClass(i, s);  // 람다식
BiFunction<Integer, String, MyClass> bf2 = MyClass::new;                // 메소드 참조
```

그리고 배열을 생성할 때는 아래와 같이 하면 된다.

```java
Function<Integer, int[]> f = x -> new int[x];  // 람다식
Function<Integer, int[]> f2 = int[]::new;      // 메소드 참조
```

<br>

## 참고 자료
- 자바의 정석 3판