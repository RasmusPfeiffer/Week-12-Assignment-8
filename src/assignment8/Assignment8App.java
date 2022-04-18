package assignment8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Assignment8App {

	public static void main(String[] args) {

		Assignment8 oAssignment = new Assignment8();
		List<Integer> iNumbersList = Collections.synchronizedList(new ArrayList<>(1000));
		ExecutorService oExecutor = Executors.newCachedThreadPool();
		List<CompletableFuture<Void>> lTasks = new ArrayList<>(1000);
		
		for (int i = 0; i < 1000; i++) {
			CompletableFuture<Void> oTask = CompletableFuture.supplyAsync(() -> oAssignment.getNumbers(), oExecutor)
									  .thenAccept(numbers -> iNumbersList.addAll(numbers));
			lTasks.add(oTask);
		}

        while (lTasks.stream().filter(CompletableFuture::isDone).count() < 1000)

        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Done");
        System.out.println(iNumbersList.size());
        
        Map<Integer, Integer> mFinalResults = iNumbersList.stream()
                .collect(Collectors.toMap(i -> i, i -> 1, (oldValue, newValue) -> oldValue + 1));
        System.out.println(mFinalResults);
	}

}
