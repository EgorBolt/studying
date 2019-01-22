#include <stdio.h>
#include <pthread.h>
#include <errno.h>

void * print_text(void * text);

int main() {
	pthread_t thread[4];
	int i = 0;
	int err = 0;
	int msg_length = 256;
	char err_msg[256] = {0};
	int error_num = 0;
	static char *texts[4];
	
	texts[0] = "Text1";
	texts[1] = "Text2";
	texts[2] = "Text3";
	texts[3] = "Text4";

	for (i = 0; i < 4; i++) {
		err = pthread_create(&thread[i], NULL, print_text, (void *)texts[i]);
		if (err) {
			strerror_r(err, err_msg, msg_length);
			fprintf(stderr, "Error in creating a child thread: %s\n",
								err_msg);
			return -1;
		}
	}
	pthread_exit(NULL);
	
	return 0;
}

void * print_text(void * text) {
	int i = 0;

	for (i = 0; i < 5; i++) {
		printf("%s\n", (char *)text);
	}
	
	return;
}
