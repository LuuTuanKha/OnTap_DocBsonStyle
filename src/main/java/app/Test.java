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

public class Test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		MongoClient client = MongoClients.create();
		ItemDao itemDao = new ItemDao(client);
		List<Items> list = new ArrayList<Items>();
		
		System.out.println("=====Câu a:");
		list = itemDao.listItemsByPrice(1.0, 100.0);
		for (Items items : list) {
			System.out.println(items);
		}
		System.out.println("=====Câu b:");
//		List<String> list_test = new ArrayList<String>();
//		list_test.add("abc");
//		Battery battery = new Battery("abc", "abc", "abc", "abc");
//		Items item = new Items(battery,list_test,10.0,"abc","abc","abc","abcx","abc",new Category("Timex30"));
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
