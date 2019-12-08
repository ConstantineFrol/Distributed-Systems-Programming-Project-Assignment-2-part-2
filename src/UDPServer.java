import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;


public class UDPServer {
	
	private enum goodGuysNames {
		SUPERMAN, BATMAN, SPIDER_MAN, THOR, CAPTAIN_AMERICA, WONDER_WOMAN, HULK;

		public static goodGuysNames getRandomNames() {
			Random rand = new Random();
			return values()[rand.nextInt(values().length)];
		}
	}
	
	DatagramSocket socket = null;
	ArrayList<Person> receiveQueue = new ArrayList<Person>();
	int count;
	Object object;
	Person thing;
	private ObjectOutputStream outputToFile;
	public UDPServer() {

	}

	public void createAndListenSocket() {
		System.out.println("\nServer is running\n");
		try {
			socket = new DatagramSocket(9876);
			// Expecting get size of package 200 bytes, not more
			byte[] incomingData = new byte[200];

			while (true) {
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);
				byte[] evilData = incomingPacket.getData();
				ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(evilData));
				try {
					thing = (Person) is.readObject();
					System.out.println("\nVillain object received from client\t#" + ++count + ":\n" + thing.toString());
					System.out.println("Package size is:\t" + incomingPacket.getLength());
					// Write to the file
					outputToFile = new ObjectOutputStream (new FileOutputStream(thing.getName() + ".dat", true));
					outputToFile.writeObject(object);
					System.out.println(thing.getName() + "'s object is stored\n");
					
					// Tidy up
					if (outputToFile != null) {
						outputToFile.close();
					}
					if (is != null) {
						is.close();
					}
					
					// Create Hero Object
					if (thing.getType().equalsIgnoreCase("villain")) {
						SuperThing evilPerson = new SuperThing();
						Person person = evilPerson.createThing("hero");
						person.setName(goodGuysNames.getRandomNames().toString());
						person.setType("hero");
						if (thing.getSuperPower().equals("strong person")) {
							person.setSuperPower("strong person");
						} else {
							person.setSuperPower("flying person");
						}
						System.out.println("Hero has bean created !!!\n" + person.toString());
						
						// Sending Hero Back to Client
						InetAddress IPAddress = incomingPacket.getAddress();
						int port = incomingPacket.getPort();
						
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ObjectOutputStream toClient = new ObjectOutputStream(outputStream);
						
						toClient.writeObject(person);					  
						byte[] objectData = outputStream.toByteArray();
						
						DatagramPacket sendPacket = new DatagramPacket(objectData, objectData.length, IPAddress, port);
						socket.send(sendPacket);
						
						System.out.println(
								"\nHero has been sent to the Client:\t#" + 
								count + "\nPackage size is:\t" + 
								objectData.length
						);// Message to Client
						
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
								
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UDPServer server = new UDPServer();
		server.createAndListenSocket();
	}

}
