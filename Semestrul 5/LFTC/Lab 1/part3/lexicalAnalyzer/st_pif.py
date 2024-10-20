from bst import ArrayBST

"""
A function to create a symbol table from given atom array and keywords dictionary
"""
def create_st(code: list[str], keywords: dict[str, int]) -> ArrayBST:
    st = ArrayBST()
    for atom in code:
        if atom not in keywords:
            st.add_if_not_exists(atom)
    return st