using Microsoft.EntityFrameworkCore;

namespace Data;

public class DatabaseContext: DbContext
{
    public DbSet<User> Users { get; set; }
    
    protected override void OnModelCreating(ModelBuilder modelBuilder) {
    }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        optionsBuilder.UseSqlite("Data Source=test.db");
    }
}

/*
Windows PowerShell
Copyright (C) Microsoft Corporation. All rights reserved.

Install the latest PowerShell for new features and improvements! https://aka.ms/PSWindows

PS C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\Data> Add-Migration Initial
Add-Migration : The term 'Add-Migration' is not recognized as the name of a cmdlet, function, script file, or operable program. Check the spelling 
of the name, or if a path was included, verify that the path is correct and try again.
At line:1 char:1
+ Add-Migration Initial
+ ~~~~~~~~~~~~~
    + CategoryInfo          : ObjectNotFound: (Add-Migration:String) [], CommandNotFoundException
    + FullyQualifiedErrorId : CommandNotFoundException

PS C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\Data> wsl
stefan@DESKTOP-0SVDHT0:/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 4/SGBD/Solution1/Data$ Add-Migration Initial
Add-Migration: command not found
stefan@DESKTOP-0SVDHT0:/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 4/SGBD/Solution1/Data$ logout
PS C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\Data> dotnet ef migrations add InitialCreate --startup-project ../path/to/startup/project

Welcome to .NET 8.0!
---------------------
SDK Version: 8.0.204

Telemetry
---------
The .NET tools collect usage data in order to help us improve your experience. It is collected by Microsoft and shared with the community. You can opt-out of telemetry by setting the DOTNET_CLI_TELEMETRY_OPTOUT environment variable to '1' or 'true' using your favorite shell.

Read more about .NET CLI Tools telemetry: https://aka.ms/dotnet-cli-telemetry

----------------
Installed an ASP.NET Core HTTPS development certificate.
To trust the certificate, run 'dotnet dev-certs https --trust'
Learn about HTTPS: https://aka.ms/dotnet-https

----------------
Write your first app: https://aka.ms/dotnet-hello-world
Find out what's new: https://aka.ms/dotnet-whats-new
Explore documentation: https://aka.ms/dotnet-docs
Report issues and find source on GitHub: https://github.com/dotnet/core
Use 'dotnet --help' to see available commands or visit: https://aka.ms/dotnet-cli
--------------------------------------------------------------------------------------
MSBUILD : error MSB1009: Project file does not exist.
Switch: C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\path\to\startup\project
Unable to retrieve project metadata. Ensure it's an SDK-style project. If you're using a custom BaseIntermediateOutputPath or MSBuildProjectExtensionsPath values, Use the --msbuildprojectextensionspath option.
PS C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\Data> dotnet ef migrations add InitialCreate                         
Build started...    
Build succeeded.
Your startup project 'Data' doesn't reference Microsoft.EntityFrameworkCore.Design. This package is required for the Entity Framework Core Tools to work. Ensure your startup project is correct, install the package, and try again.
PS C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\Data> dotnet ef migrations add InitialCreate
Build started...
Build succeeded.
The Entity Framework tools version '8.0.4' is older than that of the runtime '9.0.0-preview.3.24172.4'. Update the tools for the latest features and bug fixes. See https://aka.ms/AAc1fbw for more information.
Done. To undo this action, use 'ef migrations remove'
PS C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\Data> dotnet ef database update
Build started...
Build succeeded.
The Entity Framework tools version '8.0.4' is older than that of the runtime '9.0.0-preview.3.24172.4'. Update the tools for the latest features and bug fixes. See https://aka.ms/AAc1fbw for more information.
Applying migration '20240423152352_InitialCreate'.
Done.
PS C:\Users\Stefan\Documents\GitHub\UBB-CS-Projects\Semestrul 4\SGBD\Solution1\Data>
*/