import re

# Regex patterns
identifier_pattern = re.compile(r'[a-zA-Z]{3}\w*|[a-zA-Z]{1,2}')
constant_pattern = re.compile(r'\d{1,10}|"[^"]*"|\d+\.\d+|📌\s*<\s*[0-9]+\.[0-9]+\s*,\s*[0-9]+\.[0-9]+\s*>')
keyword_pattern = re.compile(r'🌠🔢🔣📰📌❓❗🔄↩️📣🔬')
operator_pattern = re.compile(r'[📋+\-*/%<>=!]|[<>=]=|<>')
separator_pattern = re.compile(r'[{}(),;]')
other_pattern = re.compile(r'[^\s\w]')

# Tokenizer function to split the input code into individual atoms
def tokenize(code: str) -> list[str]:
    combined_pattern = (f'({identifier_pattern.pattern}|{constant_pattern.pattern}|{keyword_pattern.pattern}|{operator_pattern.pattern}|{separator_pattern.pattern}|'
                        f'{other_pattern.pattern})')

    return re.findall(combined_pattern, code)

# Functions for checking atom types
def is_identifier(atom: str) -> bool:
    return identifier_pattern.match(atom) is not None

def is_constant(atom: str) -> bool:
    return constant_pattern.match(atom) is not None