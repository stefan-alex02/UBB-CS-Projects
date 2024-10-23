# build/Debug/dynamic.exe
# cmake-build-debug/dynamic.exe

$exeFile = $args[0] # exe class
$noRuns = $args[1] # No of runs
$inputFile = $args[2] # Input file name
$noThreads = $args[3] # No of threads
$technique = $args[4] # Technique (0 for Sequential, 1-7 for Parallel)
$csvFile = $args[5] # CSV file

# Check if the executable exists
if (-Not (Test-Path $exeFile)) {
    Write-Host "Executable not found: $exeFile"
    exit 1
}

# Execute C++ program
$sum = 0
for ($i = 0; $i -lt $noRuns; $i++) {
    Write-Host "Run" ($i + 1) "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads
    $command = "& $exeFile $inputFolder$inputFile $noThreads $technique 2>&1"

    $a = Invoke-Expression $command
    Write-Host $a

#                $command = "$exeFile $inputFolder$inputFile $noThreads $technique"
#                Write-Host "Executing command: $command"
#                $a = & $exeFile "$inputFolder$inputFile" $noThreads $technique 2>&1
#                Write-Host "Output from executable:"
#                Write-Host $a

    if ($technique -eq 0) {
        $executionTimeLine = $a | Select-String -Pattern "Seq. time: (\d+(\.\d+)?)"
    } else {
        $executionTimeLine = $a | Select-String -Pattern "Par. time: (\d+(\.\d+)?)"
    }

    if ($executionTimeLine) {
        $executionTime = $executionTimeLine.Matches[0].Groups[1].Value
        $sum += [float]$executionTime
    } else {
        Write-Host "No execution time found in output."
    }
}
$average = [float]$sum / [float]$i
$formattedAverage = "{0:N6}" -f $average
Write-Host ""
Write-Host "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads " Avg execution time:" $formattedAverage
Write-Host ""

# Create .csv file
if (!(Test-Path $csvFile)) {
    New-Item $csvFile -ItemType File
    # Write header to .csv file
    Set-Content $csvFile 'Threads,Matrix,Technique,Execution Time'
}

# Append to .csv file
Add-Content $csvFile "$noThreads,$inputFile,$technique,$formattedAverage"
