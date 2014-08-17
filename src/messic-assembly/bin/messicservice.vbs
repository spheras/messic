Set oShell = CreateObject ("Wscript.Shell") 
Dim strArgs
strArgs = "cmd /k start ""Messic Service"" /B  ./bin/jre1.8.0_05/bin/java -cp """ & WScript.Arguments(0) & """ org.messic.service.MessicMain """ & WScript.Arguments(1) & """"
'msgbox strArgs ,0, "Argumentos"
oShell.Run strArgs, 0, false