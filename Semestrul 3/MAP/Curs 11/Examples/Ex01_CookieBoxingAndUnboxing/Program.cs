using CookieBoxingAndUnboxing.Domain;

namespace CookieBoxingAndUnboxing;

internal class Program {
    public static void Main(string[] args) {
        int cookieSize = 10; // allocated on Stack
        // same as Int32 cookieSize = 10;
        
        float cookieWeight = 35.7F; // also allocated on Stack

        new Shop("Cookie shop"); // allocated on the Heap (with its name),
        // but without being referenced by any variable.
                                       
        Shop shop = new Shop("First cookie shop"); // allocated on the Heap,
        // and also referenced by variable 'shop'.
        
        Cookie cookie = new Cookie(cookieSize); // Cookie is a struct, so its allocated on Stack,
        // with its size.

        Object boxedCookie = (Object)cookie; // Boxing the cookie (a new copy of the cookie
        // is allocated on the Heap).
                                             
        Console.WriteLine("\nDoes the boxed cookie reference the same value as cookie ? " + 
                          ReferenceEquals(cookie, boxedCookie));

        cookie.Size = 20; // Modifying the size of the stack-allocated cookie,
        // the size of the boxed cookie (which is a copy) is unchanged.

        Cookie unboxedCookie = (Cookie)boxedCookie; // Unboxing the boxed cookie.

        Console.WriteLine("Size of cookie : " + cookie.Size);
        Console.WriteLine("Size of (unboxed) boxed cookie : " + unboxedCookie.Size);
    }
}