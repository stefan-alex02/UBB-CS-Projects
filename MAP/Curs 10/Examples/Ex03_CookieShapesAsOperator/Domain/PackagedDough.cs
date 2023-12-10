namespace CookieShapesAsOperator.Domain;

/// <summary>
/// A finalizable Packaged Dough class, that uses Dispose pattern and
/// Exception handling.
/// </summary>
public class PackagedDough : IDisposable {
    /// <summary>
    /// A flag that marks whether the instance was disposed or not.
    /// </summary>
    public bool Disposed { get; private set; }
    private double size;

    public double Size {
        get {
            if (Disposed) {
                throw new ObjectDisposedException("Packaged Dough", "Is already disposed");
            }

            return size;
        }
    }

    public PackagedDough(double size) {
        if (size <= 0) {
            throw new ArgumentOutOfRangeException(nameof(size), "Size cannot be 0 or less");
        }
        Disposed = false;
        
        this.size = size;
    }

    /// <summary>
    /// Consumes the dough by taking a piece out of it.
    /// </summary>
    /// <param name="amount">the maximum amount to take</param>
    /// <returns>the amount taken from the package (which can be the requested amount or less if the package had less dough</returns>
    /// <exception cref="ObjectDisposedException"></exception>
    public double Consume(double amount) {
        if (Disposed) {
            throw new ObjectDisposedException("Packaged Dough", "Is already disposed");
        }

        double amountToReturn = Math.Min(size, amount);
        
        if (amountToReturn.Equals(size)) {
            size = 0;
            Dispose(false); // Disposing the package
        }
        else {
            size -= amount;
        }

        return amountToReturn;
    }
    
    protected virtual void Dispose(bool disposing) {
        if (Disposed) return;

        // Releasing all Unmanaged Resources (if any) :
        size = 0;
        
        Disposed = true;
    }
    
    /// <summary>
    /// The finalizer.
    /// </summary>
    ~PackagedDough() {
        Dispose(false);
    }


    public void Dispose() {
        Dispose(true);
        GC.SuppressFinalize(this);
    }

    public override string ToString() {
        if (Disposed) {
            return "\ud83c\udf5e:\ud83d\uddd1\ufe0f";
        }

        return "\ud83c\udf5e:" + size;
    }
}