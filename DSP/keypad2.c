#include <lib.h>
#include <serial.h>
#include <time.h>

#define ADDR_OF_KEY_LD	0x14806000  //
#define ADDR_OF_KEY_RD	0x14807000  //키패드의 메모리 주소
#define ADDR_OF_BUZ		0x14808000  //부저 메모리 주소
#define KEY_PAD_ROW 4
#define KEY_PAD_COL 4
#define KEY_PAD_NUM KEY_PAD_COL*KEY_PAD_ROW  //키패드에 달린 버튼의 개수

void key_scan(unsigned char * key_addr, unsigned char * key_addr_rd, unsigned char* key) //현재 키패드에 무슨 값이 입력됐는지 확인하는 함수
{
   int i, j, k;
   unsigned char key_value;

   for(i = 0; i < KEY_PAD_COL; i++){
     *key_addr = (1<<i);
     for(j=0;j<10;j++); //잠시 딜레이
     key_value=*key_addr_rd;  //키값 fetch
     key_value&=0x0f; //하위 4비트만 남김
     if(key_value > 0){ //값이 눌렸으면
       for(k = 0; k < KEY_PAD_ROW; k++){
         if(key_value == (1<<k)) key[i*KEY_PAD_COL + k] = 1;  //각 자리를 스캔하여 눌린 자리를 1로 셋
       }
     }
   }
}

void c_main(void)
{
  //변수 선언
	int i,j;
	char ch;
	unsigned char * key_addr;
	unsigned char * key_addr_rd;
	unsigned short * buz_addr;
  unsigned char[KEY_PAD_NUM] old = {0, }; //이전에 눌린 값을 저장하기 위한 어레이
  unsigned char[KEY_PAD_NUM] key = {0, };  //현재 눌린 값을 저장하기 위한 어레이

	key_addr = (unsigned char *)ADDR_OF_KEY_LD;  //키패드 주소 지정
	key_addr_rd = (unsigned char *)ADDR_OF_KEY_RD; //키패드 주소 지정
	buz_addr = (unsigned short *)ADDR_OF_BUZ;  //부저 주소 지정

	while(1){

    unsigned char[KEY_PAD_NUM] dif = {0, }; //이전에 눌리지 않았다가 현재 눌린 값을 저장하기위한 어레이
    unsigned char count = 0; //키패드가 눌린 횟수를 표시하기위한 변수

		key_value=key_scan(key_addr,key_addr_rd, (unsigned char*)key); //현재 눌린 값 fetch

    for(i = 0; i < KEY_PAD_NUM; i++){
      if(key[i] > old[i]){
        dif[i] = 1; //눌린 값 저장
        count += 1; //버튼눌린 횟수 +1
      }
      old[i] = key[i];  //현재값을 이전값으로 저장
      key[i] = 0; //다음 값을 받기위해 0으로 초기화
    }
    if(count != 0){ //키패드가 한 번이라도 눌렸다면
      *buz_addr|=0x01; //부저를 울린다.
      printf("%d key pad value is ", count);
      for(i = 0; i < KEY_PAD_NUM; i++){
        if(dif[i] == 1){    //눌린 버튼 판별
          printf("%d\t",i)
        }
      }
      printf("\n");
      msWait(50);  // 50ms delay
      *buz_addr&=0x0;  //부저를 끈다.
    }
		msWait(50);  // 50ms delay

		if(GetChar(&ch) == 1){ //키보드에서 값을 받으면
			break; //while문 탈출
		}
	}

}
