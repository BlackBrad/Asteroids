# Asteroids
Asteroids sort of clone in java. 

A sort of clone of Aseroids written in Java. 

20/6/2015:
Has a working 'ship' that is currently represented by a white square which can move is all direction. Also has a moving shot. 
Enemy.java does not yet do anything. 
Has working error messages so when an out of bounds value is entred into one of the text filds then errors will appear.

22/6/2015:
Asteroids move and collision of the large asteroids with a shot now removes the large asteroids and replaces it with three smaller asteroids of size 10. Also if a large asteroid moves off screen then it it removed from the arraylist.

23/6/2015:
The medium asteroids now split into small sized ones of size 5 and when
those are hit a point is gained. I’ve also added ship collision so when
a ship hits an asteroid a game over function is called and a game over
canvas.

29/6/2015:
High score has not been implimented. No saving of high score (might add that, might not). 
The ship is not supposed to be able to move any less than 0 of the canvas but that dosen't always work. 
