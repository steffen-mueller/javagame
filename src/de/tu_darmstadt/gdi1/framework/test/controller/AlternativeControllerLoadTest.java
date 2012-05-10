package de.tu_darmstadt.gdi1.framework.test.controller;
import de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative;
import de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IController;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;


public class AlternativeControllerLoadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AlternativeControllerLoadTest();
	}
	
	private static long staticcount = 0;
	
	public synchronized long getCount() {
		return staticcount;
	}

	public synchronized long nextCount() {
		return staticcount++;
	}
	
	public AlternativeControllerLoadTest() {
		IController controller = new TestingController();
		new LoadCreator("one", controller);
		new LoadCreator("two", controller);
	}
	
	
	public class TestingController extends AbstractControllerAlternative {
		public TestingController() {
			super();
		}
		
		protected void processEvent(IControllerEvent event) {
			System.out.println(event.getString());			
		}
	}
	
	public class LoadCreator extends Thread {
		private String name;
		private IController controller;
		public LoadCreator(String name, IController controller) {
			this.name = name;
			this.controller = controller;
			this.start();
		}
		
		public void run() {
			while (getCount() < 5000) {
				
				if ((getCount() % 100) == 0) {
					controller.handleEventImmediately(
							new DefaultControllerEvent(
									name + " -prio- " + nextCount()
								)
							);
				} else {
					controller.handleEvent(
							new DefaultControllerEvent(
									name + " -norm- " + nextCount()
								)
							);
				}
			}
		}
	}

}
