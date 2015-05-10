Set oShell = CreateObject ("Wscript.Shell") 
Dim strArgs
strArgs = "cmd /c start ""Messic Service"" /B  ./bin/jre1.8.0_45/bin/java -cp """ & WScript.Arguments(0) & """ org.messic.service.MessicMain """ & WScript.Arguments(1) & """"
'msgbox strArgs ,0, "Argumentos"
oShell.Run strArgs, 0, false