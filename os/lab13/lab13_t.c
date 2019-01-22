#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

sem_t p;
sem_t c;

void * print_text(void *a);

int main() {
    pthread_t pthread;
    int i = 0;
    int err = 0;
    char err_msg[256] = {0};
    int msg_length = 256;
    
    sem_init(&p, 0, 0);
    sem_init(&c, 0, 1);
    
    err = pthread_create(&pthread, NULL, print_text, NULL);
    if (err) {
        strerror_r(err, err_msg, msg_length);
        fprintf(stderr, "Error: %s\n", err_msg);
        return -1;
    }
    
    for (i = 0; i < 10; i++) {
        sem_wait(&c);
        printf("parent\n");
        sem_post(&p);
    }
    
    pthread_join(pthread, NULL);
    
    sem_destroy(&p);
    sem_destroy(&c);
    
    return 0;
}

void * print_text(void *a) {
    int i = 0;
    
    for (i = 0; i < 10; i++) {
        sem_wait(&p);
        printf("child\n");
        sem_post(&c);
    }
    
    return NULL;
}
