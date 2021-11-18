package TP1;

public class Person {

    private String name;
    private char role;
    private int earlyPos;
    private int appointed = 0;
    
    public Person(String name, char role, int earlyPosition){
        this.name = name;
        this.role = role;
        this.earlyPos = earlyPosition;
    }

    public int getEarly(){
        return this.earlyPos;
    }

    public char getRole(){
        return this.role;
    }

    public void addAppoint(){
        this.appointed += 1;
    }

    public int getAppoint(){
        return this.appointed;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Person getObject() {
        return this;
    }
}
