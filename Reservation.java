package cz.unicorncollege.bt.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
	private MeetingRoom meetingRoom;
	private Date date;
	private String timeFrom;
	private String timeTo;
	private int expectedPersonCount;
	private String customer;
	private boolean needVideoConference;
	private String note;

	public Reservation(MeetingRoom meetingRoom, Date date, String timeFrom, String timeTo, int expectedPersonCount,
			String customer, boolean needVideoConference, String note) {
		super();
		this.meetingRoom = meetingRoom;
		this.date = date;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.expectedPersonCount = expectedPersonCount;
		this.customer = customer;
		this.needVideoConference = needVideoConference;
		this.note = note;
	}

	public long getTimeInMilliSeconds() {
		DateFormat df = new SimpleDateFormat("hh:mm");
		long a = 0;
		try {
			a = df.parse(timeFrom).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return a;
	}

	public boolean crashControll(String from, String to) throws ParseException {
		DateFormat df = new SimpleDateFormat("hh:mm");
		long f = df.parse(from).getTime();
		long t = df.parse(to).getTime();
		long tf = df.parse(timeFrom).getTime();
		long tt = df.parse(timeTo).getTime();

		if (f < tf && t > tf) { // nejaky cas OD je mezi zadanym casem
			return true;
		}
		if (f > tf && t < tt) { // zadany cas je uvnitr
			return true;
		}
		if (f > tf && t > tt) { // nejaky cas DO je mezi zadanym casem
			return true;
		}
		if (f < tf && t > tt) { // nejaka rezervace je uvnitr zadaneho casu
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Date: " + date + ", time from: " + timeFrom + ", time to: "
				+ timeTo + ", person count: " + expectedPersonCount + ", customer: " + customer
				+ ", need video conference: " + needVideoConference + ", note: " + note;
	}

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public void setMeetingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}

	public Date getDate() {
		return date;
	}

	public String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public int getExpectedPersonCount() {
		return expectedPersonCount;
	}

	public void setExpectedPersonCount(int expectedPersonCount) {
		this.expectedPersonCount = expectedPersonCount;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public boolean isNeedVideoConference() {
		return needVideoConference;
	}

	public void setNeedVideoConference(boolean needVideoConference) {
		this.needVideoConference = needVideoConference;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
