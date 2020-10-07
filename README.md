# DEPRECATED

The project does still exist, but has been moved in an other project more generic -> https://github.com/RayanoxPersonalProjects/ElectronicRemoteDriver-WS . This new project will provide a set of commands for a remote electronic device, including for the VideoAlarmClock.

Below I let the existing description of the project.


# VideoAlarmClock-WS
This repository is the server part of a bigger project which goal is to make a totally customizable video alarm clock, for people who feel the wake up moment as difficult, like me. The big project is composed of two parts: The device controling both a TV and a PlayStation (4) remote control, and this WS that is reached by the device to get the informations about the alarm time and the control sequence to execute on the remote controls.

_**If you want to use an alternative system than the Playstation 4 and you are motivated to contribute to make this possible**, please **create an issue** and I will invest my efforts to adapt the code to drive different kind of devices._

# Web Service part #

There are 10 methods endpoints that are used to provide informations to the Arduino device or to update an info.

- **getCommands** : returns a sequence of commands that the Arduino will act.
- **help** : return all the endpoints list
- **helpFormulasListInfos** : return the codes of each wakeup formula 
- **getShutdownTimer** : returns the amount of seconds before the Arduino shutdown all the devices before sleeping
- setShutdownTimer : set the amount of seconds before the Arduino shutdown all the devices before sleeping
- **getTimeAlarm** : returns the time the alarm will run.
- setTimeAlarm : set the time the alarm will run.
- **getVolum** : returns the sound level to set to the TV at wakeup time.
- setVolum : sets the sound level to set to the TV at wakeup time.
- **UpdatePlaylist** : Update the custom Youtube playlist a according to the formula that is set in configuration file.


# Arduino part #

Link of the related repo: https://github.com/RayanoxPersonalProjects/VideoAlarmClock

The arduino program is implemented in C language, meaning that the exchange messages won't be coded with an Object-Json conversion, but will follow a custom protocol that I invented, espacially for this project needs. The exchange protocol doc synthax is writen in a specific text file "Exchange_Protocol.txt"
