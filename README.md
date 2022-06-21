
# Final Examination of Software Engineering - AY 2021-2022
*Read this in other languages: [english](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/blob/main/README.md), [italiano](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/blob/main/README.it.md)*

This project is intended to satisfy the requirements of the Final Examination of Software Engineering (prof. San Pietro) for the BSc in Engineering of Computing Systems at Politecnico di Milano.
![Logo Eryantis](src/main/resources/images/LogoPNG.png)

Implementation of the [Eryantis](https://www.craniocreations.it/prodotto/eriantys/) board game as an online multiplayer game.

This project consists in the implementation of the game mentioned above as a single server able to run a single game at once and multiple clients (one for each player) that can play a single game. The MVC (Model-View-Controller) pattern is used as the base structure of the project. The communication between server and client is realized using Java Object serialization and Java.net sockets. The user can interact with the game using one of two interfaces: a Command Line Interface or a Graphical User Interface.

## Documentation

### UML
The Class Diagrams (described using UML) are available here:
- [Initial UML diagrams](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/uml_diagrams/initial)
- [Final UML diagrams](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/uml_diagrams/final)

### JavaDoc
The documentation available [here](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/javadoc)  was realized using Java documentation techniques (JavaDoc) and provides a description of all classes and their methods (and public attributes when necessary). 

### Libraries e Plugins
In this section a brief description of libraries and plugins used in the project is provided.
|Library/Plugin|Description|
|---------------|-----------|
|__Maven__|Project-management tool, provides for automation of compilation and managment of dependencies.|
|__JUnit__|Unit testing framework.|
|__JavaFx__|Library used to create Graphical User Interfaces|


## Functionalities
### Developed functionalities
- Complete ruleset
- CLI
- GUI
- 3 Advanced functionalities:
    - __Persistence:__ the state of the current game is saved on disk. This way the game can be restored and played even if the server execution is interrupter.
    - __Disconnection-resilience__ a player can reconnect after they were disconnected.
    - __All character cards:__ all characters present in the original board-game were implemented.


## Compilation and packaging
### Executables
The executables (.jar) were created using the Maven Shade plugin. These files are available [here-INSERIRE LINK].
To compile the jars on your own -------------- and save them in the ------------ folder.

## Run the game
This game requires Java 17 or higher.

### Client
In this section the procedure needed to run the Eryantis client is described.

#### CLI
To run the Command Line Interface write the following command in the terminal:
```
java -jar NOMEJAR.jar --cli
```
or
```
java -jar NOMEJAR.jar --c
```
It is also possible to use a simplified version of the CLI. In this version no drawings are shown and the game is described completely using text. This version is runnable using:
```
java -jar NOMEJAR.jar --simple
```
or
```
java -jar NOMEJAR.jar --s
```
#### GUI
Tu run the game using the Graphical User Interface two options are available:
- opening the executable file ```NOMEJAR.jar``` as you normally would (double-clicking) 
- running the following command via the command line: ```java -jar NOMEJAR.jar```

### Server
To run the server run the following command using the command line:
```
java -jar NOMEJARSERVER.jar [<port_number>]
```
#### Parameters
It is possible to specify the server port (the default value is __2351__).

## Group members
In this section all group (code PSP15) members are listed in alphabetical order.
- [__Alessandro Ferri__](https://github.com/FerriAlessandro)
- [__Alessandro Gabbini__](https://github.com/alessandroGabbini)
- [__Angelo Giovanni Gaillet__](https://github.com/aggaillet)