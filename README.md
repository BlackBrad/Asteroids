# Asteroids
Asteroids sort of clone in java. 

A sort of clone of Asteroids written in Java.

How to run this? 

Windows - Apparently it's the same as below.

Mac - You'll need the Java Development Kit (JDK).

1. Place all the files in a directory somewhere.

2. Open the Terminal.

3. CD to the directory containg all the files.

 Enter these two commands excluding the " 

4.  "javac Asteroid.java"

5. "java Asteroid"
   
   A Java window should now appear with the game.
    
    
     
  

If you compile and run the latest version of this you'll find that there will not be any ship. This is because I don't know what kind of license the sprite I'm using has so it's been excluded for legal reasons. Therefore you can:

1. Add you're own sprite image to your local directory with all the files named "ship.jpg"
2. Uncomment line 423 and comment lines 34 and 422.

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

7/7/2015:
I've added a sprite so what was a square is not represented by an image of a ship.
The ship now reappears on the top of the screen when the ship moves below canvas height and vice versa

31/7/2015: 
Got shotcool down working and fixed the bug where the ship woulden't stop at X=0. I probably did other stuff that I can't remember as well.

20/8/2015:
I've finished all specifications... Or atleast I think I have. We'll find out when my teacher grades it.
