from bst import ArrayBST
from matching import *

"""
A function to create a symbol table from given atom array and keywords dictionary
"""
def create_st(code: list[str], keywords: dict[str, int]) -> (ArrayBST, int):
    st = ArrayBST()

    no_identifiers = 0
    for atom in code:
        if atom not in keywords and is_identifier(atom):
            st.add_if_not_exists(atom)
            no_identifiers += 1
    for atom in code:
        if atom not in keywords and is_constant(atom):
            st.add_if_not_exists(atom)

    return st, no_identifiers


"""
A function to create a program internal form (PIF) from given code, keywords dict, and symbol table
"""
def create_pif(code: list[str], atoms: dict[str, int], st: ArrayBST) -> (list[tuple[str, int, int]], list[str]):
    exceptions = []

    pif = []
    for atom in code:
        if atom in atoms:
            pif.append((atom, atoms[atom], None))
        elif is_identifier(atom):
            pif.append((atom, 0, st.get(atom)))
        elif is_constant(atom):
            pif.append((atom, 1, st.get(atom)))
        else:
            exceptions.append("Invalid atom: " + atom)

    return pif, exceptions
