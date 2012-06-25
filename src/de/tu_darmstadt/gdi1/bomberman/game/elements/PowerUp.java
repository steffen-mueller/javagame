package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;


public class PowerUp extends GameElement
{
	// PowerID decides of which PowerUp-Type this is
	// 1 = Increase detonation radius
	// 2 = Increase speed
	// 3 = Increase bomb count
	// 4 = SuperBomb
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
			case 1:
				return new ImageIcon("resource/images/increase_radius.png");
		
			default:
				return new ImageIcon("resource/images/increase_radius.png");
		}
		
	}

	public String getDescription() {

		switch(powerID){
			case 1:
				return "Increase Radius - PowerUp";

			default:
				return "Unknown - PowerUp";
		}
	}

	public char getParsingSymbol() {
		
		return ' ';
	}

	@Override
	public void destroy() {
		PowerUp.super.destroy();
		System.out.println(getDescription() + " died.");
	}
}
