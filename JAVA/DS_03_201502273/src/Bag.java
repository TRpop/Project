public class Bag {

	protected static final int DEFAULT_MAX_SIZE = 100;
	protected int maxSize;
	protected int size;
	protected int totalValue;
	
	protected Coin[] coins;
	
	/*
	 * Bag Ŭ������ �⺻ ������
	 * �⺻ �ִ� ũ�⸦ Bag�� ũ��� ���´�.
	 * ���� ũ�⸦ 0���� �ʱ�ȭ�Ѵ�.
	 */
	public Bag() {
		this.maxSize = Bag.DEFAULT_MAX_SIZE;
		coins = new Coin [this.maxSize];
		this.size = 0;
	}
	
	/*
	 * Bag Ŭ������ �⺻ ������
	 * �⺻ �ִ� ũ�⸦ Bag�� ũ��� ���´�.
	 * ���� ũ�⸦ 0���� �ʱ�ȭ�Ѵ�.
	 */
	public Bag(int maxSize) {
		this.maxSize = maxSize;
		coins = new Coin [this.maxSize];
		this.size = 0;
	}
	
	/*
	 * ���� Bag�� ���ִ����� Ȯ���Ѵ�.
	 */
	public boolean isFull() {
		return (this.maxSize == this.size);
	}
	
	/*
	 * ���� Bag�� ����ִ����� Ȯ���Ѵ�.
	 */
	public boolean isEmpty() {
		return (this.size == 0);
	}
	
	/*
	 * ���� Bag�� �� ���ִ��� Ȯ���� �� ������ ������ coins�� ���ο� Coin�� �Ҵ��Ѵ�.
	 * �׸��� Bag�� ����ִ� �� ������ coin �� ��ŭ �÷��ش�.
	 * �׸��� ���� Coin������ 1 �ø���.
	 */
	public boolean add() {
		if(this.isFull()) {
			System.out.print("\n����\n");
			return false;
		}else {
			this.coins[this.size] = new Coin();
			this.totalValue += this.coins[this.size].getValue();
			this.size++;
			return true;
		}
	}
	
	/*
	 * ���� Bag�� �� ���ִ��� Ȯ���� �� ������ ������ coins�� ���ο� Coin�� �Ҵ��Ѵ�.
	 * Coin�� ������ �Էµ� value���� �ȴ�.
	 * �׸��� Bag�� ����ִ� �� ������ coin �� ��ŭ �÷��ش�.
	 * �׸��� ���� Coin������ 1 �ø���.
	 */
	public boolean add(int value) {
		if(this.isFull()) {
			System.out.print("\n����\n");
			return false;
		}else {
			this.coins[this.size] = new Coin(value);
			this.totalValue += this.coins[this.size].getValue();
			this.size++;
			return true;
		}
	}
	
	/*
	 * ���� Bag�� ����ִ� Coin�� ���� ��ȯ�Ѵ�.
	 */
	public int getSize() {
		return this.size;
	}
	
	/*
	 * ���� Bag�� ����ִ� �� ������ ��ȯ�Ѵ�.
	 */
	public int getTotalValue() {
		return this.totalValue;
	}
	
	/*
	 * Bag�� ����ִ��� Ȯ���� �� ������� ������ �Էµ� ���� ���� ������ ���� Coin�� coins���� ã�´�.
	 * ã���� �� ������ �ش� ������ ���ݸ�ŭ ���ش�.
	 * Bag�� ����ִ� ������ ���� �ϳ� ���δ�.
	 * ��� �󿡼� �� �ڿ� ����ִ� ������ �ش� ������ �ڸ��� �Ҵ��ϰ� �� �� �ڸ��� null�� �Ҵ��Ѵ�.
	 */
	public  boolean remove(int value) {
		if(!this.isEmpty()) {
			for(int i = 0; i < this.size; i++) {
				if(this.coins[i].getValue() == value) {
					this.totalValue -= this.coins[i].getValue();
					this.size--;
					this.coins[i] = this.coins[this.size];
					this.coins[this.size] = null;
					System.out.print(value + " ������ �����Ǿ����ϴ�.");
					return true;
				}
			}
			System.out.print("\n�����ϴ�\n");
			return false;
			
		}
		System.out.print("\n����\n");
		return false;
	}
	
	/*
	 * �Էµ� ���� ���� ���� ���� ������ ������ ���� ã�� ��
	 * ã�������� ���� ���� �� ���� ��ȯ�Ѵ�.
	 */
	public int frequentCoin(int value) {
		int frequency = 0;
		for(int i = 0; i < this.size; i++) {
			if(value == this.coins[i].getValue()) {
				frequency++;
			}
		}
		System.out.print(value + " ������ " + frequency + "�� �����մϴ�.\n");
		return frequency;
	}
	
	/*
	 * �Էµ� ���� ���� ���� ���� ������ Bag�ȿ� �����ϴ���
	 * Bag�ȿ� �����ϴ� ��� ������ ���� ���Ͽ� Ȯ���ϴ� �Լ��̴�.
	 */
	public boolean doesContain(int value) {
		for(int i = 0; i < this.size; i++) {
			if(value == this.coins[i].getValue()) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Bag�� ���� ���¸� ������ִ� �Լ��̴�.
	 */
	public void print() {
		
		System.out.println("�� ������ ���� : "+ this.size);
		System.out.println("���� ū ���� : "+ this.max());
		System.out.println("������ �� : "+ this.totalValue);

	}
	
	/*
	 * Bag�� ���ο� �ִ� ��� ���ε��� ���� ���Ͽ� value���� ���� �� ũ�� value�� ���� �Ҵ��Ѵ�.
	 * �׸��� �ݺ����� ������ value���� ��ȯ�ϹǷν� ���� ū ���� ã�´�.
	 */
	public int max() {
		int value = Integer.MIN_VALUE;
		for(int i = 0; i < this.size; i++) {
			if(value < this.coins[i].getValue()) {
				value = this.coins[i].getValue();
			}
		}
		return value;
	}
	
	/*
	 * Bag�� ����ִ� �� ������ ��ȯ�Ѵ�.
	 */
	public int sum() {
		return this.totalValue;
	}
}
