namespace SweetsPlacementArrays.Domain.Sweets;

public class FruitBowl {
    public FruitType FruitType { get; set; }
    public int Count { get; set; }

    public FruitBowl(FruitType type, int size) => (FruitType, Count) = (type, size);

    public override string ToString() =>
        "(" + Count +
        FruitType switch {
            FruitType.Orange     => "\ud83c\udf4a",
            FruitType.Strawberry => "\ud83c\udf53",
            FruitType.Apple      => "\ud83c\udf4e",
            FruitType.Peach      => "\ud83c\udf51",
            FruitType.Banana     => "\ud83c\udf4c",
            _                    => "[Unknown fruit]"
        } +
        ")";
}