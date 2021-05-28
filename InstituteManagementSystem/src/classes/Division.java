package classes;

import java.sql.ResultSet;
import java.util.ArrayList;

import backend.*;

public class Division {
	private String table = "DIVISIONS";
	private int id;

	public int teacherId;
	public String name;
	public String department;
	
	public ArrayList<Subject> subjects;
	
	public Division(int id, int teacherId, String name, String department) {
		this.id = id;
		this.teacherId = teacherId;
		this.name = name;
//		this.subjects = Subject.getDivisionSubjects(Main.db, id);
		this.department = department;
	}
	

	public static boolean createTable(Database db) {
		String[] conditions = {
				"ID			INT		PRIMARY KEY		GENERATED BY DEFAULT AS IDENTITY",
			"TEACHERID		INT						NOT NULL",
				"NAME		TEXT					NOT NULL",
				"DEPT		TEXT					NOT NULL",
		};
		db.createTable("DIVISIONS", conditions);
		return true;
	}
	public static boolean dropTable(Database db) {
		db.dropTable("DIVISIONS");
		return true;
	}
	
	public static void insert(Database db, int teacherId, String name, String department) {
		String schema = "TEACHERID, NAME, DEPT";
		String values = String.format("%d, '%s', '%s'", teacherId, name, department);
		
		try {
			db.insertRow("DIVISIONS", schema, values);
		} catch (Exception e) {
			System.out.println("DIVISION Error while inserting DIVISION " + name);
			e.printStackTrace();
		}
	}
	public void update(Database db) {
		String conditions[] = {
               	String.format("TEACHERID=%d", this.teacherId),
               	String.format("NAME='%s'", this.name),
               	String.format("DEPT='%s'", this.department),
		};
		
		String id = String.format("ID=%d", this.id);
		
		try {
			db.updateRow(this.table, conditions, id);
		} catch (Exception e) {
			System.out.println("DIVISION Error while updating DIVISION " + this.id);
			e.printStackTrace();
		}
	}
	
	public static Division getById(Database db, int id) {		
		String[] conditions = {
				String.format("ID = %d", id),
		};
		
		try {			
			ResultSet rs = db.getRows("DIVISIONS", conditions);
			while (!rs.isClosed() && rs.next()) {
				int teacherId = rs.getInt("TEACHERID");
				String name = rs.getString("NAME");
				String dept = rs.getString("DEPT");
				
				return new Division(id, teacherId, name, dept); 
			}
		} catch (Exception e) {
			System.out.println("DIVISION Error while getting DIVISION by id");
			e.printStackTrace();
		}
		
		return null;
	}
		
	public int getId() {
		return this.id;
	}
	
	public void getInfo() {
		System.out.println("DIVISION " + this.id);
		System.out.println("  Teacher ID: " + this.teacherId);
 		System.out.println("  Name: " + this.name);
 		System.out.println("  Department: " + this.department);
 		System.out.println("  Subjects: ");
 		for (Subject s : this.subjects) {
 			System.out.println("    " + s.name);
 		}
	}
 	
 	public static void printDivisions(Database db) {
		try {
			ResultSet rs = db.getRows("DIVISIONS");
			System.out.println("-------------------------------");
			System.out.println("ID \tTEACHERID \tNAME \tDEPT");
			System.out.println("-------------------------------");
			while (rs.next()) {
				int id = rs.getInt("ID");
				int teacherId = rs.getInt("TEACHERID");
				String name = rs.getString("NAME");
				String dept = rs.getString("DEPT");
				
				System.out.println(String.format("%d \t%d \t\t%s \t%s", id, teacherId, name, dept));
			}
			System.out.println("-------------------------------");
		} catch (Exception e) {
			System.out.println("User Error while printing users");
			e.printStackTrace();
		}
	}
 	
 	public static ArrayList<Division> getDepartmentDivisions(Database db, String dept) {
 		ArrayList<Division> divisions = new ArrayList<Division>();
 		
 		String condition[] = {"DEPT = '" + dept + "'"};
 		ResultSet rs = db.getRows("DIVISIONS", condition);
 		
 		try {
	 		while (rs.next()) {
	 			int id = rs.getInt("ID");
	 			int teacherId = rs.getInt("TEACHERID");
	 			String name = rs.getString("NAME");
	 			
	 			Division d = new Division(id, teacherId, name, dept);
	 			divisions.add(d);
	 		}
	 	} catch (Exception e) {
			System.out.println("DIVISION Error while getting DIVISION by department");
			e.printStackTrace();
		}
 		return divisions;
 	}
}