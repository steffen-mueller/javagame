package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;
import java.util.Random;

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
		Random generator = new Random();
		int rand = generator.nextInt( 3 ) + 1;
		powerID =  rand;
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
			case 2:
				return new ImageIcon("resource/images/increase_radius.png");
			case 3:
				return new ImageIcon("resource/images/increase_radius.png");
			default:
				return new ImageIcon("resource/images/increase_radius.png");
		}
		
	}

	public String getDescription() {

		switch(powerID){
			case 1:
				return "Increase Radius - PowerUp";
			case 2: 
				return "Increase Speed - PowerUp"; 
			case 3: 
				return "Increase bomb count - PowerUp";
			case 4: 
				return "SuperBomb - PowerUp";
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
