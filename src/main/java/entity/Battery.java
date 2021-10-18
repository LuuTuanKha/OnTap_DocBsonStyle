package entity;

public class Battery {

	private String voltage;
	private String technology;
	private String serial;
	private String manufacture;

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public Battery(String voltage, String technology, String serial, String manufacture) {
		super();
		this.voltage = voltage;
		this.technology = technology;
		this.serial = serial;
		this.manufacture = manufacture;
	}

	public Battery() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Battery [voltage=" + voltage + ", technology=" + technology + ", serial=" + serial + ", manufacture="
				+ manufacture + "]";
	}
	
}
