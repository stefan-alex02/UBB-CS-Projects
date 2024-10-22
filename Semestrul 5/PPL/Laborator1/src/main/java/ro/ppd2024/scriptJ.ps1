$javaClass = $args[0] # Java class
$noRuns = $args[1] # No of runs
$inputFile = $args[2] # Input file name
$noThreads = $args[3] # No of threads

$inputFolder = "../../../resources/input/" # Folder where input files are stored

<#
$param4 = $args[3] # ?
Write-Host $param2
#>

ls

# Test for all techniques (0 for Sequential, 1-7 for Parallel)
for ($technique = 0; $technique -lt 8; $technique++) {
    # Execute Java class
    $sum = 0
    for ($i = 0; $i -lt $noRuns; $i++){
        Write-Host "Run" ($i+1) $javaClass "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads
        $a = java $javaClass $inputFolder$inputFile $noThreads $technique
        Write-Host $a
        $executionTimeLine = $a | Select-String -Pattern "Time: (\d+)"
        $executionTime = $executionTimeLine.Matches[0].Groups[1].Value
        $sum += [int64]$executionTime
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
    Add-Content outJ.csv "$technique,$inputFile,$noThreads,$average"
}

