/****************************************************************************
**
** Copyright (C) 2012 Denis Shienkov <denis.shienkov@gmail.com>
** Copyright (C) 2012 Laszlo Papp <lpapp@kde.org>
** Contact: http://www.qt-project.org/legal
**
** This file is part of the QtSerialPort module of the Qt Toolkit.
**
** $QT_BEGIN_LICENSE:LGPL21$
** Commercial License Usage
** Licensees holding valid commercial Qt licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Digia.  For licensing terms and
** conditions see http://qt.digia.com/licensing.  For further information
** use the contact form at http://qt.digia.com/contact-us.
**
** GNU Lesser General Public License Usage
** Alternatively, this file may be used under the terms of the GNU Lesser
** General Public License version 2.1 or version 3 as published by the Free
** Software Foundation and appearing in the file LICENSE.LGPLv21 and
** LICENSE.LGPLv3 included in the packaging of this file. Please review the
** following information to ensure the GNU Lesser General Public License
** requirements will be met: https://www.gnu.org/licenses/lgpl.html and
** http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html.
**
** In addition, as a special exception, Digia gives you certain additional
** rights.  These rights are described in the Digia Qt LGPL Exception
** version 1.1, included in the file LGPL_EXCEPTION.txt in this package.
**
** $QT_END_LICENSE$
**
****************************************************************************/

#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "console.h"
#include "settingsdialog.h"
#include "treemodel.h"
#include <iostream>
#include <unistd.h>

#include <QMessageBox>
#include <QDockWidget>
#include <QtSerialPort/QSerialPort>
#include <QDebug>
#include <QLabel>
#include <QPixmap>
#include <QFile>
#include <QTextStream>
#include <QFileDialog>

//! [0]
MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow),
    uploadIndex(0)
{
	//! [0]
    ui->setupUi(this);
    console = new Console;
    console->setEnabled(false);
	
	view = new QTreeView(this);
	view->setAlternatingRowColors(true);
	view->setSelectionBehavior(QAbstractItemView::SelectItems);
	view->setHorizontalScrollMode(QAbstractItemView::ScrollPerPixel);
	view->setAnimated(false);
	setCentralWidget(view);
	
	QDockWidget *dock = new QDockWidget(tr("Serial Port Console"), this);
    dock->setAllowedAreas(Qt::AllDockWidgetAreas);
    dock->setWidget(console);
    addDockWidget(Qt::BottomDockWidgetArea, dock);
//! [1]
    serial = new QSerialPort(this);
//! [1]
    settings = new SettingsDialog;
	
	QStringList headers;
	headers << tr("Title") << tr("Value") << tr("Notes");
// 	TreeModel *model = new TreeModel(headers, QString("Test\t \t B \n Bla \t 1"));
	TreeModel *model = new TreeModel(headers, QString(""));
// 	model->addBrewStep(QString("1.Schritt"), 20.0, 5.0, 100.0);
// 	model->addBrewStep(QString("2.Schritt"), 20.0, 5.0, 100.0);
// 	
// // 	model->removeRows(0, 1);
// 	model->insertBrewStep(1, QString("1.5 Schrit"), 20.0, 5.5, 110.0);
	
    view->setModel(model);
	view->setColumnWidth(0, 250);
	view->setColumnWidth(1, 100);
	view->setColumnWidth(2, view->width()-350);

    ui->actionConnect->setEnabled(true);
    ui->actionDisconnect->setEnabled(false);
    ui->actionQuit->setEnabled(true);
    ui->actionConfigure->setEnabled(true);
	ui->actionAdd_Step->setEnabled(true);
	ui->actionUpload_Recipe->setEnabled(false);
	ui->actionStart_Brewing->setEnabled(false);
	ui->actionNext_Step->setEnabled(false);
	ui->actionRemove_Step->setEnabled(true);
	ui->actionSave->setEnabled(true);
	ui->actionOpen->setEnabled(true);

    initActionsConnections();

    connect(serial, SIGNAL(error(QSerialPort::SerialPortError)), this,
            SLOT(handleError(QSerialPort::SerialPortError)));

//! [2]
    connect(serial, SIGNAL(readyRead()), this, SLOT(readData()));
//! [2]
    connect(console, SIGNAL(getData(QByteArray)), this, SLOT(writeData(QByteArray)));
//! [3]
}
//! [3]

