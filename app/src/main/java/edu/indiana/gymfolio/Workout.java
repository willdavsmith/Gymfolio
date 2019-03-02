package edu.indiana.gymfolio;

/*
 * Workout.java
 *
 * A Class that encapsulates a 'workout', which is constructed from
 *  -   an integer number of sets
 *  -   an integer number of reps
 *  -   a String name
 *  -   an integer weight
 *
 *  This class is used by the activities in the application to hold these
 *  data fields for more efficient access to the data.
 *
 * Created by: Will Smith
 * Created on: 2/23/19
 * Last Modified by: Will Smith
 * Last Modified on: 3/1/19
 * Assignment/Project: A290 Android - Final Project
 * Part of: Gymfolio.
 *
 **/

public class Workout {
    // Instance Variables
    private int sets;
    private int reps;
    private String workoutName;
    private int weight;

    // The constructor that sets the sets, reps, workoutName, and weight
    public Workout(int sets, int reps, String workoutName, int weight) {
        this.sets = sets;
        this.reps = reps;
        this.workoutName = workoutName;
        this.weight = weight;
    }

    // Default constructor, sets everything to 0 or empty
    public Workout() {
        this(0,0,"",0);
    }

    // Getters and Setters
    public int getSets() {
        return sets;
    }
    public void setSets(int sets) {
        this.sets = sets;
    }
    public int getReps() {
        return reps;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }
    public String getWorkoutName() {
        return workoutName;
    }
    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return sets + "x" + reps + " " + workoutName + " at " + weight;
    }
    public String toUnparsedString() {
        return sets + "," + reps + "," + workoutName + "," + weight;
    }
}
