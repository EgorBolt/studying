#include <stdio.h>
#include <pthread.h>
#include <errno.h>

void * print_text();

int main() {
	pthread_t thread;
	int i = 0;
	int err = 0;
	int err_join = 0;
	int msg_length = 256;
	char err_msg[256] = {0};
	int error_num = 0;
	int *join_exit;

	err = pthread_create(&thread, NULL, print_text, NULL);
	if (err) {
		strerror_r(err, err_msg, msg_length);
		fprintf(stderr, "Error in creating a child thread: %s\n", err_msg);
		return -1;
	}

	err_join = pthread_join(thread, (void**)&join_exit);
	if (err_join) {
		strerror_r(err_join, err_msg, msg_length);
		fprintf(stderr, "Can't join child: %s\n", err_msg);
		return -2;
	}
	
	printf("\npthread_join result: %d\n", *join_exit);
	free(join_exit);

	for (i = 0; i < 10; i++) {
		printf("Parrent prints: this is a text.\n");
	}
	
	return 0;
}

void * print_text() {
	int i = 0;
	for (i = 0; i < 10; i++) {
		printf("Child prints: this is a text.\n");
	}
	
	int *result = (int*)malloc(sizeof(int));
	*result = i;
	return (void*)result;
}	
