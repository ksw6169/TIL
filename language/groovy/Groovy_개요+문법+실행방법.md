## Apache Groovy란?

- 자바에 파이썬, 루비, 스몰토크 등의 특징을 더한 동적 객체 지향 프로그래밍 언어다.
- JVM에서 동작하고, 자바와 호환되며 Java 클래스 파일을 그대로 Groovy 클래스로 사용할 수 있다.

<br>

## 자바와의 비교

- 자바 소스는 컴파일해야 사용할 수 있지만, 그루비 소스는 스크립트 파일 그대로 실행시킬 수도 있고, 자바처럼 컴파일하여 쓸 수도 있다. 거의 대부분 자바 소스는 파일 확장자만 바꾸면 수정 없이 그루비에서도 쓸 수 있다.
- Groovy는 접근 제어자(`public, private`) 나 세미콜론이 옵션이다.

<br>

## Groovy 실행 원리

- 그루비 소스를 컴파일하면 자바 클래스가 만들어진다. 이것을 실행하기 위해서는 `groovy.jar` 와 `asm.jar` 라이브러리 파일이 필요하다.

<br>

## 도구

### groovy

- 가장 많이 쓰는 그루비 실행 도구
- ex. `groovy hello.groovy`

<br>

### groovyc

- 자바 쪽에서 불러 쓸 수 있는 라이브러리를 만들 때 쓰는 그루비 컴파일러
- 내용은 자바 소스와 거의 같으나 파일 확장자를 `groovy` 로 하여 저장한다.

```java
// HelloWorld.groovy
public class HelloWorld {
    public static void main(String[] args) {
        println "Hello, World!"
    }
}
```

- 컴파일 명령어는 다음과 같다.

```java
>groovyc -d . HelloWorld.groovy
```

- 실행 명령어는 다음과 같다.

```java
>java -classpath . %CLASSPATH% HelloWorld
```

- 실행을 위해서는 환경변수로 CLASSPATH를 미리 설정해둬야 한다. (java로 실행할 때 필요하다.)

```java
// 윈도우
>set CLASSPATH=%GROOVY_HOME%\embeddable\groovy-all-1.7.3.jar

// 유닉스/리눅스
$ export CLASSPATH=$GROOVY_HOME/embeddable/groovy-all-1.7.3.jar
```

<br>

### groovysh

- 간단한 코드를 테스트하는 데 좋은 그루비 쉘

```java
>groovysh
Groovy Shell (1.7.3, JVM: 1.6.0_21)
Type 'help' or '\h' for help.
-------------------------------------------------------------------------------
groovy:000> println "Hello"
Hello
===> null
groovy:000> exit
```

<br>

---

<br>

## 문법

### 변수 선언과 값 할당

- 동적 타입 바인딩

```groovy
def a = 20    // 값을 할당할 때 a 변수의 타입이 결정됨
a = "문자열"   // 다른 값을 할당하면 변수의 타입이 바뀜
b = "문자열"   // 변수 선언 시 def를 생략해도 됨
```

- 정적 타입 바인딩

```groovy
int a = 20  // 변수 a는 int로 타입이 고정됨
a = "문자열" // 문자열 값을 할당했으므로 GroovyCastException 발생
```

- 문자열과 자동 형변환

```groovy
String a = "문자열"  // 변수 a는 String으로 타입이 고정됨
a = 20  // 정수 값 20이 문자열 "20"으로 자동 형변환되어 변수 a에 할당됨
```

- 자바 변수 선언

```groovy
java.util.Date a = new java.util.Date()  // 자바 문법 사용 가능
Date b = new Date()  // 기본으로 java.util package가 import 되어 있음
```

<br>

### 리스트와 맵 데이터 다루기

- 리스트 데이터 할당과 사용

```groovy
scoreList = [90, 80, 100, 87]  // ArrayList 객체가 생성됨
println scoreList[2]           // 배열처럼 사용할 수 있음
println scoreList.get(2)       // 원래대로 ArrayList의 get() 호출 가능
nameList = ["홍길동", "임꺽정", "장보고"]
println nameList[1]
emptyList = []  // 빈 리스트 생성
```

```groovy
// 출력 결과
100
100
임꺽정
```

- 맵 데이터 할당과 사용

```groovy
scoreMap = ["국어":100, "영어":90, "수학":100, "사회":89]
println scoreMap["수학"]      // 변수[키] 형태로 값 추출
println scoreMap.수학         // 변수.키 형태로 값 추출
println scoreMap.getClass()  // java.util.LinkedHashMap 출력
scoreMap.수학 = 93     // 맵의 값 변경
println scoreMap.수학
emptyMap = [:]  // 빈 맵 생성
```

```groovy
// 출력 결과
100
100
class java.util.LinkedHashMap
93
```

<br>

### 분기문

- if - else문

