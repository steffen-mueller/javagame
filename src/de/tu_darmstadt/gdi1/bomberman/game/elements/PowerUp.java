package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;


public class PowerUp extends GameElement
{
	protected int powerID;
	
	public int getpowerupID(){
		return powerID;
	}

	public PowerUp ()
	{
		powerID = 1;
	}

	public boolean isDestroyable () {
		return true;
	}

	public boolean isSolid() {
		return false;
	}

	public GameElement clone() {
	
		return new PowerUp();
	}

	public ImageIcon getImageIcon() {
	
		switch(powerID){
		case 1: return new ImageIcon("resource/images/increase_radius.png"); 
		
		default: return new ImageIcon("resource/images/increase_radius.png");
		}
		
	}

	public String getDescription() {
		
		return "PowerUp";
	}

	public char getParsingSymbol() {
		
		return ' ';
	}
}
