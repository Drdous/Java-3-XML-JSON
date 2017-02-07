package cz.unicorncollege.controller;

import java.security.cert.CRLReason;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Choices;

public class ReservationController {
	private MeetingController meetingController;
	private MeetingCentre actualMeetingCentre;
	private MeetingRoom actualMeetingRoom;
	private Date actualDate;

	/**
	 * Constructor for ReservationController class
	 * 
	 * @param meetingController
	 *            loaded data of centers and its rooms
	 */
	public ReservationController(MeetingController meetingController) {
		this.meetingController = meetingController;
		this.actualDate = new Date();
	}

	/**
	 * Method to show options for reservations
	 * 
	 * @throws ParseException
	 */

	public void showReservationMenu() throws ParseException {
		List<String> choices = new ArrayList<String>();
		// let them choose one of the loaded meeting centres
		for (MeetingCentre centre : meetingController.getMeetingCentres()) {
			choices.add(centre.getCode() + " - " + centre.getName());
		}
		int i = 1;
		for (String ch : choices) {
			System.out.println(i + ") " + ch);
			i += 1;
		}
		// get the choice as string to parse it to integer later
		System.out.println("");
		String chosenOption = Choices.getInput("Choose the Meeting Centre: ");
		System.out.println("");
		if (Integer.parseInt(chosenOption) > choices.size()) {
			System.out.println("");
			System.out.println("Wrong choice. Try it again.");
			System.out.println("");
			return;
		}
		// get the chosen meeting center

		actualMeetingCentre = meetingController.getMeetingCentres().get(Integer.parseInt(chosenOption) - 1);
		choices.clear();
		// display rooms from actual meeting center
		for (MeetingRoom room : actualMeetingCentre.getMeetingRooms()) {
			choices.add(room.getCode() + " - " + room.getName());
		}
		int j = 1;
		for (String ch : choices) {
			System.out.println(j + ") " + ch);
			j += 1;
		}
		System.out.println("");
		chosenOption = Choices.getInput("Choose the room to create reservation: ");
		if (Integer.parseInt(chosenOption) > choices.size()) {
			System.out.println("");
			System.out.println("Wrong choice. Try it again.");
			System.out.println("");
			return;
		}
		actualMeetingRoom = actualMeetingCentre.getMeetingRooms().get(Integer.parseInt(chosenOption) - 1);
		choices.clear();

		getItemsToShow();
	}

	private void editReservation() throws ParseException {
		// TODO list reservation as choices, after chosen reservation edit all
		// relevant attributes
		int i = 1;
		for (Reservation r : actualMeetingRoom.getReservations()) {
			System.out.println(i + ") " + r);
			i += 1;
		}

		String vyber = Choices.getInput("Choose reservation to edit: ");
		if (Integer.parseInt(vyber) > actualMeetingRoom.getReservations().size()) {
			System.out.println("");
			System.out.println("Wrong choice. Try it again.");
			System.out.println("");
			return;
		}
		Reservation rezervace = actualMeetingRoom.getReservations().get(Integer.parseInt(vyber) - 1);
		System.out.println("You have chosen " + rezervace);

		List<String> choices = new ArrayList<String>();
		choices.add("Edit time:");
		choices.add("Edit customer");
		choices.add("Edit need video conference");
		choices.add("Edit note");
		choices.add("Go back");

		loop: while (true) {
			System.out.println(" ");
			for (String ch : choices) {
				System.out.println(ch);
			}

			switch (Choices.getChoice("Select an option: ", choices)) {
			case 1:
				boolean breaker = false;
				String timeEditFrom = Choices.getInput("Set time from (HH:MM): ");
				String timeEditTo = Choices.getInput("Set time to (HH:MM): ");
				for (Reservation r : actualMeetingRoom.getSortedReservationsByDate(rezervace.getDate())) {
					if (r.crashControll(timeEditFrom, timeEditTo)) {
						System.out.println("Time is in collision with other reservation");
						breaker = true;
						break;
					}

				}
				if (breaker) {
					break;
				}
				rezervace.setTimeFrom(timeEditFrom);
				rezervace.setTimeTo(timeEditTo);
				break;
			case 2:
				String customerEdit = Choices.getInput("Enter customer: ");
				rezervace.setCustomer(customerEdit);

				break;
			case 3:
				String vcEdit = Choices.getInput("Do you need video conference? (YES/NO): ");
				rezervace.setNeedVideoConference(vcEdit.equals("YES"));
				break;
			case 4:
				String noteEdit = Choices.getInput("Enter note: ");
				rezervace.setNote(noteEdit);
				break;
			case 5:
				break loop;
			}
		}
	}