```groovy
age = 17

if (age) {
    println "17입니다."
} else {
    println "17이 아닙니다."
}
```

- 조건 연산자

```groovy
age = 17
title = (age < 19) ? "청소년" : "성인"
```

- switch문

```groovy
x = "aaa"
result = ""

switch (x) {
    case "aaa": result = "aaa"
    case "123": result += "123"
    case ["a", "b", "c", "aaa", 111]:    // 리스트에 포함되는지
        result = "숫자, 문자열, 참거짓 값"
        break
    case 100..200:  // 100 <= x <= 200인지(정수만 해당됨. 소수일 경우 범위 안에 있어도 X)
        result = "100 ~ 200 값"
        break
    case Number:
        result = "기타 숫자 값"
        break
    default:
        result = "기타"
}

println result
```

- groovy의 switch문은 자바와 달리 숫자나 문자열 외에 실수, Boolean, 객체, 리스트도 다룰 수 있다.

<br>

### 반복문

- while문

```groovy
while (조건) { ... }
```

- for문

```groovy
for (int i=0; i<5; i++) { }      // 0 ~ 4
for (i in 0..9) { }              // 0 ~ 9
for (i in [100, 90, 95, 80]) { } // 100, 90, 95, 80
for (i in ['a', 'd', 'e']) { }   // a, d, e
for (c in "str") { }             // s -> t -> r

for (entry in ["A":100, "B":90, "C":80])
    println entry.key + ":" + entry.value

list = [1,2,3,4,5]
list.each {i ->  println i }
list.eachWithIndex { entry, i -> println(entry + ":" + i) }

map = ["A":100, "B":90, "C":80]
map.each {entry -> println "key: " + entry.key + ", value: " + entry.value }
map.eachWithIndex { entry, i -> println "key: " + entry.key + ", value: " + entry.value + ", index: " + i}
```

<br>

### 클로저

- **Groovy의 클로저(Closure)**
- Groovy의 클로저는 인수를 취하고 값을 반환하고 변수에 할당할 수 있는 개방형 익명의 코드 블록으로 자바의 람다 같은 개념이라 보면 된다. 공식적인 정의와 반대로 Groovy 언어에서는 클로저의 블록 밖에서 정의된 free 변수를 포함할 수도 있다.
- 클로저 정의는 다음 구문을 따른다.

```groovy
{ [closureParameters -> ] statements }
```

- `closureParameters` : 선택적 쉼표로 구분된 매개변수 목록, 매개변수의 타입은 지정될 수도 있고 안될 수도 있다.
- `->` : 매개변수 목록이 지정되면 클로저 본문에서 인수를 분리하는 역할을 한다.
- 클로저는 `<클로저이름>.call()` 또는 `클로저이름>()` 으로 실행 가능

```groovy
def clos1 = {
    println 'hi'
}

// 실행
clos1.call()
clos1()
```

<br>

### 메소드와 클로저

- 메소드 정의

```groovy
def plus(a, b) {
    a + b
}

int minus(int a, int b) {
    return a - b;
}
```

- 클로저(Closure) 정의

```groovy
plus = { a, b ->
    a + b
}

println plus('a', 20)

plus = { int a, int b ->
    return a + b
}

println plus(30, 40)
```

- 클로저 Free 변수(외부에서 선언된 변수를 클로저 내부에서 사용 가능)

```groovy
pi = 3.14159
getCircleArea = { radius ->
    // pi는 'free' 변수
    pi * radius * radius
}

println getCircleArea(2)
```

- 클로저 it 파라미터
    - it 파라미터는 암시적 매개변수로, 인수로 넘어온 값의 목록을 가지고 있다.

```groovy
plus = {
    it[0] + it[1] + it[2]
}

println plus([10, 20, 30])
```

- 클로저 반환값

```groovy
plus = { a, b ->
    a + b  // 마지막 문장의 실행값으로 return을 사용해도 됨. 마지막 문장의 실행값이 없으면 null 반환
}
```

<br>

### 클래스

- 기본으로 import 되는 패키지

```groovy
java.lang, java.io, java.math, java.net, java.util, groovy.lang, groovy.util
```

- 클래스 정의와 사용

```groovy
class Test {
    void hello() {
        println "안녕하세요!"
    }
}

t = new Test()
t.hello()
```

- 프로퍼티 선언과 사용

```groovy
class Student {
    Integer no
    String name
    Date regDate
}

s = new Student(no:1, name:"홍길동", regDate:new Date())
println s.no + "," + s.name + "," + s.regDate
```

<br>

### 문자열

문자열을 표기하는 방법은 작은 따옴표(`’’`) 와 큰 따옴표(`””`) 가 있다.

