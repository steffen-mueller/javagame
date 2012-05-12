package de.tu_darmstadt.gdi1.bomberman.framework;

import de.tu_darmstadt.gdi1.framework.controller.AbstractController;

/**
 * Dieses abstrakte Klasse erweitert den AbstractController des Frameworks um einige Bomberman-
 * spezifische Hilfestellungen. Implementierungen von Bomberman sollten diese Klasse extenden
 * und die abstrakten Methoden implementieren.
 */
abstract public class AbstractBombermanController extends AbstractController {
	abstract public void handleControllerEvent (ControllerEvent evt);
}
