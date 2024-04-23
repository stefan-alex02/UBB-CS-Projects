using System.ComponentModel.DataAnnotations;

namespace Data;

public class User(int id, string name, string email) {
    [Key]
    public int Id { get; set; } = id;
    public string Name { get; set; } = name;
    public string Email { get; set; } = email;
}
