
public class SortedLinkedBag extends LinkedBag {

	public SortedLinkedBag(int maxSize) {
		super(maxSize);
		// TODO Auto-generated constructor stub
	}

	/*
	 * ������ ���� �޾Ƽ� ������ �°� �����ϴ� �Լ�
	 */
	@Override
	public boolean add(int value) {
		// TODO Auto-generated method stub
		if(this.isFull()) {
			System.out.print("\n����\n");		//�� �������� ���� ����
			return false;					//�Լ� ��, ���� ����
		} else if(this.isEmpty()){
			this.next = new Node<Coin>(new Coin(value)); //��������� ���� �տ� ����
			this.curSize++;					//���� ���� �ϳ� ����
			return true;					//�Լ� ��, ���� ����
		} else {							//�� ������ �ʰ� ��������� ������
			Node<Coin> curr = this.next;	//���� ��带 ��Ÿ���� ���
			Node<Coin> prev = null;			//���� ��带 ��Ÿ���� ���
			Node<Coin> temp = new Node<Coin>(new Coin(value));//���� ���� ����ִ� ���
			
			while(curr != null) {	//���� ��尡 null�� �ƴϸ�
				if(curr.getValue().getValue() >= value) {	//���� ������ ���� ���� ũ��
					if(prev == null) {	//�������� null�̸�, �� �������� head�̸�
						temp.setNext(curr); //������ ��忡 ���� ��带 ����
						this.next = temp;	//head�� ������ ��带 �Ҵ�
					} else {
						temp.setNext(curr);	//������ ��忡 ���� ��带 ����
						prev.setNext(temp);	//���� ��忡 ������ ��带 ����
					}
					this.curSize++;		//���� ���� �ϳ� ����
					return true;		//�Լ� ��, ���� ����
				}
				prev = curr;		//�����带 ������忡 �Ҵ�
				curr = curr.getNext();	//������带 �����忡 �Ҵ�
			}
			prev.setNext(temp);	//������忡 ���Գ�带 ����
			return true;		//�Լ� ��, ���� ����
		}
	}
}
