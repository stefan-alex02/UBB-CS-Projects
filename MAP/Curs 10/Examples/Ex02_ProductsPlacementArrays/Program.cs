using System.Text;
using SweetsPlacementArrays.Domain;
using SweetsPlacementArrays.Domain.Sweets;

namespace SweetsPlacementArrays;

internal class Program {
    public static void Main(string[] args) {
        Console.OutputEncoding = Encoding.UTF8;
        Console.WriteLine();

        UseDisplayCase();
        
        // UseCookieTray();
    }

    private static void UseDisplayCase() {
        DisplayCase displayCase = new DisplayCase();

        // Jagged array also initialises its elements to default value
        // (null in this case).
        try {
            Console.WriteLine("No display shelves:\n" + displayCase);
        }
        catch (Exception e) {
            Console.WriteLine("Error while trying to display fruit bowls (No shelves).\n");
        }
        
        displayCase.InitialiseShelves();
        
        Console.WriteLine("Empty display:\n" + displayCase);
        
        displayCase.AddFruitBowls();
        
        Console.WriteLine("Before changing bowls:\n" + displayCase);

        // Changing a couple of fruit bowls using ref variable :
        ref FruitBowl fruitBowl = ref displayCase.Content[1][3];
        fruitBowl = new FruitBowl(FruitType.Strawberry, 23);
        
        fruitBowl = ref displayCase.Content[0][0];
        fruitBowl = new FruitBowl(FruitType.Banana, 4);

        Console.WriteLine("After changing bowls:\n" + displayCase);
    }

    private static void UseCookieTray() {
        CookieTray cookieTray = new CookieTray(5, 4);
        
        Console.WriteLine("Empty cookie tray:\n" + cookieTray);
        
        // Filling the tray with a few cookies, using an out variable through a method :
        cookieTray.PutCookie(out cookieTray.Content[0,1], new Cookie(12));
        cookieTray.PutCookie(out cookieTray.Content[3,0], new Cookie(8));
        cookieTray.PutCookie(out cookieTray.Content[0,3], new Cookie(9));
        cookieTray.PutCookie(out cookieTray.Content[2,1], new Cookie(15));
        cookieTray.PutCookie(out cookieTray.Content[2,2], new Cookie(7));
        cookieTray.PutCookie(out cookieTray.Content[4,0], new Cookie(10));
        cookieTray.PutCookie(out cookieTray.Content[3,2], new Cookie(17));
        cookieTray.PutCookie(out cookieTray.Content[4,3], new Cookie(18));
        cookieTray.PutCookie(out cookieTray.Content[0,0], new Cookie(15));
        cookieTray.PutCookie(out cookieTray.Content[1,2], new Cookie(8));
        
        Console.WriteLine("Partially filled cookie tray:\n" + cookieTray);
        
        // Filling and also replacing a few cookies in the tray, using the same method :
        cookieTray.PutCookie(out cookieTray.Content[2,3], new Cookie(19));
        
        cookieTray.PutCookie(out cookieTray.Content[3,0], new Cookie(14));
        cookieTray.PutCookie(out cookieTray.Content[4,3], new Cookie(9));
        cookieTray.PutCookie(out cookieTray.Content[2,1], new Cookie(6));
        
        Console.WriteLine("Cookie tray with replaced cookies:\n" + cookieTray);
        
        // Removing and replacing a few cookies with existent ones, which are given as ref variables,
        // using another method:
        cookieTray.PutCookie(out cookieTray.Content[2,1], null!);
        cookieTray.PutCookie(out cookieTray.Content[3,0], null!);
        
        cookieTray.PutRefCookie(out cookieTray.Content[0,1], ref cookieTray.Content[0,0]);
        cookieTray.PutRefCookie(out cookieTray.Content[1,2], ref cookieTray.Content[0,0]);
        cookieTray.PutRefCookie(out cookieTray.Content[2,2], ref cookieTray.Content[0,0]);
        
        Console.WriteLine("Cookie tray with a reference 'clones':\n" + cookieTray);
        
        // Modifying the value of a cookie with is referenced by other cookies :
        cookieTray.Content[0, 0].Size += 10;
        
        Console.WriteLine("Cookie tray with modified cookies:\n" + cookieTray);
    }
}