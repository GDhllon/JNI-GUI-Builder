/*Name: Gary Dhillon 
 * Date: Friday, January 23, 2015 
 * Assignment 1*/

INFO:

This program was built indivdually by me throughout a semester for a software system development & integration course I was taking at the time. At this point the program is coded in two parts:

The first part, coded entirely in C, acts as a parser of files (a lex/yacc parser was also added later on in the course) designed to provide the layout for a gui window to be built.
The provided test.config file shows buttons and fields to be created, it is the job of this program to parse these details, store them in a data structure I designed and then to
bring this information into the Java section of the program utilizing the JNI library to do so.

In the second part of the program the fields and buttons to be made are resolved and the corresponding .java files for this new window are written by the program and can then be launched from
within the program. Also included in the java portion of the program is a robust GUI window for the opening/editing of .CONFIG files for use in the program.

By the end of the semester these windows being created by the program would have actionlisteners on their buttons that allowed the new window to act as a rudimentary database manager with buttons
that allowed the user to add,create and delete entries into a database through SQL integration.


FILES:

ParameterList.c - Contains all functions (from A1 spec and my helper functions, the inputs/outputs of these functions are commented in the file/header)

ParameterList.h - Contains all structs, function prototypes, enumerations and typedefs.

a2.c - Contains JNI functions.

Window.java - Main class file which initalizes main gui window and contains all methods for menu actions.

Dialogc.java - Contains the main method which calls Window class.

error.txt - holds error messages before they are printed in a new window.

Makefile - Contains commands to compile library 'libJNIpm.so', type 'make' to compile.

Icon folder - contains jpeg images used in main gui windo

test.config - SEE BELOW

****** Test folder - ********Note this program adds the expected actionlistener classes to each button, if the relevant class files are not found in the correct directory the
                             generated program will not run. If for some reason you are unable to run your program due to a java listener issue I have provided a test.config which you can
		             load in the compiler and compile/run to demonstrate that the program can indeed accept outside actionlisteners


Compiling/Running:

-Navigate to source directory

-Type 'make' which will compile the shared library libJNIpm.so as well as a host of relevant class files for each .java file 

-Type 'export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:.' to ensure java can load the shared library

-Type 'java Dialogc' to begin running the program

