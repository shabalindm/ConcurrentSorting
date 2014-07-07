package sorting;

import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


/**������������� ���������� ������� ����� ������� QuickSort*/
public class SortMillion {
	/**������������ ������ �����. ����� ������� ����� ������� ����������� � ���� ����� ����������� �������  Arrays.sort
	 * ����� �������� ������� ����������� �� �������� �������� ��������� QuickSort */
	public int blockSize = 10000;
	
	/**��������� ��������� �����*/
	private Random rand = new Random();
	
	/**�������, � ������� ����� ����������� ������ ��� ������� � ��������� ����������� ������ ������.
	 *  ��� ������ ��� ������ �������� ������ � ����� ������� �� ������, ���������� ����� ���������*/
	private Queue<Thread> threads = new ConcurrentLinkedQueue<>();
	
	/**��������� ���������� �������  ������� QuickSort � ��������� ������
	 * @param  data  - ����������� ������ �����
	 * @param  start  - ������ ������� ���������� (������������)
	 * @param  end  - ����� ������� ���������� (�� ������������)*/
	private void doSort(final String[] data, final int start, final int end){
		// ������� ����� ����
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
		
		//��������� ��� � ������� �� ������ � ���������
		threads.add(t);
		t.start();
		
	}
	
	/**����������� ����������  �������  ������� QuickSort � ����� ������
	 * �������� ��������� ������ doSort, �� �������� � ���� �����. 
	 * ������ ��� ����������� ������������� ��������� ���������� �� ��������� 
	 * � ������������ ������� Arrays.sort
	 * @param  data  - ����������� ������ �����
	 * @param  start  - ������ ������� ���������� (������������)
	 * @param  end  - ����� ������� ���������� (�� ������������)*/
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

	/**������������ ���������� ������� data � ����� �������*/
	public void QuickSort(String[] data){
		// ��������� ����� �������� ����������
		doSort(data, 0, data.length);
		
		while(!threads.isEmpty()){
			Thread t = threads.remove(); // ����� � ������� ������ �������� � ��� ����
			try {
				t.join(); // ����, ���� �� ����������
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//��� ������, ������������� ���������� �������� � ������� threads. �������� ���������, ����� ������ �����
		//������������� ������ �� ������ �������(����� ������ �������).
		//�������, ����� � ������� �� ��������� ���������� ������, ���������� ����� ���������
		
	}

/** �������� � �������� ����������� (pivot) ��������� ������� �� ������� (����� �������). 
 * ��� ��������, �������  pivot ������������ � ������ ����� �������, ������� pivot - � ����� �����.
 *  ��������, ������ ������ pivot �������� �������. ��������� ��������� �����������*/
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
