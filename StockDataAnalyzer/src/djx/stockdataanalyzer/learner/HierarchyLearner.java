package djx.stockdataanalyzer.learner;

import djx.stockdataanalyzer.data.ModelWithStatistic;
import djx.stockdataanalyzer.data.StockDataModel;

/**
 * Created by dave on 2016/5/1.
 */
public abstract class HierarchyLearner implements ILeaner{
    protected LearnerFactory factory;

    protected ILeaner buildSubLeaner(Object... args){
        return this.factory.buildLearner(args);
    }

}
