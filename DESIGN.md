Design
===
name: Januario Carreiro

### Design Goals
Initially, my high level design goals were to make a class to control the game, a class for
the bricks, a class for the PowerUps, and a class for the HUD. Unfortunately, as the project 
went on, I realized that I would not have time to create and implement a fully-realize HUD, so
I decided to scrap the idea of having a HUD class. Also, as I went on, I realized that there was 
a lot more background to creating a game than I realized, and that the initial goal of 3-4 classes was
not very appropriate. I would be needing closer to 10 classes if I wanted everything to have a well-defined purpose. 
Seeing as my goals changed as I learned more about how to make a game, and I realized that my initial
goals were simplistic/idealistic, and I did not have enough time to change my game into something that more
closely resembled beautiful code, my design goals were not met.

### Adding Features
* **Level:**
1. Change ``NUM_LEVELS``

2. Add case to ``makeBricks()``

3. Create brick configuration method for level, usually in format ``level<NUMBER>Builder()``

4. Should have functioning level now. If you want, you may add a cheat code to skip to that level by adding a case
to ``checkForCheats()``. Be sure to update README.md if you do this.

* **Cheat Code:**
1. Add case to ``checkForCheats()``, should try to maintain format found there.

* **Between-Levels-Screen:**
1. Create ``private Scene`` method

2. Inside this method, you should initialize a ``new Pane``

3. To add text or button you will also need to create new ``VBox`` or ``HBox`` if you want to place
the text or button at a certain location in the Pane such as ``pane.setCenter(VBox or HBox)``

4. Once you're done adding things to the pane, be sure to ``return new Scene(pane, SIZE, SIZE);``

* **Power-Up:**
1. Create new class ``PowerUp<NAME>``

2. In this class, be sure to ``extend PowerUp``

3. Pick a color and make a constructor

4. Add color to end of array ``POWERUP_COLOR`` in ``GameLoop``.

5. Add ``if statement`` to ``determinePowerUp()`` following existing format with whatever power-up you choose



### Design Choices

As described previously, my design has some major flaws. Because I did not think I would have time to both refactor
my code to switch to a new design and finish on time, I kept my design and continued to add methods to GameLoop.

By the time I had realized that the game should be created using several different classes, my game had already
depended on being able to call all the instance variables and set the scene in the same class. Moreover, I had already
made some objects extend a shape from the Shape class, so I was unable to later try to convert all my Bricks, platforms,
balls, and PowerUps to Sprites and control them all from a SpriteManager class. 
