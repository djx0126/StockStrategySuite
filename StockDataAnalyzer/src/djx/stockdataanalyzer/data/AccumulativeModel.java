package djx.stockdataanalyzer.data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 2015/11/17.
 */
public class AccumulativeModel extends ResultModel{
    private List<ResultModel> models;

    public AccumulativeModel() {
        super(null, null);
        this.models = new LinkedList<>();
    }

    public AccumulativeModel(AccumulativeModel srcModel) {
        super(null, null);
        this.models = new LinkedList<>();
        this.models.addAll(srcModel.models.stream().map(ResultModel::new).collect(Collectors.toList()));
    }

    public void addModel(ResultModel model){
        this.models.add(model);
    }

    @Override
    public boolean testOnData(StockDataModel data) {
        return models.stream().anyMatch(m -> m.testOnData(data));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.models.stream().forEach(model -> sb.append(model.toString()).append("\n"));
        return sb.toString();
    }

    public List<ResultModel> getModels() {
        return this.models;
    }

    public void setModels(List<ResultModel> models) {
        this.models = models;
    }
}
