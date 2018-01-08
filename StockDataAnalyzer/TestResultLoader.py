import urllib
import json

class TestResultLoader:
    def __init__(self):
        pass

    def load_job_info(self, job_id):
        url = "http://localhost:8081/StockStrategyAnalyzer/job/" + job_id
        return self._get_from_url(url)

    def load_test_result(self, job_id):
        url = "http://localhost:8081/StockStrategyAnalyzer/result/" + job_id
        return self._get_from_url(url)

    def load_transactions(self, job_id, strategy):
        url = "http://localhost:8081/StockStrategyAnalyzer/transactions?jobId=" + job_id + "&strategy=" + strategy
        return self._get_from_url(url)

    def _get_from_url(self, url):
        req = urllib.request.Request(url)
        res_data = urllib.request.urlopen(req)
        res = res_data.read()
        return json.loads(res)

def extract_strategy_statistics(job_id, job_prefix = 'day100_.'):
    print("extracting data for job with id " + job_id)
    test_result_loader = TestResultLoader()
    job_result = test_result_loader.load_job_info(job_id)
    start_date = job_result["param"]["startDate"]
    end_date = job_result["param"]["endDate"]

    test_result = test_result_loader.load_test_result(job_id)
    strategy_result_map = test_result['strategyResultMap']
    strategy_names = list(strategy_result_map.keys())
    if job_prefix is not None:
        strategy_names = list(filter(lambda x: x.startswith(job_prefix), strategy_names))

    strategy_statistics = {}
    for strategy in strategy_names:
        transaction_result = test_result_loader.load_transactions(job_id, strategy)
        transaction_by_date = {}
        for transaction in transaction_result:
            transaction["gain"] = transaction["sellPrice"] / transaction["buyPrice"]
            if transaction["buyDate"] not in transaction_by_date.keys():
                transaction_by_date[transaction["buyDate"]] = []
            transaction_by_date[transaction["buyDate"]].append(transaction)

        transaction_gain_by_day = []
        for buy_date in transaction_by_date.keys():
            transactions_in_one_day = transaction_by_date[buy_date]
            gains = list(map(lambda x: x["gain"], transactions_in_one_day))
            transaction_gain_by_day.append(sum(gains) / len(gains))

        count = len(transaction_gain_by_day)

        avg_gain = sum(transaction_gain_by_day) / count if count > 0 else 1.0
        avg_gain = (avg_gain - 1.0) * 100

        gain_count = 0
        loss_count = 0
        gain_value = 0
        loss_value = 0
        all_gain = 1.0
        for gain in transaction_gain_by_day:
            all_gain = all_gain * gain
            if gain > 1:
                gain_count += 1
                gain_value += gain - 1
            elif gain < 1:
                loss_count += 1
                loss_value += 1 - gain
        accuracy = 100 * gain_count / count if count > 0 else 0
        rate = gain_value / loss_value if loss_value != 0 else 9999

        strategy_statistics[strategy] = (strategy, all_gain, accuracy, rate, avg_gain, count)
        # print("[%s] %g %g %g %g %d" % (strategy, all_gain, accuracy, rate, avg_gain, count))
    return strategy_statistics,start_date,end_date

if __name__ == '__main__':
    job_prefix = '20180101.'

    job_id_1y = "14039"
    strategy_statistics_1y, start_date_1y,end_date = extract_strategy_statistics(job_id_1y, job_prefix)

    job_id_2y = "14040"
    strategy_statistics_2y, start_date_2y,end_date  = extract_strategy_statistics(job_id_2y, job_prefix)

    job_id_3y = "14041"
    strategy_statistics_3y, start_date_3y,end_date  = extract_strategy_statistics(job_id_3y, job_prefix)

    job_id_all = "14042"
    strategy_statistics_all, start_date_all,end_date  = extract_strategy_statistics(job_id_all, job_prefix)

    strategy_names = sorted(strategy_statistics_1y.keys(), reverse=True)

    
    with open(end_date + '.txt', 'w') as f:
        f.write("\t" + start_date_1y + "\t\t\t\t\t" + start_date_2y + "\t\t\t\t\t" + start_date_3y + "\t\t\t\t\t" + start_date_all + "\n")
        for strategy in strategy_names:
            line = strategy
            strategy, all_gain, accuracy, rate, avg_gain, count = strategy_statistics_1y[strategy]
            line += "\t" + str(all_gain) + "\t" + str(accuracy) + "\t" + str(rate) + "\t" + str(avg_gain) + "\t" + str(count)
            strategy, all_gain, accuracy, rate, avg_gain, count = strategy_statistics_2y[strategy]
            line += "\t" + str(all_gain) + "\t" + str(accuracy) + "\t" + str(rate) + "\t" + str(avg_gain) + "\t" + str(count)
            strategy, all_gain, accuracy, rate, avg_gain, count = strategy_statistics_3y[strategy]
            line += "\t" + str(all_gain) + "\t" + str(accuracy) + "\t" + str(rate) + "\t" + str(avg_gain) + "\t" + str(count)
            strategy, all_gain, accuracy, rate, avg_gain, count = strategy_statistics_all[strategy]
            line += "\t" + str(all_gain) + "\t" + str(accuracy) + "\t" + str(rate) + "\t" + str(avg_gain) + "\t" + str(count)
            f.write(line + "\n")

    print("end")