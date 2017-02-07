package cz.unicorncollege.bt.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MeetingCentre extends MeetingObject {
	private List<MeetingRoom> meetingRooms;

	public MeetingCentre(String name, String code, String description) {
		super(name, code, description);
		this.meetingRooms = new ArrayList<>();
	}

	public List<MeetingRoom> getMeetingRooms() {
		return meetingRooms;
	}

	@Override
	public String toString() {
		return "Meeting centre " + this.name + " with code: " + this.code + " and description: " + this.description;
	}

	public void setMeetingRooms(List<MeetingRoom> meetingRooms) {
		this.meetingRooms = meetingRooms;
	}

	public void addMeetingRooms(MeetingRoom meetingRoom) {
		this.meetingRooms.add(meetingRoom);
	}

	public String toCSV() {
		return this.name + "," + this.code + "," + this.description + ",,," + "\n";
	}

	public void roomSort() {
		Collections.sort(meetingRooms, new Comparator<MeetingRoom>() {
			@Override
			public int compare(MeetingRoom c1, MeetingRoom c2) {
				return c1.getCode().compareTo(c2.getCode());
			}
		});
	}
}
