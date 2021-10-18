package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Battery;
import entity.Category;
import entity.Items;

public class ItemDao extends AbstractDao {

	private MongoCollection<Document> itemCollection;
	private static final Gson GSON = new Gson();

	public ItemDao(MongoClient client) {
		// TODO Auto-generated constructor stub
		super(client);
		itemCollection = db.getCollection("items");

	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws InterruptedException
	 */

	public List<Items> listItemsByPrice(Double from, Double to) throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);
		itemCollection.createIndex(Indexes.ascending("price"));
		List<Items> list = new ArrayList<>();
		Document s1 = Document.parse("{$match:{price:{$gt:" + from + ",$lt:" + to + "}}}");
		itemCollection.aggregate(Arrays.asList(s1)).subscribe(new Subscriber<Document>() {
			Subscription s;

			@Override
			public void onSubscribe(Subscription s) {

				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Document t) {

//				System.out.println(t);
				String json = t.toJson();
//				System.out.println(json);
				Category category = new Category(t.getString("_id"));
				Items item = GSON.fromJson(json, Items.class);
				item.setId(t.getString("_id"));
				item.setCategory(category);
				System.out.println(item);
				this.s.request(1);
			}

			@Override
			public void onError(Throwable t) {

				t.printStackTrace();
			}

			@Override
			public void onComplete() {

				latch.countDown();
			}
		});
		latch.await();
		return list;
	}

	public boolean addItems(Items item) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		AtomicBoolean rs = new AtomicBoolean(false);
//		Parse object -> json
		String json = GSON.toJson(item);
		Document doc = Document.parse(json);

//		Tạo document từ category
		Document cateDoc = (Document) doc.get("category");
		// Vì document dùng _id nên cần xoá id và đổi thành _id
		doc.append("_id", doc.getString("id"));
		doc.remove("id");
		doc.remove("category");
		doc.append("categoryID", cateDoc.getString("id"));
		Publisher<InsertOneResult> pub = itemCollection.insertOne(doc);
		Subscriber<InsertOneResult> sub = new Subscriber<InsertOneResult>() {

			@Override
			public void onSubscribe(Subscription s) {
				s.request(1);
			}

			@Override
			public void onNext(InsertOneResult t) {
				System.out.println(t.getInsertedId() + " inserted!");
				if (t.getInsertedId() != null)
					rs.set(true);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
//				onComplete();
			}

			@Override
			public void onComplete() {
				System.out.println("Completed!");
				latch.countDown();
			}
		};

		pub.subscribe(sub);

		latch.await();

		return rs.get();
	}

	public Map<Category, Integer> getNumberItemsByCategory() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Map<Category, Integer> map = new HashMap<Category, Integer>();
		CategoryDao categoryDao = new CategoryDao(this.getClient());

		Document s1 = Document.parse("{$group:{_id:'$categoryID',sum:{$sum:1}}}");
		itemCollection.aggregate(Arrays.asList(s1)).subscribe(new Subscriber<Document>() {
			private Subscription s;

			@Override
			public void onSubscribe(Subscription s) {

				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Document t) {

				try {

					Category category = categoryDao.getCategory(t.getString("_id"));
//				
					map.put(category, t.getInteger("sum"));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				this.s.request(1);
			}

			@Override
			public void onError(Throwable t) {

				t.printStackTrace();
			}

			@Override
			public void onComplete() {

				latch.countDown();
			}
		});
		latch.await();
		return map;
	}

	public void SetTextIndexForTextSearch() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		itemCollection.createIndex(Indexes.compoundIndex(Indexes.text("description"), Indexes.text("measuringModes"),
				Indexes.text("manufacturer"))).subscribe(new Subscriber<String>() {
					private Subscription s;

					@Override
					public void onSubscribe(Subscription s) {

						this.s = s;
						this.s.request(1);
					}

					@Override
					public void onNext(String t) {

						this.s.request(1);

					}

					@Override
					public void onError(Throwable t) {

						t.printStackTrace();
					}

					@Override
					public void onComplete() {

						latch.countDown();

					}
				});
		latch.await();

	}

	public List<Items> getItems(String keywords) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		List<Items> list = new ArrayList<Items>();

		Document s = Document.parse("{$match:{$text:{$search:'" + keywords + "'}}}");
		itemCollection.aggregate(Arrays.asList(s)).subscribe(new Subscriber<Document>() {
			private Subscription s;

			@Override
			public void onSubscribe(Subscription s) {

				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Document t) {
//				System.out.println(t);
				String json = t.toJson();
//				System.out.println(json);
				Category category = new Category(t.getString("_id"));
				Items item = GSON.fromJson(json, Items.class);
				item.setId(t.getString("_id"));
				item.setCategory(category);
//				System.out.println(item);
				list.add(item);
				this.s.request(1);

			}

			@Override
			public void onError(Throwable t) {

				t.printStackTrace();
			}

			@Override
			public void onComplete() {

				latch.countDown();
			}
		});
		latch.await();

		return list;

	}

	public boolean updateManufactureBattery(String ItemID, Battery newBattery) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean rs = new AtomicBoolean(false);

		Document doc = new Document();
		Gson gson = new Gson();
		String json = gson.toJson(newBattery);
		Bson bson = doc.parse(json);
		itemCollection.updateOne(Filters.eq("_id", ItemID), new Document("$set", new Document("battery", bson)))
				.subscribe(new Subscriber<UpdateResult>() {
					Subscription s;

					@Override
					public void onSubscribe(Subscription s) {

						this.s = s;
						this.s.request(1);
					}

					@Override
					public void onNext(UpdateResult t) {
//				System.out.println(t.getMatchedCount() + " inserted!");
						if (t.getMatchedCount() != 0)
							rs.set(true);
					}

					@Override
					public void onError(Throwable t) {

						t.printStackTrace();
					}

					@Override
					public void onComplete() {

//				System.out.println("updated");
						latch.countDown();

					}

				});
		latch.await();
		return rs.get();

	}

	public List<Items> getItemsByManufacturer() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		List<Items> list = new ArrayList<Items>();
		Document q1 = Document.parse("{$group:{_id:'$manufacturer',fullDocument:{$push:'$$ROOT'}}}");
		Document q2 = Document.parse("{$match:{_id:'Sevenfriday'}}");
		Document q3 = Document.parse("{$unwind:'$fullDocument'}");
		Document q4 = Document.parse("{ $replaceRoot: { newRoot: \"$fullDocument\" } }");
		itemCollection.aggregate(Arrays.asList(q1, q2, q3, q4)).subscribe(new Subscriber<Document>() {
			Subscription s;

			@Override
			public void onSubscribe(Subscription s) {

				this.s = s;
				this.s.request(1);
			}

			@Override
			public void onNext(Document t) {
//				System.out.println(t);
				String json = t.toJson();
//				System.out.println(json);
				Category category = new Category(t.getString("_id"));
				Items item = GSON.fromJson(json, Items.class);
				item.setId(t.getString("_id"));
				item.setCategory(category);
//				System.out.println(item);
				list.add(item);
				this.s.request(1);
			}

			@Override
			public void onError(Throwable t) {

				t.printStackTrace();
			}

			@Override
			public void onComplete() {

				latch.countDown();
			}
		});
		latch.await();
		return list;

	}

}
