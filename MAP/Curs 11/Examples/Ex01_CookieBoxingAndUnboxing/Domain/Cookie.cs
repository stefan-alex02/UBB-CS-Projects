namespace CookieBoxingAndUnboxing.Domain;

/// <summary>
/// A simple cookie of a specific size.
/// </summary>
public struct Cookie {
    private int size;

    /// <summary>
    /// The size of the cookie.
    /// </summary>
    public int Size {
        get => size;
        set => size = value;
    }

    public Cookie(int size) {
        this.size = size;
    }
}