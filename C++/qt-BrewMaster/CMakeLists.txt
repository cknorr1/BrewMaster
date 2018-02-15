cmake_minimum_required (VERSION 2.6)
project(qt-BrewMaster)

find_package(Qt5Widgets REQUIRED)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(CMAKE_AUTOMOC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)
set(CMAKE_AUTORCC ON)

set(SOURCES console.cpp main.cpp mainwindow.cpp settingsdialog.cpp treeitem.cpp treemodel.cpp)

set(RESOURCES brewMaster.qrc)

set(UI_FORMS settingsdialog.ui mainwindow.ui)
set(UI_HEADER settingsdialog.h mainwindow.h)

QT5_WRAP_CPP(GENERATED_HEADER ${UI_HEADER})
QT5_WRAP_UI(GENERATED_UI ${UI_FORMS})

add_executable(brewMaster ${SOURCES} ${GENERATED_UI} ${RESOURCES} ${CMAKE_CURRENT_SOURCE_DIR}/myapp.rc)
qt5_use_modules(brewMaster Core Gui Widgets SerialPort)
