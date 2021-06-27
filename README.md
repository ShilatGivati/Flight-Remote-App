
# Flight-Remote-App

#### Contributes

* Amit Sharabi
* Shilat Givati

This is an android application for controlling FlightGear flight simulator.

1. [General](#General)
    - [Background](#background)
    - [Project Description](https://github.com/ShilatGivati/Flight-Remote-App/edit/master/README.md#project-description)
    - [Project Stucture](https://github.com/ShilatGivati/Flight-Remote-App/edit/master/README.md#project-stucture)
    - [Features](https://github.com/ShilatGivati/Flight-Remote-App/edit/master/README.md#features)
2. [Dependencies](#dependencies)
3. [Installation](#installation)

## General

### Background

This application connect to FlightGear, flight simulator, and let the user control the planes' velocity and directions.

### Project Description

The application interface and work with the FlightGear simulator (instruction for download at [Dependencies](#dependencies)) . The idea is that the user will connect to the FlightGear simulator with the FlightGeer servers' IP and Port and will be able to control the plane through the apps' controls bars. The user can control the following features: 
* Aileron
* Elevator
* Rudder
* Throttle

### Project Structure

This project designed according to MVVM architecture. The classes can be divided into two groups in order to create total segregation between the presentation logic and the business logic. The presentation logic implemented in:

* JoystickView class
* MainActivity class
* VerticalScroll

The business logic implemented in:

* FlightGeerModel class

This classes can communicate via the ViewModel class that constitutes as an abstract Model layer to the View and as an abstract View layer to the Model. You can see more information about the class hierarchy
in [UML](https://github.com/amit164/Flight-Remote-App/blob/master/FlightRemoteApp-UML.pdf).

### Features

* *Connect button* After typing the FlightGeer servers' IP and Port the user can click on connect button and a connection is made to the FlightGear server. In the case that the user entered the wrong IP or Port, the connection will fail and an error message will be shown.
* *SeekBars:* When moving the horizontal seek bar the user can control the rudder and by moving the
  vertical seek bar the user can control the throttle.
* *Joystick:* Joystick: When moving the Joystick up and down the user can control the elevator value and by
  moving the joystick to the sides you can control the aileron value.

For more features explanations, you can watch [this video](https://youtu.be/0LCHy6QXhxc).

## Dependencies

1. [FlightGear](https://www.flightgear.org/download/)
2. [Android emulator](https://developer.android.com/studio/run/emulator
   ) or an android device

## Installation

1. Open FlightGear from command line:
     ```
    $ cd C:\Program Files\FlightGear 2020.3.6
    $ cd bin
    $ start fgfs.exe --telnet=socket,in,10,127.0.0.1,5400,tcp
    ```
    Notice that ```C:\Program Files\FlightGear 2020.3.6``` is the path to the place where you download the FlightGear.

2. Click _fly_ on FlightGear.

3. Clone the repository from **another** command line:  
    ```
    $ git clone https://github.com/ShilatGivati/Flight-Inspection-App.git
    ```
