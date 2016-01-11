#include <stdio.h>
#include <string.h>
#include "a3.h"
#include <jni.h>
#include <unistd.h>


JNIEXPORT jobjectArray JNICALL Java_Window_lexParse(JNIEnv * env, jobject obj, jstring fileName)
{
	const char * fName;
	fName = (*env)->GetStringUTFChars(env,fileName,0);
	
	freopen(fName,"rt", stdin);
	
	yyparse();

	
