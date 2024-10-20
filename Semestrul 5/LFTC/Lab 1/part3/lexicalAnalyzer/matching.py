import re

# Regex patterns
identifier_pattern = re.compile(r'[a-zA-Z]{3}\w*|[a-zA-Z]{1,2}')
constant_pattern = re.compile(r'\d+\.\d+|\b\d{1,10}\b|"[^"]*"|📌\s*<\s*[0-9]+\.[0-9]+\s*,\s*[0-9]+\.[0-9]+\s*>')
keyword_pattern = re.compile(r'🌠🔢🔣📰📌❓❗🔄↩️📣🔬')
operator_pattern = re.compile(r'[📋+\-*/%<>=!]|[<>=]=|<>')
separator_pattern = re.compile(r'[{}(),;]')
other_pattern = re.compile(r'[^\s\w]')

# Tokenizer function to split the input code into individual atoms
def tokenize(code: str) -> list[str]:
    return re.findall(r'[0-9]+\.[0-9]+|[0-9]{1,10}|\w+|"[^"]*"|📌\s*<\s*[0-9]+\.[0-9]+\s*,\s*[0-9]+\.[0-9]+\s*>|'
                      r'[🌠🔢🔣📰📌❓❗🔄↩️📣🔬]|[📋+\-*/%<>=!]+|[{}(),;]|[^\s\w]', code)

# Functions for checking atom types
def is_identifier(atom: str) -> bool:
    return re.match(f'^{identifier_pattern.pattern}$', atom) is not None

def is_constant(atom: str) -> bool:
    return re.match(f'^{constant_pattern.pattern}$', atom) is not None