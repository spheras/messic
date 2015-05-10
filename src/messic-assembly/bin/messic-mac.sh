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

JAVA_GZ='jre-8u5-macosx-x64.tar.gz'
JAVA_FOLDER='jre1.8.0_45.jre'

	# testing if the directory is already decompressed
		if ! [ -d "./jre1.8.0_45.jre" ]; then
			#decompressing
			tar xzvf ../jvm/$JAVA_GZ
		fi

#concatenate the classpath files, by Casimiro
classpathVar="."
array=($(ls ../classpath))
for i in "${array[@]}"
do
	classpathVar="$classpathVar:./classpath/$i"
done

cd ..
./bin/$JAVA_FOLDER/Contents/Home/bin/java -cp "$classpathVar" org.messic.starter.Starter $1 $2 $3 $4 $5
