package ohtu;

import java.util.ArrayList;

public class Submission {
    private int week;
    private int hours;
    private ArrayList<Integer> exercises;

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeek() {
        return week;
    }
    
    public ArrayList<Integer> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Integer> exercises) {
		this.exercises = exercises;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	@Override
    public String toString() {
        return "Viikko "+week + ": tehtyjä tehtäviä yhteensä: " + exercises.size() + ", aikaa kului "+ hours +" tuntia, tehdyt tehtävät: "+exercises;
    }
    
}