- **작은 따옴표(`’’`)** : 문자열 출력에 사용
- **큰 따옴표(`””`)** : 문자열 출력에 사용, 문자열 내부에 `$변수` 를 이용해 동적인 내용 표시 가능
- **여러 행** : 여러 줄을 사용하고 싶다면 작은 따옴표나 큰 따옴표 3개를 열고 닫으면 된다.

```java
// 작은 따옴표
intro = '''가나다라마
바사아자차
카타파하'''

// 큰 따옴표
intro = """가나다라마
바사아자차
카타파하"""
```

<br>

### String과 GString

- 문자열 표현
    - **작은 따옴표(`’’`)** : 문자열 출력에 사용
    - **큰 따옴표(`””`)** : 문자열 출력에 사용, 문자열 내부에 `$변수` 를 이용해 동적인 내용 표시 가능
    - `/` : 문자열 출력에 사용. 반드시 괄호가 있어야 한다.

```groovy
println "I like 'groovy'"
println 'I like "groovy"'
println (/I like "groovy"/)
```

- 멀티 라인 문자열
    - 여러 줄을 사용하고 싶다면 작은 따옴표나 큰 따옴표 3개를 열고 닫으면 된다.

```groovy
// 작은 따옴표
intro = '''가나다라마
바사아자차
카타파하'''

// 큰 따옴표
intro = """가나다라마
바사아자차
카타파하"""
```

- GString

```groovy
name = "corgi"
println "Hi. $name"     // Hi. corgi
println "Hi. \$name"    // Hi. $name
```

- GString과 String

```groovy
str1 = "Corgi"
str2 = "My name is $str1"
str3 = str1 + str2

assert str1 instanceof String
assert str2 instanceof GString
assert str3 instanceof String
```

- GString과 Closure

```groovy
now = new Date()
println "일반객체: $now 입니다."
println "파라미터 없는 클로저: ${new Date()}"
println "파라미터 있는 클로저: ${writer -> writer << new Date()}"
```

> **GString(groovy.lang.GString)**<br>
> 
> 문자열 보간을 위해 자리 표시자가 포함된 문자열을 담는 Groovy의 클래스다.

> **문자열 보간(String interpolation)**<br>
> 
> - 문자열에 포함된 자리 표시자(일반적으로 $, %)를 문자열 리터럴의 값으로 바꾸는 프로세스를 말한다.
> - 참고 자료 : [https://www.webopedia.com/definitions/string-interpolation/](https://www.webopedia.com/definitions/string-interpolation/)

<br>

### 메소드 호출

- Groovy는 소괄호를 생략할 수 있다.
- 아래 두 줄의 코드는 같은 결과를 발생시킨다.

```java
println('hello')
println 'hello'
```

<br>

---

<br>

## Groovy 실행 방법(with IntelliJ)

### 1. Groovy SDK 다운로드(현재 버전 3.0.9)

- 다운로드 : [https://groovy.apache.org/download.html](https://groovy.apache.org/download.html)
- 압축 해제하여 적당한 디렉토리에 보관 (ex. C:\Program Files\groovy-3.0.9)

<br>

### 2. Groovy Project 생성

- `New Project` or `New Module`
- 좌측 `Groovy` > SDK(JDK 지정), Groovy library는 앞에서 설치한 SDK 디렉토리 지정
- 만약, `New Project` 나 `New Module` 에서 `Groovy` 메뉴가 안보인다면 bundle로 설치되어 있는Groovy plugin을 enable 시켜준다.

<br>

### 3. 테스트 코드 작성 및 실행

- `New`  > `Groovy Class` > `Test` (Test.groovy 생성)
- 테스트 코드 아래와 같이 작성 후 실행

```java
package groovy
 
a = 20
println a
println plus(10, 20)
 
def plus(x, y) {
    x + y
}
 
println "실행 완료!"
```

- 실행 결과는 다음과 같다.

```java
20
30
실행 완료!
```

<br>

---

## 참고 자료

- [[Spring] Gradle을 위한 Groovy 문법 요약 정리](https://k39335.tistory.com/75)
- [아파치 그루비ㅣ위키백과](https://ko.wikipedia.org/wiki/%EC%95%84%ED%8C%8C%EC%B9%98_%EA%B7%B8%EB%A3%A8%EB%B9%84)
- [BuildTool - Groovy란? (Groovy 문법과 초간단 build.gradle 작성 예제)](https://galid1.tistory.com/647)
- [IntelliJ에서 Groovy 프로젝트 생성하고 테스트 코드 실행하기ㅣ버터필드](https://atoz-develop.tistory.com/entry/IntelliJ%EC%97%90%EC%84%9C-Groovy-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%83%9D%EC%84%B1%ED%95%98%EA%B3%A0-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EC%8B%A4%ED%96%89%ED%95%98%EA%B8%B0)
- [ClosuresㅣApache Groovy](https://groovy-lang.org/closures.html#_syntax)