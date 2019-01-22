#include <stdio.h>
#include <pthread.h>
#include <errno.h>

void * print_text(void *p);
void * finish(void *p);

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
	}
	pthread_exit(NULL);
	
	return 0;
}

void * print_text(void *p) {
	pthread_cleanup_push(finish, NULL);
	char *str = "This is a text\n";
	while(1) {
		write(STDOUT_FILENO, str, 17);
		sleep(1);
	}
	pthread_cleanup_pop(1);
}	

void * finish(void *p) {
	printf("Finishing up print_text() function.\n");
}
