# Function to read code from a file and tokenize it
from bst import ArrayBST
from matching import tokenize
from st_pif import create_pif, create_st

import csv

def read_st_from_csv(file_path: str) -> dict[str, int]:
    st = {}
    with open(file_path, mode='r', encoding='utf-8') as file:
        reader = csv.reader(file)
        for row in reader:
            if len(row) == 2:
                key, value = row
                st[key] = int(value)
    return st

def save_st_to_csv(st: ArrayBST, file_path: str):
    with open(file_path, mode='w', encoding='utf-8', newline='') as file:
        writer = csv.writer(file)
        for node in st.nodes:
            writer.writerow([node.key, node.value])

def save_pif_to_csv(pif: list[tuple[str, int, int]], file_path: str):
    with open(file_path, mode='w', encoding='utf-8', newline='') as file:
        writer = csv.writer(file)
        for entry in pif:
            writer.writerow(entry)

"""
A function to read code from a file and tokenize it
"""
def get_code_pif_from_file(file_path: str, keywords: dict[str, int]) -> None:
    with open(file_path, 'r', encoding='utf-8') as file:
        code = file.read()

    # Tokenize the code into atoms
    atoms = tokenize(code)

    # Create the symbol table and PIF
    symbol_table, no_identifiers = create_st(atoms, keywords)
    pif, exceptions = create_pif(atoms, keywords, symbol_table)

    # Print the Symbol Table and PIF
    print("Symbol Table:")
    for node in symbol_table.nodes:
        print(f"Key: {node.key}, Value: {node.value}")

    print("\nPIF:")
    for entry in pif:
        print(entry)

    # Print exceptions for invalid atoms
    if exceptions:
        print("\nExceptions (Invalid atoms):")
        for ex in exceptions:
            print(ex)

    # Save the Symbol Table and PIF to CSV files
    save_st_to_csv(symbol_table, 'files/st.csv')
    save_pif_to_csv(pif, 'files/pif.csv')

if __name__ == "__main__":
    # Read the keywords from the CSV file
    keywords = read_st_from_csv('atoms.csv')

    # Process the code from the file
    get_code_pif_from_file('files/code1.txt', keywords)
