%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char * buttonList[100];
char * fieldList[100];
char * fieldTypes[100];
char * buttonListeners[100];
char * str;
char *tmp;
char * title;
char * dir;
int buttonCount, fieldCount, check, listeners, types;

int yydebug=0;

void yyerror(const char *str)
{
	fprintf(stderr,"error: %s\n",str);
}

int yywrap()
{
	return 1;
}

int arrayCheck(char * list[], char * item)
{
	int i;
	
	for(i=0;i<100;i++)
	{
		if(list[i]!=NULL)
		{
			if(strcmp(list[i], item)==0)
			{
				return i;
			}
		}
	}
	
	return -1;
}

main()
{
	int err;
	buttonCount = 0;
	fieldCount = 0;
	listeners = 0;
	types = 0;
	check = 1;
	int i, j, k;
	yyparse();
	err = mkdir(title);
	printf("%d\n", err);
	dir = malloc(sizeof(char)*100);
	strcpy(dir, "./");
	dir = strcat(dir,title);
	dir = strcat(dir, "/");
	dir = strcat(dir, title);
	dir = strcat(dir, ".java");
	printf("%s\n", dir);
	
	FILE * fp = fopen(dir, "w+");
	perror("Error");
	if(fp==NULL)
	{
		printf("YO MAN\n");
	}
	printf("yoo?\n");
	fprintf(fp, "import javax.swing.JFrame;\n");
	printf("yoo!\n");
	fprintf(fp, "import javax.swing.JButton;\n");
	fprintf(fp,"import javax.swing.JTextField;\n");
	fprintf(fp,"import javax.swing.JPanel;\n");
	fprintf(fp,"import javax.swing.JLabel;\n");
	fprintf(fp,"import java.awt.BorderLayout;\n");
	fprintf(fp,"import java.awt.GridLayout;\n");
	fprintf(fp,"import javax.swing.JTextArea;\n");
	fprintf(fp,"import javax.swing.JScrollPane;\n");
	fprintf(fp,"import java.awt.Font;\n");
	fprintf(fp,"import javax.swing.event.DocumentEvent;\n");
	fprintf(fp,"import javax.swing.event.DocumentListener;\n");
	fprintf(fp," ");
	fprintf(fp,"public class %s extends JFrame implements %sFieldEdit{\n", title, title);
	fprintf(fp," ");
	for(i =0; i<fieldCount; i++)
	{
		fprintf(fp,"        public JLabel field%d = new JLabel(\"%s\");\n",i,fieldList[i]);
		fprintf(fp,"        public JTextField inp%d = new JTextField(15);\n", i);
	}
	fprintf(fp,"        public JTextArea results;\n");
	
	for(i=0; i<buttonCount; i++)
	{
		fprintf(fp,"        public JButton button%d = new JButton(\"%s\");\n",i,buttonList[i]);
	}
	fprintf(fp,"    public static void main(String args[]) {\n");
	fprintf(fp,"        %s t = new %s();\n", title, title);
	fprintf(fp,"        t.setVisible(true);\n");
	fprintf(fp,"    }");
	fprintf(fp,"    public static int WIDTH = 500;\n");
	fprintf(fp,"    public static int HEIGHT = 600;\n");
	fprintf(fp,"    public %s(){", title);
	fprintf(fp," ");
	fprintf(fp,"        super(\"%s\");\n", title);
	fprintf(fp,"        setSize(WIDTH,HEIGHT);\n");
	fprintf(fp,"        setLayout(new BorderLayout());\n");
	fprintf(fp,"        JPanel buttonPanel = new JPanel();\n");
	fprintf(fp,"        JPanel fieldPanel = new JPanel();\n");
	
	fprintf(fp," ");
		
	fprintf(fp,"        fieldPanel.setLayout(new GridLayout(0,2));\n");
	
		
	for(i =0; i<fieldCount; i++)
	{
		fprintf(fp,"        fieldPanel.add(field%d);\n", i);
		fprintf(fp,"        fieldPanel.add(inp%d);\n", i);
	}
	
	for(i=0; i<buttonCount; i++)
	{
		fprintf(fp,"        button%d.addActionListener(new %s(this));\n", i, buttonListeners[i]);
		fprintf(fp,"        buttonPanel.add(button%d);\n", i);
	}
		
	fprintf(fp,"        add(fieldPanel, BorderLayout.NORTH);\n");
	fprintf(fp,"        add(buttonPanel, BorderLayout.CENTER);\n");
	fprintf(fp,"        results = new JTextArea(20,20);\n");
    fprintf(fp,"        Font font = new Font(\"Verdana\",Font.BOLD,16);\n");
    fprintf(fp,"        results.setFont(font);\n");
    fprintf(fp,"        JScrollPane scroll = new JScrollPane(results);\n");
    fprintf(fp,"        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);\n");
    fprintf(fp,"        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);\n");
	fprintf(fp,"        add(scroll, BorderLayout.SOUTH);\n");
	fprintf(fp,"    }");
		
    for(i=0; i<fieldCount;i++)
	{
		fprintf(fp,"\n\n");
		fprintf(fp,"    public void setDC%s(String str) {\n", fieldList[i]);
		fprintf(fp,"        field%d.setText(str);\n", i);
		fprintf(fp,"    }");
			
		fprintf(fp,"\n\n");
		fprintf(fp,"    public String getDC%s() {\n", fieldList[i]);
		fprintf(fp,"        String str = field%d.getText();\n", i);
		fprintf(fp,"        return str;\n");
		fprintf(fp,"    }");
	}
		
	fprintf(fp,"\n\n");
	fprintf(fp,"    public void appendToStatusArea(String message){\n");
	fprintf(fp,"        results.append(message);\n");
	fprintf(fp,"    }\n");
	fprintf(fp,"}\n");
	
	fclose(fp);
	free(dir);
	
	dir = malloc(sizeof(char)*100);
	strcpy(dir, "./");
	dir = strcat(dir,title);
	dir = strcat(dir, "/");
	dir = strcat(dir, title);
	dir = strcat(dir, "Field");
	dir = strcat(dir, "Edit");
	dir = strcat(dir, ".java");
	fp = fopen(dir, "wb");
		
	fprintf(fp,"public interface %sFieldEdit{\n", title);
	for(i=0; i<fieldCount;i++)
	{
		fprintf(fp,"\n\n");
		fprintf(fp,"    public void setDC%s(String str);\n", fieldList[i]);
			
		fprintf(fp,"\n\n");
		fprintf(fp,"    public String getDC%s();\n", fieldList[i]);
	}
		
	fprintf(fp,"\n\n");
	fprintf(fp,"    public void appendToStatusArea(String message);\n");
	fprintf(fp,"}\n");
	fclose(fp);
	
	FILE * t = fopen("title", "w");
	
	fprintf(t, "%s\n", title);
	
	for(i=0;i<buttonCount;i++)
	{
		fprintf(t, "%s\n", buttonListeners[i]);
	}
	
	fclose(t);
}

