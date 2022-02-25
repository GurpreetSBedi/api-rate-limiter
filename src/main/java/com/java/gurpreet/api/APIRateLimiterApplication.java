package com.java.gurpreet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;

@SpringBootApplication
public class APIRateLimiterApplication {

	private static SlidingWindowCounterRateLimiter rateLimiter = new SlidingWindowCounterRateLimiter();
	public static void main(String[] args) {

		SpringApplication.run(APIRateLimiterApplication.class, args);
		add_APIRateLimiter();
		//IntStream.range(0,10).forEach((exs)->invoke_APIRateLimiter("g23"));
		/*ExecutorService executorService = Executors.newFixedThreadPool(3);
		FutureTask<?> task = new FutureTask<Boolean>(()-> invoke_APIRateLimiter("gur123"));
		FutureTask<?> task2 = new FutureTask<Boolean>(()-> invoke_APIRateLimiter("gurp23"));
		FutureTask<?> task3 = new FutureTask<Boolean>(()-> invoke_APIRateLimiter("g23"));
		FutureTask<?> task4 = new FutureTask<Boolean>(()-> invoke_APIRateLimiter("2999"));
		executorService.execute(task);
		executorService.execute(task2);
		executorService.execute(task3);
		executorService.execute(task4);*/

		//IntStream.range(0,10).forEach((exs)->invoke_APIRateLimiter("2999"));

		CompletableFuture<Boolean> future = new CompletableFuture<>();
		future.completeAsync(()-> invoke_APIRateLimiter("gur123"))
				.completeAsync(()-> invoke_APIRateLimiter("g23"))
				.completeAsync(()-> invoke_APIRateLimiter("gurp23"))
				.completeAsync(()-> invoke_APIRateLimiter("2999")).isDone();

		future.completeAsync(()-> invoke_APIRateLimiter("gur123"))
				.completeAsync(()-> invoke_APIRateLimiter("g23"))
				.completeAsync(()-> invoke_APIRateLimiter("gurp23"))
				.completeAsync(()-> invoke_APIRateLimiter("2999")).isDone();
		try {
			System.out.println(future.get());
		}catch(InterruptedException | ExecutionException ex){

		}

	}

	public static void add_APIRateLimiter(){
		//SlidingWindowCounterRateLimiter rateLimiter = new SlidingWindowCounterRateLimiter();
		rateLimiter.addUser("gur123",5,Licenses.LOW.getLicense());
		rateLimiter.addUser("gurp23",5,Licenses.MEDIUM.getLicense());
		rateLimiter.addUser("g23",5,Licenses.HIGH.getLicense());
		rateLimiter.addUser("2999",5,Licenses.LOW.getLicense());
	}

	public static boolean invoke_APIRateLimiter(String userId){
		boolean allowed = rateLimiter.shouldAllowServiceCall(userId);
		System.out.println(userId+" request allowed : "+allowed);
		return allowed;
	}

	public static void get() throws InterruptedException {
		//Future<?> fTask = new CompletableFuture<>();
		//ExecutorService executorService = new
		CompletionService service = new ExecutorCompletionService<Boolean>(Executors.newFixedThreadPool(5));
		service.poll(20000,TimeUnit.valueOf("20000"));
	}


}
