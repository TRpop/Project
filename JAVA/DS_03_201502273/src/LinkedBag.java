public class LinkedBag {
	
	private int maxSize;
	protected int curSize;
	protected int totalValue;
	protected Node<Coin> next;

	
	/*
	 * LinkedBag�� ������
	 * �μ��� maxSize�� LinkedBag�� �ִ� ũ��� ���Ѵ�.
	 * init() �޼ҵ带 ���� �ʿ��� ������ �ʱ�ȭ�Ѵ�.
	 */
	public LinkedBag(int maxSize) {
		this.maxSize = maxSize;
		init();
	}
	
	/*
	 * ���� ��� ������ ������ �� ����, next�����͸� �ʱ�ȭ�Ѵ�.
	 */
	public void init() {
		this.curSize = 0;
		this.totalValue = 0;
		this.next = null;
	}
	
	/*
	 * ���� ������� �ִ� ����� ���Ͽ� ���� ������ �� á�ٰ� �˷��ش�.
	 */
	public boolean isFull() {
		return (this.maxSize == this.curSize);
	}
	
	/*
	 * ���� ����� 0�̸� LinkedBag�� ����ٰ� �˷��ش�.
	 */
	public boolean isEmpty() {
		return (this.curSize == 0);
	}
	
	/*
	 * Ư�� ��ġ�� value��� ���� ���� ������ �߰��ϴ� �Լ��̴�.
	 * ���� �� á���� Ȯ���ϰ� �� ���� �ʾ����� ���� �������� �ϴ� �ε����� �����̰ų� Ȥ�� ���� ����ִ� �������� ������ Ȯ���Ѵ�.
	 * �� �� �ƴҰ�� ���� ���� ���� ���, �� ���� ��带 ������ ����Ʈ�� Ž���ϸ� index�� ����Ű�� �ڸ����� �̵��Ѵ�.
	 * �� �̵��ϸ� ���� ���� ���� newNode�� ����Ʈ ���̿� ���� �ִ´�.
	 * prev.setNext(newNode);
	 * if(curr != null)
	 *		newNode.setNext(curr);
	 * ���� ����� 1 �ø��� �� ������ value��ŭ �ø���.
	 */
	public boolean addAt(int value, int index) {
		if(this.isFull()) {
			System.out.print("\n����\n");
			return false;
		}else if(index > this.curSize || index < 0){
			System.out.print("\n����\n");
			return false;
		} else {
			Node<Coin> prev = null;
			Node<Coin> curr = this.next;
			Node<Coin> newNode = new Node<Coin>(new Coin(value));
			for(int i = 0; i < index; i++) {
				prev = curr;
				curr = curr.getNext();
			}
			if(prev == null) {
				this.next = newNode;
			}else {
				prev.setNext(newNode);
				if(curr != null)
					newNode.setNext(curr);
			}
			this.curSize++;
			this.totalValue += value;
			
			return true;
		}
	}
	
	/*
	 * value��� ���� ���� ���ο� ������ �ִ� �Լ��̴�. 
	 * ���� LinkedBag�� �� á���� Ȯ���غ��� 
	 * �� ���� �ʾ����� addAt�Լ��� ���� ���� �� ���ڸ��� ������ �߰��Ѵ�.
	 */
	public boolean add(int value) {
		if(this.isFull()) {
			System.out.print("\n����\n");
		}else {
			if(this.addAt(value, this.curSize))
				return true;
		}
		return false;
	}
	
	/*
	 * ���� ����� ��ȯ���ִ� �Լ��̴�.
	 */
	public int getSize() {
		return this.curSize;
	}
	
	/*
	 * �� ������ ��ȯ���ִ� �Լ��̴�.
	 */
	public int getTotalValue() {
		return this.totalValue;
	}
	
	/*
	 * value��� ���� ���� ������ ����Ʈ���� �������ִ� �Լ��̴�.
	 * pre�� curr �� ���� ��带 ������ ����Ʈ�� Ž���ϰ� ���� ��尡 ������ �ִ� value�� ã���Ͱ� ������ ���� ��忡 next�� ���� ����� next�� �������ش�.
	 * �׸��� ���� ����� 1 ���̰� �� ���ݵ� value��ŭ ���δ�.
	 */
	public  boolean remove(int value) {
		if(!this.isEmpty()) {
			Node<Coin> pre = null;
			Node<Coin> curr = this.next;
			while(curr != null) {
				if(curr.getValue().getValue() == value) {
					pre.setNext(curr.getNext());
					this.curSize--;
					this.totalValue -= value;
					return true;
				}
				pre = curr;
				curr = curr.getNext();
			}
			return false;
		}
		System.out.print("\nFull\n");
		return false;
	}
	
	/*
	 * ��� ������ �����ϴ� �Լ��̴�.
	 * ���� ������ ���� coins��� Coin ��̸� ����� ���� ����ִ� �����ŭ�� ũ�⸦ �ش�.
	 * temp��� ���� ����Ʈ ��ü�� Ž���ϸ鼭 ������ ���ε��� coins�� ������� �����Ѵ�.
	 * �׸��� �۾��� ��� ������ ����Ʈ�� �ٽ� �ʱ�ȭ�ϱ� ���� init() �޼ҵ带 ȣ���Ѵ�.
	 */
	public Coin[] removeAll() {
		Coin[] coins = new Coin[this.curSize];
		Node<Coin> temp = this.next;
		
		for(int i = 0; i < this.curSize; i++) {
			coins[i] = temp.getValue();
			temp = temp.getNext();
		}

		init();
		
		return coins;
	}
	
	/*
	 * ����Ʈ�� �ִ� ���� ū ���� �����Ѵ�.
	 */
	public boolean removeMax() {
		return this.remove(this.max());
	}
	
	/*
	 * �Էµ� ���� ������ LinkedBag�� ��� ����ִ����� �˷��ִ� �޼ҵ��̴�.
	 * frequency��� ������ ����� temp��� ��带 ���� ����Ʈ ��ü�� Ž���ϸ� value�� ���� ���� ������ ���������� frequency�� 1 �ø���.
	 * �׸��� Ž���� ������ frequency�� ��ȯ�Ѵ�.
	 */
	public int frequentCoin(int value) {
		int frequency = 0;
		Node<Coin> temp = this.next;
		while(temp != null) {
			if(temp.getValue().getValue() == value) {
				frequency++;
			}
		}
		return frequency;
	}
	
	/*
	 * temp��带 ����� ����Ʈ ��ü�� Ž���ϴٰ� value�� ���� ���� ������ true�� return�Ѵ�.
	 * ã�� ���ϸ� false�� return�Ѵ�.
	 */
	public boolean doesContain(int value) {
		Node<Coin> temp = this.next;
		while(temp != null) {
			if(temp.getValue().getValue() == value) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * next��� ��尡 null�� �ƴϸ� �ش� ����� print() �޼ҵ带 ȣ���Ѵ�.
	 * null�̸� EMPTY�� ����Ѵ�.
	 */
	public void print() {
		if(this.next != null)
			this.next.print();
		else
			System.out.println("EMPTY");
	}
	
	/*
	 * ���� ���濡 ����� ���ε� �� ���� ū ���� ��ȯ�Ѵ�.
	 * temp��� ��带 ���� ����Ʈ�� Ž���ϰ� ���簪(value)���� �� ū���� ���ö����� value���� �� ������ �����Ѵ�.
	 * Ž���� �� ������ value���� ��ȯ�Ѵ�.
	 */
	public int max() {
		int value = Integer.MIN_VALUE;
		Node<Coin> temp = this.next;
		while(temp != null) {
			if(temp.getValue().getValue() > value) {
				value = temp.getValue().getValue();
			}
			temp = temp.getNext();
		}
		return value;
	}
	
	/*
	 * ������ ũ�⸦ �� �����ϴ� �Լ��̴�.
	 * ���� ���ϴ� ũ�⸦ �μ��� �޾ƿ��� ���� ���ϴ� ����� ���� ������� Ŭ��쿡�� �׳� �ִ��� Ű���.
	 * �׷��� ���Ұ�쿡�� ���� ���ϴ� ����� �ִ� ������ ã�ư� ����Ʈ�� ���������.
	 * �׸��� �� ������ �ٽ� ����ϱ� ���� totalValue�� 0���� �ʱ�ȭ�ϰ� ����Ʈ ��ü�� Ž���ؼ� ��� ������ ���� totalValue�� ���Ѵ�.
	 */
	public boolean resize(int size) {
		
		if(this.curSize > size) {
			Node<Coin> temp = this.next;
			for(int i = 1; i < size; i++) {
				temp = temp.getNext();
			}
			temp.setNext(null);
		}
		
		this.curSize = size;
		this.totalValue = 0;
		
		Node<Coin> temp = this.next;
		while(temp != null) {
			this.totalValue += temp.getValue().getValue();
			temp = temp.getNext();
		}
		
		return false;
	}
	
	/*
	 * ���� ���濡 ����ִ� ������ �� ���� ��ȯ�Ѵ�.
	 */
	public int sum() {
		return this.totalValue;
	}
	
	/*
	 * ��� Ŭ�����̴�.
	 */
	class Node<T> {
		
		private Node<T> next; //�ش� ��� �������� ���� ����̴�. ������� null;
		private T value; //�ش� ��尡 ������ �ִ� value�̴�.
		
		/*
		 * value�� setter�̴�.
		 */
		public void setValue(T value) {
			this.value = value;
		}

		/*
		 * �⺻ �����ڷ� ���� ���� �� ��� null�� �ʱ�ȭ�Ѵ�.
		 */
		public Node() {
			this.next = null;
			this.value = null;
		}
		
		/*
		 * �������̴�. ���� �μ��� ����� ���� �ʱ�ȭ�Ѵ�.
		 */
		public Node(T value) {
			this.next = null;
			this.value = value;
		}
		
		/*
		 * ����� ���� ��ȯ�ϴ� �޼ҵ��̴�.
		 */
		public T getValue() {
			return this.value;
		}
		
		/*
		 * �ش� ��� ������ ���� ��带 �����ϴ� �޼ҵ��̴�.
		 * ������ ���� ��尡 null�ϰ�� null�� ��ȯ�ϰ� �׷��� ������ ���� ��ȯ�Ѵ�.
		 */
		public T setNext(Node<T> node) {
			
			this.next = node;
			if(node == null) {
				return null;
			}
			return node.getValue();
		}
		
		/*
		 * �ش� ��� ������ ���� ��带 ��ȯ�ϴ� �޼ҵ��̴�.
		 */
		public Node<T> getNext() {
			return next;
		}
		
		/*
		 * ����� ���踦 �����ִ� �޼ҵ��̴�. ���� ��尡 null�� �ƴϸ� value�� ���� ������� �� ���� ����� print() �޼ҵ带 ȣ���Ѵ�.
		 * ���� ��尡 null�̸� ���� ����� ���� ����Ѵ�.
		 */
		public void print() {
			if( this.next != null) {
				if(this.value.getClass() == Coin.class) {
					System.out.print( ((Coin)this.value).getValue() + " -> ");
					next.print();
				}
			} else {
				if(this.value.getClass() == Coin.class) {
					System.out.println( ((Coin)this.value).getValue());
				}
			}
		}
		
		
	}
}
