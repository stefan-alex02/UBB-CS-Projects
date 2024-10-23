# "Main.java" 10 "data_10_10_3.txt" 8

$javaClass = $args[0] # Java class
$noRuns = $args[1] # No of runs
$inputFile = $args[2] # Input file name
$noThreads = $args[3] # No of threads
$technique = $args[4] # Technique (0 for Sequential, 1-7 for Parallel)

if ($compiledFiles.Count -eq 0) {
    # Compile Java code
    $compileResult = javac -d bin -sourcepath src src/main/java/ro/ppd2024/*.java src/main/java/ro/ppd2024/distributor/*.java src/main/java/ro/ppd2024/util/*.java src/main/java/ro/ppd2024/thread/*.java src/main/java/ro/ppd2024/thread/linear/*.java src/main/java/ro/ppd2024/thread/cyclic/*.java

    if ($compileResult) {
        Write-Host "Compilation failed. Exiting..."
        exit 1
    }
} else {
    Write-Host "Code is already compiled. Skipping compilation."
}

# Execute Java class only if compilation succeeds
$sum = 0
for ($i = 0; $i -lt $noRuns; $i++) {
    Write-Host "Run" ($i + 1) $javaClass "Runs:" $noRuns "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads
    $a = java -cp bin $javaClass "$inputFolder$inputFile" $noThreads $technique
    Write-Host $a

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
if (!(Test-Path outJ.csv)) {
    New-Item outJ.csv -ItemType File
    # Write header to .csv file
    Set-Content outJ.csv 'Threads,Matrix,Technique,Execution Time'
}

# Append to .csv file
Add-Content outJ.csv "$noThreads,$inputFile,$technique,$formattedAverage"
