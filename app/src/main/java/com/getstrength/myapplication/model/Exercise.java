package com.getstrength.myapplication.model;

/**
 * Created by Joe on 25/02/2016.
 */
public class Exercise {

    private int id;
    private String exerciseName;
    private String date;
    private int sets;
    private int reps;
    private int weight;

    public Exercise(int id, int sets, int reps, int weight, String date, String exercise) {
        this(sets, reps, weight, date, exercise);
        this.id = id;
    }

    public Exercise(int sets, int reps, int weight, String date, String exercise) {
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.date = date;
        this.exerciseName = exercise;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
