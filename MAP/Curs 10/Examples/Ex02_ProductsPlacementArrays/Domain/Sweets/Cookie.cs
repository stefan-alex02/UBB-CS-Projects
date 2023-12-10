namespace SweetsPlacementArrays.Domain.Sweets;

/// <summary>
/// A simple cookie of a specific size.
/// </summary>
public class Cookie {
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

    public override string ToString() {
        return "\ud83c\udf6a:" + Size + (Size < 10 ? " " : "");
    }
}