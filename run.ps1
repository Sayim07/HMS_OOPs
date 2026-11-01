Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "Compiling Hospital Management System..." -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

javac -d bin src/hospital/*.java src/hospital/model/*.java src/hospital/exception/*.java src/hospital/service/*.java src/hospital/util/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful! Starting application...`n" -ForegroundColor Green
    java -cp bin hospital.Main
} else {
    Write-Host "[ERROR] Compilation failed." -ForegroundColor Red
}

