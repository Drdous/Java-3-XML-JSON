package cz.unicorncollege.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.FileParser;

public class MeetingController {
	private List<MeetingCentre> meetingCentres;

	/**
	 * Method to initialize data from the saved datafile.
	 */
	public void init() {

		// TODO: nacist z ulozeneho souboru vsechna meeting centra, mistnosti a
		// rezervace
		meetingCentres = FileParser.loadDataFromFile();
	}

	/**
	 * Method to list all meeting centres to user and give him some options what
	 * to do next.
	 */
	public void listAllMeetingCentres() throws IOException {

		// TODO: vypsat data o meeting centrech
		for (MeetingCentre mc : meetingCentres) {
			System.out.println(mc);
		}
		List<String> choices = new ArrayList<String>();
		choices.add("1) Show Details of Meeting Centre with code:");
		choices.add("2) Edit Meeting Centre with code:");
		choices.add("3) Delete Meeting Centre with code:");
		choices.add("4) Go Back to Home");

		loop: while (true) {
			System.out.println(" ");
			for (String ch : choices) {
				System.out.println(ch);
			}

			String chosenOption = Choices.getInput("Choose option (including code after '-', example 1-M01): ");
			int option = chosenOption.contains("-") ? Integer.parseInt(chosenOption.substring(0, 1))
					: Integer.parseInt(chosenOption);
			String code = chosenOption.contains("-") ? chosenOption.substring(2, chosenOption.length()) : "";

			switch (option) {
			case 1:
				showMeetingCentreDetails(code);
				break;
			case 2:
				editMeetingCentre(code);
				break;
			case 3:
				deleteMeetingCentre(code);
				break;
			case 4:
				break loop;
			}
		}

	}

	/**
	 * Method to add a new meeting centre.
	 */
	public void addMeeMeetingCentre() {
		String locationFilter = Choices.getInput("Enter name of MeetingCentre: ");
		// TODO: doplnit zadavani vsech dalsich hodnot + naplneni do noveho
		// objektu
		String code = Choices.getInput("Enter code of MeetingCentre: ");
		String description = Choices.getInput("Enter description of MeetingCentre: ");
		MeetingCentre novy = new MeetingCentre(locationFilter, code, description);
		meetingCentres.add(novy);
	}

