#!/bin/bash

APPNAME=${2:-$(basename "$1" ".sh")}
DIR="$APPNAME.app/Contents/MacOS"

if [ -d "$APPNAME.app" ]; then
  echo "$PWD/$APPNAME.app already exists :("
  exit 1
fi

mkdir -p "$DIR"
cp "$1" "$DIR/$APPNAME"
chmod +x "$DIR/$APPNAME"

echo "$PWD/$APPNAME.app"

#$ appify your-shell-script.sh "Your App Name"
#http://mathiasbynens.be/notes/shell-script-mac-apps
