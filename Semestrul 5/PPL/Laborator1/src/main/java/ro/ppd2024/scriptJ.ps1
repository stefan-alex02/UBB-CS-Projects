# "Main.java" 10 "data_10_10_3.txt" 8

$javaClass = $args[0] # Java class
$noRuns = $args[1] # No of runs
$inputFile = $args[2] # Input file name
$noThreads = $args[3] # No of threads
$technique = $args[4] # Technique (0 for Sequential, 1-7 for Parallel)

# Execute Java class
$sum = 0
for ($i = 0; $i -lt $noRuns; $i++) {
    Write-Host "Run" ($i + 1) $javaClass "Runs:" $noRuns "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads
    $a = java $javaClass $inputFile $noThreads $technique
    Write-Host $a
    $executionTimeLine = $a | Select-String -Pattern "Time: (\d+)"
    $executionTime = $executionTimeLine.Matches[0].Groups[1].Value
    $sum += [int64]$executionTime
    Write-Host ""
}
$average = $sum / $i
Write-Host "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads " Average execution time:" $average

# Create .csv file
if (!(Test-Path outJ.csv)) {
    New-Item outJ.csv -ItemType File
    # Write header to .csv file
    Set-Content outJ.csv 'Technique,Matrix,Threads,Execution Time'
}

# Append to .csv file
Add-Content outJ.csv "$technique,$inputFile,$noThreads,$average"
