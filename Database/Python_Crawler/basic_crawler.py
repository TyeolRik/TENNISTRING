# -*- encoding: utf-8 -*-

import time     # Time check
from urllib.request import urlopen
from bs4 import BeautifulSoup

output_file_path = "./"
output_file_name = "output_test.txt"

start = time.time()

# https://www.stringforum.net/
# This URL is all String ratings due to php queries. (start=0 & limit=2524)
target_base_url = "https://www.stringforum.net/stringsearch.php?start=0&limit=2524&minbew=1&details=1&hide=0"
html = urlopen(target_base_url)
print("URL open      Time:", time.time() - start)
bsObject = BeautifulSoup(html, "html.parser", from_encoding='utf-8') 
print("BeautifulSoup Time:", time.time() - start)
f = open(output_file_path + output_file_name, 'w', encoding='utf8')
f.write(str(bsObject))

print("Execution     Time:", time.time() - start)