#include <stdio.h>
#include <pthread.h>
#include <errno.h>

void * print_text();

int main() {
	pthread_t thread;
	int i = 0;
	int err = 0;
	int msg_length = 256;
	char err_msg[256] = { 0 };
	int error_num = 0;
	
	for (i = 0; i < 10; i++) {
		printf("Parent prints: this is a text.\n");
	}
	err = pthread_create(&thread, NULL, print_text, NULL);
	if (err) {
		strerror_r(err, err_msg, msg_length);
		fprintf(stderr, "Error in creating a child thread: %s\n", 
err_msg);
		return -1;
	}
	pthread_exit(NULL);	
		
	return 0;
}

void * print_text() {
	int i = 0;
	for (i = 0; i < 10; i++) {
		printf("Child prints: this is a text.\n");
	}
	
	return;
}
