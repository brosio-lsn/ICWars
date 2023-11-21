# Project Description

### General usage
![Game Example Screenshot](https://github.com/brosio-lsn/ICWars/tree/main/res/game_example_screenshot.png)

The player can move his cursor thanks to the arrows buttons. Information about the cell the cursor is on is provided in
the bottom left corner. When the cursor is on an ally unit (colored in blue) the player can press ENTER to select the
unit. A white area then appears on the screen which symbolises the range within which the unit can be moved. To chose
where to move the unit, the player can move his cursor to the position within the range and then press ENTER, which
initiates the move. A panel is then displayed in the top right corner where the available actions offered by the
selected unit are displayed. The player can then press either A or W:

- By pressing the A button on the keyboard the player can make his selected unit attacks enemies. If the player wishes his unit to do another action instead, he can press tab. If he stills decide to attack, if no enemies are in an attackable range, the player can choose an action again. Else, a pointer is displayed next to the enemy unit to attack
  and the player can choose which enemy to attack with the arrows buttons. When the pointer is next to the enemy the
  player wants to attack, the ENTER button will trigger the attack. If the attacked enemy is dead, he disappears from
  the map. After the attack, the player can continue playing with other units.
- By pressing the W button, the selected unit does nothing and the player can continue playing with other units.

Units can only do one action per turn and units that were already used are transparent and can't be selected. If the
player wants to stop his turn, he can press TAB and the opponent starts playing.

### Other buttons

-the N button allows the player to change levels. If the player is in the last level and presses N, the game stops.
-the R button allows the player to restart the game at the first level.
-when the player moves a unit on a city, a new action is offered by the unit. The player can press and the selected unit takes the city, which becomes an ally. (Units that have taken a city during a turn receive HP at the end of the turn). 

### Night/day cycle
If the area appears with a dark filter, it is the night and the unit's range is reduced. 


## Development method

This is where we should plan:

- What to do;
- How to do it;
- Who should do what;
- etc...

Things we need to sort out:

- [ ] Game design
- [ ] Overall architecture
- [ ] JavaDoc comments
- [ ] This file documentation (this will probably move to `CONCEPTION.md`)

I can make diagrams and then export to `.html` or `.pdf` for the final hand in.

## Hand-in Instructions

**Date**: ==Thursday 23 December at 09:00==

**Hand in Format**:

- Source code will be an archive (`.zip`)
- A `README.md` explaining how to use the program including:
    - How to start it;
    - The controls and what they do;
    - Any other game rules / objectives;
- A `CONCEPTION.md` file explaining the overarching design decisions such as:
    - Architectural changes (with justification);
    - Any added classes / interfaces and how they fit into the architecture;
    - What functionality each component adds (for any behavior divergent from the instructions and any added
      functionality)

They should be written succinctly and informally. In `.md`, `.txt`, or `.pdf` preferably.

Add JavaDoc comments for all the functions, similar to the given examples.

For any fundamental changes (i.e. to the graphical interface) you should ask for their opinion by sending an email to
[cs107](cs107@epfl.ch).

You can add additional images that are copyright free (and reference their source in the `README.md`). They have to be
small because the hand in size is limited. Any extension that makes the files too big email [cs107](cs107@epfl.ch).

|                                                     | Points | Maximum grade out of 6 |
| :-------------------------------------------------: | :----: | :--------------------: |
| General documentation, `README.md`, `CONCEPTION.md` |   10   |          1.5           |
| Base components (step 1)                            |   35   |          3.25          |
| Unit interactions (step 2)                          |   35   |          5.0           |
| Actions and AI (step 3)                             |   20   |          6.0           |
| Extension (step 4, bonus/competition)               |   20   |        (bonus)         |

## Structure

**Steps**:

1. **Basic components**:
    - Have a player controlled *cursor* capable of moving and changing level;
    - Have *units* that can be used by the player.
2. **Unit interactions**:
    - Allow the cursor to have basic interaction with the units to move them.
3. **Actions and AI**:
    - Make the units have actions;
    - Have a basic "AI" that can act as an *opponent*;
    - Add a UI to display static information.
4. **Extensions**:
    - Add additional features and functionality.
