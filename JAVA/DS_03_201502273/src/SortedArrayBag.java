
public class SortedArrayBag extends Bag {

	/*
	 * SortedArrayBag�� ������
	 * �θ�Ŭ������ �����ڸ� ȣ���Ѵ�.
	 */
	public SortedArrayBag(int i) {
		// TODO Auto-generated constructor stub
		super(i);
	}

	/*
	 * ������ ���� �޾Ƽ� ������ �°� �����ϴ� �Լ�
	 */
	@Override
	public boolean add(int value) {
		if(this.isFull()) {
			System.out.print("\n����\n");      //�� �������� ���� ����
			return false;					//�Լ� ��, ���� ����
		} else if(this.isEmpty()){
			this.coins[0] = new Coin(value); //��������� ���� �տ� ����
			this.size++;					//���� ���� �ϳ� ����
			return true;					//�Լ� ��, ���� ����
		} else {								//�� ������ �ʰ� ��������� ������
			if(this.coins[0].getValue() > value) {  //0��° ������ ���� �Է°����� ũ��
				for(int i = 1; i <= this.size; i++)
					coins[i] = coins[i-1];			//����� ��� ���� ��ĭ�� �ڷ� �̷��
				coins[0] = new Coin(value);			//0��° �ڸ��� �Էµ� ���� ���� ����
				this.size++;						//���� ���� �ϳ� ����
				return true;						//�Լ� ��, ���� ����
			}
			for(int i = 1; i < this.size; i++) {	//0��°�� ������ ������ �κ� ��
				if(this.coins[i].getValue() > value) {	//�ش� ���� �Է� ������ Ŭ��
					for(int j = i+1; j <= this.size; j++)
						coins[j] = coins[j-1];		//����� �ش� ��ġ ������ ���� ��ĭ�� �ڷ� �̷��
					coins[i] = new Coin(value);		//�ش� �ڸ��� �Էµ� ���� ���� ����
					this.size++;					//���� ���� �ϳ� ����
					return true;					//�Լ� ��, ���� ����
				}
			}
			//�Է°��� ���� ��̿� �ִ� ������ ū���� ���
			this.coins[this.size] = new Coin(value);//�ǵ� �ڸ��� �Էµ� ���� ���� ����
			this.size++;							//���� ���� �ϳ� ����
			return true;							//�Լ� ��, ���� ����
		}
	}

	
}
