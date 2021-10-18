package entity;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Items {
	
	@BsonId
	private String id;
	private Battery battery;
	
	private List<String> measuringModes;
	
	private Double price;
	
	private String manufacturer;
	
	private String strapType;
	
	private String size;
	

	
	private String description;

	private Category category;
	

	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Battery getBattery() {
		return battery;
	}
	public void setBattery(Battery battery) {
		this.battery = battery;
	}
	public List<String> getMeasuringModes() {
		return measuringModes;
	}
	public void setMeasuringModes(List<String> measuringModes) {
		this.measuringModes = measuringModes;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getStrapType() {
		return strapType;
	}
	public void setStrapType(String strapType) {
		this.strapType = strapType;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Items() {
		// TODO Auto-generated constructor stub
	}

	public Items(Battery battery, List<String> measuringModes, Double price, String manufacturer, String strapType,
			String size, String id, String description, Category category) {
		super();
		this.battery = battery;
		this.measuringModes = measuringModes;
		this.price = price;
		this.manufacturer = manufacturer;
		this.strapType = strapType;
		this.size = size;
		this.id = id;
		this.description = description;
		this.category = category;
	}
	@Override
	public String toString() {
		return "Items [battery=" + battery + ", measuringModes=" + measuringModes + ", price=" + price
				+ ", manufacturer=" + manufacturer + ", strapType=" + strapType + ", size=" + size + ", id=" + id
				+ ", description=" + description + ", categoryId=" + category.get_id() + "]";
	}
	
	
	
	
	
}
