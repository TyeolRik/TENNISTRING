# -*- encoding: utf-8 -*-

string_textfile = open("../Python_Crawler/stringforum_copy_cut.txt", 'r', encoding='utf8')
string_lines = string_textfile.readlines()

for line in string_lines:
    print(line)