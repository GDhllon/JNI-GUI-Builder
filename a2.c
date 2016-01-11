/*Name: Gary Dhillon 
 * Date: Friday, February 13, 2015 
 * Assignment 2*/

#include <stdio.h>
#include "ParameterManager.h"
#include <jni.h>
#include <unistd.h>

/*Takes filename to parse from, outputs parsed fields and buttons in string array*/
JNIEXPORT jobjectArray JNICALL Java_Window_getFields
  (JNIEnv * env, jobject obj, jstring fileName)
{
	FILE * file;
	const char * fName;
	ParameterManager * params;
	ParameterList * pList;
	ParameterList * pList2;
	char * names[100];
	char * name;
	jobjectArray args;
	int i, j, k, err;
	
	/*Redirect stderr to error file*/
	k = dup(fileno(stderr));
	freopen("error.txt", "a", stderr);
	
	
	
	params = PM_create(10);
	
	/*Parse title fields and buttons*/
	PM_manage(params,"title",STRING_TYPE,1);
	PM_manage(params,"fields",LIST_TYPE,1);
	PM_manage(params,"buttons",LIST_TYPE,1);
		
	fName = (*env)->GetStringUTFChars(env,fileName,0);
	file = fopen(fName, "rt");
	err = PM_parseFrom(params,file,'#');
	
	if(err==0)
	{
		return NULL;
	}
	
	names[0] = PM_getValue(params,"title").str_val;
	pList = PM_getValue(params,"fields").list_val;
	pList2 = PM_getValue(params,"buttons").list_val;
		
	args = (*env)->NewObjectArray(env,100, (*env)->FindClass(env, "java/lang/String"),(*env)->NewStringUTF(env, ""));
	
	/*Format string array with dividers*/
	name = PL_next(pList);
	i=1;
	while(name!=NULL)
	{
		names[i] = name;
		i++;
		name = PL_next(pList);
	}
	names[i] = "FFF";
	i++;	
	name = PL_next(pList2);
	while(name!=NULL)
	{
		names[i] = name;
		i++;
		name = PL_next(pList2);
	}
	names[i] = "BBB";
	j=i+1;
	
	/*Format string array to jstring[] array*/
	for(i=0;i<j;i++)
	{
		(*env)->SetObjectArrayElement(env,args,i,(*env)->NewStringUTF(env, names[i]));
	}
	
	PM_destroy(params);
	
	dup2(k, fileno(stderr));
	close(k);
	
	
	return(args);
	
	

	
}

/*Takes total number of buttons and fields, string array of button/field names, as well as filename string, outputs string array of button/field values*/
JNIEXPORT jobjectArray JNICALL Java_Window_getNames
  (JNIEnv * env, jobject obj, jobjectArray stringArray, jint num, jstring fileName)
{
	
	char * names[100];
	char * parsedValues[100];
	const char * name;
	const char *fName;
	FILE * file;
	jobjectArray args;
	ParameterManager * params;
	int err;
	int i,j,k;
	
	/*Redirect stderr to error file*/
	k = dup(fileno(stderr));
	freopen("error.txt", "a", stderr);
	
	params =PM_create(10);
	
	
	j = (int)num;
	/*Convert all jstring items to char* items*/
	for(i =0; i<j+3; i++)
	{
		jstring string = (*env)->GetObjectArrayElement(env, stringArray, i);
		name = (*env)->GetStringUTFChars(env,string, 0);
		names[i] = malloc(sizeof(char)*strlen(name) +1);
		strcpy(names[i], name);
	}
	
	/*Manage the names of buttons/fields*/
	for(i=1;i<j+3;i++)
	{
		if(strcmp(names[i], "FFF")!=0 && strcmp(names[i], "BBB")!=0)
		{
			PM_manage(params,names[i],STRING_TYPE,1);
		}
	}
	
	fName = (*env)->GetStringUTFChars(env,fileName,0);
	file = fopen(fName, "rt");
	
	err = PM_parseFrom(params, file, '#');
	
	/*If error, return early*/
	if(err==0)
	{
		return NULL;
	}
	
	/*Format string array for parsed values*/
	for(i=1;i<j+3;i++)
	{
		if(strcmp(names[i], "FFF")!=0 && strcmp(names[i], "BBB")!=0)
		{
			parsedValues[i-1] = PM_getValue(params, names[i]).str_val;
		}
		else
		{
			parsedValues[i-1] = names[i];
		}
	}
	/*convert string array to jstring[] array*/
	args = (*env)->NewObjectArray(env,100, (*env)->FindClass(env, "java/lang/String"),(*env)->NewStringUTF(env, ""));	
	for(i=0;i<j+2;i++)
	{
		
		(*env)->SetObjectArrayElement(env,args,i,(*env)->NewStringUTF(env, parsedValues[i]));
	}
		
	
	PM_destroy(params);
	
	dup2(k, fileno(stderr));
	close(k);
	
	return (args);
	 
}
