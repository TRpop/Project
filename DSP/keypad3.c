#include <lib.h>
#include <serial.h>
#include <time.h>

#define ADDR_OF_KEY_LD	0x14806000  //
#define ADDR_OF_KEY_RD	0x14807000  //키패드의 메모리 주소
#define ADDR_OF_BUZ		0x14808000  //부저 메모리 주소
#define EQ_LENGTH 100 //사칙연산에 입력 가능한 최대 길이

long postfix_stack[EQ_LENGTH] = {0, };
char OP_stack[EQ_LENGTH] = {0, };
int postfix_stack_pointer = -1;
int OP_stack_pointer = -1;

char key_scan(unsigned char * key_addr, unsigned char * key_addr_rd) //현재 키패드에 무슨 값이 입력됐는지 확인하는 함수
{
   unsigned char key_value;  //입력값을 받기위한 변수
   char ret = -7;  //반환될 키값
   int i;
   *key_addr=0x01;  //첫번째 줄
   for(i=0;i<10;i++); //잠시 딜레이
   key_value=*key_addr_rd;  //키값 fetch
   key_value&=0x0f; //하위 4비트만 남김
   if(key_value>0){ //값이 있으면
      if(key_value==0x01)ret=7;
      if(key_value==0x02)ret=8;
      if(key_value==0x04)ret=9;
      if(key_value==0x08)ret=-1; //값에 따라 출력값 설정
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
      if(key_value==0x08)ret=-2; //값에 따라 출력값 설정
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
      if(key_value==0x08)ret=-3; //값에 따라 출력값 설정
      goto scan_end;  //함수 끝으로
   }
   *key_addr=0x08;  //네번째 줄
   for(i=0;i<10;i++);  //잠시 딜레이
   key_value=*key_addr_rd;  //키값 fetch
   key_value&=0x0f; //하위 4비트만 남기기
   if(key_value>0){ //값이 있으면
      if(key_value==0x01)ret=-4;
      if(key_value==0x02)ret=0;
      if(key_value==0x04)ret=-5;
      if(key_value==0x08)ret=-6; //값에 따라 출력값 설정
      goto scan_end;  //함수 끝으로
   }
scan_end:   //함수 끝
	return ret;  //출력값 반환
}

int getPriority(char operation){//사칙연산의 우선순위를 반환해주는 함수
  switch(operation){
    case -1://곱하기
    case -2://나누기
      return 3;//높은 우선순위
    case -3://더하기
    case -6://빼기
      return 2;//낮은 우선순위
    case -5:
      return 0;
      default:
      return -1;
  }
}

void pushPost(char value_or_operation){//postfix 스택에 push하는 함수
  if(value_or_operation >= 0){  //value
    if(postfix_stack_pointer > -1 && postfix_stack[postfix_stack_pointer] >= 0){  //현재 postfix 스택의 peek도 value이면
      postfix_stack[postfix_stack_pointer] = postfix_stack[postfix_stack_pointer] * 10 + value_or_operation;//자릿수 계산
    }else{
      postfix_stack_pointer++;
      postfix_stack[postfix_stack_pointer] = value_or_operation;//받은 값 push
    }
  }else if(value_or_operation > -7){  //operation이면
    postfix_stack_pointer++;
    postfix_stack[postfix_stack_pointer] = value_or_operation;//받은 값 push
  }
}

long popPost(){//postfix 스택에서 pop하는 함수
  unsigned char ret = postfix_stack[postfix_stack_pointer];
  postfix_stack_pointer--;
  return ret;
}

void pushOP(char operation){//연산자 스택에 push하는 함수
  OP_stack_pointer++;
  OP_stack[OP_stack_pointer] = operation;
}

char popOP(){//연산자 스택에서 pop하는 함수
  unsigned char ret = OP_stack[OP_stack_pointer];
  OP_stack_pointer--;
  return ret;
}

char topOP(){//연산자 스택의 peek값을 반환하는 함수
  return OP_stack[OP_stack_pointer];
}

