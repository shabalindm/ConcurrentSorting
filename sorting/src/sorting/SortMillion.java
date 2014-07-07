package sorting;

import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


/**Многопоточная сортировка массива строк методом QuickSort*/
public class SortMillion {
	/**Максимальный размер блока. Блоки меньшие этого размера сортируются в один поток библиотечым методом  Arrays.sort
	 * Блоки большего размера разделяются на подблоки согласно алгоритму QuickSort */
	public int blockSize = 10000;
	
	/**Генератор случайных чисел*/
	private Random rand = new Random();
	
	/**Очередь, в которую будут добавляться потоки при запуске и удаляться закончившие работу потоки.
	 *  Как только все потоки закончат работу и будут удалены из очерди, сортировка будет закончена*/
	private Queue<Thread> threads = new ConcurrentLinkedQueue<>();
	
	/**Запускает сортировку массива  методом QuickSort в отдельном потоке
	 * @param  data  - сортируемый массив строк
	 * @param  start  - начало участка сортировки (включительно)
	 * @param  end  - конец участка сортировки (не включительно)*/
	private void doSort(final String[] data, final int start, final int end){
		// создаем новый тред
		Thread t = new Thread(){
			@Override
			public void run() {
				if(end <= start+1)
					return;		
				if( end - start <= blockSize)
					Arrays.sort(data, start, end);
				else{
					int div = Partition(data, start, end);
					doSort(data, start, div);
					doSort(data, div, end);
				} 
			}			
		};
		
		//добавляем его в очередь из тредов и запускаем
		threads.add(t);
		t.start();
		
	}
	
	/**Осущетвляет сортировку  массива  методом QuickSort в одном потоке
	 * Алгоритм идентичен методу doSort, но работает в один поток. 
	 * Сделан для определения эффективности алгоритма сортировки по сравнению 
	 * с библиотечным методом Arrays.sort
	 * @param  data  - сортируемый массив строк
	 * @param  start  - начало участка сортировки (включительно)
	 * @param  end  - конец участка сортировки (не включительно)*/
	public void singleThreadSort( String[] data,  int start,  int end){
		if(end <= start+1)
			return;		
		if( end - start <= blockSize)
			Arrays.sort(data, start, end);
		else{
			int div = Partition(data, start, end);
			singleThreadSort(data, start, div);
			singleThreadSort(data, div, end);
		} 

	}

	/**Осуществялет сортировку массива data в много потоков*/
	public void QuickSort(String[] data){
		// запускаем много поточную сортировку
		doSort(data, 0, data.length);
		
		while(!threads.isEmpty()){
			Thread t = threads.remove(); // Берем и очереди первый попавший в нее тред
			try {
				t.join(); // ждем, пока он завершится
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Все потоки, осущетвляющие сортировку попадают в очередь threads. Согласно алгоритму, новые потоки могут
		//возбужадаются только из других потоков(кроме самого первого).
		//Поэтому, когда в очереди не останется работающих тредов, сортировка будет завершена
		
	}

/** Вибирает в качестве разделителя (pivot) случайный элемент из массива (части массива). 
 * Все элементы, большие  pivot перемещаются в правую часть массива, меньшие pivot - в левую часть.
 *  Элементы, строго равные pivot меняются местами. Возращает положение разделителя*/
	private int Partition(String[] data, int start, int end) {
		String pivot = data[start + rand.nextInt(end - start)];
		int leftCur = start-1; 
		int rightCur = end;
		
		while(leftCur <  rightCur){
			while(data[++leftCur].compareTo(pivot) < 0);
			while(leftCur < rightCur  && data[--rightCur].compareTo(pivot)>0);	
			
			String tmp = data[leftCur];
			data[leftCur] = data[rightCur];
			data[rightCur] = tmp;			
		}
		
		return leftCur;
	}
	
	
}
