using System;

namespace rt.MathUtil;

public class Solution {
    public static Solution Empty = new();

    public double[] Values { get; }
    public bool IsUnique => Values.Length == 1;
    public bool IsEmpty => Values.Length == 0;

    private Solution() {
        Values = Array.Empty<double>();
    }

    public Solution(double[] values) {
        Values = values;
    }

    public static Solution Unique(double value) {
        return new Solution(new[] { value });
    }
}