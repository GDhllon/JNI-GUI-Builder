%{
#include "y.tab.h"
#include <stdio.h>
%}

%%

fields			return FIELDTOK;
buttons			return BUTTONTOK;
title			return TITLETOK;
"\""(\\.|[^"])*"\""		yylval.string2=strtok(strdup(yytext), "\""); return STRING;
[a-zA-Z][a-zA-Z0-9]*    yylval.string=strdup(yytext); return WORD;
\{                      return OBRACE;
\}                      return EBRACE;
;                       return SEMICOLON;
,						return COMMA;
=						return EQUAL;
\n                      /* ignore EOL */;
[ \t]+                  /* ignore whitespace */;
"#"(.)*"\n"						/* ignore comments */
%%
