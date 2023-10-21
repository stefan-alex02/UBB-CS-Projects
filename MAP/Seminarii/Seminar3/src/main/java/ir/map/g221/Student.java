package ir.map.g221;

import java.util.Comparator;
import java.util.Objects;

//public class Student implements Comparable<Student>{
public class Student {
    private String nume;
    private float media;

    public Student(String nume, float media) {
        this.nume = nume;
        this.media = media;
    }

//    @Override
//    public int compare(Student o1, Student o2) {
//        return 0;
//    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Eq: " + this + " " + o);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Float.compare(media, student.media) == 0 && Objects.equals(nume, student.nume);
    }

//    @Override
//    public int hashCode() {
//        System.out.println(this);
//        var h = Objects.hash(nume, media);
//        return h;
//    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public String getNume() {
        return nume;
    }

    public float getMedia() {
        return media;
    }

    @Override
    public String toString() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", media=" + media +
                '}';
    }

//    @Override
//    public int compareTo(Student o) {
////        return (int)(this.media - o.media);
//        return Float.compare(this.media, o.media);
////        return ((Float)this.media).compareTo(o.media);
//    }
}
