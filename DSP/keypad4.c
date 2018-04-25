#include <lib.h>
#include <serial.h>
#include <time.h>

#define ADDR_OF_KEY_LD	0x14806000  //
#define ADDR_OF_KEY_RD	0x14807000  //키패드의 메모리 주소
#define ADDR_OF_BUZ		0x14808000  //부저 메모리 주소

unsigned char key_scan(unsigned char * key_addr, unsigned char * key_addr_rd) //현재 키패드에 무슨 값이 입력됐는지 확인하는 함수
{
   unsigned char key_value;  //반환될 키값
   int i;
   *key_addr=0x01;  //첫번째 줄
   for(i=0;i<10;i++); //잠시 딜레이
   key_value=*key_addr_rd;  //키값 fetch
   key_value&=0x0f; //하위 4비트만 남김
   if(key_value>0){ //값이 있으면
      if(key_value==0x01)ret=7;
      if(key_value==0x02)ret=8;
      if(key_value==0x04)ret=9;
      if(key_value==0x08)ret=10; //값에 따라 출력값 설정
      goto scan_end;  //함수 끝으로
   }
   *key_addr=0x02;  //두번째 줄
   for(i=0;i<10;i++);  //잠시 딜레이
   key_value=*key_addr_rd;  //키값 fetch
   key_value&=0x0f; //하위 4비트만 남기기
   if(key_value>0){ //값이 있으면
      if(key_value==0x01)ret=4;
      if(key_value==0x02)ret=5;
      if(key_value==0x04)ret=6;
      if(key_value==0x08)ret=11; //값에 따라 출력값 설정
      goto scan_end;  //함수 끝으로
   }
   *key_addr=0x04;  //세번째 줄
   for(i=0;i<10;i++);  //잠시 딜레이
   key_value=*key_addr_rd;  //키값 fetch
   key_value&=0x0f; //하위 4비트만 남기기
   if(key_value>0){ //값이 있으면
      if(key_value==0x01)ret=1;
      if(key_value==0x02)ret=2;
      if(key_value==0x04)ret=3;
      if(key_value==0x08)ret=12; //값에 따라 출력값 설정
      goto scan_end;  //함수 끝으로
   }
   *key_addr=0x08;  //네번째 줄
   for(i=0;i<10;i++);  //잠시 딜레이
   key_value=*key_addr_rd;  //키값 fetch
   key_value&=0x0f; //하위 4비트만 남기기
   if(key_value>0){ //값이 있으면
      if(key_value==0x01)ret=13;
      if(key_value==0x02)ret=0;
      if(key_value==0x04)ret=14;
      if(key_value==0x08)ret=15; //값에 따라 출력값 설정
      goto scan_end;  //함수 끝으로
   }
scan_end:   //함수 끝
	return key_value;  //출력값 반환
}

void buzz(unsigned short* addr){
  int i;
  for(i = 0; i < 10; i++){
    *addr |= 1;
    msWait(100);
    *addr &= 0;
  }
}

void c_main(void)
{
  //변수 선언
	int i = 0,j;
	char ch;
	unsigned char * key_addr;
	unsigned char * key_addr_rd;
	unsigned short * buz_addr;
	unsigned char key_value;  //현재 눌린 값을 저장하기 위한 어레이
	unsigned char old=0; //직전에 눌린 값을 저장할 변수
  unsigned char flag[4] = {0, };
  unsigned char passcode[4] = {0,4,0,7};

	key_addr = (unsigned char *)ADDR_OF_KEY_LD;  //키패드 주소 지정
	key_addr_rd = (unsigned char *)ADDR_OF_KEY_RD; //키패드 주소 지정
	buz_addr = (unsigned short *)ADDR_OF_BUZ;  //부저 주소 지정

	while(1){

		key_value=key_scan(key_addr,key_addr_rd); //현재 눌린 값 fetch

		if((key_value!=old)&&(key_value>0)){  //눌린 값이 직전값과 같지 않으면
			*buz_addr|=0x01; //부저를 울린다.

      if(flag[i] == 0 && passcode[i] == key_value){
        flag[i] = 1;//비밀번호 맞음
      }else if(flag[i] == 0 && passcode[i] != key_value){
        flag[i] = -1;//비밀번호 틀림
      }
      if(i < 4){
        i++;//입력된 비밀번호 개수 +1
      }

      if(flag[0] != 0){
        if(flag[1] != 0){
          if(flag[2] != 0){
            if(flag[3] != 0){
              if(key_value == 15){
                if(flag[0] == 1 && flag[1] == 1 && flag[2] == 1 && flag[3] == 1){
                  msWait(50);  // 50ms delay
                  *buz_addr&=0x00;  //부저를 끈다.
                  //OPENED
                  flag[0] = 0;
                  flag[1] = 0;
                  flag[2] = 0;
                  flag[3] = 0;  //비밀번호 입력 history 초기화
                  //OPENED
                  msWait(100);  // 100ms delay
                  *buz_addr|=0x01; //부저를 울린다.
                }else{
                  flag[0] = 0;
                  flag[1] = 0;
                  flag[2] = 0;
                  flag[3] = 0;  //비밀번호 입력 history 초기화
                  buzz(buz_addr);//10번 부저를 울린다.
                }
              }else{
                flag[0] = -1;
                flag[1] = -1;
                flag[2] = -1;
                flag[3] = -1;  //비밀번호 입력 history 다 틀린것으로 초기화
            }
          }
        }
      }


			msWait(50);  // 50ms delay
			*buz_addr&=0x0;  //부저를 끈다.
		}

		old=key_value;  //받은 값을 직전값에 저장해준다.
		msWait(50);  // 50ms delay

		if(GetChar(&ch) ==1){ //키보드에서 값을 받으면
			break; //while문 탈출
		}
	}

}