	private void addNewReservation() throws ParseException {
		// TODO enter data one by one, add new reservation object to the actual
		// room, than inform about successful adding
		int epc = 0;
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = f.parse(Choices.getInput("Enter date (YYYY-MM-DD): "));
		String newTimeFrom = Choices.getInput("Set time from (HH:MM): ");
		String newTimeTo = Choices.getInput("Set time to (HH:MM): ");

		boolean breaker = false;
		for (Reservation r : actualMeetingRoom.getSortedReservationsByDate(newDate)) {
			if (r.crashControll(newTimeFrom, newTimeTo)) {
				System.out.println("Time is in collision with other reservation");
				breaker = true;
				return;
			}
		}
		String newEPC = Choices.getInput("Enter expected person count: ");
		if (Integer.parseInt(newEPC) > actualMeetingRoom.getCapacity()) {
			System.out.println(
					"Maximum capacity of this room is " + actualMeetingRoom.getCapacity() + ". Enter it again:");
			newEPC = Choices.getInput("Enter expected person count: ");
		}
		epc = Integer.parseInt(newEPC);
		String newCustomer = Choices.getInput("Enter customer: ");
		String newVC = Choices.getInput("Do you need video conference (YES/NO): ");
		String newNote = Choices.getInput("Enter note: ");

		Reservation newReservation = new Reservation(actualMeetingRoom, newDate, newTimeFrom, newTimeTo, epc,
				newCustomer, newVC.equals("YES"), newNote);
		actualMeetingRoom.addReservation(newReservation);
		System.out.println("Reservation was successfully added.");
		System.out.println(newReservation);

	}

	private void deleteReservation() {
		// TODO list all reservations as choices and let enter item for
		// deletion, delete it and inform about successful deletion
		int i = 1;
		for (Reservation r : actualMeetingRoom.getReservations()) {
			System.out.println(i + ") " + r);
			i += 1;
		}

		String vyber = Choices.getInput("Choose reservation to delete: ");
		if (Integer.parseInt(vyber) > actualMeetingRoom.getReservations().size()) {
			System.out.println("");
			System.out.println("Wrong choice. Try it again.");
			System.out.println("");
			return;
		}

			actualMeetingRoom.removeReservation(Integer.parseInt(vyber) - 1);
			System.out.println("Reservation was successfully removed");
		
		
	}

	private void changeDate() throws ParseException {
		// TODO let them enter new date in format YYYY-MM-DD, change the actual
		// date, list actual reservations on this date and menu by
		// getItemsToShow()
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = df.parse(Choices.getInput("Enter new date in format YYYY-MM-DD: "));
		actualDate = newDate;
		getItemsToShow();

	}

	private void getItemsToShow() throws ParseException {
		listReservationsByDate(actualDate);

		List<String> choices = new ArrayList<String>();
		choices.add("Edit Reservations");
		choices.add("Add New Reservation");
		choices.add("Delete Reservation");
		choices.add("Change Date");
		choices.add("Exit");

		while (true) {
			switch (Choices.getChoice("Select an option: ", choices)) {
			case 1:
				editReservation();
				break;
			case 2:
				addNewReservation();
				break;
			case 3:
				deleteReservation();
				break;
			case 4:
				changeDate();
				break;
			case 5:
				return;
			}
		}
	}

	private void listReservationsByDate(Date date) {
		// list reservations
		List<Reservation> list = actualMeetingRoom.getSortedReservationsByDate(date);
		if (list != null && list.size() > 0) {
			System.out.println("");
			System.out.println("Reservations for " + getActualData());
			for (Reservation reserv : list) {
				System.out.println(reserv);
			}
			System.out.println("");
		} else {
			System.out.println("");
			System.out.println("There are no reservation for " + getActualData());
			System.out.println("");
		}
	}

	/**
	 * Method to get formatted actual date
	 * 
	 * @return
	 */
	private String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(actualDate);
	}

	/**
	 * Method to get actual name of place - meeteng center and room
	 * 
	 * @return
	 */
	private String getCentreAndRoomNames() {
		return "MC: " + actualMeetingCentre.getName() + " , MR: " + actualMeetingRoom.getName();
	}

	/**
	 * Method to get actual state - MC, MR, Date
	 * 
	 * @return
	 */
	private String getActualData() {
		return getCentreAndRoomNames() + ", Date: " + getFormattedDate();
	}
}
