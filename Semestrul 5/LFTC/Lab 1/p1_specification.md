# Mini-language specification

## 1. Lexical elements

### 1.0 Data types

    <digit> ::= 0 | 1 | ... | 9
    <unsigned_integer> ::= <digit> | <digit><unsigned_integer>
    <character> ::= `'a' | 'b' | ... | 'z' | 'A' | ... | 'Z'
    <integer> ::= <unsigned_integer> | -`<unsigned_integer>` .
    <float> ::= <integer> | <integer> "." [{0} <unsigned_integer>] .
    <point> ::= ( <float> <float> )

### 1.1 Reserved words

- `void` - `void` data type
- 🔢 - `int`
- 🔣 - `float`
- 🔤 - `char`
- 📌 - `point` (data type)
- ❓ - `if`
- ❗ - `else`
- 🔄 - `while`
- ↩️ - `return`
- 🖨️ - `cout <<` / output operation
- 👓 - `cin >>` / input operation
- `[]` - `array` version of a data type

### 1.2 Identifiers (ID)

***Restriction:** a digit must not be among the **first 3 characters** of an ID:

- ok: `abc123`, `abc`, `aa`
- not ok: `ab1`, `a1bc`

### 1.3 Constants (CONST)

- constant integers: `[0, 1, ..., 10^10-1]`
- constant floats
- characters of the alphabet
- all points in `R^2` plane

***Restriction:** an integer constant must not have more than **10 digits** in length

### 1.4 Separators

- ` ` - space
- `,` - comma
- `;` - end of instruction
- `{` - open code block (brackets)
- `}` - closing code block (brackets)
- `(` - opening parathesis
- `)` - closing parathesis

### 1.5 Operators

- `<-` - assignment
- `+` - addition
- `-` - subtraction
- `*` - multiplication
- `/` - division
- `%` - modulo
- `==` - equal verification
- `!=` - not equal verification
- `<` - less than
- `>` - greater than
- `<=` - less than or equal
- `>=` - greater than or equal
- `!` - not

## 2. (E)BNF Specification (Mini-Grammar)

### 2.0 Legend
    
    e / [] - empty sequence
    { } - optional one or multiple times
    [ ] - optional zero or multiple times

### 2.1 Specification

    <program> ::= { <function> }
    <function> ::= <header><code_block>
    <header> ::= <type> ID "(" <declaration_list> ")"
    <type> ::= <simple_type> | <simple_type>🗄️
    <simple_type> ::= 🔢 | 🔣 | 🔤 | 📌 | 🙂
    <declaration_list> ::= [] | <declaration> | <declaration> "," <declaration_list>
    <declaration> ::= <type> ID | <type> <assign_instr>
    <assign_instr> ::= ID 📷 <comp_expr>
    <comp_expr> ::= <simple_expr> | "(" <simple_expr> "," <comp_expr> ")"
    <simple_expr> ::= ID | CONST | <al_expr>
    <al_expr> ::= ID | CONST | <al_expr><binary_op><al_expr> | <unary_op><al_expr>
    <binary_op> ::= + | - | * | / | % | == | != | < | > | <= | >=
    <unary_op> ::= !
    <code_block> ::= "{" <comp_instr> "}"
    <comp_instr> ::= <simple_instr> ";" | <cond_instr> | <loop_instr> | [ <comp_instr> ]
    <simple_instr> ::= <declaration> | <assign_instr> | <return_instr> | <input_instr> | <output_instr>
    <return_instr> ::= ↩️ <comp_expr>
    <input_instr> ::= 👓 [ ID "," ] ID
    <output_instr> ::= 🖨️ <comp_expr>
    <cond_instr> ::= ❓ "(" <comp_expr> ")" <code_block> | ❓ "(" <comp_expr> ")" <code_block> ❗ <code_block>
    <loop_instr> ::= 🔁 "(" <comp_expr> ")" <code_block>

## 3. Symbol table

| Atom  |	Code  | Description |
| ---   |    ---  | ---         |
| ID    | 0       | identifier  |
| CONST | 1       | constant    |
| 🔶   | 2        | void type  |
| 🔢 | 3 |	int type |
| 🔣 | 4 |	float type |
| 🅰️ | 5 |	char type | 
| 📌 | 6 |	point type |
| 🗄️ | 7 |	array of type |
| ❓ | 8 |	if statement |
| ❗ | 9 |	else statement |
| 🔄 | 10 |	while loop |
| ↩️ | 11 |	return statement |
| 🖨️ | 12 |	Output (cout <<) |
| 👓 | 13 |	Input (cin >>) |
| ⬅️ | 14 |	Assignment (=) |
| + | 15 |	Addition |
| -	| 16 |	Subtraction |
| *	| 17 |	Multiplication |
| /	| 18 |	Division |
| % | 19 |		Modulo |
| ==| 20 |	Equal comparison |
| != | 21 |		Not equal comparison |
| <	| 22 |	Less than comparison |
| >	| 23 |	Greater than comparison |
| <= | 24 |		Less than or equal comparison |
| >= | 25 |		Greater than or equal comparison |
| !	| 26 |	Logical NOT |
| ,	| 27 |	Comma (separator) |
| ;	| 28 |	End of instruction |
| {	| 29 |	Open code block |
| }	| 30 |	Close code block |
| (	| 31 |	Open parenthesis |
| )	| 32 |	Close parenthesis |