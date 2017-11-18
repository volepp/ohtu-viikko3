package ohtu;

import java.util.ArrayList;

public class Course {

	private String name;
	private String term;
	private ArrayList<Integer> exercises;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
	
	
	public ArrayList<Integer> getExercises() {
		return exercises;
	}
	public void setWxercises(ArrayList<Integer> exercices) {
		this.exercises = exercices;
	}
	@Override
	public String toString() {
		return "Kurssi: " + name + ", " + term;
	}
	
}
