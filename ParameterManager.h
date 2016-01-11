/*Name: Gary Dhillon 
 * Date: Friday, January 23, 2015 
 * Assignment 1*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

enum Boolean
{
	false,
	true
};

enum param_t
{
	INT_TYPE,
	REAL_TYPE,
	BOOLEAN_TYPE,
	STRING_TYPE,
	LIST_TYPE
};

typedef struct Parameter Parameter;
typedef enum Boolean Boolean;
typedef struct ParameterList ParameterList;
typedef struct ParameterManager ParameterManager;
typedef enum param_t param_t;

union param_value
{
    int int_val;
    float real_val;
    Boolean bool_val;
    char *str_val;
    ParameterList *list_val;
};

struct Parameter
{
    char * key;
    Boolean req;
    union param_value value;
    struct Parameter * next;
    int ptype, initialized;
};

struct ParameterList
{
    char * value;
    int size;
    ParameterList * next;
};

struct ParameterManager
{
    Parameter * hashTable[100];
};

ParameterManager * PM_create(int size);/*Same as spec*/

int PM_destroy(ParameterManager *p);/*Same as spec*/

int PM_parseFrom(ParameterManager * p, FILE * fp, char comment);/*Same as spec*/

int PM_manage(ParameterManager *p, char *pname, param_t ptype, int required);/*Same as spec*/

int PM_hasValue(ParameterManager *p, char *pname);/*Same as spec*/

union param_value PM_getValue(ParameterManager *p, char *pname);/*Same as spec*/

char * PL_next(ParameterList *l);/*Same as spec*/

int hash(char *key);/*Input key string, calculates hash value and returns int to array location*/

void P_destroy(Parameter *p);/*Input parameter object, free parameter object*/

Boolean search(Parameter *p, char *pname);/*Search to see if a parameter with pname is currently being managed*/

ParameterList * insertNode(ParameterList * head, ParameterList * insert);/*Insert node into parameterlist*/

ParameterList * newNode(char * data);/*Create new parameter list node with data as value*/

void L_destroy(ParameterList * l);/*Free memory for parameter list*/
