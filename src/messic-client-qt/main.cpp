#include "mainwindow.h"
#include <QApplication>
#include <stdio.h>
#include <QProcess>
#include <QStringList>

#include <unistd.h>
#include <iostream>

int main(int argc, char *argv[])
{

    //starting jetty server
    QProcess grep;
    QStringList params;
    chdir("jetty-distribution-9.0.5.v20130815");
    params << "-jar" << "start.jar";
    grep.start("../jdk1.7.0_25/bin/java", params);
    grep.setReadChannelMode(QProcess::ForwardedChannels);
    //grep.setReadChannel(QProcess::StandardOutput);
    grep.waitForStarted();



    QApplication a(argc, argv);
    MainWindow w;
    w.show();
    w.init();

    return a.exec();
}
