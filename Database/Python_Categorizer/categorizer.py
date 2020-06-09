# -*- encoding: utf-8 -*-

import csv

# Parsed
brand = ['Luxilon', 'MSV', 'Topspin', 'Signum Pro', 'WeissCANNON', 'Babolat', 'Yonex', 'Head', 'Luxilon', 'Kirschbaum', 'Tecnifibre', 'Isospeed', 'Solinco', "Pro's Pro", 'Dyreex', 'Wilson', 'Genesis', 'Prince', 'Gamma', 'Pacific', 'Polystar', 'Völkl', 'Oehms', 'Tier One', 'Gosen', 'Grapplesnake', 'Polyfibre', 'Tyger', 'Tourna', 'RAJ', 'Mantis', 'Mayami', 'Ytex', 'Leopard', 'Dunlop', 'Pro Supex', 'Still in Black', 'SuperString', 'RS Paris', 'Big Star', 'Ashaway', 'Toalson', 'Discho', 'Eagnas', 'Comido', 'LaserTec', 'Double AR', 'Target', 'WaveTennis', 'Tennisman', 'Bow Brand', 'Sportastic']

def is_diameter(input_string: str) -> bool:
    if input_string.isalpha():
        return False
    try:
        input_string = float(input_string)
        if input_string > 1.6:
            return False
        else:
            return True
    except ValueError:
        return False

def is_gauge(input_string: str) -> bool:
    if 'L' in input_string:
        input_string = input_string[:len(input_string) - 1]
    try:
        temp = int(input_string)
        if 15 <= temp <= 19:
            return True
        else:
            return False
    except ValueError:
        return False

def US_gauge_to_Diameter(input_US_Gauge: str) -> float:
    if input_US_Gauge == "19":
        return 1.05
    elif input_US_Gauge == "18":
        return 1.13
    elif input_US_Gauge == "17L":
        return 1.18
    elif input_US_Gauge == "17":
        return 1.22
    elif input_US_Gauge == "16L":
        return 1.24
    elif input_US_Gauge == "16":
        return 1.30
    elif input_US_Gauge == "15L":
        return 1.37
    elif input_US_Gauge == "15":
        return 1.45
    else:
        return -1.0

def diameter_to_US_gauge(input_diameter_to_US_Gagues: float) -> str:
    # According to, https://m.blog.naver.com/okhwa5606/221283410759
    if 1.00 <= input_diameter_to_US_Gagues < 1.10:
        return "19"
    elif 1.10 <= input_diameter_to_US_Gagues < 1.16:
        return "18"
    elif 1.16 <= input_diameter_to_US_Gagues < 1.20:
        return "17L"
    elif 1.20 <= input_diameter_to_US_Gagues < 1.24:
        return "17"
    elif 1.22 <= input_diameter_to_US_Gagues < 1.26:
        return "16L"
    elif 1.26 <= input_diameter_to_US_Gagues <= 1.33:
        return "16"
    elif 1.34 <= input_diameter_to_US_Gagues <= 1.40:
        return "15L"
    elif 1.41 <= input_diameter_to_US_Gagues < 1.50:
        return "15"
    else:
        return "ERROR"

def parsing_brand_stringname_diameter(information: str) -> list:
    outputlist = ["Unknown", "", ""]
    for eachBrand in brand:
        if eachBrand in information:
            outputlist[0] = eachBrand
            information = information[len(eachBrand):]
            break
    get_diameter_info = information.split(' ')
    diameter = get_diameter_info[len(get_diameter_info) - 1]

    if is_diameter(diameter):
        outputlist[2] = diameter
        information = information[:len(information) - len(diameter)].rstrip().lstrip()
        outputlist[1] = information
    
    elif is_gauge(diameter):
        outputlist[2] = US_gauge_to_Diameter(diameter)
        information = information[:len(information) - len(diameter)].rstrip().lstrip()
        outputlist[1] = information

    else:
        print("WARNING :: UNKNOWN Diameter Type!")
        print("Diameter:", diameter)
        print("Write fit structure")
        diameter = input()
        if is_diameter(diameter):
            outputlist[2] = '%.2f' % diameter
            information = information[:len(information) - len(diameter)].rstrip().lstrip()
            outputlist[1] = information
        elif is_gauge(diameter):
            outputlist[2] = '%.2f' % US_gauge_to_Diameter(diameter)
            information = information[:len(information) - len(diameter)].rstrip().lstrip()
            outputlist[1] = information
        else:
            print("ERROR")
            print("Check This again!")

    return outputlist

string_csv_file_path = "./"
string_csv_file_name = "datacutted.csv"
string_csvfile = open(string_csv_file_path + string_csv_file_name, 'r', encoding='utf-8', newline='')
csv_reader = csv.reader(string_csvfile)

string_writing_csvfile = open(string_csv_file_path + "Data_splited_brand_and_gauge.csv", 'w', encoding='utf-8', newline='')
csv_writer = csv.writer(string_writing_csvfile)

each_row = ["Brand", \
            "Name", \
            "Diameter", \
            "Ratings(Reliability)", \
            "Durability", \
            "Power", \
            "Control", \
            "Feel", \
            "Comfort", \
            "Spin", \
            "Tension Stability", \
            "Overall", \
            "Performance-Price Ratio"]
csv_writer.writerow(each_row)

for line in csv_reader:
    each_row = ["", "", "", "", "", "", "", "", "", "", "", "", ""]
    data = parsing_brand_stringname_diameter(line[0])
    each_row[0] = data[0]
    each_row[1] = data[1]
    each_row[2] = data[2]
    for i in range(10):
        each_row[i + 3] = line[i + 1]
    csv_writer.writerow(each_row)
    print(each_row[0].ljust(20), each_row[1])
    
string_csvfile.close()