# gomoku-selfconsole-color
An optimized fork of https://github.com/canhta/Gomoku-console (added features like multi-platform color support, multiplatform selfconsole, multi-platform screen clearing, better game flow and some more...).

Currently on Linux, for selfconsole only xfce4-terminal is supported. Feel free to start a pull request at https://github.com/Lartsch/java-selfconsole if you want other terminals added.

Utilizes jansi library (for cross platform console colors), which is not included here. Include it in your buildpath manually.
The release jar includes jansi.

_If the jar does not work for your terminal (missing selfconsole support), simply run the jar directly from the terminal with any run argument._
