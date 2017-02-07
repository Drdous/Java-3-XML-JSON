package cz.unicorncollege.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.FileParser;

/**
 * Main controller class. Contains methods to communicate with user and methods
 * to work with files.
 *
 * @author UCL
 */
public class MainController {
	private MeetingController controll;
	private ReservationController controllReservation;

	/**
	 * Constructor of main class.
	 */
	public MainController() {
		controll = new MeetingController();
		controll.init();

		controllReservation = new ReservationController(controll);
	}

	/**
	 * Main method, which runs the whole application.
	 *
	 * @param argv
	 *            String[]
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] argv) throws IOException, ParseException {
		MainController instance = new MainController();
		instance.run();
	}

	/**
	 * Method which shows the main menu and end after user chooses Exit.
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	private void run() throws IOException, ParseException {
		List<String> choices = new ArrayList<String>();
		choices.add("List all Meeting Centres");
		choices.add("Add new Meeting Centre");
		choices.add("Reservations");
		choices.add("Import Data");
		choices.add("Export Data");
		choices.add("Exit and Save");
		choices.add("Exit");

		while (true) {
			switch (Choices.getChoice("Select an option: ", choices)) {
			case 1:
				controll.listAllMeetingCentres();
				break;
			case 2:
				controll.addMeeMeetingCentre();
				break;
			case 3:
				controllReservation.showReservationMenu();
				break;
			case 4:
				controll.setMeetingCentres(FileParser.importData());
				controll.listAllMeetingCentres();
				break;
			case 5:
				FileParser.exportDataToJSON(controll.getMeetingCentres());
				break;
			case 6:
				FileParser.saveData(controll.toSaveString());
				return;
			case 7:
				String confirmation = Choices.getInput("Are you sure to exit without save? (YES/NO): ");
				if (confirmation.equals("YES")) {
					return;
				}
				else {
					break;
				}
			}
		}
	}
}
