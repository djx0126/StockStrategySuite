from urllib import request
from sina import stock_data_parser
from sina import future_data_parser

import urllib.request
import urllib.parse

# 请求头
headers = {
    'User-Agent': "Mozilla/5.0 (Windows NT 10.0; WOW64)",
    'Accept-Language': 'zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4',
    # 'Accept-Encoding': 'gzip, deflate, sdch'#,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8'
}


# get取网页数据

# post取网页数据
def posturl(url, data={}):
    try:
        params = urllib.parse.urlencode(data).encode(encoding='UTF8')
        req = urllib.request.Request(url, params, headers)
        r = urllib.request.urlopen(req)
        html = r.read()
        return html
    except urllib.error.HTTPError as e:
        print(e.code)
        print(e.read().decode("utf8"))


class DataLoader:
    def __init__(self, parser):
        self.parser = parser

    def load_data(self):
        try:
            url = self.parser.get_url()
            data = {}
            params = urllib.parse.urlencode(data).encode(encoding='gbk')
            req = urllib.request.Request("%s?%s" % (url, params))
            # 设置headers
            for i in headers:
                req.add_header(i, headers[i])
            r = urllib.request.urlopen(req)
            html = r.read()
            html = html.decode("gbk", "ignore")
            stock_data = self.parser.parse_response(html)

            return stock_data
        except urllib.error.HTTPError as e:
            print(e.code)
            print(e.read().decode("utf8"))

    def print_data(self, data):
        self.parser.print_data(data)


if __name__ == '__main__':
    data_loader = DataLoader(stock_data_parser)
    data = data_loader.load_data()
    data_loader.print_data(data)

    data_loader = DataLoader(future_data_parser)
    data = data_loader.load_data()
    data_loader.print_data(data)