MainWindow::~MainWindow()
{
    delete settings;
    delete ui;
}

//! [4]
void MainWindow::openSerialPort()
{
    SettingsDialog::Settings p = settings->settings();
    serial->setPortName(p.name);
    if (serial->open(QIODevice::ReadWrite)) {
        if (serial->setBaudRate(p.baudRate)
                && serial->setDataBits(p.dataBits)
                && serial->setParity(p.parity)
                && serial->setStopBits(p.stopBits)
                && serial->setFlowControl(p.flowControl)) {

            console->setEnabled(true);
            console->setLocalEchoEnabled(p.localEchoEnabled);
            ui->actionConnect->setEnabled(false);
            ui->actionDisconnect->setEnabled(true);
            ui->actionConfigure->setEnabled(false);
			ui->actionUpload_Recipe->setEnabled(true);
			ui->actionStart_Brewing->setEnabled(true);
			ui->actionNext_Step->setEnabled(true);
            ui->statusBar->showMessage(tr("Connected to %1 : %2, %3, %4, %5, %6")
                                       .arg(p.name).arg(p.stringBaudRate).arg(p.stringDataBits)
                                       .arg(p.stringParity).arg(p.stringStopBits).arg(p.stringFlowControl));

        } else {
            serial->close();
            QMessageBox::critical(this, tr("Error"), serial->errorString());

            ui->statusBar->showMessage(tr("Open error"));
        }
    } else {
        QMessageBox::critical(this, tr("Error"), serial->errorString());

        ui->statusBar->showMessage(tr("Configure error"));
    }
}
//! [4]

//! [5]
void MainWindow::closeSerialPort()
{
    serial->close();
    console->setEnabled(false);
    ui->actionConnect->setEnabled(true);
    ui->actionDisconnect->setEnabled(false);
    ui->actionConfigure->setEnabled(true);
	ui->actionUpload_Recipe->setEnabled(false);
	ui->actionStart_Brewing->setEnabled(false);
	ui->actionNext_Step->setEnabled(false);
    ui->statusBar->showMessage(tr("Disconnected"));
}
//! [5]

//! [6]
void MainWindow::writeData(const QByteArray &data)
{
    serial->write(data);
}
//! [6]

//! [7]
void MainWindow::readData()
{
    QByteArray data = serial->readAll();
    console->putData(data);
}
//! [7]

//! [8]
void MainWindow::handleError(QSerialPort::SerialPortError error)
{
    if (error == QSerialPort::ResourceError) {
        QMessageBox::critical(this, tr("Critical Error"), serial->errorString());
        closeSerialPort();
    }
}
//! [8]

void MainWindow::addBrewStep() {
	TreeModel *model = dynamic_cast<TreeModel*>(view->model());
	if(model) {
		model->addEmptyBrewStep();
// 		for (int column = 0; column < model->columnCount(); ++column)
// 			view->resizeColumnToContents(column);
	}
}

void MainWindow::uploadBrewRecipe() {
	TreeModel *model = dynamic_cast<TreeModel*>(view->model());
	if(model) {
		QStringList brewRecipe(model->getBrewStepsAsStrings());
		std::cout << "Send: " << std::string(brewRecipe.at(uploadIndex).toLocal8Bit()) << std::endl; 
		console->putData("Send: " + brewRecipe.at(uploadIndex).toLocal8Bit() + "\n");
		writeData(brewRecipe.at(uploadIndex).toLocal8Bit()+ "\n");
		sleep(1);
		if(uploadIndex < brewRecipe.size()-1) {
			uploadIndex++;
		} else {
			uploadIndex = 0;
		}
		
// 		for (int i = 0; i < brewRecipe.size(); ++i) {
// 			std::cout << "Send: " << std::string(brewRecipe.at(i).toLocal8Bit()) << std::endl; 
// 			console->putData("Send: " + brewRecipe.at(i).toLocal8Bit() + "\n");
// 			writeData(brewRecipe.at(i).toLocal8Bit()+ "\n");
// 			sleep(1);
// 		}
	}
}

