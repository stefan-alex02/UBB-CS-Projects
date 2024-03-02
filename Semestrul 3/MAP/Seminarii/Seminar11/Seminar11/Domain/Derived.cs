namespace Seminar11;

public class Derived : Base {
    // public override void m() {
    public new void m() {
        Console.WriteLine("Derived");
    }
}