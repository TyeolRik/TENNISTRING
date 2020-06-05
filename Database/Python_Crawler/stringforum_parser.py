# -*- encoding: utf-8 -*-

import csv
import re   # For Split string

string_text_file_path = "./"
string_text_file_name = "stringforum_copy_cut.txt"
string_textfile = open(string_text_file_path + string_text_file_name, 'r', encoding='latin1')
string_lines = string_textfile.readlines()

string_csv_file_path = string_text_file_path
string_csv_file_name = "string_info_latin1.csv"
string_csvfile = open(string_csv_file_path + string_csv_file_name, 'w', encoding='latin1', newline='')
csv_writer = csv.writer(string_csvfile)

column_count = 11

each_row = ["Name", "Ratings(Reliability)", "Durability", "Power", "Control", "Feel", "Comfort", "Spin", "Tension Stability", "Overall", "Performance-Price Ratio"]
csv_writer.writerow(each_row)

count = 0
for line in string_lines:
    if count == column_count:
        count = 0
        csv_writer.writerow(each_row)
        each_row = ["" for x in range(column_count)]

    # String name
    if line.startswith("<td class=\"b3\"><a class=\"y\""):
        each_row[count] = re.split('[<>]', line)[4]
        count += 1
        continue
    
    # Ratings(Reliability)
    if line.startswith("<td align=\"right\" class=\"b3\"><a class=\"y\""):
        each_row[count] = re.split('[<>]', line)[4]
        count += 1
        continue
    
    # Datas
    if line.startswith("<td class=\"b3\" width=\"50\"><img height=\"1\""):
        each_row[count] = re.split('[="/<>]', line)[30]
        count += 1
        continue

    # Performance-Price Ratio
    if line.startswith("<td align=\"right\" class=\"b3\" width="):
        each_row[count] = re.split('[="/<>]', line)[24]
        count += 1
        continue
    
    if count == column_count:
        count = 0
        csv_writer.writerow(each_row)
        each_row = ["" for x in range(column_count)]

test_line_number = 24
"""
if string_lines[3].startswith("<td class=\"b3\" width=\"50\"><img height=\"1\""):
    temp = re.split('[="/<>]', string_lines[3])
    for i in range(len(temp)):
        print(i, ":", temp[i])
    print("TEST", temp[30])
"""
"""
for x in range(3, 11):
    if string_lines[x].startswith("<td class=\"b3\" width=\"50\"><img height=\"1\""):
        temp = re.split('[="/<>]', string_lines[x])
    print("TEST", temp[30])
"""
"""
if string_lines[15].startswith("<td align=\"right\" class=\"b3\"><a class=\"y\""):
    temp = re.split('[<>]', string_lines[15])
    print(temp[4])
"""
"""
if string_lines[test_line_number].startswith("<td align=\"right\" class=\"b3\" width="):
    temp = re.split('[="/<>]', string_lines[test_line_number])
    print(temp[24])
"""
string_textfile.close()
string_csvfile.close()