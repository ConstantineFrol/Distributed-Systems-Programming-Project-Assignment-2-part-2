public class Person implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	String name;
	String type;
	String superPower;

	public Person() {
	}

	public Person(String name, String type, String power) {
		setName(name);
		setType(type);
		setSuperPower(power);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getSuperPower() {
		return superPower;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSuperPower(String superPower) {
		this.superPower = superPower;
	}

	public String toString() {
		return "\nPerson:" + "\nName:\t" + getName() + "\nType:\t" + getType() + "\nPower\t" + getSuperPower();
	}
}