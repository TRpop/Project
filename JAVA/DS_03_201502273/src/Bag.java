public class Bag {

	protected static final int DEFAULT_MAX_SIZE = 100;
	protected int maxSize;
	protected int size;
	protected int totalValue;
	
	protected Coin[] coins;
	
	/*
	 * Bag 클래스의 기본 생성자
	 * 기본 최대 크기를 Bag의 크기로 갖는다.
	 * 현재 크기를 0으로 초기화한다.
	 */
	public Bag() {
		this.maxSize = Bag.DEFAULT_MAX_SIZE;
		coins = new Coin [this.maxSize];
		this.size = 0;
	}
	
	/*
	 * Bag 클래스의 기본 생성자
	 * 기본 최대 크기를 Bag의 크기로 갖는다.
	 * 현재 크기를 0으로 초기화한다.
	 */
	public Bag(int maxSize) {
		this.maxSize = maxSize;
		coins = new Coin [this.maxSize];
		this.size = 0;
	}
	
	/*
	 * 현재 Bag이 차있는지를 확인한다.
	 */
	public boolean isFull() {
		return (this.maxSize == this.size);
	}
	
	/*
	 * 현재 Bag이 비어있는지를 확인한다.
	 */
	public boolean isEmpty() {
		return (this.size == 0);
	}
	
	/*
	 * 먼저 Bag이 꽉 차있는지 확인한 뒤 차있지 않으면 coins에 새로운 Coin을 할당한다.
	 * 그리고 Bag에 들어있는 총 가격을 coin 값 만큼 올려준다.
	 * 그리고 현재 Coin개수를 1 올린다.
	 */
	public boolean add() {
		if(this.isFull()) {
			System.out.print("\n실패\n");
			return false;
		}else {
			this.coins[this.size] = new Coin();
			this.totalValue += this.coins[this.size].getValue();
			this.size++;
			return true;
		}
	}
	
	/*
	 * 먼저 Bag이 꽉 차있는지 확인한 뒤 차있지 않으면 coins에 새로운 Coin을 할당한다.
	 * Coin의 가격은 입력된 value값이 된다.
	 * 그리고 Bag에 들어있는 총 가격을 coin 값 만큼 올려준다.
	 * 그리고 현재 Coin개수를 1 올린다.
	 */
	public boolean add(int value) {
		if(this.isFull()) {
			System.out.print("\n실패\n");
			return false;
		}else {
			this.coins[this.size] = new Coin(value);
			this.totalValue += this.coins[this.size].getValue();
			this.size++;
			return true;
		}
	}
	
	/*
	 * 현재 Bag에 들어있는 Coin의 수를 반환한다.
	 */
	public int getSize() {
		return this.size;
	}
	
	/*
	 * 현재 Bag에 들어있는 총 가격을 반환한다.
	 */
	public int getTotalValue() {
		return this.totalValue;
	}
	
	/*
	 * Bag이 비어있는지 확인한 후 비어있지 않으면 입력된 값과 같은 가격을 갖는 Coin을 coins에서 찾는다.
	 * 찾으면 총 가격을 해당 코인의 가격만큼 빼준다.
	 * Bag에 들어있는 코인의 수를 하나 줄인다.
	 * 어레이 상에서 맨 뒤에 들어있는 코인을 해당 코인의 자리로 할당하고 맨 뒤 자리는 null을 할당한다.
	 */
	public  boolean remove(int value) {
		if(!this.isEmpty()) {
			for(int i = 0; i < this.size; i++) {
				if(this.coins[i].getValue() == value) {
					this.totalValue -= this.coins[i].getValue();
					this.size--;
					this.coins[i] = this.coins[this.size];
					this.coins[this.size] = null;
					System.out.print(value + " 코인이 삭제되었습니다.");
					return true;
				}
			}
			System.out.print("\n없습니다\n");
			return false;
			
		}
		System.out.print("\n실패\n");
		return false;
	}
	
	/*
	 * 입력된 값과 같은 값을 갖는 코인을 일일히 비교해 찾은 후
	 * 찾을때마다 수를 세어 그 수를 반환한다.
	 */
	public int frequentCoin(int value) {
		int frequency = 0;
		for(int i = 0; i < this.size; i++) {
			if(value == this.coins[i].getValue()) {
				frequency++;
			}
		}
		System.out.print(value + " 코인은 " + frequency + "개 존재합니다.\n");
		return frequency;
	}
	
	/*
	 * 입력된 값과 같은 값을 갖는 코인이 Bag안에 존재하는지
	 * Bag안에 존재하는 모든 코인의 값을 비교하여 확인하는 함수이다.
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
	 * Bag의 현재 상태를 출력해주는 함수이다.
	 */
	public void print() {
		
		System.out.println("총 코인의 개수 : "+ this.size);
		System.out.println("가장 큰 코인 : "+ this.max());
		System.out.println("코인의 합 : "+ this.totalValue);

	}
	
	/*
	 * Bag의 내부에 있는 모든 코인들의 값을 비교하여 value보다 값이 더 크면 value에 값을 할당한다.
	 * 그리고 반복문이 끝나고 value값을 반환하므로써 가장 큰 값을 찾는다.
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
	 * Bag에 들어있는 총 가격을 반환한다.
	 */
	public int sum() {
		return this.totalValue;
	}
}
