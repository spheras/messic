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

echo "Starting messic service..."

JAVA_GZ='jre-8u45-macosx-x64.tar.gz'
JAVA_FOLDER='jre1.8.0_45.jre'

#concatenate the classpath files, by Casimiro
classpathVar="."
array=($(ls ../classpath))
for i in "${array[@]}"
do
	classpathVar="$classpathVar:./classpath/$i"
done

./bin/$JAVA_FOLDER/Contents/Home/bin/java -cp "./classpath/org.apache.felix.framework-4.2.1.jar:./classpath/log4j-1.2.14.jar:./classpath/tools-1.4.2.jar:./classpath/aopalliance-1.0.jar:./classpath/hibernate-core-4.2.1.Final.jar:./classpath/spring-core-4.0.0.RELEASE.jar:./classpath/aspectjweaver-1.7.4.jar:./classpath/commons-logging-1.1.2.jar:./classpath/spring-beans-4.0.0.RELEASE.jar:./classpath/dom4j-1.6.1.jar:./classpath/commons-dbcp-1.4.jar:./classpath/antlr-2.7.7.jar:./classpath/h2-1.3.171.jar:./classpath/spring-aop-4.0.0.RELEASE.jar:./classpath/javassist-3.15.0-GA.jar:./classpath/hibernate-commons-annotations-4.0.1.Final.jar:./classpath/spring-expression-4.0.0.RELEASE.jar:./classpath/hibernate-entitymanager-4.2.1.Final.jar:./classpath/jboss-logging-3.1.0.GA.jar:./classpath/messic-configuration-1.0.0-alpha.jar:./classpath/commons-pool-1.5.4.jar:./classpath/messic-service-1.0.0-alpha.jar:./classpath/spring-context-4.0.0.RELEASE.jar:./classpath/hibernate-jpa-2.0-api-1.0.1.Final.jar:./classpath/commons-io-2.4.jar:./classpath/messic-starter-1.0.0-alpha.jar:./classpath/jboss-transaction-api_1.1_spec-1.0.1.Final.jar:./classpath/org.apache.felix.main-4.2.1.jar:./classpath/commons-cli-1.2.jar" org.messic.service.MessicMain $1