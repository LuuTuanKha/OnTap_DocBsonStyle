package dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Category;

public class CategoryDao extends AbstractDao {
	private MongoCollection<Category> categoryCollec;

	public CategoryDao(MongoClient client) {
		super(client);
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		categoryCollec = db.getCollection("categories", Category.class).withCodecRegistry(pojoCodecRegistry);
	}

	public Category getCategory(String id) throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);

		AtomicReference<Category> category = new AtomicReference<Category>();
		
		categoryCollec.find(Filters.eq("_id", id)).subscribe(new Subscriber<Category>() {
			Subscription s;
			@Override
			public void onSubscribe(Subscription s) {
				this.s=s;
				
				this.s.request(1);
			}

			@Override
			public void onNext(Category t) {
//				System.out.println(t);
				t.set_id(id);
				category.set(t);
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
		return category.get();
	}

	
}
