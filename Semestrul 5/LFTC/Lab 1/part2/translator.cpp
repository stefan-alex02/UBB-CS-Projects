#include <iostream>
#include <unordered_map>
#include <vector>
#include <sstream>
#include <regex>
#include <fstream>
#include <iomanip>  // For formatting

using namespace std;

// Function to initialize the symbol table with atom-to-code mappings
unordered_map<string, int> initializeSymbolTable() {
    unordered_map<string, int> symbolTable;

    symbolTable["ID"] = 0;
    symbolTable["CONST"] = 1;
    symbolTable["🙂"] = 2;   // void
    symbolTable["🔢"] = 3;   // int
    symbolTable["🔣"] = 4;   // float
    symbolTable["🔤"] = 5;   // char
    symbolTable["📌"] = 6;   // point
    symbolTable["📚"] = 7;   // array
    symbolTable["❓"] = 8;   // if
    symbolTable["❗"] = 9;   // else
    symbolTable["🔄"] = 10;  // while
    symbolTable["↩️"] = 11;  // return
    symbolTable["🖨️"] = 12;  // cout <<
    symbolTable["👓"] = 13;  // cin >>
    symbolTable["📷"] = 14;  // assignment
    symbolTable["+"] = 15;   // addition
    symbolTable["-"] = 16;   // subtraction
    symbolTable["*"] = 17;   // multiplication
    symbolTable["/"] = 18;   // division
    symbolTable["%"] = 19;   // modulo
    symbolTable["=="] = 20;   // equal comparison
    symbolTable["!="] = 21;  // not equal comparison
    symbolTable["<"] = 22;   // less than
    symbolTable[">"] = 23;   // greater than
    symbolTable["<="] = 24;  // less than or equal to
    symbolTable[">="] = 25;  // greater than or equal to
    symbolTable["!"] = 26;   // logical NOT
    symbolTable[","] = 27;   // comma
    symbolTable[";"] = 28;   // end of instruction
    symbolTable["{"] = 29;   // open block
    symbolTable["}"] = 30;   // close block
    symbolTable["("] = 31;   // open parenthesis
    symbolTable[")"] = 32;   // close parenthesis

    return symbolTable;
}

// Function to identify whether the token is a number (constant)
bool isConstant(const string &token) {
    return regex_match(token, regex("\\d+"));
}

// Function to identify whether the token is an identifier (ID)
bool isIdentifier(const string &token) {
    return regex_match(token, regex("[a-zA-Z][a-zA-Z0-9]*"));
}

// Function to split the program into tokens (atoms)
vector<string> tokenize(const string &program) {
    vector<string> tokens;
    regex tokenRegex("🙂|🔢|🔣|🔤|📌|📚|❓|❗|🔄|↩️|🖨️|👓|📷|[\\+\\-*/%]|[!<>=]=?|[a-zA-Z][a-zA-Z0-9]*|\\d+|[{}(),;]");
    
    for (sregex_iterator i = sregex_iterator(program.begin(), program.end(), tokenRegex);
         i != sregex_iterator(); ++i) {
        tokens.push_back(i->str());
    }
    return tokens;
}

// Function to print the Program Internal Form (PIF)
void printPIF(const vector<string> &tokens, const unordered_map<string, int> &symbolTable, const string& fileName) {
    ofstream file(fileName);

    // Print header for table
    cout << left << setw(15) << "Atom" << setw(10) << "Code" << setw(15) << "Extra" << endl;
    cout << "--------------------------------------------" << endl;
    
    for (const string &token : tokens) {
        string extra = "N/A";  // Default value for non-ID or non-CONST tokens
        int code;

        if (symbolTable.count(token)) {
            code = symbolTable.at(token);
        } else if (isConstant(token)) {
            code = 1;
            extra = token;  // Store the constant value in the extra column
        } else if (isIdentifier(token)) {
            code = 0;
            extra = token;  // Store the identifier name in the extra column
        } else {
            cout << token << " -> Unrecognized token!" << endl;
            continue;
        }

        // Print to console in table format
        cout << left << setw(15) << token << setw(10) << code << setw(15) << extra << endl;

        // Write to CSV file (bare result with code and extra column)
        file << code << "," << extra << endl;
    }

    file.close();
}

int main() {
    // Example mini-language program (you can replace this with any input)
    string program = R"(
        🙂📚 prog2() {
            🔢 a, b;
            👓 a;
            👓 b;
            🔄 (a != b) {
                ❓ (a > b) {
                    a 📷 a - b;
                }
                ❗ {
                    b 📷 b - a;
                }
            }
            🖨️ a;
        }
    )";

    // Initialize the symbol table
    unordered_map<string, int> symbolTable = initializeSymbolTable();

    // Tokenize the program
    vector<string> tokens = tokenize(program);

    // Print the Program Internal Form (PIF) and save to file
    printPIF(tokens, symbolTable, "PIF_output.csv");

    return 0;
}
