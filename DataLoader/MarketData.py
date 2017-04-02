#!/usr/local/bin/python
#-*-coding:utf-8-*-
#Get market data from futures exchanges
#designed and programed by lianxiangbin(LianZhang,qq785674410)
#2014-10-20

import urllib.request
import urllib.parse
from pyquery import PyQuery
import sqlite3
import datetime


class MarketDataFromDCE:

    #define the attributes
    def __init__(self,date,SaveUrl):
        self.date=date[0:4]+date[5:7]+date[-2:];#"2014/05/23"
        self.url=r"http://www.dce.com.cn/PublicWeb/MainServlet";
        self.PostDic={'action':'Pu00011_result','Pu00011_Input.trade_date':self.date,
                      'Pu00011_Input.trade_type':'0','Pu00011_Input.variety':'all',
                      'Submit':u'查 询'}
        #print(self.PostDic)
        self.Rename={'大豆':'a','豆二':'b','豆粕':'m','豆一':'a','豆油':'y','鸡蛋':'jd', \
                     '焦煤':'jm','焦炭':'j','玉米':'c','胶合板':'bb','聚丙烯':'pp','聚乙烯':'l','铁矿石':'i','纤维板':'fb','棕榈油':'p','聚氯乙烯':'v'};
        self.SaveUrl=SaveUrl;
        self.Data={};

    #get the html page
    def GetWebPage(self):
        PostDicDecoded=urllib.parse.urlencode(self.PostDic).encode(encoding='gbk');
        Request=urllib.request.Request(self.url,PostDicDecoded);
        MyPage=urllib.request.urlopen(Request).read().decode("gbk","ignore");
        if not MyPage:
            print("Dear Host,I can not find the web page");
        return MyPage;

    #this is the trading date
    def GetTheTradingDay(self):
        TradingDay=datetime.datetime(int(self.date[0:4]),int(self.date[4:6]),int(self.date[-2:]));
        return TradingDay;

    #Get the data we needed from the origin file
    def GetNeededData(self):
        MyPage=self.GetWebPage();
        TradingDay=self.GetTheTradingDay();
        Contents=PyQuery(MyPage);
        #print(Contents);
        A=Contents("table").eq(1);
        A=PyQuery(A);
        for tr in A("tr"):
            Contrasts=[];
            values=[TradingDay];
            for i in range(len(tr)):
                data=PyQuery(tr).find("td").eq(i).text();
                if i<2:
                    Contrasts.append(data);
                    if i==0:
                        Symbol=Contrasts[0];
                        if Symbol.strip() in self.Rename:
                            Symbol=self.Rename[Symbol];
                        values.append(Symbol);
                else:
                    values.append(data);
            key=Contrasts[0]+Contrasts[1];
            if (u"商品名称" in key) or (u"小计" in key) or (u"总计" in key):
                continue;
            values[1:]=[element.replace(",","").replace("-","").strip() for element in values[1:]];
            self.Data.setdefault(key,values);

    #Insert into DB
    def InsertDataToDB(self):
        self.GetNeededData();
        conna=sqlite3.connect(self.SaveUrl);
        #if conna:
        #    print("database is successfully connected");
        cursor=conna.cursor();
        SQLquery1="create table if not exists DCE(Contracts varchar(20),date datetime,Symbol nvarchar(30),Open numeric(15,2),\
                  High numeric(15,2),Low numeric(15,2),Close numeric(15,2),PreSettlement numeric(15,2),Settlement numeric(15,2),\
                  Change1 numeric(15,2),Change2 numeric(15,2),Volume numeric(25,2),OpenInt numeric(25,2),\
                  ChangeofOpenInt numeric(25,2),Turnover numeric(30,2))";
        cursor.execute(SQLquery1);
        for key,value in self.Data.items():
            Iter=(key,value[0],value[1],value[2],value[3],value[4],value[5],value[6],value[7],value[8],value[9],value[10],value[11],value[12],value[13]);
            #print(Iter);
            SQLquery2="insert into DCE"+" "+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            cursor.execute(SQLquery2,Iter);
        conna.commit();
        conna.close();



if __name__=="__main__":
    try:
        test=MarketDataFromDCE("2014/10/15");
        # test.InsertDataToDB();
    except Exception as e:
        print("something wrong")
        print(e);