%}

%token WORD QUOTE OBRACE EBRACE SEMICOLON FIELDTOK COMMA EQUAL BUTTONTOK TITLETOK STRING

%union 
{
	char *string;
	char *string2;
}

%token <string> WORD
%token <string2> STRING

%%

commands:
	|	 
	commands command SEMICOLON
	;


command:
	title
	|
	field_list 
	|
	button_list
	|
	declaration
	;
	
	title:
	TITLETOK EQUAL quotedname
	{
		check =2;
	}

	field_list:
	FIELDTOK EQUAL listcontent
	{
		check =3;
	}
	;
	
	button_list:
	BUTTONTOK EQUAL listcontent
	{
		check =4;
	}
	;
	
	declaration:
	WORD EQUAL quotedname
	{
		if(arrayCheck(fieldList, $1)!=-1)
		{
			fieldTypes[arrayCheck(fieldList, $1)] = str;
			types++;
		}
		else if(arrayCheck(buttonList, $1)!=-1)
		{
			buttonListeners[arrayCheck(buttonList, $1)] = str;
			listeners++;
		}
	}
	;

listcontent:
	OBRACE liststatements EBRACE 

quotedname:
	STRING
	{
		if(check==1)
		{
			tmp = $1;
			for(;*tmp;++tmp)
			{
				if(*tmp==' ')
				{
					*tmp = '_';
				}
				printf("%c\n", *tmp);
			}
			title = $1;
		}
		else if(check==2)
		{
			fieldList[fieldCount] = $1;
			fieldCount++;
		}
		else if(check == 3)
		{
			buttonList[buttonCount] = $1;
			buttonCount++;
		}
		else if(check==4)
		{
			str = $1;
		}
	}
	;

liststatements:
	|
	liststatements quotedname COMMA
	|
	liststatements quotedname
	;
