/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tu_darmstadt.gdi1.bomberman.game;

import de.tu_darmstadt.gdi1.framework.controller.AbstractController;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;

/**
 *
 * @author Steffen Müller
 */
public class BombermanController extends AbstractController {

	@Override
	protected void processEvent(IControllerEvent event) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

  	public static void main(String args[])
	{
		BombermanController ctr = new BombermanController();
		
	}
}


