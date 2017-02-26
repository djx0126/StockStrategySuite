package djx.stockdataanalyzer.data;

import com.djx.stockgainanalyzer.data.Field;
import com.djx.stockgainanalyzer.data.MAField;
import com.djx.stockgainanalyzer.data.RelativeMAData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 2015/11/11.
 */
public class FieldModel {

    public int[] daysFields;

    public MAField[] maFields; // = {Field.ma5, Field.ma10, Field.ma20, Field.ma30};
    public int[] maList; //{5, 10, 20, 30};
    public int maxMaDays;// = Arrays.stream(maList).max().getAsInt();
    public int[] maDaysMapping;

    public MAField[] overAllMaFields; // = {Field.ma5, Field.ma10, Field.ma20, Field.ma30};
    public int[] overAllMaList; //{5, 10, 20, 30};
    public int overAllMaxMaDays;// = Arrays.stream(maList).max().getAsInt();
    public int[] overAllMaDaysMapping;

    public FieldModel(int[] dayFields, int[] maFields, int[] overAllMaFields) {
        this.daysFields = dayFields;
        getMaFields(maFields);
        getOverAllMaFields(overAllMaFields);
    }

    private void getOverAllMaFields(int[] shMaFields) {
        List<MAField> maFieldList = Arrays.stream(shMaFields).mapToObj(i -> getMAFieldByDays(i)).filter(f -> f != null).collect(Collectors.toList());
        this.overAllMaFields = new MAField[maFieldList.size()];
        maFieldList.toArray(this.overAllMaFields);

        this.overAllMaList = maFieldList.stream().mapToInt(f -> f.getMaDays()).toArray();
        this.overAllMaxMaDays = this.overAllMaList.length > 0 ? Arrays.stream(overAllMaList).max().getAsInt() : 0;

        this.overAllMaDaysMapping = RelativeMAData.buildMAFieldsMapping(this.overAllMaFields);
    }

    private void getMaFields(int[] maFields) {
        List<MAField> maFieldList = Arrays.stream(maFields).mapToObj(i -> getMAFieldByDays(i)).filter(f -> f != null).collect(Collectors.toList());
        this.maFields = new MAField[maFieldList.size()];
        maFieldList.toArray(this.maFields);

        this.maList = maFieldList.stream().mapToInt(f -> f.getMaDays()).toArray();
        this.maxMaDays = this.maList.length > 0 ? Arrays.stream(maList).max().getAsInt() : 0;

        this.maDaysMapping = RelativeMAData.buildMAFieldsMapping(this.maFields);
    }

    private MAField getMAFieldByDays(int days) {
        MAField maFieldToGet = null;
        for (Field field: Field.fields) {
            if (field instanceof MAField) {
                if (((MAField) field).getMaDays() == days) {
                    maFieldToGet = (MAField) field;
                    break;
                }
            }
        }
        return maFieldToGet;
    }
}
