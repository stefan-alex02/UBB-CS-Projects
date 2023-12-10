namespace CookieShapesAsOperator.Exceptions;

public class UnavailableShapeException : Exception{
    public UnavailableShapeException(string? message) : base(message) {
    }
}