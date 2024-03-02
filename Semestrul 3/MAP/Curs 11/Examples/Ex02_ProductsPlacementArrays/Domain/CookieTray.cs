using System.Text;
using SweetsPlacementArrays.Domain.Sweets;

namespace SweetsPlacementArrays.Domain;

public class CookieTray {
    // A multi-dimensional array, is an array with a fixed number of
    // rows and columns, in this case.
    private Cookie[,] content;
    
    public Cookie[,] Content {
        get => content;
    }

    public CookieTray(int rows, int columns) {
        // When initializing a multi-dimensional array, all elements of the main array
        // (e.g. the Cookies on every row and column) are initialised to default value.
        this.content = new Cookie[rows, columns];
    }
    
    public override string ToString() {
        StringBuilder stringBuilder = new();

        int i = 0;
        foreach (var cookie in content) {
            if (cookie is not null) {
                stringBuilder.Append(cookie + "  ");
            }
            else {
                stringBuilder.Append("  _    ");
            }

            if (++i % content.GetLength(1) == 0) {
                stringBuilder.AppendLine();
            }
        }

        return stringBuilder.ToString();
    }

    public void PutCookie(out Cookie oldCookie, Cookie newCookie) {
        oldCookie = newCookie;
    }

    public void PutRefCookie(out Cookie oldCookie, ref Cookie newCookie) {
        oldCookie = newCookie;
    }
}