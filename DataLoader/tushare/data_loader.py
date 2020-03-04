import tushare as ts
import pandas as pd

print(ts.__version__)

token = '2287d672ca15928ee4b271c342a4e0f5e636e87d9767764ad3b46dfe'

# ts.set_token(token)
pro = ts.pro_api(token)

# ts.get_hist_data('600848')

# df = ts.pro_bar(ts_code='000001.SZ', adj='qfq', start_date='20180101', end_date='20181011')

# df = pro.query('trade_cal', exchange='', start_date='20180901', end_date='20202001', fields='exchange,cal_date,is_open,pretrade_date', is_open='0')

# df = ts.pro_bar(ts_code='600848.SH', adj='qfq', start_date='20180101', end_date='20181011', freq='D')

df = ts.get_hist_data('600848', ktype='60', start='2018-01-01', end='2020-01-09')

df.to_csv('600848.csv')

# print(df)

