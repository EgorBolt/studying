#include <stdio.h>
#include <pthread.h>
#include <errno.h>
#include <unistd.h>

void * print_text(void *p);

int main() {
	pthread_t thread;
	int err = 0;
	int err_cancel = 0;
	int msg_length = 256;
	char err_msg[256] = {0};
	
	err = pthread_create(&thread, NULL, print_text, NULL);
	if (err) {
		strerror_r(err, err_msg, msg_length);
		fprintf(stderr, "Error in creating a child thread: %s\n", err_msg);
		return -1;
	}
	
	sleep(2);
	err_cancel = pthread_cancel(thread);
	if (err_cancel) {
		strerror_r(err_cancel, err_msg, msg_length);
		fprintf(stderr, "Something wrong happened while canceling the thread: %s\n",
			 err_msg);
		return -2;
	}
	
	return 0;
}

void * print_text(void *p) {
	char i = 'a';
	while(1) {
		printf("%c", i);
		//write(STDOUT_FILENO, &i, 1);
		i++;
		if (i > 'z') {
			i = 'a';
		}
	}
}	
