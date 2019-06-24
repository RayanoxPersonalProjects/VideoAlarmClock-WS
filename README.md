# VideoAlarmClock-WS
This repository is the server part of a bigger project which goal is to make a totally customizable video alarm clock, for people who feel the wake up moment as difficult, like me. The big project is composed of two parts: The device controling both a TV and a PlayStation (4) remote control, and this WS that is reached by the device to get the informations about the alarm time and the control sequence to execute on the remote controls.

# Web Service part #

There are 3 methods endpoints that are used to provide informations to the Arduino device or to update an info.

. Get-Commands : returns a sequence
. Get-time-alarm
. Set-time-alarm

# Arduino part #

(not implemented yet, it's the last part. When started, I will put the link of the related Github repository)

The arduino program is implemented in C language, meaning that the exchange messages won't be coded with an Object-Json conversion, but will follow a custom protocol that I invented, espacially for this project needs. The exchange protocol doc synthax is writen in a specific text file "Exchange_Protocol.txt"
