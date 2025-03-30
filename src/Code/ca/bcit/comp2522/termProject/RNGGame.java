package ca.bcit.comp2522.termProject;

import javafx.application.Application;

/**
 * An abstract class for javaFX games based on Random Number Generator.
 * @author Jonny Twist
 * @version 1.0
 */
public abstract class RNGGame extends Application
{
    abstract int roll(final int min, final int max);
}
