This instructions details the creation of the nsis .exe installer

1. first download nsis and install it at any windows computer
2. Modify the file nsis_installer/script.nsi, the line 18, something like (!define ZIP_FILE "messic-1.0.0-beta.app.zip"), to the new zip name
3. Modify the version numbering major minor build
4. copy there the zip of messic
5. IMPORTANT! ensure that the zip contains directly messic (by default the zip contains a folder which contains messic).
6. compile with nsis the script at nsis_installer/script.nsi
7. that's all!