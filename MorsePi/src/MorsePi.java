/**
 * A simple morse program for Raspberry Pi.
 * Make sure you have a led light connected to the gpio-port 0
 * or change the port in the source code.
 * 
 * @author Snorre Magnus Davøen
 * @version 2013.03.15
 * 
 * License (Beerware):
 * The user is allowed to do anything with the licensed material.
 * Should the user of the product meet the author and consider the
 * software useful, he is encouraged to buy the author a beer
 * "in return" (or, in some variations, to drink a beer in the 
 * author's honor).
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.stream.events.Characters;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Class that makes morse code from text
 *
 */
public class MorsePi {

	private HashMap<String, LinkedList<Boolean>> characterConversions;
	private GpioController gpioController;
	private GpioPinDigitalOutput gpio0;

	public MorsePi(String[] words) {
		gpioController = GpioFactory.getInstance();
		gpio0 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "gpio0", PinState.LOW);

		// Make alphabet to morsecode dictionary:
		characterConversions = new HashMap<>(40);
		characterConversions.put("a", makeBooleanList(new Boolean[]{false, true}));
		characterConversions.put("b", makeBooleanList(new Boolean[]{true, false, false, false}));
		characterConversions.put("c", makeBooleanList(new Boolean[]{true, false, true, false}));
		characterConversions.put("d", makeBooleanList(new Boolean[]{true, false, false}));
		characterConversions.put("e", makeBooleanList(new Boolean[]{false}));
		characterConversions.put("f", makeBooleanList(new Boolean[]{false, false, true, false}));
		characterConversions.put("g", makeBooleanList(new Boolean[]{true, true, false}));
		characterConversions.put("h", makeBooleanList(new Boolean[]{false, false, false, false}));
		characterConversions.put("i", makeBooleanList(new Boolean[]{false, false}));
		characterConversions.put("j", makeBooleanList(new Boolean[]{false, true, true, true}));
		characterConversions.put("k", makeBooleanList(new Boolean[]{true, false, true}));
		characterConversions.put("l", makeBooleanList(new Boolean[]{false, true, false, false}));
		characterConversions.put("m", makeBooleanList(new Boolean[]{true, true}));
		characterConversions.put("n", makeBooleanList(new Boolean[]{true, false}));
		characterConversions.put("o", makeBooleanList(new Boolean[]{true, true, true}));
		characterConversions.put("p", makeBooleanList(new Boolean[]{false, true, true, false}));
		characterConversions.put("q", makeBooleanList(new Boolean[]{true, true, false, true}));
		characterConversions.put("h", makeBooleanList(new Boolean[]{false, true, false}));
		characterConversions.put("s", makeBooleanList(new Boolean[]{false, false, false}));
		characterConversions.put("t", makeBooleanList(new Boolean[]{true}));
		characterConversions.put("u", makeBooleanList(new Boolean[]{false, false, true}));
		characterConversions.put("v", makeBooleanList(new Boolean[]{false, false, false, true}));
		characterConversions.put("x", makeBooleanList(new Boolean[]{true, false, false, true}));
		characterConversions.put("y", makeBooleanList(new Boolean[]{true, false, true, true}));
		characterConversions.put("z", makeBooleanList(new Boolean[]{true, true, false, false}));

		// For each word in input string
		for(String word : words) {
			try {
				morse(word);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gpio0.setState(PinState.LOW);
		gpioController.shutdown();

	}

	/**
	 * Takes one string argument and outputs that text as morse code in light.
	 * Only accept a - z.
	 * @param args
	 */
	public static void main(String[] args) {

		String[] words = args[0].split(" ");
		new MorsePi(words);

	}

	public LinkedList<Boolean> makeBooleanList(Boolean[] booleans) {
		return new LinkedList<Boolean>(Arrays.asList(booleans));
	}

	public void morse(String word) throws InterruptedException {
		// Get each individual character in that word
		char letters[] = word.toCharArray();
		for(char letter : letters) {
			// Get the morse code if it exist
			LinkedList<Boolean> morseCode = characterConversions.get(Character.toString(letter));
			if(morseCode != null) {
				// Let there be light (short or long)
				for(boolean morse : morseCode) {
					if(morse) {
						gpio0.pulse(600);
						Thread.sleep(1100);

					} else {
						gpio0.pulse(250);
						Thread.sleep(750);
					}
				}
			}
			Thread.sleep(1500);
		}

	}
}