package models;

import java.awt.Color;
import java.awt.Graphics;

import exceptions.InvalidInputException;

public class Airport extends Model{
	
	//Airport is a model that appears on the map. 
	//It has a 2D location and is drawn as a gray square.
	
	private String code; //3 Letter code unique to every airport
	private String name;
	
	public Airport(String code, String name, int x, int y) {
		super(x, y, 15);
		
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" ").append(code).append(" ");
		sb.append("(").append(x).append(", ").append(y).append(")");
		return sb.toString();
	}

	@Override
	public void paint(Graphics g, int screenWidth) {
		paint(g, screenWidth, Color.DARK_GRAY);
	}
	
	public void paint(Graphics g, int screenWidth, Color color) {
		Color prevColor = g.getColor();
		g.setColor(color);
		
		int drawX = (int) Math.round((x + 90) * screenWidth / 180);
		int drawY = (int) Math.round((90 - y) * screenWidth / 180);
		g.fillRect(drawX - width/2, drawY - width/2, width, width);
		int xOffset = (width*4)/5;
		int yOffset = 0;
		if(x > 80) {
			xOffset = -(width*12)/5;		
		}
		if(y > 85) {
			yOffset = (width*2)/3;
		}
		else if(y < -85) {
			yOffset = -(width)/3;
		}
		g.drawString(code, drawX + xOffset, drawY + yOffset);
		g.setColor(prevColor);
	}
	
	public static void validateAirport(String code, String name, String xStr, String yStr, AirportBase airportBase) throws InvalidInputException{
		
		if(code.isEmpty() || name.isEmpty() || xStr.isEmpty() || yStr.isEmpty()) {
			throw new InvalidInputException("All fields must be filled out.");
		}
		
		if (airportBase.findByCode(code) != null) {
			throw new InvalidInputException("Airport with code " + code + " already exists.");
		}
		
		String errorMsg = "";
		boolean typeError = false;
		
		if (!code.matches("[A-Z]{3}")) {
			typeError = true;
			errorMsg += "Code must contain exactly 3 letters";
		}
		
		int xTest;
		int yTest;
		try {
			xTest = Integer.parseInt(xStr);
			yTest = Integer.parseInt(yStr);
		} catch (NumberFormatException e) {
	    	if (typeError)
	    		errorMsg += ", ";
			errorMsg += "X and Y coordinates must be integers.";
			throw new InvalidInputException(errorMsg);
		}
		
		if (xTest < -90 || xTest > 90 || yTest < -90 || yTest > 90) {
			if (typeError)
	    		errorMsg += ", ";
			typeError = true;
			errorMsg += "X and Y coordinates must be in range [-90, 90]";
		}
		
		if (typeError) {
			errorMsg += ".";
			throw new InvalidInputException(errorMsg);
		}
	}
	
}