CC = gcc
CFLAGS = -Wall -ansi -pedantic
OBJ = ParameterManager.o a2.o

Dialogc: libPath
	javac Dialogc.java

libPath: libJNIpm.so

libJNIpm.so: $(OBJ)
	$(CC) -shared -Wl,-soname,libJNIpm.so -I/home/gary/jdk1.8.0_20/include -I/home/gary/jdk1.8.0_20/include/linux -o libJNIpm.so a2.o ParameterManager.o


$(OBJ): ParameterManager.c a2.c yadc
	$(CC) -c a2.c ParameterManager.c -I/home/gary/jdk1.8.0_20/include -I/home/gary/jdk1.8.0_20/include/linux -fPIC

yadc: lex.yy.c y.tab.c
	gcc lex.yy.c y.tab.c -o yadc

lex.yy.c:
	lex a3.l

y.tab.c:
	yacc -d a3.y

clean:

	rm -f *.o


	
