package ca.bcit.comp2522.termProject;

import javafx.application.Application;

public abstract class RNGGame extends Application
{
    abstract int roll(final int min, final int max);
}
