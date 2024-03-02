namespace CookieBoxingAndUnboxing.Domain;

/// <summary>
/// A shop with a specific name.
/// </summary>
public class Shop {
    private String name;

    /// <summary>
    /// The name of the shop.
    /// </summary>
    /// <exception cref="ArgumentNullException">if the given shop name is null</exception>
    public string Name {
        get => name;
        set => name = value ?? throw new ArgumentNullException(nameof(value));
    }

    public Shop(string name) {
        this.name = name;
    }

    public override string ToString() {
        return name;
    }
}