
public class Coin {
	
	private static final int DEFAULT_VALUE = 0;
	private int value;
	
	/*
	 * ������ �⺻ ������
	 * ������ ���� �⺻������ �����Ѵ�.
	 */
	public Coin() {
		this.value = Coin.DEFAULT_VALUE;
	}
	
	/*
	 * ������ ������
	 * ������ ���� �Էµ� ������ �����Ѵ�.
	 */
	public Coin(int value) {
		this.value = value;
	}
	
	/*
	 * ������ ���� ��ȯ���ִ� �޼ҵ�
	 */
	public int getValue() {
		return this.value;
	}
}
