import json

stock_data_url = 'http://money.finance.sina.com.cn/quotes_service/api/jsonp_v2.php/%20/CN_MarketData.getKLineData?symbol' \
                 '=sh000001&scale=30&datalen=10&ma=no'


def parse_response(data):
    stock_data = data.replace('(', '').replace(')', '')
    stock_data = stock_data.replace(':"', '#').replace(':', '')
    stock_data = stock_data.replace('{', '{"').replace(',', ',"').replace('#', '":"').replace(',"{"', ',{"')
    stock_data = json.loads(stock_data)
    # print(stock_data)
    for day_data in stock_data:
        date, time = day_data['day'].split(' ')
        date = date.replace('-', '')
        day_data['day'] = date + time
    # print(day_data)

    stock_data = sorted(stock_data, key=lambda x: x['day'], reverse=False)
    return stock_data


def print_data(stock_data):
    for day_data in stock_data:
        print(day_data)
