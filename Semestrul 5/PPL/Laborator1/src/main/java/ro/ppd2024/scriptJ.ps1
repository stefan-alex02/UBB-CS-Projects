$javaClass = $args[0] # Java class
$noRuns = $args[1] # No of runs
$inputFile = $args[2] # Input file name
$noThreads = $args[3] # No of threads

<#
$param4 = $args[3] # ?
Write-Host $param2
#>

ls

# Test for all techniques (0 for Sequential, 1-7 for Parallel)
for ($technique = 0; $technique -lt 8; $technique++){
    # Execute Java class
    $sum = 0
    for ($i = 0; $i -lt $noRuns; $i++){
        Write-Host "Rulare" ($i+1)
        $a = java $javaClass $inputFile $noThreads $technique
        Write-Host $a[$a.length-1]
        $sum += $a[$a.length-1]
        Write-Host ""
    }
    $average = $sum / $i
    Write-Host "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads " Average execution time:" $average

    # Create .csv file
    if (!(Test-Path outJ.csv)){
        New-Item outJ.csv -ItemType File
        # Write header to .csv file
        Set-Content outJ.csv 'Technique,Matrix,Threads,Execution Time'
    }

    # Append to .csv file
    Add-Content outJ.csv "$technique,$($args[0]),$($args[1]),$($media)"
}

