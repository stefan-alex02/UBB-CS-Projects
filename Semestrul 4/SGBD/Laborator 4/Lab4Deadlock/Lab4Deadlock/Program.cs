using Microsoft.Data.SqlClient;

internal class Program {
    //private const string dbDataSource = "DESKTOP-HH5PFSL\\SQLEXPRESS";
    private const string dbDataSource = "DESKTOP-0SVDHT0\\SQLEXPRESS";
        
    private const string connectionString = 
        "Data Source=" + dbDataSource + 
        ";Initial Catalog=Cinematography;Integrated Security=True;TrustServerCertificate=True";

    
    public static void Main(string[] args) {
        // Initializing the values that will be used in the transactions
        InitializeValues();

        Thread thread1 = new Thread(Transaction1);
        Thread thread2 = new Thread(Transaction2);

        thread1.Start();
        thread2.Start();

        thread1.Join();
        thread2.Join();

        Console.WriteLine("Both transactions completed successfully.");
    }

    private static void InitializeValues() {
        using var conn = new SqlConnection(connectionString);
        conn.Open();
        using var tx = conn.BeginTransaction();
        try {
            SqlCommand cmd1 = new SqlCommand(
                "UPDATE Movies SET Title = 'Interstellar' where MovieId = 1", 
                conn, tx);
            cmd1.ExecuteNonQuery();

            SqlCommand cmd2 =  new SqlCommand(
                "UPDATE Actors SET Name = 'Matt Damon' where ActorID = 1", 
                conn, tx);
            cmd2.ExecuteNonQuery();
            
            SqlCommand cmd3 = new SqlCommand(
                "SELECT * FROM Movies WHERE MovieId = 1", conn, tx);
            
            using var reader = cmd3.ExecuteReader();
            if (reader.Read()) {
                Console.WriteLine($"Movie Title: {reader["Title"]}");
            }
            reader.Close();
            
            SqlCommand cmd4 = new SqlCommand(
                "SELECT * FROM Actors WHERE ActorID = 1", conn, tx);
            
            using var reader2 = cmd4.ExecuteReader();
            if (reader2.Read()) {
                Console.WriteLine($"Actor Name: {reader2["Name"]}");
            }
            reader2.Close();

            tx.Commit();
            Console.WriteLine("Initialization completed successfully.");
        }
        catch (SqlException ex) {
            Console.WriteLine($"Transaction failed with error: {ex.Message}");
            tx.Rollback();
        }
    }

    static void Transaction1() {
        int maxAttempts = 3;
        int attempts = 0;

        while (true) {
            using var conn = new SqlConnection(connectionString);
            conn.Open();
            using var tx = conn.BeginTransaction();
            try {
                SqlCommand cmd1 = new SqlCommand(
                    "UPDATE Movies SET Title = 'Incredibles 1' where MovieId = 1", 
                    conn, tx);
                cmd1.ExecuteNonQuery();
                LogChanges("UPDATE", "Movies", DateTime.Now, "T1");

                Thread.Sleep(3000);
                
                SqlCommand cmd2 =  new SqlCommand(
                    "UPDATE Actors SET Name = 'Jason Statham 1' where ActorID = 1", 
                    conn, tx);
                cmd2.ExecuteNonQuery();
                LogChanges("UPDATE", "Actors", DateTime.Now, "T1");

                tx.Commit();
                Console.WriteLine("Transaction 1 committed successfully.");
                break;
            }
            catch (SqlException ex) {
                if (ex.Number == 1205 && ++attempts < maxAttempts) {
                    Console.WriteLine("Transaction 1 was a deadlock victim. Retrying...");
                    Thread.Sleep(1000);
                }
                else {
                    Console.WriteLine($"Transaction 1 failed with error: {ex.Message}");
                    tx.Rollback();
                    break;
                }
            }
        }
    }

    static void Transaction2() {
        int maxAttempts = 3;
        int attempts = 0;

        while (true) {
            using var conn = new SqlConnection(connectionString);
            conn.Open();
            using var tx = conn.BeginTransaction();
            try {
                Thread.Sleep(1000);
                
                SqlCommand cmd1 = new SqlCommand(
                    "UPDATE Actors SET Name = 'Jason Statham 2' where ActorID = 1", 
                    conn, tx);
                cmd1.ExecuteNonQuery();
                LogChanges("UPDATE", "Actors", DateTime.Now, "T2");

                Thread.Sleep(1000);

                SqlCommand cmd2 = new SqlCommand(
                    "UPDATE Movies SET Title = 'Incredibles 2' where MovieId = 1", 
                    conn, tx);
                cmd2.ExecuteNonQuery();
                LogChanges("UPDATE", "Movies", DateTime.Now, "T2");

                tx.Commit();
                Console.WriteLine("Transaction 2 committed successfully.");
                break;
            }
            catch (SqlException ex) {
                if (ex.Number == 1205 && ++attempts < maxAttempts) {
                    Console.WriteLine("Transaction 2 was a deadlock victim. Retrying...");
                    Thread.Sleep(1000);
                }
                else {
                    Console.WriteLine($"Transaction 2 failed with error: {ex.Message}");
                    tx.Rollback();
                    break;
                }
            }
        }
    }
    
    static void LogChanges(string operation, string table, DateTime timestamp, string transactionId) {
        Console.WriteLine($"[{timestamp}] {operation} on {table} by {transactionId}.");
        
        using var conn = new SqlConnection(connectionString);
        conn.Open();
        using var tx = conn.BeginTransaction();
        try {
            SqlCommand cmd = new SqlCommand(
                "INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate) VALUES (@operation, @table, @timestamp)", 
                conn, tx);
            cmd.Parameters.AddWithValue("@operation", operation);
            cmd.Parameters.AddWithValue("@table", table);
            cmd.Parameters.AddWithValue("@timestamp", timestamp);
            cmd.ExecuteNonQuery();
            
            tx.Commit();
        }
        catch (SqlException ex) {
            Console.WriteLine($"Failed to log changes: {ex.Message}");
            tx.Rollback();
        }
    }
}