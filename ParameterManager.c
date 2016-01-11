/*Name: Gary Dhillon 
 * Date: Friday, January 23, 2015 
 * Assignment 1*/

#include "ParameterManager.h"

ParameterManager * PM_create(int size)/*Allocate memory for PM obj, initialize all places in hashtable to null*/
{
    
    ParameterManager *p;
    int i;
    
    p = malloc(sizeof(ParameterManager));
    
    if(p!=NULL)
    {
        for(i=0;i<100;i++)
        {
            p->hashTable[i] = NULL;
        }
    }
    
    
    return(p);
}
    
int PM_destroy(ParameterManager *p)/*Free PM object*/
{
    
    int i;
    
    for(i=0; i<100; i++)
    {
        if(p->hashTable[i]!=NULL)
        {
            P_destroy(p->hashTable[i]);
        }
    }
    
    free(p);
    
    return 1;
}

void P_destroy(Parameter *p)/*Free Parameter Objects in hash table slot*/
{
    
    if(p->next != NULL)
    {
        P_destroy(p->next);
    }
    if(p->ptype == 4)
    {
        L_destroy(p->value.list_val);
    }
    if(p->ptype == 3)
    {
        free(p->value.str_val);
    }
    free(p->key);
    free(p);
    
}

void L_destroy(ParameterList * l)/*free list*/
{
    if(l==NULL)
    {
        return;
    }
    if(l->next!=NULL)
    {
        L_destroy(l->next);
    }
    
    free(l->value);
    free(l);
}

int hash(char *key)/*Find hash table location*/
{
    
    int value;
    char *ptr;
    value = 0;
    
    for(ptr = key; *ptr!='\0'; ptr++)
    {
        value = value + *ptr;
    }
    
    value = value % 100;
    
    return value;

}

int PM_manage(ParameterManager *p, char *pname, param_t ptype, int required)
{
    
    int loc;
    Boolean tf;
    Parameter * new;
    
    if(p==NULL)
    {
        return 0;
    }
    
    loc = hash(pname);/*Find possible location for parameter, check to see if it already is being managed*/
    tf = search(p->hashTable[loc], pname);
    
    if(tf == true)/*Duplicate name, return 0*/
    {
        return 0;
    }
    else/*If it is not already being managed, allocate new space for the parameter and store ptype, name and store the parameter in the hashTable*/
    {
        new = malloc(sizeof(Parameter));
        new->key = malloc(sizeof(char)*(strlen(pname))+1);
        strcpy(new->key, pname);
        new->ptype = ptype;
        new->req = required;
        new->initialized = 0;
        new->next = NULL;
        
        if(p->hashTable[loc] == NULL)
        {
            p->hashTable[loc] = new;
        }
        else
        {
            new->next = p->hashTable[loc];
            p->hashTable[loc] = new;
        }
        
        return 1;
    }
        
    
}

Boolean search(Parameter *p, char *pname)/*Returns true if parameter is already in the table, false otherwise*/
{
    int i;
    if(p==NULL)
    {
        return false;
    }
    else
    {
        i = strcmp(p->key, pname);
        if(i!=0 && p->next!=NULL)
        {
            while(p->next!=NULL && i!=0)
            {
                p = p->next;
                i = strcmp(p->key, pname);
                if(i==0)
                {
                    return true;
                }
            }
        }
        else if(i!=0 && p->next==NULL)
        {
            return false;
        }
        else if(i==0)
        {
            return true;
        }
        return false;
    }
                

}