double calc(){//만들어진 postfix 수식을 가지고 실제로 계산하는 함수
  double value_stack[EQ_LENGTH] = {0, };//값이 저장될 임시 스택
  int value_stack_pointer = -1;//임시 스택을 위한 스택 포인터

  int i = 0;
  for(i = 0; i < postfix_stack_pointer + 1; i++){//postfix 스택의 크기만큼 반복
    long val = postfix_stack[i];//postfix에 있는 값 fetch

    switch(val){
      case -1:  //곱하기
        long a = value_stack[value_stack_pointer];
        value_stack_pointer--;
        long b = value_stack[value_stack_pointer];//value stack에서 두 개를 pop하여
        value_stack[value_stack_pointer] = a*b;//곱한다.
      break;
      case -2:  //나누기
        long a = value_stack[value_stack_pointer];
        value_stack_pointer--;
        long b = value_stack[value_stack_pointer];//value stack에서 두 개를 pop하여
        value_stack[value_stack_pointer] = (double)b/a;//나눈다

      break;
      case -3:  //더하기
        long a = value_stack[value_stack_pointer];
        value_stack_pointer--;
        long b = value_stack[value_stack_pointer];//value stack에서 두 개를 pop하여
        value_stack[value_stack_pointer] = a+b;//더한다

      break;
      case -6:  //빼기
        long a = value_stack[value_stack_pointer];
        value_stack_pointer--;
        long b = value_stack[value_stack_pointer];//value stack에서 두 개를 pop하여
        value_stack[value_stack_pointer] = b-a;//뺀다

      break;
      case -5:

      break;
      case -4:

      break;
      default://연산자가 아닌 숫자인 경우
        value_stack_pointer++;
        value_stack[value_stack_pointer] = val;//value 스택에 push해준다.
      break;
    }
  }

  return value_stack[value_stack_pointer];
}

void print(char ch){
  switch(val){
    case -1:  //곱하기
      printf("*");
    break;
    case -2:  //나누기
      printf("/");
    break;
    case -3:  //더하기
      printf("+");
    break;
    case -6:  //빼기
      printf("-");
    break;
    case -5:
      printf("=");//=출력
    break;
    case -4:

    break;

    default:
      printf("%d", ch);//숫자 출력
    break;

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
	char key_value;  //현재 눌린 값을 저장하기 위한 어레이
	unsigned char old=0; //직전에 눌린 값을 저장할 변수

	key_addr = (unsigned char *)ADDR_OF_KEY_LD;  //키패드 주소 지정
	key_addr_rd = (unsigned char *)ADDR_OF_KEY_RD; //키패드 주소 지정
	buz_addr = (unsigned short *)ADDR_OF_BUZ;  //부저 주소 지정

	while(1){

		key_value=key_scan(key_addr,key_addr_rd); //현재 눌린 값 fetch

		if((key_value!=old)&&(key_value>-7)){  //눌린 값이 직전값과 같지 않으면
			*buz_addr|=0x01; //부저를 울린다.
      print(key_value);

      if(key_value >= 0){ //value
        pushPost(key_value);  //Postfix 스택에 push
      }else if(key_value == -5){  //입력값이 =이면
        while(OP_stack_pointer > -1){ //연산자 스택이 빌때까지
          pushPost(popOP());//연산자 스택에서 pop하여 postfix 스택에 push한다.
        }

        printf("%g\n", calc());//postfix로 표현된 수식을 실제로 계산하여 출력한다.
        OP_stack_pointer = -1;//연산자 스택을 초기화한다.
        postfix_stack_pointer = -1;//postfix 스택을 초기화한다.

      }else if(key_value > -7){ //입력값이 =이 아닌 operation이면
        if(OP_stack_pointer > -1){//연산자 스택이 비지 않았으면
          if(getPriority(topOP()) >= getPriority(key_value) ){//현재 스택에 들어있는 연산자가 입력된 연산자보다 우선순위가 높으면
            pushPost(popOP());//연산자 하나를 pop해서 postfix 스택에 push하고
            pushOP(key_value);//입력된 연산자를 연산자 스택에 push한다.
          }else{//받은 연산자가 우선순위가 더 높으면
            pushOP(key_value);//연산자 스택에 push한다.
          }
        }else{//연산자 스택이 비었으면
          pushOP(key_value);//연산자 스택에 push한다.
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
