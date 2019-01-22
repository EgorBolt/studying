#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

int ready = 0;
pthread_mutex_t mute[3];

void * print_text(void *p);

int main() {
    pthread_t thread;
    pthread_mutexattr_t mattr;
    int i = 0;
    int err = 0;
    int msg_length = 256;
    char err_msg[256] = {0};
    
    pthread_mutexattr_init(&mattr);
    pthread_mutexattr_settype(&mattr, PTHREAD_MUTEX_ERRORCHECK);
    
    for (i = 0; i < 3; i++) {
        pthread_mutex_init(&mute[i], &mattr);
    }
    
    pthread_mutex_lock(&mute[2]);
    
    err = pthread_create(&thread, NULL, print_text, (void*)"Child\n");
    if (err) {
        strerror_r(err, err_msg, msg_length);
        fprintf(stderr, "Error: %s\n", err_msg);
        return -1;
    }
    
    while (ready == 0) {
        continue;
    }
    
    print_text("Parent\n");
    
    pthread_join(thread, NULL);
    
    for (i = 0; i < 3; i++) {
        pthread_mutex_destroy(&mute[i]);
    }
    
    return 0;
}

void * print_text(void *p) {
    int i = 0;
    int k = 1;
    
    if (ready == 0) {
        pthread_mutex_lock(&mute[1]);
        ready = 1;
        k = 0;
    }
    
    for (i = 0; i < 30; i++) {
        pthread_mutex_lock(&mute[k]);
        k = (k + 1) % 3;
        pthread_mutex_unlock(&mute[k]);
        if (k == 2) {
            printf("%s", (char*)p);
        }
        k = (k + 1) % 3;
    }
    pthread_mutex_unlock(&mute[2]);
    
    return NULL;
}


