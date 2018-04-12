
public class SortedArrayBag extends Bag {

	/*
	 * SortedArrayBag의 생성자
	 * 부모클래스의 생성자를 호출한다.
	 */
	public SortedArrayBag(int i) {
		// TODO Auto-generated constructor stub
		super(i);
	}

	/*
	 * 삽입할 값을 받아서 순서에 맞게 삽입하는 함수
	 */
	@Override
	public boolean add(int value) {
		if(this.isFull()) {
			System.out.print("\n실패\n");      //꽉 차있으면 삽입 실패
			return false;					//함수 끝, 삽입 실패
		} else if(this.isEmpty()){
			this.coins[0] = new Coin(value); //비어있으면 제일 앞에 삽입
			this.size++;					//현재 길이 하나 증가
			return true;					//함수 끝, 삽입 성공
		} else {								//꽉 차지도 않고 비어있지도 않으면
			if(this.coins[0].getValue() > value) {  //0번째 원소의 값이 입력값보다 크면
				for(int i = 1; i <= this.size; i++)
					coins[i] = coins[i-1];			//어레이의 모든 값을 한칸씩 뒤로 미루고
				coins[0] = new Coin(value);			//0번째 자리에 입력된 값의 코인 삽입
				this.size++;						//현재 길이 하나 증가
				return true;						//함수 끝, 삽입 성공
			}
			for(int i = 1; i < this.size; i++) {	//0번째를 제외한 나머지 부분 비교
				if(this.coins[i].getValue() > value) {	//해당 값이 입력 값보다 클때
					for(int j = i+1; j <= this.size; j++)
						coins[j] = coins[j-1];		//어레이의 해당 위치 이후의 값을 한칸씩 뒤로 미루고
					coins[i] = new Coin(value);		//해당 자리에 입력된 값의 코인 삽입
					this.size++;					//현재 길이 하나 증가
					return true;					//함수 끝, 삽입 성공
				}
			}
			//입력값이 현재 어레이에 있는 값보다 큰값인 경우
			this.coins[this.size] = new Coin(value);//맨뒤 자리에 입력된 값의 코인 삽입
			this.size++;							//현재 길이 하나 증가
			return true;							//함수 끝, 삽입 성공
		}
	}

	
}
