import json

stock_data_url = 'http://hq.sinajs.cn/?_=1491052856441/&list=M0,M1709,M1705,M1801,M1708,M1707,M1711'
# stock_data_url = 'http://qt.gtimg.cn/q=sh600519'


def get_url():
    return stock_data_url


def parse_response(data):
    return data

    # stock_data = data.replace('(', '').replace(')', '')
    # stock_data = stock_data.replace(':"', '#').replace(':', '')
    # stock_data = stock_data.replace('{', '{"').replace(',', ',"').replace('#', '":"').replace(',"{"', ',{"')
    # stock_data = json.loads(stock_data)
    # # print(stock_data)
    # for day_data in stock_data:
    #     date, time = day_data['day'].split(' ')
    #     date = date.replace('-', '')
    #     day_data['day'] = date + time
    # # print(day_data)
    #
    # stock_data = sorted(stock_data, key=lambda x: x['day'], reverse=False)
    # return stock_data


def print_data(data):
    print(data)