int PM_hasValue(ParameterManager *p, char *pname)/*Is the parameter's initialized variable set?*/
{
    int loc, i;
    Boolean tf;
    Parameter * temp;
    
    loc = hash(pname);
    tf = search(p->hashTable[loc], pname);
    
    if(tf == false)
    {
        return 0;
    }
    
    else
    {
        i = strcmp(p->hashTable[loc]->key, pname);
        
        if(i ==0)
        {
            if(p->hashTable[loc]->initialized == 0)
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
        
        else
        {
            if(p->hashTable[loc]->next == NULL)
            {
                return 0;
            }
            else
            {
                i=1;
                temp = p->hashTable[loc];
                while(i!=0 && temp!=NULL)
                {
                    temp = temp->next;
                    if(temp!=NULL)
                    {
                        i = strcmp(p->hashTable[loc]->key, pname);
                        if(i==0)
                        {
                            if(p->hashTable[loc]->initialized == 0)
                            {
                                return 0;
                            }
                            else
                            {
                                return 1;
                            }
                        }
                    }
                }
                return 0;
            }
        }
    }
}           

union param_value PM_getValue(ParameterManager *p, char *pname)/*Return union value*/
{
    int loc;
    Parameter * temp;
    union param_value val;

    loc = hash(pname);
    temp = p->hashTable[loc];
    while(strcmp(temp->key, pname)!=0 && temp!=NULL)
    {
        temp = temp->next;
    }
    
    if(temp!=NULL){     
        return(temp->value);
    }
    fprintf(stderr, "No value found in this location using getValue, returning uninitialized param value\n");
    val.str_val = NULL;
    return(val);
}
    
char * PL_next(ParameterList *l)
{
    int i, j;
    if(l==NULL)
    {
        return NULL;
    }
    i=0;
    j = l->size;
    l->size++;
    while(i<j)
    {
        l = l->next;
        i++;
    }
    if(l!=NULL)
    {
        return l->value;
    }
    else
    {
        return NULL;
    }
}   

int PM_parseFrom(ParameterManager * p, FILE * fp, char comment)
{
    int flag;
    int c, i, j, com, loc;
    char * key;
    char * strVal;
    char * token;
    char buf[2];
    Boolean check;
    ParameterList * head;
    ParameterList * temp;
    Parameter * t;
    t= NULL;
    head = NULL;
    temp = NULL;
    
    buf[1] = '\0';
    
    com = comment;
    
    /*Flags
     * 0-Looking for alphanumeric character
     * 1-Looking for '='
     * 2-Looking for value
     * 3-Looking for semicolon*/
     
     flag = 0;
     
     do
     {       
         c = fgetc(fp);
         
         if(c==' ' || c == '\n')
         {
             
         }
         else if(c == com)
         {
             while(c!='\n' && c!=EOF)
             {
                 c = fgetc(fp);
             }
         }
         else if((c>=65 && c<=90) || (c>=97 && c<=122 )|| (c>=48 && c<=57) || (c==95) || c==45)/*Alphanumeric character, '-' or '_' detected can be a name or value*/
         {
             fseek(fp, -1, SEEK_CUR);/*Put the character back*/
             switch(flag)
             {
                 case 0:
                 i=0;
                 do
                 {
                     c = fgetc(fp);
                     i++;
                 }while((c>=65 && c<=90) || (c>=97 && c<=122 )|| (c>=48 && c<=57) || (c==95));/*take characters until you reach a non alphanumeric character*/
                 key = malloc(sizeof(char)*i);
                 fseek(fp, -i, SEEK_CUR);/*jump back to take the string*/
                 fgets(key, i, fp);
                 flag = 1;
                 break;
                 
                 case 1:
                 fprintf(stderr, "Parse Error: Expecting '=' however encountered alphanumeric/underscore character\n");
                 return 0;
                 break;
                 
                 case 2:
                 i=0;
                 do
                 {
                     c = fgetc(fp);
                     i++;
                 }while((c>=65 && c<=90) || (c>=97 && c<=122 )|| (c>=48 && c<=57) || (c==95) || c==46 || c==45);/*take characters until you reach a non alphanumeric character*/
                 strVal = malloc(sizeof(char)*i);
                 fseek(fp, -i, SEEK_CUR);/*jump back to take the string*/
                 for(j=0; j<i-1; j++)
                 {
                    buf[0] = fgetc(fp);
                    strcat(strVal, buf);
                 }
                 flag = 3;
                 break;
                 
                 case 3:
                 fprintf(stderr, "Parse Error: Expecting ';' however encountered alphanumeric/underscore character\n");
                 return 0;
                 break;  
            }
         }
         
         else if(c == '=')/*If we expect an '=' sign keep moving*/
         {
             switch(flag)
             {
                 case 0:
                 fprintf(stderr, "Parse Error: Expecting alphanumeric character however encountered '=' character\n");
                 return 0;
                 break;
                 
                 case 1:
                 flag = 2;
                 break;
                 
                 case 2:
                 fprintf(stderr, "Parse Error: Expecting alphanumeric/'{' character however encountered '=' character\n");
                 return 0;
                 break;
                 
                 case 3:
                 fprintf(stderr, "Parse Error: Expecting ';' however encountered '='\n");
                 return 0;
                 break;
             }
        }
        
        else if(c=='"')/*Parse a string, only flag 2 is valid*/
        {
            switch(flag)
             {
                 case 0:
                 fprintf(stderr, "Parse Error: Expecting alphanumeric character however encountered double quote character \n");
		 return 0;
                 break;
                 
                 case 1:
                 fprintf(stderr, "Parse Error: Expecting '=' however encountered double quote character \n");
                 return 0;
                 break;
                 
                 case 2:
                 i=1;
                 strVal = malloc(sizeof(char)*i);
                 strcpy(strVal, "\0");
                 buf[0] = ' ';
                 while(buf[0]!='"')
                 {
                    buf[0] = fgetc(fp);
                    if(buf[0] != '"' && buf[0]!=com)
                    {
                        strcat(strVal, buf);
                        i++;
                        strVal = realloc(strVal, i);
                    }
                    else if(buf[0] == com)
                    {
                        while(buf[0]!='\n')
                        {
                            buf[0] = fgetc(fp);
                        }
                    }
                        
                 }
                 flag = 3;
                 break;
                 
                 case 3:
                 fprintf(stderr, "Parse Error: Expecting ';' however encountered encountered double quote character \n");
                 return 0;
                 break;
            }
        }
        
        else if(c == '{')/*Parse a list only flag 2 is valid*/
        {
            switch(flag)
            {
                case 0:
                fprintf(stderr, "Parse Error: Expecting alphanumeric character however encountered '{' character\n");
                return 0;
                break;
                
                case 1:
                fprintf(stderr, "Parse Error: Expecting '=' character however encountered '{' character\n");
                return 0;
                break;
                
                case 2:
                i=1;
                strVal = malloc(sizeof(char)*i);
				buf[0] = '\0';
				strcpy(strVal,"\0");
                while(buf[0]!='}')/*read until end of list*/
                {
                    buf[0] = fgetc(fp);
                    if(buf[0]== '"')
                    {
                        do{
                            buf[0] = fgetc(fp);
                            if(buf[0]!='"'){
                                i++;
                                strVal = realloc(strVal, i);
                                strcat(strVal, buf);
                            }
                        }while(buf[0]!='"');
                        i++;
                        strVal = realloc(strVal, i);
                        strcat(strVal, ",");
                    }
                    else if(buf[0] == com)
                    {
                        while(buf[0]!='\n')
                        {
                            buf[0] = fgetc(fp);
                        }
                    }
                        
                }
                if(i==1)/*Was the list empty?*/
                {
                    strcpy(strVal, "\"");
                }
                flag = 3;
                break;
                
                case 3:
                fprintf(stderr, "Parse Error: Expecting ';' character however encountered '{' character\n");
                return 0;
                break;
             }
        }
        
        else if(c == ';')/*Assign values if the parameter is managed*/
        {
            switch(flag)
            {
                case 0:
                fprintf(stderr, "Parse Error: Expecting alphanumeric character however encountered ';' character\n");
                return 0;
                break;
                
                case 1:
                fprintf(stderr, "Parse Error: Expecting '=' character however encountered ';' character\n");
                return 0;
                break;
                
                case 2:
                fprintf(stderr, "Parse Error: Expecting '{' character however encountered ';' character\n");
                return 0;
                break;
                
                case 3:/*Assign values to parameters of they are currently being managed*/
                loc = hash(key);
                flag = 0;
                check = search(p->hashTable[loc], key);/*Check if the key is currently being managed, if true see which type of value it requires*/
                if(check == true)
                {
                    t = p->hashTable[loc];
                    while(strcmp(t->key, key)!=0)
                    {
                        t = t->next;
                    }
                    switch(t->ptype)
                    {
                        case 0:/*int type*/
                        t->value.int_val = atoi(strVal);
                        t->initialized = 1;
                        break;
                        
                        case 1:/*float type*/
                        t->value.real_val = atof(strVal);
                        t->initialized = 1;
                        break;
                        
                        case 2:/*bool type*/
                        if(strcmp(strVal, "true") == 0)
                        {
                            t->value.bool_val = true;
                            t->initialized = 1;
                        }
                        else if(strcmp(strVal, "false") == 0)
                        {
                            t->value.bool_val = false;
                            t->initialized = 1;
                        }
                        break;
                        
                        case 3:/*String type*/
                        t->value.str_val = malloc(sizeof(char)*(strlen(strVal))+1);
                        strcpy(t->value.str_val, strVal);
                        t->initialized = 1;
                        break;
                        
                        case 4:/*list type*/
                        if(strcmp("\"", strVal) !=0)
                        {
                            token = strtok(strVal, ",");
                            while(token!=NULL)
                            {
                                temp = newNode(token);
                                head = insertNode(head, temp);
                                token = strtok(NULL, ",");
                            }
                            head->size = 0;
                            t->value.list_val = head;
                            head = NULL;
                            temp = NULL;
                            t->initialized = 1;
                        }
                        else
                        {
                            t->initialized = 1;
                            t->value.list_val = NULL;
                        }
                        break;
                    }
                    free(strVal);
                    free(key);
                }
                else
                {
                    free(strVal);
                    free(key);
                }
            }
        }
                         
    }while(c!=EOF);/*parse until eof*/
    
    
    for(i=0; i<100; i++)/*Make sure all required parameters are initialized with values*/
    {
        if(p->hashTable[i]!=NULL)
        {
            if(p->hashTable[i]->initialized == 0 && p->hashTable[i]->req == true)
            {
                fprintf(stderr, "A required parameter has not been found in the file and has not been assigned a value!\n");
                return 0;
            }
        }
    }  
    return 1;
}    
                 
ParameterList * newNode(char * data)/*Creates new parameter list node*/
{
    ParameterList * node;
    node = malloc(sizeof(ParameterList));
    node->value = malloc(sizeof(char)*strlen(data)+1);
    strcpy(node->value, data);
    node->next = NULL;
    
    return node;
}            
         
ParameterList * insertNode(ParameterList * head, ParameterList * insert)/*insert parameter list node*/
{
    ParameterList * copy;
    copy = head;
    if(head == NULL)
    {
        return insert;
    }
    else
    {
        while(copy->next!=NULL)
        {
            copy = copy->next;
        }
        
        copy->next = insert;
        
        return head;
    }
}    
            
