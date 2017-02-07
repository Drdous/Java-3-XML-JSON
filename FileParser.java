package cz.unicorncollege.bt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.controller.MeetingController;
import cz.unicorncollege.controller.ReservationController;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileParser {

	/**
	 * Method to import data from the chosen file.
	 */
	public static List<MeetingCentre> importData() throws IOException {

		String locationFilter = Choices.getInput("Enter path of imported file: ");

		List<MeetingCentre> allMeetingCentres = new ArrayList<>();
		try {
			DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docBF.newDocumentBuilder();
			Document doc = docBuild.parse("Data.xml");
			doc.getDocumentElement().normalize();

			NodeList nodeListOfMC = doc.getElementsByTagName("meetingCenter");

			for (int i = 0; i < nodeListOfMC.getLength(); i++) {

				Node nodeOfMC = nodeListOfMC.item(i);
				Element elementOfMC = (Element) nodeOfMC;

				allMeetingCentres
						.add(new MeetingCentre(elementOfMC.getElementsByTagName("name").item(0).getTextContent(),
								elementOfMC.getElementsByTagName("code").item(0).getTextContent(),
								elementOfMC.getElementsByTagName("description").item(0).getTextContent()));

				NodeList nodeListOfMRChildNodes = elementOfMC.getElementsByTagName("meetingRoom");

				for (int j = 0; j < nodeListOfMRChildNodes.getLength(); j++) {
					Node nodeOfMR = nodeListOfMRChildNodes.item(j);
					Element elementOfMR = (Element) nodeOfMR;

					allMeetingCentres.get(allMeetingCentres.size() - 1)
							.addMeetingRooms(new MeetingRoom(
									elementOfMR.getElementsByTagName("name").item(0).getTextContent(),
									elementOfMR.getElementsByTagName("code").item(0).getTextContent(),
									elementOfMR.getElementsByTagName("description").item(0).getTextContent(),
									Integer.parseInt(
											elementOfMR.getElementsByTagName("capacity").item(0).getTextContent()),
									elementOfMR.getElementsByTagName("hasVideoConference").item(0).getTextContent()
											.equals("YES"),
									allMeetingCentres.get(allMeetingCentres.size() - 1)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
		System.out.println("**************************************************");
		System.out.println("Data was imported. " + allMeetingCentres.size() + " objects of MeetingCentres was loaded");
		System.out.println("**************************************************");
		System.out.println();

		return allMeetingCentres;
	}

	/**
	 * Method to save the data to file.
	 */
	public static void saveData(List<MeetingCentre> allMeetingCenters) {
		// TODO: ulozeni dat do XML souboru, jmeno souboru muze byt natvrdo,
		// adresar stejny jako se nachazi aplikace

		DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = null;
		try {
			docBuild = docBF.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		assert docBuild != null;
		Document doc = docBuild.newDocument();

		Element rootElement = doc.createElement("xa03");
		doc.appendChild(rootElement);

		Element meetingCenters = doc.createElement("meetingCenters");
		rootElement.appendChild(meetingCenters);

		for (MeetingCentre mc : allMeetingCenters) {
			Element meetingCenter = doc.createElement("meetingCenter");
			meetingCenters.appendChild(meetingCenter);

			Element mcName = doc.createElement("name");
			mcName.appendChild(doc.createTextNode(mc.getName()));
			meetingCenter.appendChild(mcName);

			Element mcCode = doc.createElement("code");
			mcCode.appendChild(doc.createTextNode(mc.getCode()));
			meetingCenter.appendChild(mcCode);

			Element mcDescription = doc.createElement("description");
			mcDescription.appendChild(doc.createTextNode(mc.getDescription()));
			meetingCenter.appendChild(mcDescription);

			if (!mc.getMeetingRooms().isEmpty()) {
				Element mcRooms = doc.createElement("meetingRooms");
				meetingCenter.appendChild(mcRooms);

				for (MeetingRoom mr : mc.getMeetingRooms()) {
					Element mcRoom = doc.createElement("meetingRoom");
					mcRooms.appendChild(mcRoom);

					Element mrName = doc.createElement("name");
					mrName.appendChild(doc.createTextNode(mr.getName()));
					mcRoom.appendChild(mrName);

					Element mrCode = doc.createElement("code");
					mrCode.appendChild(doc.createTextNode(mr.getCode()));
					mcRoom.appendChild(mrCode);

					Element mrDescription = doc.createElement("description");
					mrDescription.appendChild(doc.createTextNode(mr.getDescription()));
					mcRoom.appendChild(mrDescription);

					Element mrCapacity = doc.createElement("capacity");
					mrCapacity.appendChild(doc.createTextNode(Integer.toString(mr.getCapacity())));
					mcRoom.appendChild(mrCapacity);

					Element mrVideoConference = doc.createElement("hasVideoConference");
					mrVideoConference.appendChild(doc.createTextNode(mr.isHasVideoConference() ? "YES" : "NO"));
					mcRoom.appendChild(mrVideoConference);
				}
			}
		}

		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource domSource = new DOMSource(doc);
			StreamResult streamResult = new StreamResult(new File("Data.xml"));

			t.transform(domSource, streamResult);
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		System.out.println();

		System.out.println("**************************************************");
		System.out.println("Data was saved correctly.");
		System.out.println("**************************************************");

		System.out.println();
	}

	/**
	 * Method to export data to JSON file
	 * 
	 * @param controllReservation
	 *            Object of reservation controller to get all reservation and
	 *            other data if needed
	 */
	public static void exportDataToJSON(List<MeetingCentre> meetingCentres) {
		// TODO: ulozeni dat do souboru ve formatu JSON

		String locationFilter = Choices.getInput("Enter name of the file for export: ");
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		JsonObjectBuilder jCentre = Json.createObjectBuilder();
		JsonArrayBuilder jCentreArray = Json.createArrayBuilder();

		Map<String, JsonArrayBuilder> jhash = new HashMap<>();
		Collections.sort(meetingCentres, new Comparator<MeetingCentre>() {

			@Override
			public int compare(MeetingCentre o1, MeetingCentre o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});

		for (MeetingCentre mc : meetingCentres) {
			mc.roomSort();
			for (MeetingRoom mr : mc.getMeetingRooms()) {
				mr.resSort();
				for (Reservation res : mr.getReservations()) {
					jhash.putIfAbsent(df.format(res.getDate()), Json.createArrayBuilder());
					jhash.put(df.format(res.getDate()), jhash.get(df.format(res.getDate())).add(Json
							.createObjectBuilder().add("from", res.getTimeFrom()).add("to", res.getTimeTo())
							.add("expectedCount", res.getExpectedPersonCount()).add("customer", res.getCustomer())
							.add("videoConference", res.isNeedVideoConference()).add("note", res.getNote()).build()));
				}

				JsonObjectBuilder reserve = Json.createObjectBuilder();

				for (Map.Entry<String, JsonArrayBuilder> e : jhash.entrySet()) {
					assert reserve != null;
					reserve.add(e.getKey(), e.getValue());
				}

				assert reserve != null;
				JsonObject mCentre = Json.createObjectBuilder().add("meetingCentre", mc.getCode())
						.add("meetingRoom", mr.getCode()).add("reservations", reserve.build()).build();
				jCentreArray.add(mCentre);
			}
		}

		JsonObject jmr = jCentre.add("schema", "PLUS4U.EBC.MCS.MeetingRoom_Schedule_1.0")
				.add("uri", "ues:UCL-BT:UCL.INF/DEMO_REZERVACE:EBC.MCS.DEMO/MR001/SCHEDULE")
				.add("data", jCentreArray.build()).build();

		try (FileWriter fw = new FileWriter(locationFilter)) {
			JsonWriter jWriter = Json.createWriter(fw);
			jWriter.writeObject(jmr);
			jWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();

		if (locationFilter != null) {
			System.out.println("**************************************************");
			System.out.println("Data was exported correctly. The file is here: " + locationFilter);
			System.out.println("**************************************************");
		} else {
			System.out.println("**************************************************");
			System.out.println("Something terrible happend during exporting!");
			System.out.println("**************************************************");
		}
		System.out.println();
	}

	/**
	 * Method to load the data from file.
	 * 
	 * @return
	 */
	public static List<MeetingCentre> loadDataFromFile() {
		// TODO: nacist data z XML souboru

		List<MeetingCentre> allMeetingCentres = new ArrayList<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse("Data.xml");
			doc.getDocumentElement().normalize();

			NodeList nodeListOfMC = doc.getElementsByTagName("meetingCenter");

			for (int i = 0; i < nodeListOfMC.getLength(); i++) {

				Node nodeOfMC = nodeListOfMC.item(i);
				Element elementOfMC = (Element) nodeOfMC;

				allMeetingCentres
						.add(new MeetingCentre(elementOfMC.getElementsByTagName("name").item(0).getTextContent(),
								elementOfMC.getElementsByTagName("code").item(0).getTextContent(),
								elementOfMC.getElementsByTagName("description").item(0).getTextContent()));

				NodeList nodeListOfMRChildNodes = elementOfMC.getElementsByTagName("meetingRoom");

				for (int j = 0; j < nodeListOfMRChildNodes.getLength(); j++) {
					Node nodeOfMR = nodeListOfMRChildNodes.item(j);
					Element elementOfMR = (Element) nodeOfMR;

					allMeetingCentres.get(allMeetingCentres.size() - 1)
							.addMeetingRooms(new MeetingRoom(
									elementOfMR.getElementsByTagName("name").item(0).getTextContent(),
									elementOfMR.getElementsByTagName("code").item(0).getTextContent(),
									elementOfMR.getElementsByTagName("description").item(0).getTextContent(),
									Integer.parseInt(
											elementOfMR.getElementsByTagName("capacity").item(0).getTextContent()),
									elementOfMR.getElementsByTagName("hasVideoConference").item(0).getTextContent()
											.equals("YES"),
									allMeetingCentres.get(allMeetingCentres.size() - 1)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
		System.out.println("**************************************************");
		System.out.println("Data was loaded correctly.");
		System.out.println("**************************************************");
		System.out.println();

		return allMeetingCentres;
	}
}
