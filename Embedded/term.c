//Trig = PF6,

typedef enum {GO, , BACK, STOP, TURN_RIGHT, TURN_LEFT} car_state;
typedef enum {FREE, UP, DOWN, LEFT, RIGHT } surround_state;

typedef struct distance{
  int left, front_left, front, front_right, right;
}Distance;

surround_state ss = FREE;
car_state cs = STOP;


int main(){

  Distance dist;

  while(1){

  }
}

timer_handler(){//period 0.1s
  sensor_update();
  state_check();
  change_state();
}

void sensor_update(){
  encoder_update();
  usonic_update();
  psd_update();
  //
  //wait until value received
}

void encoder_update();
void usonic_update();
void psd_update();

void state_check(){

}

void
