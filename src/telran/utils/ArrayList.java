package telran.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")

public class ArrayList<T> extends AbstractList<T> {
	private class ArrayListIterator implements Iterator<T> {
		
		public int currentIndex = 0;
		@Override
		public boolean hasNext() {
			return currentIndex < size;
		}

		@Override
		public T next() {
			T obj = get(currentIndex);
			currentIndex++;
			return obj;
		}
		
		@Override
		public void remove() {
			ArrayList.this.remove(currentIndex-1);
			currentIndex--;
		}
		
	}
	
	//добавляя <T> после названия класса мы абстрагируем описание класса от типа данных
	//@Override перед названием метода делает проверку на соответствие описания метода (название, параметры) на описание в 
	//интерфейсе
	
	//стараемся сделать сложность алгоритма реализации методов равным 1 
	
	private static final int DEFAULT_CAPACITY = 16;
	private T array[];
	// int size=0; поле вынесено в абстрактный класс
	
	//массив объектов будет строить метод-конструктор
	//метод-конструктор имеет то же имя, что имя класса
	
	public ArrayList(int capacity) {
		array = (T[]) new Object[capacity];
	}

	public ArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	@Override
	public void add(T obj) {
		if(size>=array.length) {
			allocate();
		}
		array[size++] = obj;
		}
	
	//метод выделяет дополнительную память под хранение элементов списка, если capacity массива переполнен
	private void allocate() {
		array = Arrays.copyOf(array, array.length*2);
		
	}
// ДЗ: реализовать методы класса
	@Override
	public boolean add(T obj, int index) {
		if(index<0 || index>size) {
			return false;
		}
		if(size>=array.length) {
			allocate();
		}
		System.arraycopy(array, index, array, index+1, size-index);
		array[index]=obj;
		size++;
		return true;
	}

	@Override
	public T get(int index) {
		if(index>=0 && index<size) {
			T itemValue = array[index];
			return itemValue;
		}
		else return null;
	}

	@Override
	public boolean remove(int index) {
		if(index<0 ||index>size-1) {
			return false;
		}
		System.arraycopy(array, index+1, array, index, size-index-1);
		size--;
		array[size]=null;
		return true;
	}
	/*
	@Override
	public int size() {
		return size;
	}
	метод реализован в абстракном классе AbstractList<T>
	*/
	/*
	private boolean isValidIndex(int index) {
		return index >= 0 && index < size;
	}
	метод реализован в абстракном классе AbstractList<T>
	*/
/*
	@Override
	public int indexOf(T pattern) {
		int index = 0;
		while(index<size && !array[index].equals(pattern)) {
			index++;
		}
		return index<size ? index:-1;
	}
*/	
	@Override
	public int indexOf(T pattern) {
		return indexOf((o1) -> o1.equals(pattern));
	}
/*
	@Override
	public int lastIndexOf(T pattern) {
		int index = size-1;
		while(index>=0 && !array[index].equals(pattern)) {
			index--;
		}
		return index>=0 ? index : -1;
	}
*/
	@Override
	public int lastIndexOf(T pattern) {
		return lastIndexOf((o1) -> o1.equals(pattern));
	}
	
	@Override
	public boolean remove(T pattern) {
		return remove(indexOf(pattern));
	}

//	@Override
//	public void addAll(List<T> objects) {
//		for(T obj: objects) {
//			add(obj);
//		}
//	}
	
//	@Override
//	public boolean removeAll(List<T> patterns) {
//		/*
//		int sizePattern = patterns.size();
//		boolean res = false;
//		for (int i = 0; i<sizePattern; i++) {
//			T current = patterns.get(i);
//			if(this.contains(current)) {
//				this.removeIf((o1) -> o1.equals(current));
//				res = true;
//			}
//		}
//		*/
//		boolean res;
//		boolean isRetain = false;
//		if (this == patterns) {
//			size = 0;
//			clean();
//			res = true;
//		}
//		else {
//			/*
//			int index = 0;
//			res = false;
//			while (index<this.size) {
//				T current = this.get(index);
//				if (patterns.contains(current)) {
//					this.removeIf((o1) -> o1.equals(current));
//					res = true;
//				}
//				else {
//					index++;
//				}
//			}
//			*/
//			res =  removing(patterns, isRetain);
//		}
//		
//		return res;
//		
//	}
/*
	@Override
	public boolean removeAll(list<T> patterns) {
		boolean isRetain = false;
		boolean res = false;
		if (this == patterns) {
			size = 0;
			clean(0, size);
			res = true;
		} else {
			res =  removing(patterns, isRetain);
		}
		return res;
		
	}
*/
	
