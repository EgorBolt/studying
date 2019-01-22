#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define ERROR_CREATE_THREAD -11
#define ERROR_JOIN_THREAD -12
#define SUCCESS 15

void * helloWorld(void *args) {
	printf("Hey!\n");
	return SUCCESS;
}

int main() {
	pthread_t thread;
	int status;
	int status_addr;

	status = pthread_create(&thread, NULL, helloWorld, NULL);
	if (status != 0) {
		printf("Error cant create thread\n");
		exit(ERROR_CREATE_THREAD);
	}
	
	printf("Hello from main!\n");
	
	status = pthread_join(thread, (void**)&status_addr);
	printf("joined with address %d\n", status_addr);
	
	return 0;
}
