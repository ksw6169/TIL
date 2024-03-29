import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaEx5 {

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
