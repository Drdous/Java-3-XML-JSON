package cz.unicorncollege.bt.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MeetingRoom extends MeetingObject {
	private int capacity;
	private boolean hasVideoConference;
	private MeetingCentre meetingCentre;
	private List<Reservation> reservations;

	public MeetingRoom(String name, String code, String description, int capacity, boolean hasVideoConference,
			MeetingCentre meetingCentre) {
		super(name, code, description);
		this.capacity = capacity;
		this.hasVideoConference = hasVideoConference;
		this.meetingCentre = meetingCentre;
		this.reservations = new ArrayList<>();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isHasVideoConference() {
		return hasVideoConference;
	}

	public void setHasVideoConference(boolean hasVideoConference) {
		this.hasVideoConference = hasVideoConference;
	}

	public MeetingCentre getMeetingCentre() {
		return meetingCentre;
	}

	public void setMeetingCentre(MeetingCentre meetingCentre) {
		this.meetingCentre = meetingCentre;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public List<Reservation> getSortedReservationsByDate(Date getSortedReservationsByDate) {
		// TODO get reservations by date and return sorted reservations by hours
		List<Reservation> rezervace = new ArrayList<>();
		for (Reservation r : reservations) {
			if (r.getDate().equals(getSortedReservationsByDate)) {
				rezervace.add(r);
			}
		}
		// Collections.sort(rezervace, (a, b) -> a.getTimeFrom() <
		// b.getTimeFrom() ? -1 : 1);
		Collections.sort(rezervace, (a, b) -> (a.getTimeInMilliSeconds() < b.getTimeInMilliSeconds()) ? -1 : 1);
		return rezervace;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public void addReservation(Reservation rez) {
		this.reservations.add(rez);
	}

	public void removeReservation(int index) {
		this.reservations.remove(index);
	}

	public void resSort() {
		Collections.sort(reservations, new Comparator<Reservation>() {

			@Override
			public int compare(Reservation d1, Reservation d2) {
				int r = d1.getDate().compareTo(d2.getDate());

				if (r < 0) {
					return -1;
				} else if (r == 0) {
					String tf1 = d1.getTimeFrom();
					String tf2 = d2.getTimeFrom();

					String[] p1 = tf1.split(":");
					int t1 = Integer.parseInt(p1[0]);
					int t2 = Integer.parseInt(p1[1]);
					int result = (t1 * 60) + (t2);

					String[] p2 = tf2.split(":");
					int ti1 = Integer.parseInt(p2[0]);
					int ti2 = Integer.parseInt(p2[1]);
					int result2 = (ti1 * 60) + (ti2);

					return result - result2;
				} else {
					return 1;
				}
			}
		});
	}

}
