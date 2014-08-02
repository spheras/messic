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
./bin/jre1.8.0_05/bin/java -cp ".:./classpath/antlr-2.7.7.jar:./classpath/aspectjweaver-1.7.4.jar:./classpath/c3p0-0.9.2.1.jar:./classpath/commons-dbcp-1.4.jar:./classpath/commons-io-2.4.jar:./classpath/commons-pool-1.5.4.jar:./classpath/dom4j-1.6.1.jar:./classpath/h2-1.3.171.jar:./classpath/hibernate-commons-annotations-4.0.1.Final.jar:./classpath/hibernate-core-4.2.1.Final.jar:./classpath/hibernate-entitymanager-4.2.1.Final.jar:./classpath/hibernate-jpa-2.0-api-1.0.1.Final.jar:./classpath/javassist-3.15.0-GA.jar:./classpath/jboss-logging-3.1.0.GA.jar:./classpath/jboss-transaction-api_1.1_spec-1.0.1.Final.jar:./classpath/log4j-1.2.14.jar:./classpath/mchange-commons-java-0.2.3.4.jar:./classpath/org.apache.felix.framework-4.2.1.jar:./classpath/org.apache.felix.main-4.2.1.jar:./classpath/tools-1.4.2.jar:./classpath/messic-starter-1.0-SNAPSHOT.jar" org.messic.starter.Starter $1