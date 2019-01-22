#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

pthread_mutex_t m;
pthread_cond_t cond;

void * print_text(void *p);

int main() {
    pthread_t thread;
    int i = 0;
    int err = 0;
    char err_msg[256] = {0};
    int msg_length = 256;
    
    pthread_mutex_init(&m, NULL);
    pthread_cond_init(&cond, NULL);
    
    pthread_mutex_lock(&m);
    
    err = pthread_create(&thread, NULL, print_text, NULL);
    if (err) {
        strerror_r(err, err_msg, msg_length);
        fprintf(stderr, "Error: %s\n", err_msg);
        return -1;
    }
    
    for (i = 0; i < 10; i++) {
        pthread_cond_signal(&cond);
        pthread_cond_wait(&cond, &m);
        printf("Parent\n");
    }
    
    pthread_mutex_unlock(&m);
    pthread_cond_signal(&cond);
    
    pthread_join(thread, NULL);
    
    pthread_mutex_destroy(&m);
    pthread_cond_destroy(&cond);
    
    return 0;
}

void * print_text(void *p) {
    int i = 0;
    
    pthread_mutex_lock(&m);
    
        for (i = 0; i < 10; i++) {
        pthread_cond_signal(&cond);
        pthread_cond_wait(&cond, &m);
        printf("Child\n");
    }
    
    pthread_mutex_unlock(&m);
    pthread_cond_signal(&cond);
    
    return NULL;
}
