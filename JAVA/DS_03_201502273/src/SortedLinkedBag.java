
public class SortedLinkedBag extends LinkedBag {

	public SortedLinkedBag(int maxSize) {
		super(maxSize);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 삽입할 값을 받아서 순서에 맞게 삽입하는 함수
	 */
	@Override
	public boolean add(int value) {
		// TODO Auto-generated method stub
		if(this.isFull()) {
			System.out.print("\n실패\n");		//꽉 차있으면 삽입 실패
			return false;					//함수 끝, 삽입 실패
		} else if(this.isEmpty()){
			this.next = new Node<Coin>(new Coin(value)); //비어있으면 제일 앞에 삽입
			this.curSize++;					//현재 길이 하나 증가
			return true;					//함수 끝, 삽입 성공
		} else {							//꽉 차지도 않고 비어있지도 않으면
			Node<Coin> curr = this.next;	//현재 노드를 나타내는 노드
			Node<Coin> prev = null;			//이전 노드를 나타내는 노드
			Node<Coin> temp = new Node<Coin>(new Coin(value));//넣을 값이 들어있는 노드
			
			while(curr != null) {	//현재 노드가 null이 아니면
				if(curr.getValue().getValue() >= value) {	//받은 값보다 현재 값이 크면
					if(prev == null) {	//이전값이 null이면, 즉 현개값이 head이면
						temp.setNext(curr); //삽입할 노드에 현재 노드를 연결
						this.next = temp;	//head에 삽입할 노드를 할당
					} else {
						temp.setNext(curr);	//삽입할 노드에 현재 노드를 연결
						prev.setNext(temp);	//이전 노드에 삽입할 노드를 연결
					}
					this.curSize++;		//현재 길이 하나 증가
					return true;		//함수 끝, 삽입 성공
				}
				prev = curr;		//현재노드를 이전노드에 할당
				curr = curr.getNext();	//다음노드를 현재노드에 할당
			}
			prev.setNext(temp);	//이전노드에 삽입노드를 연결
			return true;		//함수 끝, 삽입 성공
		}
	}
}
