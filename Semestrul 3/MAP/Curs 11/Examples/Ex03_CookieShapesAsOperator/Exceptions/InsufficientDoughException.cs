namespace CookieShapesAsOperator.Exceptions;

public class InsufficientDoughException : Exception{
    public double NeededAmount { get; }
    
    public InsufficientDoughException(string? message, double neededAmount) : base(message) {
        NeededAmount = neededAmount;
    }
}