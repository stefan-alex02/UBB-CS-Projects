using System.Text;
using SweetsPlacementArrays.Domain.Sweets;

namespace SweetsPlacementArrays.Domain;

public class DisplayCase {
    // A jagged array is simply an array of arrays (which can be of any size).
    private FruitBowl[][] content;

    public FruitBowl[][] Content {
        get => content;
    } 

    public DisplayCase() {
        // When initializing a jagged array, all elements of the main array
        // (which are also arrays -> reference type) are set to default value, which is null.
        this.content = new FruitBowl[4][];
    }

    public void InitialiseShelves() {
        for (int i = 0; i < content.Length; i++) {
            content[i] = Array.Empty<FruitBowl>();
        }
    }

    public void AddFruitBowls() {
        this.content[0] = new[] {
            new FruitBowl(FruitType.Apple, 4)
        };
        this.content[1] = new[] {
            new FruitBowl(FruitType.Orange, 7),
            new FruitBowl(FruitType.Strawberry, 20),
            new FruitBowl(FruitType.Apple, 5),
            new FruitBowl(FruitType.Peach, 3),
            new FruitBowl(FruitType.Orange, 5)
        };
        this.content[2] = new[] {
            new FruitBowl(FruitType.Banana, 3),
            new FruitBowl(FruitType.Peach, 6)
        };
        this.content[3] = new[] {
            new FruitBowl(FruitType.Strawberry, 16),
            new FruitBowl(FruitType.Peach, 4),
            new FruitBowl(FruitType.Orange, 6)
        };
    }

    public override string ToString() {
        StringBuilder stringBuilder = new();
        
        foreach (var shelf in content) {
            stringBuilder.Append("[ ");
            foreach (var shortcake in shelf) {
                stringBuilder.Append(shortcake + " ");
            }
            stringBuilder.Append("]\n");
        }

        return stringBuilder.ToString();
    }
}