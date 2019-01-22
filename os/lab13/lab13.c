#include <pthread.h>
#include <stdio.h>
#include <semaphore.h>
#include <string.h>

sem_t p;
sem_t c;

void* print_text(void * str){
    for (int i = 0; i < 10; i++){
      sem_wait(&p);
      printf("Child\n");
      sem_post(&c);
    }
    return NULL;
}

int main(int argc, char* argv){
    pthread_t thread;
    int i = 0;
    int err = 0;
    int msg_length = 256;
	char err_msg[256] = {0};

    sem_init(&p, 0, 0);
    sem_init(&c, 0, 1);
    
    err = pthread_create(&thread, NULL, print_text, NULL);
	if (err) {
		strerror_r(err, err_msg, msg_length);
		fprintf(stderr, "Error in creating a child thread: %s\n", err_msg);
		return -1;
	}

    for(int i = 0; i < 10; i++){
      sem_wait(&c);
      printf("Parent\n");
      sem_post(&p);
    }

    pthread_join(thread, NULL);

    sem_destroy(&p);
    sem_destroy(&c);

    pthread_exit(NULL);
}
