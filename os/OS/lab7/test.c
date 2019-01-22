#include <stdio.h>
#include <stdlib.h>

int main(int argc, char ** argv) {
	int i = 0;
	int thread_amount = atoi(argv[1]);
	int massive[thread_amount];
	
	for (i = 0; i < thread_amount; i++) {
		massive[i] = i;
	}
	for (i = 0; i < thread_amount; i++) {
		printf("%d ", massive[i]);
	}

	return 0;
}
