# Conception

- A `CONCEPTION.md` file explaining the overarching design decisions such as:
  - Architectural changes (with justification);
  - Any added classes / interfaces and how they fit into the architecture;
  - What functionality each component adds (for any behavior divergent from the instructions and any added
    functionality)

Be CONCISE and if necessary, you can just ref the green commentaries we put above methods Also, for things were we
strictly followed the instructions, you can just say that

## Explain our conception of `ICWarsActor`:

- the fact we decided to use an enumeration for Faction
- our leaveArea/EnterArea method
- etc

## Explain our conception of units for part 1:

- the abstract Unit class that we decided to put abstract because there can't be an instance of the object
- explain the methods we decided to put in unit because they are both inherited by Tank and Soldier (so that we avoid
  the code repetition)
- explain the way we created the constructors of unit/Soldier/Tank
- if you see something else that seems important to talk about for the part 2.2.1
- explain how we get the right name for the unit's sprite in Tank and Soldier with the method that checks what the
  faction of the unit is
- etc

## Initiation of levels:

- explain how the units are given to the player when he enters the game
- explain how you determined their spawn position (all the bonus things you created with the hashmap)

## Explain our conception of the ICWarsPlayer for part 1:

- maybe we should put it as abstract and explain why or why not
- how the player's unit are initiated (with the list) and how there are registered in the area
  (`RegisterUnitsAsActors` method)
- how in the update method the dead units are eliminated:
  + created the isDead/isAlive method in the units
  + need to remove the units both from the player's unit list and from the area
- the leaveArea and enterArea method
- how we can center the camera on the player
- how we get the right sprite name depending on the faction (same thing as for the units) (in the instruction it was
  said we should to this in realPlayer, but we did it here because it is the same thing for the AIPlayer)
- what we did for checking if a player was defeated -etc

## Explain our conception of the RealPlayer for part 1:

- how we make him move
- etc

## Explain our conception of ICWars for part 1:

- the interesting things we did for part 1
- how the N keyboard functionality works with also an explanation of the method nextArea()
- same for R

## Explain the graphic info for part 1:

- how we create the units range
- How the player can select a unit (just say that it is in the later part that we really implement this with the
  interactions between a player and a unit)
- how the player can draw the path of the unit thanks to his PlayerGUI
- what we did for when the U button is pressed
- how we respected the constraints for this part (see part 2.4 of the instructions) and check if the way the GUI
  receives the selected unit is great and explain why we did it like this

## Part 2:

### Explain the States of the player for part 2 (so everything except the things related to the actions)

- the enum we did for the states
- for details, you can go into details for the MOVE_UNIT state and explain how we set units already moved + the
  changePosition method
- quickly the RealPlayerCanMove method
- quickly the override of onLeaving and the startTurn method
- briefly the switch in draw

### Explain our interactions structure:

- explain how we created the interface ICWarsInteractionVisitor with default interaction methods
- RealsPlayer implements interactor, he has a private class ICWarsUnitInteractionHandler that handles the interactions
  (explain briefly the important methods in this class), he has an attribute handler...
- explain the acceptInteraction method we implemented in unit
- explain the way a unit is selected and transmitted to the playerGUI too
- explain how the opacity of the used units' sprite is changed

### Explain the game state:

- the different lists of players we have (the one that contains all the players, the one that contains those waiting for
  next turn and for the current turn)
- the way we remove players from the list
- the endTurn method
- the TAB button

## Part 3:

### Explain what we did for the actions:

- the inheritance process between actable, action, attack and wait
- quickly go over the doAction of Wait
- for attack, the doAction (focus on the fact that for the encapsulation, most of the process is done by the area)
  explain also the attack method in doAction and what is important is to focus on the fact we decided to make the units
  interactors so that when interacting with a cell they can get the number of stars in it, which is important in the
  calculus when a unit takes damage
- the action list in the units

### Explain what we did for the actions with the RealPlayer:

- the things related to `actions` in update of real Player
- the panels that are drawn by the GUI (explain the interaction with a cell in ICWarsPlayerInteractionHandler and the
  methods that draw the panels in the GUI)

### Explain the AIPlayer:

- the switch in the APlayer update
- the doAutoAction for the actions (especially for the attack explain how moveUnitTowardsClosestEnemy works, see the
  comments for the algorithm that moves the unit)
    
 ## Part 4 (extensions):   
 ### the cities
 For the extension we decided to implement the suggested features related to cities. First we created a class City that extends ICWarsActor and implements Interactable. The major method there is the draw since the sprite color is adapted to the current faction of the city.
 When an area is created, cities are generated automaticaly on the cell with the appropriate ICWarsCellType. To do so, in the begin method of ICWarsArea we call the registerCities method on its icWarsBehavior. This method checks the ICWarsCellType of each cell of the area and creates a new city if the string of the type equals city.
Cities can be taken by units, which changes the faction of the city. To do so, we made units able to interact with a city when they share the same cell. The way the interaction is handled is similar to what was asked for part 2. The interaction itself consists to inform the unit that he is on a city (boolean isOnACity) and give him the city (City cityOnCell). We created an update method both in Soldier and Tank that does the same thing. We could have created this update in units to avoid copying code but it deels with the units actions and it is written in the instructions that actions can t be defined at the level of abstraction of Unit. The update method simply checks if the unit is on a city and if it is the case, adds an action TakeCity (explained later) to the list of available actions for the unit. If the unit isn't on a city, the TakeCity action of the unit is removed from the actions list of the unit if it is there. 
When placing a unit on a city, the player can make his unit do the action TakeCity with keyboard button T. The city's faction is then set to the one of the unit. 
Finally, at the end of a turn ICwars calls updatePlayersUnitsHP on each of the alive players. This method calls updateHP on each of the player's unit which increases the unit's HP by a static constant defined in City. 

### the day-night cycle
We also decicided to implement a night-day cycle which has an impact on the range of the units. First we created in ICWars Area a boolean attribute night and a setter/getter for this attribute being true if the area is in night mode. In the constructor of an ICWarsArea, a new NightPannel is registered as an actor in the area and is given the area as a paramater in his constructor. NightPannel is a class implementing Graphics and actor. The main method there is draw. In this method, depending on wheter the area pocessing the NightPannel is in night mode, a dark gray filter is created, using the classes Polygon and ShapeGraphics, and drawn in front of the area image. 
To make the day-night cycle dynamic, the ICWars updates the cycle of the current area being displayed. To do so, we created 2 attributes in ICWars, a final int durationNightDayCycle, which gives the number of turn a night or a day lasts, and a int numberOfRoundsInCurrentArea which counts the number of turns that occured in the game. In the update method of ICWars, when a turn is ended, we check if numberOfRoundsInCurrentArea is a multiple of durationNightDayCycle and if so we change the night attribute of the area. 
Finally, the radius of attacks/displacment of the units is adapted to the night. We indeed created an effectiveRadius attribute in Unit which is changed in the update method of unit depending on whether it is the day or night in the ownerArea of the unit. The range of the unit then adapts to the new effectiveRadius. 
    
    
    
    
