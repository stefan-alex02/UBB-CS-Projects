# Main.java

$javaClass = $args[0] # Java class
$noRuns = 10 # No of runs

$inputFolder = "../../../resources/input/" # Folder where input files are stored
$inputFiles = Get-ChildItem -Path $inputFolder -File

# Iterating through all input files
foreach ($inputFile in $inputFiles) {
    if ($inputFile.Name -eq "data_10_10_3.txt") {
        $threadCounts = @(4)
    } else {
        $threadCounts = @(2, 4, 8, 16) # Array of thread counts
    }

    # Iterating through all techniques (0 for Sequential, 1-7 for Parallel)
    for ($technique = 0; $technique -lt 8; $technique++) {
        # Iterating through all thread counts
        foreach ($noThreads in $threadCounts) {
            # Execute scriptJ.ps1
            .\scriptJ.ps1 $javaClass $noRuns "$inputFolder$inputFile" $noThreads $technique
        }
    }
}