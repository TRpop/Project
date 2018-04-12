import java.util.Random;

public class PerformanceMeasurement {
	private static final int size = 1000;
	private SortedArrayBag arrBag;
	private SortedLinkedBag linkedBag;
	private Random rand;
	private int[] data;
	private long startTime, endTime, insertingTime, findingMaxTime;
	
	public PerformanceMeasurement() {
		
	}
	
	public void generateData() {
		data = new int[5000];	//랜덤한 수를 위한 공간
		rand = new Random();	//랜덤 객체 생성
		rand.setSeed(System.currentTimeMillis());	//그때그때 다른 랜덤한 값을 받기위해 시드 설정
		for(int i = 0; i < data.length; i++) {
			data[i] = rand.nextInt();			//랜덤한 수를 data에 저장
		}
	}
	
	public void testSortedArrayBag() {
		System.out.println("{Sorted Array}");
		
		for(int i = 1; i < 6; i++) {			//i는 1에서 5까지
			this.arrBag = new SortedArrayBag(size * i);	//SortedArrayBag 생성
			this.startTime = System.nanoTime(); //시작시간 기록
			for(int j = 0; j < i * size; j++) {
				this.arrBag.add(data[j]);		//data에 있는 값을 어레이 백에 저장
			}
			this.endTime = System.nanoTime();	//종료시간 기록
			this.insertingTime = this.endTime - this.startTime; //어레이 백에 저장하는데 걸린 시간
			
			this.startTime = System.nanoTime(); //시작시간 기록
			this.arrBag.max();			//어레이 백에 들은 값 중 최댓값 서치
			this.endTime = System.nanoTime();	//종료시간 기록
			this.findingMaxTime = this.endTime - this.startTime; //어레이 백에서 최댓값 서치하는데 걸린 시간
			
			System.out.println("Size " + i * size + ",\t insertion " + this.insertingTime + "\t Search Max " + this.findingMaxTime);
		}
	}
	
	public void testSortedLinkedBag() {
		System.out.println("{Sorted Linked}");
		
		for(int i = 1; i < 6; i++) {			//i는 1에서 5까지
			this.linkedBag = new SortedLinkedBag(size * i);	//SortedLinkedBag 생성
			this.startTime = System.nanoTime(); //시작시간 기록
			for(int j = 0; j < i * size; j++) {
				this.linkedBag.add(data[j]);		//data에 있는 값을 링크드 백에 저장
			}
			this.endTime = System.nanoTime();	//종료시간 기록
			this.insertingTime = this.endTime - this.startTime; //링크드 백에 저장하는데 걸린 시간
			
			this.startTime = System.nanoTime(); //시작시간 기록
			this.linkedBag.max();			//링크드 백에 들은 값 중 최댓값 서치
			this.endTime = System.nanoTime();	//종료시간 기록
			this.findingMaxTime = this.endTime - this.startTime; //링크드 백에서 최댓값 서치하는데 걸린 시간
			
			System.out.println("Size " + i * size + ",\t insertion " + this.insertingTime + "\t Search Max " + this.findingMaxTime);
		}
	}
}
