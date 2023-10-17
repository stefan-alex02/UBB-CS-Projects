package ir.map.g221;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Student s1= new Student("Dan", 4.5f);
        Student s2= new Student("Ana", 4.5f);
        Student s3= new Student("Dan", 4.5f);
        Student s4= new Student("Dan", 6.5f);

        TreeSet<Student> multime4 = new TreeSet<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Float.compare(o1.getMedia(), o2.getMedia());
            }
        });
        multime4.add(s1);
        multime4.add(s2);
        multime4.add(s3);
        multime4.add(s4);

        multime4.forEach(System.out::println);
        System.out.println();

        TreeSet<Student> multime3 = new TreeSet<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getNume().compareTo(o2.getNume());
            }
        });
        multime3.add(s1);
        multime3.add(s2);
        multime3.add(s3);
        multime3.add(s4);

        multime3.forEach(System.out::println);


//        TreeSet<Student> multime2 = new TreeSet<>();
//        multime2.add(s1);
//        multime2.add(s2);
//        multime2.add(s3);
//        multime2.add(s4);
//
//        multime2.forEach(System.out::println);

//        HashSet<Student> multime = new HashSet<>();
//        multime.add(s1);
//        multime.add(s2);
//        multime.add(s3);

//        System.out.println(s1.hashCode());
//        System.out.println(s2.hashCode());
//        System.out.println(s3.hashCode());

//        Object o1 = new Student("Dan", 6.5f);
//        Object o2 = new Student("Dan", 6.5f);
//        Object o3 = o1;
//
//        System.out.println(o1.hashCode());
//        System.out.println(o2.hashCode());
//        System.out.println(o3.hashCode());

//        for (var elem:multime) {
//            System.out.println(elem);
//        }

//        multime.forEach(System.out::println);
//        multime.forEach(x -> x.setMedia(9));
//        multime.forEach(x -> {
//            if (x.getNume().compareTo("Dan") == 0) {
////                multime.remove(x);
//                System.out.println(x);
//            }
//        });

//        multime.forEach(x -> System.out.println(x));
    }
}