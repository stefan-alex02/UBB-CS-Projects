namespace SweetsPlacementArrays.Domain.Sweets;

public enum FruitType : int {
    Orange,      // int value is 0
    Strawberry,  // int value is 1 (it is automatically incremented if not specified)
    Apple = 5,   // int value is 5
    Peach,       // int value is 6 (the next value after the maximum previous one)
    Banana       // int value is 7
}