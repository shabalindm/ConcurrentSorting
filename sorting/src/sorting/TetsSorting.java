package sorting;

import java.util.Arrays;
import java.util.Random;

public class TetsSorting {
	
	static StringBuilder sb = new StringBuilder();
	private static Random rand = new Random();
	
	/**√енерирует случайную строку 
	 * @param maxStringSize -  максимальный размер строки
	 * @param base  - массив из символов, из которых стро€тс€ строки*/
	private static String getRandomString(int maxStringSize, char [] base){
		sb.delete(0, sb.length());
		int stringSize = rand.nextInt(maxStringSize)+10;
		for(int i = 0; i < stringSize; i++)
			sb.append(base[rand.nextInt(base.length)]);		
		return sb.toString();
		
	}
	public static void main(String[] args) {
		// √ененриуем массив input размером в миллион строк
		int maxStringSize = 40;
		char [] base = new char[60];
		for (int i = 0; i < base.length; i++)
				base[i] = (char) ('1'+i);
		
		
		SortMillion s = new SortMillion();
		String [] input = new String[1000000];
		for (int i = 0; i < input.length; i++)
			input[i] = getRandomString(maxStringSize, base);
		
		// ѕриступаем к тестам
		for (int count = 0; count<10; count++){	
		// врем€ начала сортировки
			Long start;
		// копи€ исходного массива
			String [] inputCopy;
			
			inputCopy = Arrays.copyOf(input, input.length);				
			System.out.print( "¬рем€ на многопоточную сортировку: ");
			start = System.currentTimeMillis();		
		    s.QuickSort(inputCopy);
			System.out.println( System.currentTimeMillis() - start);

			inputCopy = Arrays.copyOf(input, input.length);				
			System.out.print( "¬рем€ на однопоточную сортировку тем же алгоритомом: ");
			start = System.currentTimeMillis();		
			s.singleThreadSort(inputCopy, 0, inputCopy.length);
			System.out.println( System.currentTimeMillis() - start);


			inputCopy = Arrays.copyOf(input, input.length);
			System.out.print("¬рем€ на однопоточную сортировку методом Arrays.sort: ");
			start = System.currentTimeMillis();
			Arrays.sort(inputCopy);
			System.out.println(System.currentTimeMillis() - start);
		}
		
		// проверка правильности сортировки
		
		String[] inputCopy = Arrays.copyOf(input, input.length);
		s.QuickSort(inputCopy);
		Arrays.sort(input);
		
		System.out.println("ѕроверка правильности сортировки: " + Arrays.equals(input, inputCopy));
//	
	}


}