	/**
	 * Method to show meeting centre details by id.
	 */
	public void showMeetingCentreDetails(String input) {
		// TODO: doplnit nacteni prislusneho meeting centra a vypsani jeho
		// zakladnih hodnot
		boolean found = false;
		MeetingCentre tmp = null;
		for (MeetingCentre mc : meetingCentres) {
			if (mc.getCode().equals(input)) {
				System.out.println(mc);
				tmp = mc;
				found = true;
				break;
			}
		}
		if (!found) {
			System.out.println("Wrong code, try it again");
			return;
		}
		// System.out.println(tmp);
		List<String> choices = new ArrayList<String>();
		choices.add("1) Show meeting rooms");
		choices.add("2) Add meeting room");
		choices.add("3) Edit details");
		choices.add("4) Go Back");

		// TODO: doplnit metody pro obsluhu meeting rooms atd.

		while (true) {
			System.out.println(" ");
			for (String ch : choices) {
				System.out.println(ch);
			}

			String chosenOption = Choices.getInput(
					"Choose option: (including code after '-', example 1-M01 (Not for choice 1 - Show meeting rooms))");
			int option = chosenOption.contains("-") ? Integer.parseInt(chosenOption.substring(0, 1))
					: Integer.parseInt(chosenOption);
			String code = chosenOption.contains("-") ? chosenOption.substring(2, chosenOption.length()) : "";
			if (option == 1) {
				String vctf = null;
				for (MeetingRoom mr : tmp.getMeetingRooms()) {
					if (mr.isHasVideoConference() == true) {
						vctf = "YES";
						System.out.println(mr.getName() + "," + mr.getCode() + "," + mr.getDescription() + ","
								+ mr.getCapacity() + "," + vctf);
					} else {
						vctf = "NO";
						System.out.println(mr.getName() + "," + mr.getCode() + "," + mr.getDescription() + ","
								+ mr.getCapacity() + "," + vctf);
					}
				}
			}
			switch (option) {
			case 1:
				/*
				 * for (MeetingRoom mr : tmp.getMeetingRooms()) { if
				 * (mr.getCode().equals(code)) { System.out.println(mr); } }
				 */
				break;
			case 2:
				String name = Choices.getInput("Enter name of MeetingRoom: ");
				String codeOfMr = Choices.getInput("Enter code of MeetingRoom: ");
				String description = Choices.getInput("Enter description of MeetingRoom: ");
				int capacity = Integer.parseInt(Choices.getInput("Enter capacity of MeetingRoom: "));
				if (capacity < 0 || capacity > 100) {
					System.out.println("Wrong input");
				}
				String hasVideoConference = Choices.getInput("Has this MeetingRoom video conference? (YES/NO): ");
				tmp.addMeetingRooms(
						new MeetingRoom(name, codeOfMr, description, capacity, hasVideoConference.equals("YES"), tmp));
				break;
			case 3:
				MeetingRoom tmp2 = null;
				for (MeetingRoom mr : tmp.getMeetingRooms()) {
					if (mr.getCode().equals(code)) {
						tmp2 = mr;
						break;
					}
				}

				List<String> choicesEdit = new ArrayList<String>();
				choicesEdit.add("Name");
				choicesEdit.add("Code");
				choicesEdit.add("Description");
				choicesEdit.add("Capacity");
				choicesEdit.add("Possibility of video conference");
				choicesEdit.add("Return");

				while (true) {
					switch (Choices.getChoice("Select an option: ", choicesEdit)) {
					case 1:
						String nameEdit = Choices.getInput("Please write new name: ");
						tmp2.setName(nameEdit);
						break;
					case 2:
						String codeEdit = Choices.getInput("Enter new code: ");
						tmp2.setCode(codeEdit);
						break;
					case 3:
						String descriptionEdit = Choices.getInput("Write some description of the room: ");
						tmp2.setDescription(descriptionEdit);
						break;
					case 4:
						int capacityEdit = Integer.parseInt(Choices.getInput("Set new capacity: "));
						tmp2.setCapacity(capacityEdit);
						break;
					case 5:
						String videoConferenceEdit = Choices.getInput("Has this room video conference? (YES/NO)");
						tmp2.setHasVideoConference(videoConferenceEdit.equals("YES"));
						break;
					case 6:
						return;
					}
				}

			case 4:
				return;
			}
		}

	}

	/**
	 * Method to edit meeting centre data by id.
	 */
	public void editMeetingCentre(String input) {
		// String locationFilter = Choices.getInput("Enter new name of
		// MeetingCentre: ");
		// TODO: doplneni editace, bud vsech polozek s moznosti preskoceni nebo
		// vyber jednotlive polozky

		MeetingCentre tmp2 = null;
		for (MeetingCentre mc : meetingCentres) {
			if (mc.getCode().equals(input)) {
				tmp2 = mc;
				break;
			}

		}

		List<String> choices = new ArrayList<String>();
		choices.add("Name");
		choices.add("Code");
		choices.add("Description");
		choices.add("Return");

		while (true) {
			switch (Choices.getChoice("Select an option: ", choices)) {
			case 1:
				String nameEdit = Choices.getInput("Please write new name: ");
				tmp2.setName(nameEdit);
				break;
			case 2:
				String codeEdit = Choices.getInput("Enter new code: ");
				tmp2.setCode(codeEdit);
				break;
			case 3:
				String descriptionEdit = Choices.getInput("Write some description of the room: ");
				tmp2.setDescription(descriptionEdit);
				break;
			case 4:
				return;
			}
		}
	}

	/**
	 * Method to delete by id
	 */
	public void deleteMeetingCentre(String input) {
		// TODO: doplnit vymazani meeting centra a jeho mistnosti a vypsani
		// potvrzeni o smazani
		String confirmation = Choices.getInput("Are you sure? (YES/NO): ");
		if (confirmation.equals("YES")) {
			Iterator<MeetingCentre> i = meetingCentres.iterator();
			while (i.hasNext()) {
				MeetingCentre mc = i.next();
				if (mc.getCode().equals(input)) {
					i.remove();
					System.out.println("Meeting Centre was successfully removed");
				}
			}
		}
		else {
			return;
		}
	}

	/**
	 * Method to get all data to save in string format
	 * 
	 * @return
	 */
	public List<MeetingCentre> toSaveString() {
		return meetingCentres;
	}

	public List<MeetingCentre> getMeetingCentres() {
		return meetingCentres;
	}

	public void setMeetingCentres(List<MeetingCentre> meetingCentres) {
		this.meetingCentres = meetingCentres;
	}
}
