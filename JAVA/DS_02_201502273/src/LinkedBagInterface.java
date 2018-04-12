public interface LinkedBagInterface {

	public void init();
	public boolean isFull();
	public boolean isEmpty();
	public boolean addAt(int value, int index);
	public boolean add(int value);
	public int getSize();
	public int getTotalValue();
	public  boolean remove(int value);
	public Coin[] removeAll();
	public boolean removeMax();
	public int frequentCoin(int value);
	public boolean doesContain(int value);
	public void print();
	public int max();
	public boolean resize(int size);
}
