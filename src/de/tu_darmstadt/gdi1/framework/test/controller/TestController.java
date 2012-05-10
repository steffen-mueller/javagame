package de.tu_darmstadt.gdi1.framework.test.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import de.tu_darmstadt.gdi1.framework.controller.AbstractController;
import de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Some tests for the {@link AbstractController}.<br>
 * Because of the own thread of the class under test  we need to work hardly which locks in this test.<br>
 * Also for reasons of multi-threading some results were not defined precise - so we were not able to test this perfectly.<br> 
 * <p>Please notice that all Tests have defined timeouts - is there is something wrong by dealing with locks
 * (in the class under test OR in this test case) you will get a fail because of timeout (maybe a timeout exception).<br>
 * If you need to debug a test you should comment the timeout definition out ( <code>@Test(timeout=5000)</code> to this <code>@Test //(timeout=5000)</code> )  
 * @author jonas
 *
 */
public class TestController {

    /** the time the inserting thread should sleep at the half of the insertion. */
    private static final long SLEEP_TIME = 5;
    private ReentrantLock wakeUpLock;
    private ReentrantLock isFinishedLock;
    /** call signal to let the test-fake-controller wake up.*/
    private Condition wakeUpCondition;
    /** call signal to say that the queue of the controller is empty. */
    private Condition isFinishedCondition;
    private List<Integer> resultList;
    private TestContr testController;
    private LinkedList<Integer> shouldBe;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestDataForAllReceive.preCheck();
        TestDataForMixedCalls.preCheck();
        
        System.out.println("infos zum logging: um die Locks zu visualisieren wird immer \n" +
                "\"Klasse.Methode -> LockName.Action\" ausgegeben. \n" +
                "Achtung: 'yy -> xx.lock()/.await()' wird VOR dem Aufruf ausgegeben.\n" +
                " ABER 'yy -> xx.unlock()/.signal()' wird NACH dem Aufruf ausgeben.\n" +
                "Das bedeutet:\nHaengt der Test/schlaegt wegen TimeOut fehl kann man in der Konsole einfach zaehlen, ob mehr '-> lock()'-Ausgaben da sind als '-> unlock()'.\n" +
                "Ist das der Fall ist hier im TestCase etwas falsch. Stimmen die lock/unlocks muss es wo anders haengen.\n" +
                "Zwischen den Tests werden alle relevanten Objekte (Locks, Conditions, TestController, Listen) komplett neu erzeugt. Man muss also nur zwischen den entsprechenden \"setUp()\" Ausgaben suchen. ");
//        System.out.println("Please notice, if you get the following uncaught exception its an indicator that you run out of time:\n" +
//                "java.lang.InterruptedException\n" +
//                "at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.reportInterruptAfterWait(AbstractQueuedSynchronizer.java:1899)\n" +
//                "at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:1934)\n" +
//                "Why such an exception instead of a \"normal\" JUnit-fail because timeout?\n" +
//                "Because JUnit call interrupt on timeout - and the way to lock of this testcase (ReentrantLocks) is listening to the interrupted state.\n");

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        
        System.out.println("########################\n    setUp()\n########################");
        
        wakeUpLock = new ReentrantLock();
        isFinishedLock = new ReentrantLock();
        wakeUpCondition = wakeUpLock.newCondition();
        isFinishedCondition = isFinishedLock.newCondition();
        resultList = new LinkedList<Integer>();
        testController = new TestContr();
        shouldBe = new LinkedList<Integer>();
    }

    @After
    public void tearDown() throws Exception {
    	testController.stopWorker();
        wakeUpLock = null;
        isFinishedLock = null;
        wakeUpCondition = null;
        isFinishedCondition = null;
        resultList = null;
        testController = null;
        shouldBe = null;
    }
    
    /** test ob der controller alle events "wahrnimmt" die wir in die Queue stecken
     * und ob diese in der selben Reinfolge bearbeitet werden wie sie eingefuellt werden.
     * Nutzt zum befuellen NICHT die "immediate" Methode
     * Controller wird während des einfägens eingeschläfert
     */
    @Test(timeout=5000)
    public void allElementsReceiveInCorrectOrder_normal_sleeping() {
        try {
            helperAllElementsReceiveInCorrectOrder(false, true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We get an InterruptedException. Could be timeout?");
        }
    }
    
    /** test ob der controller alle events "wahrnimmt" die wir in die Queue stecken
     * und ob diese in der selben Reinfolge bearbeitet werden wie sie eingefuellt werden.
     * Nutzt zum befuellen die "immediate" Methode
     * Controller wird während des einfägens eingeschläfert
     */
    @Test(timeout=5000)
    public void allElementsReceiveInCorrectOrder_immediate_sleeping() {
        try {
            helperAllElementsReceiveInCorrectOrder(true, true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We get an InterruptedException. Could be timeout?");
        }
    }
    
    

    /** test ob der controller alle events "wahrnimmt" die wir in die Queue stecken
     * und ob diese in der selben Reinfolge bearbeitet werden wie sie eingefuellt werden.
     * Nutzt zum befuellen NICHT die "immediate" Methode
     * Controller wird während des einfägens NICHT eingeschläfert
     */
    @Test(timeout=5000)
    public void allElementsReceiveInCorrectOrder_normal_awake() {
        try {
            helperAllElementsReceiveInCorrectOrder(false, false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We get an InterruptedException. Could be timeout?");
        }
    }



    /** test ob der controller alle events "wahrnimmt" die wir in die Queue stecken
     * und ob diese in der selben Reinfolge bearbeitet werden wie sie eingefuellt werden.
     * Nutzt zum befuellen die "immediate" Methode
     * Controller wird während des einfägens NICHT eingeschläfert
     */
    @Test(timeout=5000)
    public void allElementsReceiveInCorrectOrder_immediate_awake() {
        try {
            helperAllElementsReceiveInCorrectOrder(true, false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We get an InterruptedException. Could be timeout?");
        }
    }
    
    
    /**
     * kommen die Events in richtiger Reihenfolge raus, wenn sie
     * äber gemischte wichtigkeiten eingefägt werden
     * Controller wird während des einfägens eingeschläfert
     */
    @Test(timeout=5000)
    public void allElementsReceiveInCorrectOrder_mixedCall_sleeping()  {
        try {
            allElementsReceiveInCorrectOrder_mixedCall_sleeping_wrapper();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We get an InterruptedException. Could be timeout?");
        }       
    }
          
	/**
     * kommen die Events in richtiger Reihenfolge raus, wenn sie
     * äber gemischte wichtigkeiten eingefägt werden
     * Controller wird während des einfägens NICHT eingeschläfert
     * daher ist die rauskommende Reihenfolge nicht genau definiert.
     * Hier wird nur die Reihenfolge innerhalb der verschiedenen wichtigkeiten getestet
     */
    @Test(timeout=5000)
    public void allElementsReceiveInCorrectOrder_mixedCall_awake() {
        try {
            allElementsReceiveInCorrectOrder_mixedCall_awake_wrapper();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We get an InterruptedException. Could be timeout?");
        }
    }
    
    /**
     * 
     * @param Important
     *      use {@link de.tu_darmstadt.gdi1.framework.test.controller.TestController.TestContr#handleEventImmediately(de.tu_darmstadt.gdi1.framework.test.controller.TestController.TestEvent)}?<br>
     *      if false {@link de.tu_darmstadt.gdi1.framework.test.controller.TestController.TestContr#handleEvent(de.tu_darmstadt.gdi1.framework.test.controller.TestController.TestEvent)} will be used.
     * @param letSleepDuringInsert
     *      should the {@link de.tu_darmstadt.gdi1.framework.test.controller.TestController.TestContr} sleep during the insertion of new events?<br>
     *       if not - the insertion will pause shortly at the half. 
     * @throws InterruptedException
     */
    public void helperAllElementsReceiveInCorrectOrder(boolean Important, boolean letSleepDuringInsert) throws InterruptedException {

        LOGGER.LOG("helperAllElementsReceiveInCorrectOrder -> isFinishedLock.lock");
        //get the lock for finish
        isFinishedLock.lock();

        if (letSleepDuringInsert) {
            //let the controller fall asleep:
            testController.handleEvent(TestEvent.createWait());
        }
        //add some events:
        int eventCount = TestDataForAllReceive.generatedNumbers.length;
        for (int i = 0; i < eventCount; i++) {

            //if controller does not sleep and we reached the half we wait and let the controller work..
            if ((!letSleepDuringInsert) && (i == eventCount/2)) {
                Thread.sleep(SLEEP_TIME);
            }

            int currentInt = TestDataForAllReceive.generatedNumbers[i];
            testController.handleEvent(new TestEvent(currentInt));
            shouldBe.add(currentInt);
        }
        
        
        // all Test-Events added.
        testController.handleEvent(TestEvent.createFinish());
        
        letWakeUpAndReturnFirstIfFinished(letSleepDuringInsert, false);
        
        //test if the controller has procesed all events we filled in:
        assertEquals(resultList.size(), shouldBe.size());
        //test if the controller process the events in the same order we put in:
        assertEquals(shouldBe, resultList);
    }
    
    


    public void allElementsReceiveInCorrectOrder_mixedCall_sleeping_wrapper() throws InterruptedException {
        //let the controller fall asleep:
        testController.handleEvent(TestEvent.createWait());
        
        List<Integer> shouldBeNormal = new LinkedList<Integer>();
        
        //add some events:
        int countOfImmediate = TestDataForMixedCalls.generatedNumbersImmediate.length;
        int nextImmediate = 0;
        int countOfNormal = TestDataForMixedCalls.generatedNumbersNormal.length;
        for (int i = 0; i < countOfNormal; i++) {
            //jedes dritte event wird immediate:
            if ((i % 3 == 0) && (nextImmediate < countOfImmediate)) {
                Integer currentIntImmediate = TestDataForMixedCalls.generatedNumbersImmediate[nextImmediate++];
                testController.handleEventImmediately(new TestEvent(currentIntImmediate));
                shouldBe.add(currentIntImmediate);
            }
            int currentInt = TestDataForMixedCalls.generatedNumbersNormal[i];
            testController.handleEvent(new TestEvent(currentInt));
            shouldBeNormal.add(currentInt);
        }
        
        shouldBe.addAll(shouldBeNormal);
        
        // all Test-Events added.
        testController.handleEvent(TestEvent.createFinish());
        testController.handleEventImmediately(TestEvent.createFinish());
        
        
        LOGGER.LOG("allElementsReceiveInCorrectOrder_mixedCall_sleeping_wrapper -> isFinishedLock.lock()");
        isFinishedLock.lock();
        
        letWakeUpAndReturnFirstIfFinished(true, true);
        
        //test if the controller has procesed all events we filled in:
        assertEquals(resultList.size(), shouldBe.size());

        //test if the controller process the events in the same order we put in:
        assertEquals(shouldBe, resultList);
    }
    
    public void allElementsReceiveInCorrectOrder_mixedCall_awake_wrapper() throws InterruptedException {
        
        //get finished lock, we do not want to mis a finish signal anyway.
        LOGGER.LOG("allElementsReceiveInCorrectOrder_mixedCall_awake_wrapper -> isFinishedLock.lock()");
        isFinishedLock.lock();

        List<Integer> shouldBeNormal = new LinkedList<Integer>();
        
        //add some events:
        int countOfImmediate = TestDataForMixedCalls.generatedNumbersImmediate.length;
        int nextImmediate = 0;
        int countOfNormal = TestDataForMixedCalls.generatedNumbersNormal.length;
        for (int i = 0; i < countOfNormal; i++) {
            
            
            if (i == countOfNormal/2) {
                Thread.sleep(SLEEP_TIME);
            }

            //jedes dritte event wird immediate:
            if ((i % 3 == 0) && (nextImmediate < countOfImmediate)) {
                int currentIntImmediate = TestDataForMixedCalls.generatedNumbersImmediate[nextImmediate++];
                testController.handleEventImmediately(new TestEvent(currentIntImmediate));
                shouldBe.add(currentIntImmediate);
            }
            int currentInt = TestDataForMixedCalls.generatedNumbersNormal[i];
            testController.handleEvent(new TestEvent(currentInt));
            shouldBeNormal.add(currentInt);
        }
        
        // all Test-Events added.
        testController.handleEvent(TestEvent.createFinish());
        testController.handleEventImmediately(TestEvent.createFinish());
        
        //fall asleep until the worker finish.
        //we already get the lock before we at the events - so we get the isFinished signal anyway.
        letWakeUpAndReturnFirstIfFinished(false, true);

        //test if the controller process the events in the same order we put in,
        // see above we only check the ordner of the importance level inside
        
        //notice:
        //preCheck (beforeClass) grants that every element is a singleton
        //and no element of the immediate-list is contained by the normal-list.
        // so its enough to check if:
        //      - the total number of elements fit
        //      - every element is contained in the result
        //      - every element is behind the element which was in the initial list before him.

        //test if the controller has procesed all events we filled in:
        assertEquals(resultList.size(), shouldBeNormal.size() + shouldBe.size());

        { //test immediate:
            int lastIndex = -1;
            Integer currentNumber;
            int indexOfCurrent;
            for (int i = 0; i < shouldBe.size(); i++) {
                currentNumber = shouldBe.get(i);
                indexOfCurrent = resultList.indexOf(currentNumber);
                if (indexOfCurrent == -1) {
                	fail("Immediate-Element was lost:" + currentNumber);
                } else if (indexOfCurrent <= lastIndex) {
                    fail("Inside Order of Immediate failed.");
                } else {
                    lastIndex = indexOfCurrent;
                }
            }
        }
        { //test normal:
            int lastIndex = -1;
            Integer currentNumber;
            int indexOfCurrent;
            for (int i = 0; i < shouldBeNormal.size(); i++) {
                currentNumber = shouldBeNormal.get(i);
                indexOfCurrent = resultList.indexOf(currentNumber);
                if (indexOfCurrent == -1) {
                	fail("Normal-Element was lost:" + currentNumber);
                } else if (indexOfCurrent <= lastIndex) {
                    fail("Inside Order of normal failed.");
                } else {
                    lastIndex = indexOfCurrent;
                }
            }
        }
        
    }
    
    /**
     * will let the current thread fall asleep - and will return when the other thread had informed about "finished".<br>
     * You need to lock {@link #isFinishedLock} before this method.
     * Unlocking will be done inside.
     * @throws InterruptedException
     */
    private void letWakeUpAndReturnFirstIfFinished(boolean sendWakeUpBefore, boolean waitForTwoInsteadOfOneFinish) throws InterruptedException  {
    	
    	if (sendWakeUpBefore) {
    		//get lock for wakeup
    		LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpLock.lock()");
    		wakeUpLock.lock();
    		//let the worker wake up to process these events  
    		wakeUpCondition.signal();
    		LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpCondition.signal()");
    		wakeUpLock.unlock();
    		LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpLock.unlock()");
    	}
        
        //fall asleep until the worker finish.
        //we already get the lock before we send the wakeUp signal - so we get the isFinished signal anyway.
        LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> isFinishedCondition.await()");
        isFinishedCondition.await();


        LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpLock.lock()");
        wakeUpLock.lock();
        wakeUpCondition.signal();
        LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpCondition.signal()");
        wakeUpLock.unlock();
        LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpLock.unlock()");
        
        if (waitForTwoInsteadOfOneFinish) {
        	LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> isFinishedCondition.await()");
        	isFinishedCondition.await();
        	
            LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpLock.lock()");
            wakeUpLock.lock();
            wakeUpCondition.signal();
            LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpCondition.signal()");
            wakeUpLock.unlock();
            LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> wakeUpLock.unlock()");
        }

        LOGGER.LOG("letWakeUpAndReturnFirstIfFinished -> isFinishedLock.unlock()");
        isFinishedLock.unlock();
	}
    
    protected class TestContr extends AbstractController {
        private TestContr() {
            super();
        }
        
        protected void stopWorker() {
        	super.stopWorker();
        }
        
		@Override
		protected void processEvent(IControllerEvent aEvent) {
			TestEvent event = (TestEvent) aEvent;
            if (event.isWaitEvent()) {
                waitForWakeUp();
            } else if (event.isFinishEvent()) {
                LOGGER.LOG("TestContr.processEvent -> isFinishedLock.lock");
                isFinishedLock.lock();
                isFinishedCondition.signal();
                LOGGER.LOG("TestContr.processEvent -> isFinishedLock.signal()");
                LOGGER.LOG("TestContr.processEvent -> wakeUpLock.lock()");
                wakeUpLock.lock();
                isFinishedLock.unlock();
                LOGGER.LOG("TestContr.processEvent -> isFinishedLock.unlock(), now fall asleep:");
                waitForWakeUp();
                wakeUpLock.unlock();
                LOGGER.LOG("TestContr.processEvent -> wakeUpLock.unlock()");
            } else {
                resultList.add(event.getNumber());
            }
        }
        
        private void waitForWakeUp() {
            LOGGER.LOG("TestContr.waitForWakeUp -> wakeUpLock.lock()");
            wakeUpLock.lock();
            try {
                LOGGER.LOG("TestContr.waitForWakeUp -> wakeUpCondition.await");
                wakeUpCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new IllegalAccessError("Its illegal to interrupt the controller.");
            } finally {
                wakeUpLock.unlock();
                LOGGER.LOG("TestContr.waitForWakeUp -> wakeUpLock.unlock()");
            }
        }
    }
    
    protected static class TestEvent extends DefaultControllerEvent {

        private final Integer number;
        private boolean waitEvent = false;
        private boolean finishEvent = false;
        
        public static TestEvent createWait() {
            TestEvent tmp = new TestEvent();
            tmp.waitEvent = true;
            return tmp;
        }
        public static TestEvent createFinish() {
            TestEvent tmp = new TestEvent();
            tmp.finishEvent = true;
            return tmp;
        }
        
        private TestEvent() {
            number = null;
        }

        private TestEvent(Integer aNumber) {
            number = aNumber;
        }
        public boolean isWaitEvent() {
            return waitEvent;
        }
        public int getNumber() {
            return number;
        }
        public boolean isFinishEvent() {
            return finishEvent;
        }
    }

    private static final class TestDataForAllReceive {
		public static Integer[] generatedNumbers = new Integer[] { 289, 63, 423, 413, 237, 249, 33, 195, 181, 498, 16,
				338, 365, 205, 495, 252, 366, 454, 327, 368, 418, 52, 206, 220, 307, 200, 31, 24, 201, 269, 394, 105,
				396, 393, 330, 288, 484, 280, 235, 214, 496, 483, 481, 193, 306, 459, 352, 437, 348, 382, 434, 405,
				172, 83, 497, 155, 81, 345, 486, 170, 187, 468, 487, 210, 426, 168, 376, 253, 422, 194, 259, 212, 364,
				260, 398, 124, 47, 271, 304, 282 };    	
    	
    	public static void preCheck() {
    		List<Integer> murks = Arrays.asList(generatedNumbers);
    		for (Integer ele : murks) {
    			if (murks.indexOf(ele) != murks.lastIndexOf(ele)) {
    				throw new IllegalArgumentException("Every element of generatedNumbers have to be a singleton: "
    						+ ele.intValue());
    			}
    		}
    	}
    }

    private static final class TestDataForMixedCalls {
    	
		/** have to be three times more entrys than {@link #generatedNumbersImmediate}. */
		public static Integer[] generatedNumbersNormal = new Integer[] { 464, 138, 291, 162, 472, 423, 339, 377, 7, 68,
				442, 480, 144, 254, 332, 158, 239, 204, 493, 177, 151, 43, 34, 320, 85, 270, 99, 146, 205, 223, 368,
				235, 245, 206, 167, 375, 404, 288, 48, 123, 295, 252, 225, 269, 387, 110, 419, 64, 92, 337, 428, 316,
				392, 244, 72, 12, 321, 462, 415, 299, 73, 192, 237, 168, 420, 171, 202, 13, 203, 88, 219, 14, 21, 369,
				487, 75, 426, 405, 249, 184, 116, 0, 176, 479, 460, 58, 172, 260, 137, 390 };
		/** have to be a third of {@link #generatedNumbersNormal}. */
		public static Integer[] generatedNumbersImmediate = new Integer[] { 247, 251, 471, 159, 238, 456, 477, 217,
				435, 145, 445, 108, 413, 42, 87, 365, 476, 82, 319, 128, 157, 468, 30, 328, 349, 451, 484, 388, 20, 229 };
    	
    	public static void preCheck() {
    		List<Integer> normal = Arrays.asList(generatedNumbersNormal);
    		List<Integer> immediate = Arrays.asList(generatedNumbersImmediate);
    		for (Integer ele : normal) {
    			if (normal.indexOf(ele) != normal.lastIndexOf(ele)) {
    				throw new IllegalArgumentException(
    						"Every element of generatedNumbersNormal have to be a singleton: " + ele.intValue());
    			}
    			if (immediate.indexOf(ele) != -1) {
    				throw new IllegalArgumentException(
    						"Every element of generatedNumbersNormal is not allowed for generatedNumbersImmediate:"
    						+ ele.intValue());
    			}
    		}
    		for (Integer ele : immediate) {
    			if (normal.indexOf(ele) != normal.lastIndexOf(ele)) {
    				throw new IllegalArgumentException(
    						"Every element of generatedNumbersImmediate have to be a singleton:" + ele.intValue());
    			}
    		}
    	}
    }

    
    /**
     * just a simple helper class to create a number of random numbers.
     * @author jonas
     *
     */
    public static class numberCreator {
    	/** count of the numbers you want. */
    	private static final int NEED_NUM_COUNT = 30;
    	/** the minimal random number. */ 
    	private static final int MIN_NUM = 0;
    	/** a random number will be created between zero and this value. Than the {@link de.tu_darmstadt.gdi1.framework.test.controller.TestController.numberCreator#MIN_NUM} will be added.*/
    	private static final int MAX_DIFF_MIN_TO_MAX= 500;
    	
    	private static final Integer[] OtherArray = TestDataForMixedCalls.generatedNumbersNormal; //TestDataForAllReceive.generatedNumbers;

    	public static void main(String args[]) {
    		System.out.println("numberCreator for " + NEED_NUM_COUNT + " numbers. Range: " + MIN_NUM + "-" + (MIN_NUM + MAX_DIFF_MIN_TO_MAX) + ".");
    		Random rand = new Random();

    		List<Integer> tmp = new ArrayList<Integer>();
    		List<Integer> tmp2 = new ArrayList<Integer>();
    		if (OtherArray != null) {
    			tmp2 = Arrays.asList(OtherArray);
    		}

    		System.out.print("public static Integer[] generatedNumbers = new Integer[] { ");
    		//we write as first a number, than the insertion of "," is without problems/checks at the correct position
    		System.out.print(MIN_NUM + rand.nextInt(MAX_DIFF_MIN_TO_MAX));
    		//because we printed the first number external of the loop we have to decrement the start by one instead of zero and work only to "smaller than" 
    		for (int i = 1; i < NEED_NUM_COUNT; i++) {
    			
    			Integer nextNumber;
    			if (OtherArray != null) {
        			do {
        				nextNumber = MIN_NUM + rand.nextInt(MAX_DIFF_MIN_TO_MAX);
        			} while ((tmp.indexOf(nextNumber) != -1) || (tmp2.indexOf(nextNumber) != -1));
    			} else {
        			do {
        				nextNumber = MIN_NUM + rand.nextInt(MAX_DIFF_MIN_TO_MAX);
        			} while (tmp.indexOf(nextNumber) != -1);
    			}
    			
    			tmp.add(nextNumber);
    			
    			System.out.print(", ");
    			System.out.print(nextNumber);
    		}
    		System.out.println(" };");
    	}
    }

    private static class LOGGER {

    	private static SimpleDateFormat simpleDateFormatExactly = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

    	public static String getCurrentExactTime() {
    		return simpleDateFormatExactly.format(new Date());
    	}

    	public static void LOG(final String msg) {
    		System.out.println(getCurrentExactTime() + " " + Thread.currentThread().getName() + " || " + msg);
    	}
    }


   
// ############################    
//    How to do it bad:
// ############################   
//    public static void main(String[] args) {
//        final ReentrantLock lock = new ReentrantLock();
//        final Condition listenToMe = lock.newCondition();
//        
//        lock.lock();
//        new Thread() {
//          public void run() {
//              lock.lock();
//              listenToMe.signal();
//              //forgotten: lock.unlock;
//              LOGGER.LOG("seperate Thread: signaled but still locked");
//              lock.unlock();
//              LOGGER.LOG("seperate Thread: unlocked");
//              lock.lock();
//              listenToMe.signal();
//              LOGGER.LOG("seperate Thread: signaled but still locked");
//              lock.unlock();
//              LOGGER.LOG("seperate Thread: unlocked, finished");
//          }
//        }.start();
//        LOGGER.LOG("main Thread: fall asleep.");
//        try {
//            listenToMe.await();
//            LOGGER.LOG("main Thread: waked up.");
//            listenToMe.await();
//            LOGGER.LOG("main Thread: waked up.");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        LOGGER.LOG("main Thread: finished.");
//        lock.unlock();
//    }
    
}
