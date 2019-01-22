#include <stdio.h>
#include <unistd.h>

int main() {
	char i = 'a';
	write(STDOUT_FILENO, &i, 1);
	return 0;
}


