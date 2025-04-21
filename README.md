# COMP 2522 Term Project

## Description

Developed for the COMP 2522 Term Project, this project is intended to show
evidence of learning object-oriented programming concepts and best practices.

### Features
* A console based menu
* A console based geography game
* A JavaFX GUI based 'Number Game' where a player must place randomized numbers in ascending order
* A JavaFX GUI based personal spin on a two player game of Tablut and Chess 

## Contributors
* Jonny Twist

## Installation Instructions

### Prerequisites

- Java Development Kit (JDK) 17 or later
- IntelliJ IDEA (or any IDE with JavaFX support)
- JavaFX SDK
- JUnit 5 (for testing)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/JonnyTwist/Comp2522TermProject.git
   ```
2. Open the project in IntelliJ IDEA.
3. Make sure the project SDK is set to JDK 17 or later.
4. Add JavaFX SDK to your project libraries if not already configured.
5. Run the Main class directly from the IDE.
6. (optional)  Run tests by right-clicking the test folder and selecting [Run 'All Tests'] or using the built-in test runner.

## Known Bugs and Limitations
* None yet (^_^)

## Contents of Folder
Note: I have excluded .idea folder and contents as well as Comp2522TermProject.iml from this list.

```
├── src/
   ├── Code/
      ├── ca/
         ├── bcit/
            ├── comp2522/
               ├── termproject/
                  ├── Main.java                 # Entry point with console menu to select a game
                  ├── Playable.java             # Interface implemented by each game class
                  ├── numbergame/
                     ├── NumberGame.java        # Number placement game
                     ├── RNGGame.java           # Abstract class for RNG-based GUI games
                  ├── tablut/
                     ├── King.java              # Represents the King piece in Tablut
                     ├── Pawn.java              # Represents Pawn pieces in Tablut
                     ├── Piece.java             # Superclass for Tablut pieces
                     ├── Player.java            # Enum for Attacker and Defender roles
                     ├── TablutSpinoff.java     # Core logic and entry for the Tablut game
                     ├── TablutStats.java       # Handles reading/writing stats for Tablut
                  ├── wordgame/
                     ├── Country.java           # Allows the creation of country objects to group data
                     ├── Score.java             # Manages scoring and file I/O for WordGame
                     ├── WordGame.java          # Core logic and entry for the Word game
                     ├── World.java             # Reads a-z country info files 
   ├── resources/
      ├── a.txt - z.txt                         # Files containing country data for WordGame
      ├── docs/
         ├── applications.txt                   # Personal txt including how I applied something from each lesson into Tablut game
         ├── prompts.txt                        # Personal txt including how I AI was used in dev of Tablut game
      ├── images/
         ├── attacker.png                       # Attacker image for Tablut GUI
         ├── defender.png                       # Defender image for Tablut GUI
         ├── king.png                           # King image for Tablut GUI
      ├── styles/
         ├── numberGameStyles.css               # CSS for NumberGame GUI
         ├── tablutStyles.css                   # CSS for Tablut GUI
   ├── Tests/
      ├── ca/
         ├── bcit/
            ├── comp2522/
               ├── termproject/
                  ├── tablut/
                     ├── PieceTest.java         # Unit test for Piece-related logic in Tablut
                  ├── wordgame/
                     ├── ScoreTest.java         # Unit test for Score logic in WordGame
├── .gitignore                                  # Git ignore rules
├── README.md                                   # Project readme file
├── score.txt                                   # Keeps track of previous scores for the word game
├── TablutStats.txt                             # Keeps track of how many times the attacker or defender has won
```

## Acknowledgements
This project was completed as part of the British Columbia Institute of Technology's
Computer Systems Technology (CST) program. The specific course was COMP 2522 
Object-Oriented Programming 1.