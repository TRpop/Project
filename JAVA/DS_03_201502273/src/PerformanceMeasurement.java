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
		data = new int[5000];	//������ ���� ���� ����
		rand = new Random();	//���� ��ü ����
		rand.setSeed(System.currentTimeMillis());	//�׶��׶� �ٸ� ������ ���� �ޱ����� �õ� ����
		for(int i = 0; i < data.length; i++) {
			data[i] = rand.nextInt();			//������ ���� data�� ����
		}
	}
	
	public void testSortedArrayBag() {
		System.out.println("{Sorted Array}");
		
		for(int i = 1; i < 6; i++) {			//i�� 1���� 5����
			this.arrBag = new SortedArrayBag(size * i);	//SortedArrayBag ����
			this.startTime = System.nanoTime(); //���۽ð� ���
			for(int j = 0; j < i * size; j++) {
				this.arrBag.add(data[j]);		//data�� �ִ� ���� ��� �鿡 ����
			}
			this.endTime = System.nanoTime();	//����ð� ���
			this.insertingTime = this.endTime - this.startTime; //��� �鿡 �����ϴµ� �ɸ� �ð�
			
			this.startTime = System.nanoTime(); //���۽ð� ���
			this.arrBag.max();			//��� �鿡 ���� �� �� �ִ� ��ġ
			this.endTime = System.nanoTime();	//����ð� ���
			this.findingMaxTime = this.endTime - this.startTime; //��� �鿡�� �ִ� ��ġ�ϴµ� �ɸ� �ð�
			
			System.out.println("Size " + i * size + ",\t insertion " + this.insertingTime + "\t Search Max " + this.findingMaxTime);
		}
	}
	
	public void testSortedLinkedBag() {
		System.out.println("{Sorted Linked}");
		
		for(int i = 1; i < 6; i++) {			//i�� 1���� 5����
			this.linkedBag = new SortedLinkedBag(size * i);	//SortedLinkedBag ����
			this.startTime = System.nanoTime(); //���۽ð� ���
			for(int j = 0; j < i * size; j++) {
				this.linkedBag.add(data[j]);		//data�� �ִ� ���� ��ũ�� �鿡 ����
			}
			this.endTime = System.nanoTime();	//����ð� ���
			this.insertingTime = this.endTime - this.startTime; //��ũ�� �鿡 �����ϴµ� �ɸ� �ð�
			
			this.startTime = System.nanoTime(); //���۽ð� ���
			this.linkedBag.max();			//��ũ�� �鿡 ���� �� �� �ִ� ��ġ
			this.endTime = System.nanoTime();	//����ð� ���
			this.findingMaxTime = this.endTime - this.startTime; //��ũ�� �鿡�� �ִ� ��ġ�ϴµ� �ɸ� �ð�
			
			System.out.println("Size " + i * size + ",\t insertion " + this.insertingTime + "\t Search Max " + this.findingMaxTime);
		}
	}
}
