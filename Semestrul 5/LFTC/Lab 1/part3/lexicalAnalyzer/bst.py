"""
A node in a binary search tree
"""
class Node:
    def __init__(self, key: str, value: int):
        self.key: str = key
        self.value: int = value
        self.left: int|None = None
        self.right: int|None = None

"""
A binary search tree implemented using an array
"""
class ArrayBST:
    def __init__(self):
        self.nodes: list[Node] = []
        self.root: Node|None = None

    def get(self, key: str) -> int|None:
        return self._get_rec(self.root, key) if self.root is not None else None

    def _get_rec(self, root: Node, key: str):
        if key == root.key:
            return root.value
        elif key < root.key:
            return self._get_rec(self.nodes[root.left], key) if root.left is not None else None
        else:
            return self._get_rec(self.nodes[root.right], key) if root.right is not None else None

    def add_if_not_exists(self, key: str):
        if self.root is None:
            self.root = Node(key, 0)
            self.nodes.append(self.root)
        else:
            self._add_if_not_exists_rec(self.root, key)

    def _add_if_not_exists_rec(self, root: Node, key: str):
        if key == root.key:
            return
        elif key < root.key:
            if root.left is None:
                new_node = Node(key, len(self.nodes))
                self.nodes.append(new_node)
                root.left = len(self.nodes) - 1
            else:
                self._add_if_not_exists_rec(self.nodes[root.left], key)
        else:
            if root.right is None:
                new_node = Node(key, len(self.nodes))
                self.nodes.append(new_node)
                root.right = len(self.nodes) - 1
            else:
                self._add_if_not_exists_rec(self.nodes[root.right], key)

    def size(self) -> int:
        return len(self.nodes)


def test_bst():
    bst = ArrayBST()
    bst.add_if_not_exists("<>")
    bst.add_if_not_exists(">=")
    bst.add_if_not_exists("==")
    bst.add_if_not_exists("<>")
    bst.add_if_not_exists(">=")
    bst.add_if_not_exists("qwerty")
    bst.add_if_not_exists("12345")
    bst.add_if_not_exists("==")

    assert bst.size() == 5
    assert bst.get("<>") == 0
    assert bst.get(">=") == 1
    assert bst.get("==") == 2
    assert bst.get("qwerty") == 3
    assert bst.get("12345") == 4

    assert bst.get(">>") is None
    assert bst.get("<<") is None
    assert bst.get("qwertyuiop") is None

test_bst()