void MainWindow::startBrewProcess() {
	QString startMsg("<start>");
	console->putData("Send: " + startMsg.toLocal8Bit()  + "\n");
	writeData(startMsg.toLocal8Bit()+ "\n");
}

void MainWindow::nextBrewStep() {
	QString startMsg("<next>");
	console->putData("Send: " + startMsg.toLocal8Bit() + "\n");
	writeData(startMsg.toLocal8Bit()+ "\n");
}

void MainWindow::removeBrewStep() {
	QModelIndex index = view->selectionModel()->currentIndex();
	QAbstractItemModel *model = view->model();
	model->removeRow(index.row(), index.parent());
}

void MainWindow::saveBrewRecipe() {
	QString filename = QFileDialog::getSaveFileName(this, tr("Save Brew Recipe"), "", tr("Brew Recipe (*.txt);;All Files (*)"));
	std::cout << filename.toStdString() << std::endl;
	QFile file(filename);
	QTextStream stream(&file);
	if (file.open(QIODevice::ReadWrite | QIODevice::Truncate | QIODevice::Text)) {
		TreeModel *model = dynamic_cast<TreeModel*>(view->model());
		if(model) {
			QStringList brewRecipe(model->getBrewStepsAsStrings());
			for (int i = 0; i < brewRecipe.size(); ++i) {
				stream << brewRecipe.at(i).toLocal8Bit() << "\n";
			}
		}
		file.close();
	}
}

void MainWindow::openBrewRecipe() {
	QString filename = QFileDialog::getOpenFileName(this, tr("Open Brew Recipe"), "", tr("Brew Recipe (*.txt);;All Files (*)"));
	QFile file(filename);
	QTextStream stream(&file);
	if (file.open(QIODevice::ReadWrite)) {
		TreeModel *model = dynamic_cast<TreeModel*>(view->model());
		if(model) {
			while (!stream.atEnd()) {
				QString line = stream.readLine();
				QStringList brewStep = line.split(";");
				model->addBrewStep(brewStep.at(1), brewStep.at(2).toFloat(), brewStep.at(3).toFloat(), brewStep.at(4).toFloat());
			}
		}
		file.close();
	}
}

void MainWindow::initActionsConnections()
{
    connect(ui->actionConnect, SIGNAL(triggered()), this, SLOT(openSerialPort()));
    connect(ui->actionDisconnect, SIGNAL(triggered()), this, SLOT(closeSerialPort()));
    connect(ui->actionQuit, SIGNAL(triggered()), this, SLOT(close()));
    connect(ui->actionConfigure, SIGNAL(triggered()), settings, SLOT(show()));
    connect(ui->actionClear, SIGNAL(triggered()), console, SLOT(clear()));
	connect(ui->actionAdd_Step, SIGNAL(triggered()), this, SLOT(addBrewStep()));
	connect(ui->actionUpload_Recipe, SIGNAL(triggered()), this, SLOT(uploadBrewRecipe()));
	connect(ui->actionStart_Brewing, SIGNAL(triggered()), this, SLOT(startBrewProcess()));
	connect(ui->actionNext_Step, SIGNAL(triggered()), this, SLOT(nextBrewStep()));
	connect(ui->actionRemove_Step, SIGNAL(triggered()), this, SLOT(removeBrewStep()));
	connect(ui->actionSave, SIGNAL(triggered()), this, SLOT(saveBrewRecipe()));
	connect(ui->actionOpen, SIGNAL(triggered()), this, SLOT(openBrewRecipe()));
}
