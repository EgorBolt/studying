#define NUM_STEPS 2000000
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct countPart {
    double result;
    int iteration_point;
    int amount_to_count;
} cp;

void * count(void * p);

int main(int argc, char** argv) {
    pthread_t *thread;
    double pi = 0;
    int i = 0;
    cp * countPart = NULL;
    cp * join_exit;
    int err = 0;
    int err_join = 0;
    char err_msg[256] = {0};
    int msg_length = 256;
    int excess = 0;

    if (argc != 2) {
	printf("Wrong amount of arguments\n");
	return -4;
    }

    int thread_amount = atoi(argv[1]);
    if (thread_amount > 0) {

        thread = (pthread_t*)malloc(sizeof(pthread_t) * thread_amount);
        for (i = 0; i < thread_amount; i++) {
            countPart = (cp*)malloc(sizeof(cp));
            countPart->iteration_point = i;
            countPart->amount_to_count = NUM_STEPS;
            err = pthread_create(&thread[i], NULL, count, countPart);
            if (err) {
                    strerror_r(err, err_msg, msg_length);
                    fprintf(stderr, "Error in creating a child thread: %s\n", err_msg);
                    return -1;
            }
        }

        for (i = 0; i < thread_amount; i++) {
            err_join = pthread_join(thread[i], (void**)&join_exit);
            if (err_join) {
                strerror_r(err_join, err_msg, msg_length);
                fprintf(stderr, "Can't join child: %s\n", err_msg);
                return -2;
            }
            pi += join_exit->result;
        }

    } else {
        printf("Can't count pi because amount of threads is 0\n");
        return -3;
    }

    pi = pi * 4.0;
    printf("pi done - %.15g \n", pi); 
    
    return 0;
}

void * count(void * p) {
    cp *c = (cp *)p;
    double pi = 0;
    int begin = c->amount_to_count * c->iteration_point;
    int end = c->amount_to_count + begin;
    double i = 0;
    
    for (i = begin; i < end; i++) {
        pi += 1.0/(i * 4.0 + 1.0);
        pi -= 1.0/(i * 4.0 + 3.0);
    }
    c->result = pi;
    return (void*)c;
}
