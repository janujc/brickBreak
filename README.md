game
====

This project implements the game of Breakout.

Name: Januario Carreiro

### Timeline

Start Date: 13 January 2019

Finish Date: 21 January 2019

Hours Spent: ~36 hours

### Resources Used

Used stackoverflow extensively, also looked at tutorials provided but was already many hours into the project so I did
not apply the concepts introduced in the tutorials.

### Running the Program

Main class: 

GameLoop

Data files needed: 

BounceBrick.wav and PlatformBounce.wav, included. Note: these are not in the resources folder due to some ambiguities
when accessing music files from resources folder. 

Key/Mouse inputs: 

A to move left, D to move right, mouse to click start buttons.

Cheat keys: 

1, 2, 3, 4, 5 to jump to respective levels.

j to slow down ball

k to increase size of platform

l to add lives

Known Bugs:

Sometimes, if the ball hits corner of a brick or the platform, it will "vibrate" erratically. If such a thing happens
between the ball and the right and left walls, and the ball is hit by the side of the platform, the ball may leave the
screen.

Extra credit:

Added two sounds: when the ball bounces off walls and the platform and when the ball bounces off the bricks.

### Notes

The first bounce off the platform at the start of the game results in some pretty massive lag. Unsure of the reason 
for this.

### Impressions

Definitely more difficult than at first glance. Was unsure of how to do many things, so most of JavaFX had to be learned
while trying to code, which resulted in having to rewrite the code many times. 

Would be good if a general layout of the game was given beforehand so one could be more aware of how many classes one
should be writing. I ended up writing over 500 lines of code in the GameLoop class, which definitely is not the most
readability-friendly option.
