#define PRADDR(A, B) printf("texts element %d at %p\n", B, A)
#include <stdio.h>
#include <pthread.h>
#include <errno.h>

extern  _etext;
extern _edata;
extern _end;

void * print_text(void * text);

int main() {
	pthread_t thread[4];
	int i = 0;
	int err = 0;
	int msg_length = 256;
	char err_msg[256] = {0};
	int error_num = 0;
	static char *texts[4];
	char *target;
	
	texts[0] = "Text1a";
	texts[1] = "Text2b";
	texts[2] = "Text3c";
	texts[3] = "Text4d";

	printf("end of user text: %p\n", &_etext);
	printf("end of initialized data: %p\n", &_edata);
	printf("end of uninitialized data: %p\n", &_end);
	for (i = 0; i < 4; i++) {
		PRADDR(texts[i], i);
	}
	
	/*
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
	*/
	return 0;
}

void * print_text(void * text) {
	int i = 0;

	for (i = 0; i < 5; i++) {
		printf("%s\n", (char *)text);
	}
	
	return;
}
