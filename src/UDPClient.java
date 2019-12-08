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
import java.net.UnknownHostException;
import java.util.Random;

public class UDPClient {
	
	enum badGuysNames {
		MAGNETO, JOKER, KINGPIN, LOKI, MYSTIQUE, VENOM, SHREDDER, DOCTOR_DOOM, NORMAN_OSBORN, RED_SKULL, GALACTUS,
		ULTRON, THANOS, APOCALYPSE, DOCTOR_OCTAPUS, ANNIHILUS, JUGGERNAUT;

		public static badGuysNames getRandomNames() {
			Random rand = new Random();
			return values()[rand.nextInt(values().length)];
		}
	}

	public static class Consumer implements  Runnable {
		
	// Enum Generate random names
	

	DatagramSocket Socket;
//	private ObjectOutputStream outputToFile;
	Object object;
	Person hero;
	private ObjectOutputStream outputToFile;

	public void run() {
	
		try {

			// Setup Connection And packet data size
			Socket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName("localhost");
//			byte[] incomingData = new byte[200];

			// Create Object of Villain
			SuperThing evilPerson = new SuperThing();
			Person thing = evilPerson.createThing("villain");

			thing.setName(badGuysNames.getRandomNames().toString());
			thing.setType("villain");

			int random = (int) (Math.random() * 2);
			if (random == 1) {
				thing.setSuperPower("strong person");
			} else {
				thing.setSuperPower("flying person");
			}

			System.out.println(
					"Villain created:\n" + 
					thing.toString() + 
					"\nSending villain to the server using UDP\n"
			);// Display message

			// Sending Villain to the server
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream toServer = new ObjectOutputStream(byteOutputStream);
			toServer.writeObject(thing);
			toServer.flush();
			byte[] evilData = byteOutputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(evilData, evilData.length, IPAddress, 9876);
			Socket.send(sendPacket);
			System.out.println("\nVillain has been sent to the server");// Display message
			
			
			// Receiving Message from Server
			byte[] incomingData = new byte[200];
			DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
			Socket.receive(incomingPacket);
			byte[] HeroData = incomingPacket.getData();
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(HeroData);
			ObjectInputStream fromServer = new ObjectInputStream(byteInputStream);
			
			
			try {
				hero = (Person) fromServer.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("\nHero object received from Server:\n" + hero.toString());
			System.out.println("Incoming packet size:\t" + incomingData.length);
			
			// Write to the file
			outputToFile = new ObjectOutputStream (new FileOutputStream(hero.getName() + ".dat", true));
			outputToFile.writeObject(object);
			System.out.println(hero.getName() + "'s object is stored\n");
				
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		}
	}
	public static void main(String[] args) {
		
		Thread client1 = new Thread(new Consumer());
		client1.start();
		
	}
}


