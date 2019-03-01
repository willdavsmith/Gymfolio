package edu.indiana.gymfolio;

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
}
