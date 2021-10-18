package entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Category {
	@BsonId
	@BsonProperty("_id")
	private String _id;
	@BsonProperty("name")
	private String name;
	@BsonProperty("description")
	private String description;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public Category(String _id, String name, String description) {
		super();
		this._id = _id;
		this.name = name;
		this.description = description;
	}
	public Category() {
		// TODO Auto-generated constructor stub
	}
	
	public Category(String _id) {
		super();
		this._id = _id;
	}
	@Override
	public String toString() {
		return "Category [_id=" + _id + ", name=" + name + ", description=" + description + "]";
	}
	

}
