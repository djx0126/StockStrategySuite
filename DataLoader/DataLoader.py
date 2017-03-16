from urllib import request
from sina import stock_data_parser

url = stock_data_parser.stock_data_url

with request.urlopen(url) as f:
    data = f.read()
    print('Status:', f.status, f.reason)
    data = data.decode('utf-8')

    stock_data = stock_data_parser.parse_response(data)
    stock_data_parser.print_data(stock_data)

