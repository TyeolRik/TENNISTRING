# -*- encoding: utf-8 -*-

import csv
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

data_format = {
    u'Brand' : u'',
    u'Name' : u'',
    u'Diameter' : u'',
    u'Ratings(Reliability)' : u'',
    u'Durability' : u'',
    u'Power' : u'',
    u'Control' : u'',
    u'Feel' : u'',
    u'Comfort' : u'',
    u'Spin' : u'',
    u'Tension Stability' : u'',
    u'Overall' : u'',
    u'Performance-Price Ratio' : u''
}

# Use a service account
cred = credentials.Certificate('../tennistring-tyeolrik-firebase-adminsdk-pl8fk-9c67a2bb8d.json')
firebase_admin.initialize_app(cred)

db = firestore.client()
# doc_ref = db.collection(u'string').document("test")

string_csvfile = open("../Python_Categorizer/Data_splited_brand_and_gauge.csv", 'r', encoding='utf8', newline='')
csv_reader = list(csv.reader(string_csvfile))

temp = ''

for i in range(len(csv_reader)):
    if i != 0:
        document_name = csv_reader[i][0] + u"-" + csv_reader[i][1]
        doc_ref = db.collection(u'string').document(document_name)
        data = {
            u'Brand' : csv_reader[i][0],
            u'Name' : csv_reader[i][1],
            u'Diameter' : float(csv_reader[i][2]),
            u'Ratings(Reliability)' : int(csv_reader[i][3]),
            u'Durability' : float(csv_reader[i][4]) / 50,
            u'Power' : float(csv_reader[i][5]) / 50,
            u'Control' : float(csv_reader[i][6]) / 50,
            u'Feel' : float(csv_reader[i][7]) / 50,
            u'Comfort' : float(csv_reader[i][8]) / 50,
            u'Spin' : float(csv_reader[i][9]) / 50,
            u'Tension Stability' : float(csv_reader[i][10]) / 50,
            u'Overall' : float(csv_reader[i][11]) / 50,
            u'Performance-Price Ratio' : csv_reader[i][12]
        }
        doc_ref.set(data)
        print(data)


"""
for line in csv_reader:
    print(line[0].ljust(20), line[1])
"""