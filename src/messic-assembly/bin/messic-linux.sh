#!/bin/bash
#
# Copyright (C) 2013
#
#  This file is part of Messic.
# 
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

echo "Starting messic..."

CURRENT_PATH=$(pwd)

cd ./bin

	#discovering architecture
	if [ "$(uname -m | grep '64')" != "" ]; then
		echo "64 bits!"
		JAVA_GZ='jre-8u45-linux-x64.tar.gz'
        JAVA_FOLDER='jre1.8.0_45'
	  
		# testing if the directory is already decompressed
		if ! [ -d "./jre1.8.0_45-x64" ]; then
			#decompressing
			tar xzvf ../jvm/$JAVA_GZ
		fi
	else
		echo "32 bits!"
		JAVA_GZ='jre-845-linux-i586.tar.gz'
        JAVA_FOLDER='jre1.8.0_45'
		
		# testing if the directory is already decompressed
		if ! [ -d "./jre1.8.0_45" ]; then
			#decompressing
			tar xzvf ../jvm/$JAVA_GZ
		fi
	fi	


#recreating the messic.desktop each time (due to the need to put absolute path to icon and exe)
if [ -f "../messic.desktop" ]; then
    rm ../messic.desktop
fi
echo "[Desktop Entry]" >> ../messic.desktop
echo "Name=messic" >> ../messic.desktop
echo "Comment=messic" >> ../messic.desktop
echo "Terminal = false" >> ../messic.desktop
echo "Type=Application" >> ../messic.desktop
echo 'Exec=bash -c "cd ' $CURRENT_PATH ' && ./bin/messic.sh"' >> ../messic.desktop
echo "Icon=$CURRENT_PATH/bin/messic-icon.png" >> ../messic.desktop
echo "Categories = Audio;Player;;" >> ../messic.desktop


#concatenate the classpath files, by Casimiro
classpathVar="."
array=($(ls ../classpath))
for i in "${array[@]}"
do
	classpathVar="$classpathVar:./classpath/$i"
done


cd ..
./bin/$JAVA_FOLDER/bin/java -cp "$classpathVar" org.messic.starter.Starter $1 $2 $3 $4 $5
