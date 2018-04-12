
public class Coin {
	
	private static final int DEFAULT_VALUE = 0;
	private int value;
	
	/*
	 * 코인의 기본 생성자
	 * 코인의 값을 기본값으로 설정한다.
	 */
	public Coin() {
		this.value = Coin.DEFAULT_VALUE;
	}
	
	/*
	 * 코인의 생성자
	 * 코인의 값을 입력된 값으로 설정한다.
	 */
	public Coin(int value) {
		this.value = value;
	}
	
	/*
	 * 코인의 값을 반환해주는 메소드
	 */
	public int getValue() {
		return this.value;
	}
}
