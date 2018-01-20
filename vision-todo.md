2018 vision strategy
====================

Changes from 2017bot
--------------------

-   No third-party vision subsystem like the TK1 \* Ergo, no networking problems with the two-port roboRIO
-   Vision processing on driver station
-   **?** Use NetworkTables to talk back to the roboRIO more simply

What hasn't changed
-------------------

-   **?** Vision processing code still written in C++ \* Advantage :: Much less rewriting \* Disadvantage :: NetworkTables in C++ is apparently hard. Not sure why.
-   Vision solutions are still sent to the roboRIO from whoever is calculating them
-   RoboRIO still cares only about solutionFound, papasDistance, and papasAngle
-   Vision targets are two vertical strips again (adjusted peg solution)

New challenges
--------------

1.  **How** will we get the camera image from the roboRIO to the driver station? \* UA: How does GRIP do it? Find out, then do what it's doing. \* The only alternative to this is vision processing on the robot, which ~~even GRIP won't do.~~ ?
2.  **How** do we build NetworkTables for C++?
3.  **How** do we test NetworkTables independently of the local presence of the robot?

GRIP research
-------------
