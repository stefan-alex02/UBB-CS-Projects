package ir.map.gr221;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static <E> void  printArie(List<E> l, Arie<E> f) {
        l.forEach(x -> System.out.println(f.compute(x)));
        /// TODO : de cugetat la inlocuirea functiei lambda din linia 7 cu referinta la metoda
    }

    public static void main(String[] args) {
        Cerc cerc1 = new Cerc(2.0);
        Cerc cerc2 = new Cerc(3.0);

        List<Cerc> listaCercuri = Arrays.asList(cerc1, cerc2);

        Patrat patrat1 = new Patrat(1.0);
        Patrat patrat2 = new Patrat(2.0);

        List<Patrat> listaPatratele = Arrays.asList(patrat1, patrat2);

        Arie<Cerc> arieCerc = cerc -> Math.PI * cerc.getRaza() * cerc.getRaza();
        Arie<Cerc> arieCerc2 = (Cerc c) -> {
            return Math.PI * c.getRaza() * c.getRaza();
        };
        Arie<Patrat> ariePatratel = patrat -> patrat.getLatura() * patrat.getLatura();

        printArie(listaCercuri, arieCerc);
        printArie(listaPatratele, ariePatratel);

//        Arie<Patrat> ariePatrat2 = new Arie<Patrat>() {
//            @Override
//            public Double compute(Patrat entity) {
//                return null;
//            }
//        };

//        System.out.println(arieCerc.compute(cerc1));
//        System.out.println(arieCerc.compute(cerc2));
//        System.out.println(ariePatrat.compute(patrat1));
//        System.out.println(ariePatrat.compute(patrat2));

//        List<String> stringList = Arrays.asList("ab", "abc", "aabbcc", "def", "xyz", "mnpq");
//        stringList.forEach(str -> {
//            if (str.startsWith("a")) {
//                System.out.println(str);
//            }
//        });
//        stringList.stream()
//                .filter(str -> str.startsWith("a"))
//                .forEach(System.out::println);

        List<String> stringList2 = Arrays.asList(
                "ab", "abc", "Anel", "def", "xyz", "A", "NAneluta", "Ane", "ane");
//        stringList2.stream()
//                .filter("Aneluta"::startsWith)
//                .forEach(System.out::println);

        List<String> stringList3 = new ArrayList<>();
        stringList3.addAll(stringList2);

//        stringList3.removeIf("Aneluta"::startsWith);

//        Predicate<String> estePrefixAlAnelutei = (String s) -> {
//            return "Aneluta".startsWith(s);
//        };
//        stringList3.removeIf(estePrefixAlAnelutei);

//        Predicate<String> estePrefixAlAnelutei2 = "Aneluta"::startsWith;
//        stringList3.removeIf(estePrefixAlAnelutei2);
//
//        stringList3.forEach(System.out::println);

        List<String> stringList4 = Arrays.asList(
                "abc", "abd", "bcd", "bce", "cab"
        );

        stringList4.stream()
                .filter(str -> str.startsWith("b"))
                .map(String::toUpperCase)
                .forEach(System.out::println);

        stringList4.stream()
                .filter(str -> {
                    System.out.println(str);
                    return str.startsWith("b");
                })
                .map(str -> {
                    System.out.println(str);
                    return str.toUpperCase();
                })
                .forEach(System.out::println);
        /// TODO : de incercat si varianta cu sortare
        /// Care este outputul secv de cod al liniilor 77-90 ?
    }
}