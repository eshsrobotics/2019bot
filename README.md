# 2018bot
Code for the 2018 FRC competition Power Up.

## Gradle build
As we did with the [2017bot](http://github.com/eshsrobotics/2017bot), we have
included a command-line, self-bootstrapping Gradle build which allows you to
bypass Eclipse entirely (something which is often important on the Raspberry
Pi 3 systems that we typically use for building.)  You can build the code
from the `2018bot` directory using the following command:

``` shell
./gradlew build -info
```

The Gradle script can also deploy the 2018bot program directly to the
RoboRIO.  To do this, connect the RobotRIO to your system using an Ethernet
cable or the `1759` WiFi network, then run:

``` shell
./gradlew deploy -info
```

This doesn't always work, but when it fails, the chances are more likely than
not that you have a faulty connection.

## Waypoint Simulator
Included in the codebase is a crude simulation program we used to debug our
autonomous algorithms.  To build it, simply right-click on
[WaypointSimulator.jardesc](/WaypointSimulator.jardesc) and select **Create
JAR**.  This will create an executable JAR file called `WaypointSimulator.jar`
in the [build](/build) directory.

To run the simulation, enter the following command from the `2018bot`
directory:

``` shell
java -jar build/WaypointSimulator.jar
```

### Note for Windows users
The program uses ANSI SGR escape code sequences to render colors on the
terminal, so it will not work well on non-ANSI terminals like `cmd.exe` or
`powershell.exe`.  To run the simulation, you will need to use an ANSI
terminal like Cmder or [Cygwin](http://cygwin.com)'s `mintty.exe`.
