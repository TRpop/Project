
import java.util.Scanner;


public class MainClass_02_201502273 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Scanner scanner = new Scanner(System.in);
		int menu = 0, bagSize;
		System.out.println("<< 프로그램을 시작합니다 >>");
		System.out.print("+ 가방에 들어갈 총 코인의 개수를 입력하시오 : ");
		bagSize = scanner.nextInt();
		while(bagSize < 0) {
			System.out.print("<< 가방의 크기가 음수가 될 수 없습니다 >>");
			bagSize = scanner.nextInt();
		}
		LinkedBag bag = new LinkedBag(bagSize);
		
		while(menu != 9) {
			
			
			System.out.println("+ 메뉴를 선택하세요 \t\t 1:add | 2:remove | 3:print | 4:search | 5:removeAll | 6:add index | 7:removeMax | 8:resize | 9:exit");
			menu = scanner.nextInt();
			
			int value;
			
			switch(menu) {
			case 1:
				System.out.print("코인의 액수를 입력하세요 : ");
				value = scanner.nextInt();
				if(bag.add(value)) {
					System.out.println(value + "코인을 넣었습니다.");
				}else {
					System.out.println(value + "코인을 넣을 수 없습니다.");
				}
				break;
			case 2:
				System.out.print("코인의 액수를 입력하세요 : ");
				value = scanner.nextInt();
				if(bag.remove(value)) {
					System.out.println(value + "코인이 제거되었습니다.");
				}else {
					System.out.println(value + "코인이 없습니다.");
				}
				break;
			case 3:
				bag.print();
				break;
			case 4:
				System.out.print("코인의 액수를 입력하세요 : ");
				value = scanner.nextInt();
				System.out.println(value + "코인은 " + bag.frequentCoin(value) + "개 존재합니다.");
				break;
			case 5:
				Coin[] coins = bag.removeAll();
				System.out.print("[ ");
				for (Coin coin : coins) {
					System.out.print(coin.getValue() + " ");
				}
				System.out.println("]");
				System.out.println("코인들이 제거되었습니다.");
				break;
			case 6:
				System.out.print("코인의 액수를 입력하세요 : ");
				value = scanner.nextInt();
				System.out.print("넣을 인덱스를 입력하세요 : ");
				if(bag.addAt(value, scanner.nextInt())) {
					System.out.println(value + "코인을 넣었습니다.");
				}else {
					System.out.println(value + "코인을 넣을 수 없습니다.");
				}
				break;
			case 7:
				System.out.print("가장 큰 값을 제거합니다 : ");
				System.out.println(bag.removeMax());
				break;
			case 8:
				System.out.print("크기를 입력해주세요");
				bag.resize(scanner.nextInt());
				System.out.println("크기를 " + bag.getSize() + "로 변경합니다.");
				break;
			case 9:
				System.out.print("<9가 입력되어 종료합니다>\n");
				bag.print();
				System.out.print("<<프로그램을 종료합니다>>");
				break;
				
			default:
				
			}
			
			
		}
		
		
	}

}