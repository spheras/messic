#!/bin/bash
#
# Copyright (C) 2013 José Amuedo
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



# testing if the directory is already decompressed
if [ -d "./jre1.8.0_05" ]; then
	#nothing todo
	echo "using existing decompressed java"
else
	#discovering architecture
	if [ "$(uname -m | grep '64')" != "" ]; then
		echo "64 bits!"
	  JAVA_GZ='jre-8u5-linux-x64.tar.gz'
	else
		echo "32 bits!"
	  JAVA_GZ='jre-8u5-linux-i586.tar.gz'
	fi
	
    #decompressing
    tar xzvf ../jvm/$JAVA_GZ
fi

cd ..
./bin/jre1.8.0_05/bin/java -cp ".:./classpath/aspectjweaver-1.7.4.jar:./classpath/commons-io-2.4.jar:./classpath/log4j-1.2.14.jar:./classpath/messic-service-1.0-SNAPSHOT.jar:./classpath/messic-starter-1.0-SNAPSHOT.jar:./classpath/org.apache.felix.framework-4.2.1.jar:./classpath/org.apache.felix.main-4.2.1.jar:./classpath/tools-1.4.2.jar" org.messic.starter.Starter $1