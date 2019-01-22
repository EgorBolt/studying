#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

pthread_mutex_t m1 = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

void* print_text(void * p);

int main () {
    pthread_t thread;
    int err = 0;
	int msg_length = 256;
	char err_msg[256] = {0};

    pthread_mutex_lock(&m1);
    
    err = pthread_create(&thread, NULL, print_text, NULL);
	if (err) {
		strerror_r(err, err_msg, msg_length);
		fprintf(stderr, "Error in creating a child thread: %s\n", err_msg);
		return -1;
	}

    for(int i = 0; i < 10; i++){
      pthread_cond_signal(&cond);
      pthread_cond_wait(&cond, &m1);
      write(STDOUT_FILENO, "Message : parent\n", 17);
    }

    pthread_mutex_unlock(&m1);
    pthread_cond_signal(&cond);

    pthread_join(thread, NULL);
    
    pthread_mutex_destroy(&m1);

    return 0;
}

void* print_text(void * p) {
    pthread_mutex_lock(&m1);
    
    for(int i = 0; i < 10; i++){
      pthread_cond_signal(&cond);
      pthread_cond_wait(&cond, &m1);
      write(STDOUT_FILENO, "Message : child\n", 16);
    }
    
    pthread_mutex_unlock(&m1);
    pthread_cond_signal(&cond);
    
    return NULL;
}
