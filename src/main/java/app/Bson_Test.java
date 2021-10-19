package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import dao.ItemDao;
import entity.Battery;
import entity.Category;
import entity.Items;

public class Bson_Test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		MongoClient client = MongoClients.create();
		ItemDao itemDao = new ItemDao(client);
		List<Items> list = new ArrayList<Items>();
		
		System.out.println("=====Câu a:");
		list = itemDao.listItemsByPrice(1.0, 100.0);
		for (Items items : list) {
			System.out.println(items);
//			System.out.println("xxx");
		}
		System.out.println("=====Câu b:");
//		List<String> list_test = new ArrayList<String>();
//		list_test.add("abc");
//		Battery battery = new Battery("abc", "abc", "abc", "abc");
//		Items item = new Items(battery,list_test,10.0,"abc","abc","abc","abcx","abc",new Category("Timex30"));
//		itemDao.addItems(item);
		System.out.println("=====Câu b insertMany:");
		List<String> list_test = new ArrayList<String>();
		list_test.add("List test");
		Battery battery = new Battery("abc", "abc", "abc", "abc");
		Items item1 = new Items(battery,list_test,10.0,"abc","abc","abc","1","abc",new Category("Timex30"));
		Items item2 = new Items(battery,list_test,10.0,"abc","abc","abc","2","abc",new Category("Timex30"));
		Items item3 = new Items(battery,list_test,10.0,"abc","abc","abc","3","abc",new Category("Timex30"));
		Items item4 = new Items(battery,list_test,10.0,"abc","abc","abc","4","abc",new Category("Timex30"));
		List<Items> insert_List = new ArrayList<Items>();
		insert_List.add(item1);
		insert_List.add(item2);
		insert_List.add(item3);
		insert_List.add(item4);
		boolean rs1 = itemDao.insertMany(insert_List);
		System.out.println(rs1);
//		itemDao.addItems(item);
		
		System.out.println("=====Câu c:");
		Map<Category, Integer> map = new HashMap<Category, Integer>();
		map = itemDao.getNumberItemsByCategory();
		System.out.println(map);
		
		System.out.println("=====Câu d:");
		itemDao.SetTextIndexForTextSearch(); 
		list = itemDao.getItems("Quartz movement");
		for (Items itemss : list) {
			System.out.println(itemss);
		}
		System.out.println("=====Câu e:");
		Boolean rs = itemDao.updateManufactureBattery("V3/01", new Battery("test3", "test3", "", "test3"));
		if(rs==true) System.out.println("Đã cập nhật");
		else System.out.println("Không tìm thấy document phù hợp");
		
		System.out.println("=====Câu f:");
		list = itemDao.getItemsByManufacturer();
		for (Items itemss : list) {
			System.out.println(itemss);
		}

	}

}
