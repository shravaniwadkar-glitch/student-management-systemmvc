package sms.model;

public class Student 
{
	private int id;
    private String name;
    private String course;
    private double marks;
    
    public Student(int id, String name, String course, double marks) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.marks = marks;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public double getMarks() { return marks; }
}