	private void clean(int startIndex, int sizeBefore) {
		for (int i = startIndex; i < sizeBefore; i++) {
			array[i] = null;
		}
		
	}

	/*
	private boolean removing(list<T> patterns, boolean isRetain) {
		int sizeBeforeRemoving = size;
		int indexAfterRemoving = 0;
		for(int i = 0; i < sizeBeforeRemoving; i++) {
			T current = array[i];
			if (conditionRemoving(patterns, current, isRetain)) {
				size--;
			} else {
				array[indexAfterRemoving++] = array[i];
			}
		}
		boolean res = sizeBeforeRemoving > size;
		if (res) {
			clean(size, sizeBeforeRemoving);
		}
		return res;
	}
	 */
	private boolean removing(List<T> patterns, boolean isRetain) {
		int index = 0;
		boolean res = false;
		while (index<this.size) {
			T current = this.get(index);
			if (conditionRemoving(patterns, current, isRetain)) {
				this.removeIf((o1) -> o1.equals(current));
				res = true;
			}
			else {
				index++;
			}
		}
		return res;
	}
	
	private boolean conditionRemoving(List<T> patterns, T current, boolean isRetain) {
		boolean  res = patterns.contains(current);
		// res = patterns.indexOf(current) >= 0; replaced with method contains
		return isRetain ? !res : res;
	}

	@Override
	public boolean retainAll(List<T> patterns) {
		boolean isRetain = true;
		return this == patterns ? false : removing(patterns, isRetain);
	}

	@Override
	public T set(T object, int index) {
		T res = null;
		if (isValidIndex(index)) {
			res = array[index];
			array[index] = object;
		}
		return res;
	}

	@Override
	public boolean swap(int index1, int index2) {
		boolean res = false;
		if (isValidIndex(index1) && isValidIndex(index2)) {
			T tmp = array[index1];
			array[index1] = array[index2];
			array[index2] = tmp;
			res = true;
		}
		return res;
	}

	@Override
	public int indexOf(Predicate<T> predicate) {
		int index = 0;
		while(index < size && !predicate.test(array[index])) { 
			/* метод test - это метод интерфейса Predicate,
			который определяет подходит ли объект под некоторое условие.
			Что значит "подходит" должен описать тот, кто будет использовать класса
			*/
			index++;
		}
		return index < size ? index : -1; 
		/* если "подходящий" index был найден, то вернется значение index, а если нет, то вернется -1
		 * 
		 */
	}

	@Override
	public int lastIndexOf(Predicate<T> predicate) {
		
		int index = size-1;
		while(index >=0 && !predicate.test(array[index])) { 
			index--;
		}
		return index >= 0 ? index : -1; 
	}

//	@Override
//	public boolean removeIf(Predicate<T> predicate) {
//		boolean isRemovedExists = false;
//		while (indexOf(predicate)!=-1) {
//			remove(lastIndexOf(predicate));
//			isRemovedExists = true;
//		}
//		return isRemovedExists;
//	}
//
	private void clean(int sizeBefore) {
		for (int i = size; i < sizeBefore; i++) {
			array[i] = null;
		}
		
	}
	
	@Override
	public void clean() {
		int sizeBeforeRemoving = size;
		size = 0;
		clean(sizeBeforeRemoving);
	}

	@Override
	public Iterator<T> iterator() {
		Iterator<T> ArrayListIterator = new ArrayListIterator();
		return ArrayListIterator;
	}
}
