# build/Debug/dynamic.exe
# cmake-build-debug/dynamic.exe

#$exeFile = $args[0] # Executable file
$exeFile = "build/Debug/dynamic.exe" # Executable file
$noRuns = 4 # No of runs

# Check if the executable exists
if (-Not (Test-Path $exeFile)) {
    Write-Host "Executable not found: $exeFile"
    exit 1
}

$inputFolder = "data/input/" # Folder where input files are stored
$inputFiles = Get-ChildItem -Path $inputFolder -File | Sort-Object Name

# Getting thread counts
$threadCounts = @(2, 4, 8, 16)

# Iterating through all thread counts
foreach ($noThreads in $threadCounts) {

    # Iterating through all input files
    foreach ($inputFile in $inputFiles) {

        # Iterating through all techniques (0 for Sequential, 1-7 for Parallel)
        for ($technique = 0; $technique -lt 8; $technique++) {

            # Execute Java class only if compilation succeeds
            $sum = 0
            for ($i = 0; $i -lt $noRuns; $i++) {
                Write-Host "Run" ($i + 1) "Technique:" $technique " Input file:" $inputFile " Threads:" $noThreads
                $command = "& $exeFile $inputFolder$inputFile $noThreads $technique 2>&1"
                $a = Invoke-Expression $command
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
        }
    }
}
