package com.djx.stockgainanalyzer;

import com.djx.stockgainanalyzer.data.CachedStockGainData;
import com.djx.stockgainanalyzer.data.Field;
import com.djx.stockgainanalyzer.data.PreviousData;
import com.djx.stockgainanalyzer.data.StockGainData;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;

import java.util.*;
import java.util.concurrent.*;


public class StockAnalyzer {
	public static int PREVIOUS_NUM = 10;
	public static int GAIN_NUM = 5;
//	public static int[] MA_LIST = {5, 10, 30};
	public static final double E = 1e-4f;
//	public static final Field[] PARAMS = {Field.close };
	public static Field[] PARAMS = { Field.close, Field.open, Field.high, Field.low, Field.vol};
//	public static final Field[] PARAMS = {Field.close, Field.open};
	
	public static final boolean debug = true;
	
	public static final boolean usingPreFilter = false; // pre-filter out part of data with a rate specified, to decrease the memory usage
	public static final float preFilterRate = 0.5f;
	
	public static final int NUM_OFFSET_RUN = 3;
	public static final int NUM_SCALE_RUN = 4000;
	public static final int offset_steps = 15;//47
	public static final int scale_steps = 17;//23
	
	
	public static final String SELECT_TEST_DATA_BY_DATE_STRING = null; //"20140101"; // if set to null, will use a random selector by stock code
	
	public static final int THREADNUM_FOR_RANDOM_OFFSET_PARAMS = 1;
	public static final int THREADNUM_FOR_LEARN_WITH_SCALE = 12;
	public static Map<String, double[]> dataMIN = new HashMap<>() ;
	public static Map<String, double[]> dataMAX = new HashMap<>() ;
	public static Map<String, double[]> offsetInitPoint = new HashMap<>();
	public static Map<String, Double> scaleStep = new HashMap<>();  // step length, the delta
	public static double[][] offsetSteps = new double[Field.LENGTH][PREVIOUS_NUM];  // step length, the delta
	public static double[][] scaleParamList = new double[Field.LENGTH][scale_steps]; 
	public static double[][] offsetParamList = new double[Field.LENGTH][offset_steps]; 
//	public static double[][][] offsetParamLists = new double[PARAMS.length][PREVIOUS_NUM][offset_steps];
	public static final int offsetRandomizeTryTimes = 5;
	public static final int offsetRandomizeLimit = 5;  // at least this many records left in the collection
	private static final double MIN = -1000000000.0d;
	
	private static List<StockGainData> allData = new ArrayList<>();
	
	private static int MIN_CLUSTER_SIZE = 125;

	public static final double TARGET_HIGH_GAIN = 5.0d;
	public static final double TARGET_GAIN_MISS = 1.0d;
	private static List<StockGainData> dataWithHighGain = new ArrayList<>();
	
	public static synchronized void addData(StockGainData stockGainData){
		allData.add(stockGainData);
	}
	
	public static synchronized List<StockGainData> getData(){
		return allData;
	}
	
	private static IStockSelector dataSelector;
	private static Random random = new Random(System.currentTimeMillis());

	
    /**
     * @param args
     */
    public static void main(String[] args) {
		ArgParser.loadInitConfigures(args, Constant.class);
		if (usingPreFilter){
			MIN_CLUSTER_SIZE = (int)(MIN_CLUSTER_SIZE * preFilterRate);
		}


        // TODO Auto-generated method stub
        System.out.println("Start");
        StockAnalyzer analyzer = new StockAnalyzer();
//        DBHelper.clearGainData();
        long startTime = System.currentTimeMillis();
        StrategyHelper.generateData();
        System.out.println("Total "+allData.size()+" records loaded.");
        if (allData.size()<=0){
        	return;
        }
//        Utils.transeferToLog(allData, PARAMS);
        transeferToLog();
        
        for (int i = 0; i < allData.size(); i++) {
        	StockGainData data = allData.get(i);
        	double gain = data.getGain();
        	double adjustedGain = (Math.pow(10,gain)-1.00000000000d)*100.0d;
        	if(adjustedGain > TARGET_HIGH_GAIN){
        		dataWithHighGain.add(data);
        	}
		}
        
        for(Field field:PARAMS){
        	prepareOffsetInitPoint(field.getName());
    	}
        
        if (SELECT_TEST_DATA_BY_DATE_STRING!=null){
        	dataSelector = new StockDateSelector(allData, SELECT_TEST_DATA_BY_DATE_STRING);
        }else{
        	dataSelector = new StockRandomSelector(allData);
        }
        
        System.out.println("Start at "+ new Date());
        System.out.println("Env Params:");
		System.out.println("pre_num=" + PREVIOUS_NUM);
		System.out.println("gain_num=" + GAIN_NUM);
        System.out.println("NUM_OFFSET_RUN="+NUM_OFFSET_RUN+"\tNUM_SCALE_RUN="+NUM_SCALE_RUN);
        System.out.println("THREADNUM_FOR_RANDOM_OFFSET_PARAMS="+THREADNUM_FOR_RANDOM_OFFSET_PARAMS+"\tTHREADNUM_FOR_LEARN_WITH_SCALE="+THREADNUM_FOR_LEARN_WITH_SCALE);
    	
        double[][] maxOffsetParams = new double[Field.LENGTH][];
        double[][] maxScaleParams = new double[Field.LENGTH][];
    	for(Field field:PARAMS){
    		double[] offsetParam = new double[PREVIOUS_NUM];
    		maxOffsetParams[field.getIdx()]=offsetParam;
    		double[] scaleParam = new double[PREVIOUS_NUM];
    		maxScaleParams[field.getIdx()]=scaleParam;
    	}
    	
    	double targetValue = analyzer.runWithRandomInitScale(NUM_SCALE_RUN,  maxOffsetParams, maxScaleParams);
    	System.out.println("gain from learn is: "+targetValue);
    	
    	StockStrategyCaculator caculator = new StockStrategyCaculator();
    	Result resultFromCalMap =  caculator.calculateOnList(allData, dataSelector, false, maxOffsetParams, maxScaleParams);
    	System.out.println("*********************  All done, verify "+resultFromCalMap);
    	
    	for(Field field:PARAMS){
    		System.out.println("final "+field.getName()+"Offset:"+Arrays.toString(maxOffsetParams[field.getIdx()]).replace('[', '{').replace(']', '}'));
    	}
    	for(Field field:PARAMS){
    		System.out.println("final "+field.getName()+"Scale:"+Arrays.toString(maxScaleParams[field.getIdx()]).replace('[', '{').replace(']', '}'));
    	}
    	
        
        long endTime = System.currentTimeMillis();
        System.out.println("time passed:"+(endTime-startTime)/1000 +" seconds.");
        System.out.println("End at "+ new Date());
        
    }
    
    
    public double runWithRandomInitScale(int numToRun, double[][] maxOffsetParams, double[][] maxScaleParams){
    	prepareParamSteps();
    	
    	double[][] offsetParams =  new double[Field.LENGTH][];
    	for(Field field:PARAMS){
    		double[] offsetParam = new double[PREVIOUS_NUM];
        	offsetParams[field.getIdx()]=offsetParam;
    	}
    	
    	double[][] scaleParams =  new double[Field.LENGTH][];
    	
    	for (Field field:PARAMS){
    		double[] scaleParam = new double[PREVIOUS_NUM];
    		scaleParams[field.getIdx()]=scaleParam;
    		for (int i=0;i<PREVIOUS_NUM;i++){
        		scaleParams[field.getIdx()][i] = scaleParamList[field.getIdx()][scale_steps/4];
        	}
    		copyArray(scaleParams[field.getIdx()], maxScaleParams[field.getIdx()]);
    	}
    	
    	double maxGain = MIN; //learnForScaleWithRandomInitOffset(NUM_OFFSET_RUN, offsetParams, scaleParams);
    	
    	for(Field field:PARAMS){
    		copyArray(offsetParams[field.getIdx()], maxOffsetParams[field.getIdx()]);
    	}
		for (int i=0;i<numToRun;i++){
			System.out.println("** Random scale at "+i);
			
			for (Field field:PARAMS){
				randomizeScaleParams(scaleParams[field.getIdx()], field);
			}
			
			double tempMaxGain = learnForScaleWithRandomInitOffset(NUM_OFFSET_RUN, offsetParams, scaleParams);
    		if (tempMaxGain>maxGain+E){
    			
    			for (Field field:PARAMS){
    				copyArray(offsetParams[field.getIdx()], maxOffsetParams[field.getIdx()]);
    	    		copyArray(scaleParams[field.getIdx()], maxScaleParams[field.getIdx()]);
    			}
	    		maxGain = tempMaxGain;
    		}
		}
		
    	return maxGain;
    }
    
    public double learnForScaleWithRandomInitOffset(int numOfRunWithRandomOffset, /*out*/double[][] offsetParams, /*inout*/double[][] scaleParams){ 
    	double[][] tempOffsetParams =  new double[Field.LENGTH][];
    	for (Field field:PARAMS){
    		double[] offsetParam = new double[PREVIOUS_NUM];
    		tempOffsetParams[field.getIdx()]=offsetParam;
        	copyArray(offsetInitPoint.get(field.getName()), tempOffsetParams[field.getIdx()]);
        	copyArray(tempOffsetParams[field.getIdx()], offsetParams[field.getIdx()]);
    	}
    	double[][] oriScaleParams =  new double[Field.LENGTH][];
    	double[][] tempScaleParams =  new double[Field.LENGTH][];
    	for (Field field:PARAMS){
    		double[] scaleParam = new double[PREVIOUS_NUM];
        	oriScaleParams[field.getIdx()] = scaleParam;
        	copyArray(scaleParams[field.getIdx()], oriScaleParams[field.getIdx()]);
        	tempScaleParams[field.getIdx()]=scaleParams[field.getIdx()];
    	}
//    	double maxGain = learnForScaleOffset(tempOffsetParams, tempScaleParams);
    	double maxGain = MIN;
		for (int i=0;i<numOfRunWithRandomOffset;i++){
			System.out.println("*** Random offset at "+i);
			
    		for (Field field:PARAMS){
    			double[] scaleParam = new double[PREVIOUS_NUM];
        		copyArray(oriScaleParams[field.getIdx()], scaleParam);
        		tempScaleParams[field.getIdx()]=scaleParam;
    		}
    		
    		List<StockGainData> leftDatas = randomizeOffsetParams(tempOffsetParams, tempScaleParams);
    		long randomStart = System.currentTimeMillis();
    		int tryTimes = offsetRandomizeTryTimes;
    		while(leftDatas.size()<=offsetRandomizeLimit && tryTimes>0){
    			tryTimes--;
    			leftDatas = randomizeOffsetParams(tempOffsetParams, tempScaleParams);
    		}
    		if (leftDatas.size()>offsetRandomizeLimit){
    			long randomEnd = System.currentTimeMillis();
    			
    			if (debug){
    				System.out.println("offset randomized with "+leftDatas.size()+" records left, cost "+(randomEnd-randomStart)/1000+"s.");
    			}
    			
    			double tempMaxGain = learnForScaleOffset(tempOffsetParams, tempScaleParams);
        		if (tempMaxGain>maxGain+E){
        			maxGain = tempMaxGain;
        			
        			for(Field field:PARAMS){
        				copyArray(tempOffsetParams[field.getIdx()], offsetParams[field.getIdx()]);
        				copyArray(tempScaleParams[field.getIdx()], scaleParams[field.getIdx()]);
        			}
        		}
    		}
    		
    	}
		return maxGain;
    }
    
    public double learnForScaleOffset(/*inout*/double[][] offsetParams, /*inout*/double[][] scaleParams){
    	StockStrategyCaculator caculator = new StockStrategyCaculator();
    	Result resultFromCal =  caculator.calculateOnList(allData, dataSelector, true, offsetParams, scaleParams);
    	double maxTargetValue = resultFromCal.getTargetResult();
    	int latchCount = PREVIOUS_NUM * PARAMS.length *2;
    	if (debug){
    		System.out.println("start learn for scale and offset from "+resultFromCal+",start at "+ new Date());
    		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    	}
    	
		long startTime = System.currentTimeMillis();
		int iter = 0;
		
		List<CachedStockGainData> cachedDatas = buildCachedStockGainData(allData, offsetParams, scaleParams);
		
		String learningLog = "";
		String lastLearnString = "";
		while(true){
			iter++;
    		long iterStart = System.currentTimeMillis();
			boolean updateInIter = false;/////////////////////////////////////
    		ExecutorService threadPool = Executors.newFixedThreadPool(THREADNUM_FOR_LEARN_WITH_SCALE);
    		CountDownLatch latch = new CountDownLatch(latchCount);
    		List<Future<Map<String, Object>>> resultList = new ArrayList<>();
    		
    		for (Field field:PARAMS){
    			for (int i=0;i<scaleParams[field.getIdx()].length;i++){
    				double originPriceScale = scaleParams[field.getIdx()][i];
    				double[] scaleList = scaleParamList[field.getIdx()];
        			if (scaleParams[field.getIdx()][i]<scaleList[scaleList.length-1]){
        				scaleParams[field.getIdx()][i]+=scaleStep.get(field.getName());
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "scale:"+field.getName()+String.valueOf(i)+":U");
        				
//        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculate(threadPool,latch,PREVIOUS_NUM, offsetParams, scaleParams);
            			resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			scaleParams[field.getIdx()][i] = originPriceScale;
        			if (scaleParams[field.getIdx()][i]>scaleList[0]){
        				scaleParams[field.getIdx()][i]-=scaleStep.get(field.getName());
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "scale:"+field.getName()+String.valueOf(i)+":D");
        				
//        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculate(threadPool,latch,PREVIOUS_NUM, offsetParams, scaleParams);
            			resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			scaleParams[field.getIdx()][i] = originPriceScale;
        			
        			if (scaleParams[field.getIdx()][i]<scaleList[scaleList.length-1]){
        				scaleParams[field.getIdx()][i]+= 2 * scaleStep.get(field.getName());
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "scale:"+field.getName()+String.valueOf(i)+":U2");
        				
        				resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			scaleParams[field.getIdx()][i] = originPriceScale;
        			if (scaleParams[field.getIdx()][i]>scaleList[0]){
        				scaleParams[field.getIdx()][i]-=2*scaleStep.get(field.getName());
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "scale:"+field.getName()+String.valueOf(i)+":D2");
        				
            			resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			scaleParams[field.getIdx()][i] = originPriceScale;
        		}
    			for (int i=0;i<PREVIOUS_NUM;i++){
    				double origin = offsetParams[field.getIdx()][i];
    				double step = offsetSteps[field.getIdx()][i];
        			offsetParams[field.getIdx()][i]+=step;
        			if (offsetScaleInMAXAndMIN(offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], field.getName(), i)){
        				
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "offset:"+field.getName()+String.valueOf(i)+":U");
//        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculate(threadPool,latch,PREVIOUS_NUM, offsetParams, scaleParams);
        				resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			offsetParams[field.getIdx()][i] = origin-step;
        			if (offsetScaleInMAXAndMIN(offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], field.getName(), i)){
        				
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "offset:"+field.getName()+String.valueOf(i)+":U");
//        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculate(threadPool,latch,PREVIOUS_NUM, offsetParams, scaleParams);
            			resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			offsetParams[field.getIdx()][i] = origin;
        			
        			offsetParams[field.getIdx()][i]+=2*step;
        			if (offsetScaleInMAXAndMIN(offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], field.getName(), i)){
        				
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "offset:"+field.getName()+String.valueOf(i)+":U2");
        				resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			offsetParams[field.getIdx()][i] = origin-2*step;
        			if (offsetScaleInMAXAndMIN(offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], field.getName(), i)){
        				
        				Future<Map<String, Object>> future = prepareExecuteThreadForCalculateWithCachedData(threadPool,latch,PREVIOUS_NUM,cachedDatas,field.getIdx(),i,offsetParams[field.getIdx()][i], scaleParams[field.getIdx()][i], "offset:"+field.getName()+String.valueOf(i)+":U2");
        				resultList.add(future);
        			}else{
        				latch.countDown();
        			}
        			offsetParams[field.getIdx()][i] = origin;
        		}
    		}
    		
    		threadPool.shutdown();
    		try {
				latch.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
    		
    		Map<String, Object> targetResult=null;
    		for (Future<Map<String, Object>> future:resultList){
    			Map<String, Object> result;
				try {
					result = future.get();
					Result calResult = (Result) result.get("calResult");
					double targetValue = calResult.getTargetResult();
					if(targetValue > maxTargetValue+E){
						maxTargetValue = targetValue;
						targetResult = result;
	    			}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
    		}
    		
    		if (targetResult!=null){
    			updateInIter = true;

    			
    			int fieldIdx = (int) targetResult.get("fieldId");
    			int preIdx = (int) targetResult.get("preId");
    			double offsetValue = (double) targetResult.get("offset");
    			double scaleValue = (double) targetResult.get("scale");
    			offsetParams[fieldIdx][preIdx] = offsetValue;
    			scaleParams[fieldIdx][preIdx] = scaleValue;
    			
    			updateCachedStockGainData(cachedDatas, fieldIdx, preIdx, offsetValue, scaleValue);
    			
    			String partLearningLog = "";
    			long iterEnt = System.currentTimeMillis();
        		if(iter==1){
        			partLearningLog += "learing with gain:\n";
        		}else if (iter>1 ){
        			partLearningLog += "//";
        		}

				Result iterResult = (Result) targetResult.get("calResult");
        		
        		lastLearnString = iterResult+" time:"+(iterEnt-iterStart)/1000+"s "+targetResult.get("tag");
        		
        		partLearningLog += lastLearnString;
        		
        		if(iter%2==0){
        			partLearningLog+="\n";
        		}
    			
    			
    			if (debug){
    				System.out.print(partLearningLog);
    			}else{
    				learningLog += partLearningLog;
    			}

				if (resultFromCal.getAccuracy() >= 70.0d){
					FileHelper.writeResult(PREVIOUS_NUM, GAIN_NUM, PARAMS, offsetParams, scaleParams, iterResult, lastLearnString);
				}
    		}

			if (MIN_CLUSTER_SIZE > 0){
				if (!updateInIter || ((Result) targetResult.get("calResult")).getCount() <= MIN_CLUSTER_SIZE ){
					break;
				}
			}

    	}
		
		resultFromCal =  caculator.calculateOnList(allData, dataSelector, false, offsetParams, scaleParams);
//		resultFromCal = calculateOnList(false, offsetParams, scaleParams);
		long endTime = System.currentTimeMillis();
		
		if (debug){
			System.out.println();
		}else if (resultFromCal.isValid()){
			learningLog+="\n";
			System.out.print(learningLog);
		}else{
			System.out.println("End learn with:"+lastLearnString);
		}
		
		System.out.println("//**********  end run with >>>>>>>> "+resultFromCal+", end at "+ new Date()+" passed "+(endTime-startTime)/1000+"s");
		if (resultFromCal.isValid() || debug){
			for(Field field:PARAMS){
	    		System.out.println("private static double[] "+field.getName()+"Offset="+Arrays.toString(offsetParams[field.getIdx()]).replace('[', '{').replace(']', '}')+";");
	    	}
	    	for(Field field:PARAMS){
	    		System.out.println("private static double[] "+field.getName()+"Scale="+Arrays.toString(scaleParams[field.getIdx()]).replace('[', '{').replace(']', '}')+";");
	    	}
	    	System.out.println("//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}

        if (resultFromCal.getAccuracy() >= 70.0d){
            FileHelper.writeResult(PREVIOUS_NUM, GAIN_NUM, PARAMS, offsetParams, scaleParams, resultFromCal, lastLearnString);
        }
		
		return resultFromCal.getTargetResult();
    }
    
    
    private static Future<Map<String, Object>> prepareExecuteThreadForCalculateWithCachedData(ExecutorService threadPool, CountDownLatch latch, int length, final List<CachedStockGainData> cachedDatas, final int paramIdx, final int preIdx, final Double offsetValue, final double scaleValue, final String tag){
    	
    	ExecuteThread worker = new ExecuteThread(latch, length) {
			@Override
			public Map<String, Object> work(double[][] offsetParams,
					double[][] scaleParams, Map<String, Object> result) {
				StockStrategyCaculator caculator = new StockStrategyCaculator();
		    	Result resultFromCal =  caculator.calculateOnListWithCache(cachedDatas, dataSelector, /*learnphase*/true, paramIdx, preIdx, offsetValue, scaleValue, /*debug*/false);
				result.put("calResult", resultFromCal);
				
				result.put("fieldId", paramIdx);
				result.put("preId", preIdx);
				result.put("offset", offsetValue);
				result.put("scale", scaleValue);
				
				result.put("tag", tag);
				return result;
			}
		};
    	Future<Map<String, Object>> future = threadPool.submit(worker);
    	return future;
    }
    
    private static Future<Map<String, Object>> prepareExecuteThreadForCalculate(ExecutorService threadPool, CountDownLatch latch, int length, double[][] offsetParams, double[][] scaleParams){
    	
    	ExecuteThread worker = new ExecuteThread(latch, length, offsetParams, scaleParams) {
			@Override
			public Map<String, Object> work(double[][] offsetParams,
					double[][] scaleParams, Map<String, Object> result) {
				StockStrategyCaculator caculator = new StockStrategyCaculator();
		    	Result resultFromCal =  caculator.calculateOnList(allData, dataSelector, true, offsetParams, scaleParams);
				result.put("calResult", resultFromCal);
				for (Field field:PARAMS){
					result.put(field.getName()+"Offset", offsetParams[field.getIdx()]);
					result.put(field.getName()+"Scale", scaleParams[field.getIdx()]);
				}
				return result;
			}
		};
    	Future<Map<String, Object>> future = threadPool.submit(worker);
    	return future;
    }
    
    private List<CachedStockGainData> buildCachedStockGainData(List<StockGainData> datas, double[][] offsetParams, double[][] scaleParams){
    	List<CachedStockGainData> cachedData = new ArrayList<>();
    	for (StockGainData data:datas){
    		cachedData.add(new CachedStockGainData(data, offsetParams, scaleParams));
    	}
    	return cachedData;
    }
    
    private void updateCachedStockGainData(List<CachedStockGainData> cachedDatas, int fieldIdx, int preIdx, double offsetValue, double scaleValue){
    	double scaleValue2 = scaleValue*scaleValue;
    	for (CachedStockGainData cachedData:cachedDatas){
    		cachedData.updateWithSqareScale(fieldIdx, preIdx, scaleValue2, offsetValue); // update with scale*scale performance improve
    	}
    } 
    
//    
    public static void randomizeScaleParams(double[] scaleParam, Field field){
    	randomizeParams(scaleParam, field, scaleParamList);
    }
    
    public static void randomizeParams(double[] param, Field field, double[][] paramLists){
    	double[] paramList = paramLists[field.getIdx()];
    	for (int j=0;j<param.length;j++){
    		param[j] =  paramList[random .nextInt(paramList.length)];
    	}
    }
    
    public static List<StockGainData> randomizeOffsetParams(double[][] offsetParams, double[][] scaleParams){
//    	System.out.println("try to randomize offset params.");
    	List<StockGainData> leftDatas = new LinkedList<>();
    	int size = dataWithHighGain.size();
    	StockGainData center =  dataWithHighGain.get(random.nextInt(size));
//    	System.out.println("try to randomize offset params with center of gain: "+(Math.pow(10,center.getGain())-1.00000000000d)*100.0d);
    	
    	for (int i = 0;i<PREVIOUS_NUM;i++){
			PreviousData centerPData = center.getPreviousData().get(i);
			for (Field field:PARAMS){
				double[] offsetParam = offsetParams[field.getIdx()];
	    		offsetParam[i] = centerPData.getValue(field);
	    	}
		}
    	
    	for(int i=0;i<allData.size();i++){
    		StockGainData data = allData.get(i);
    		boolean inSet = true;
    		for (int t=0;t<PREVIOUS_NUM;t++){
    			PreviousData pData = data.getPreviousData().get(t);
    			PreviousData centerPData = center.getPreviousData().get(t);
    			for (Field field:PARAMS){
    				double value = pData.getValue(field);
					double centerValue = centerPData.getValue(field);
            		if (value<centerValue-scaleParams[field.getIdx()][t] || value>centerValue+scaleParams[field.getIdx()][t]){
            			inSet = false;
            		}
    			}
        		
    		}
    		if (inSet){
    			leftDatas.add(data);
    		}
    	}
    	return leftDatas;
    }
    
    public static void prepareParamSteps(){
    	for (Field field:PARAMS){
    		double min = getDataMIN(field.getName(), 0)*0.95f;
        	double max = getDataMAX(field.getName(), 0)*1.0f;
        	scaleStep.put(field.getName(), (max-min)/(double)(scale_steps-1));
        	for (int i=0;i<scale_steps;i++){
        		scaleParamList[field.getIdx()][i] = (i+1)*(scaleStep.get(field.getName()));
        	}
        	
        	double[] fieldOffsetSteps = new double[PREVIOUS_NUM];
        	for (int i=0;i<PREVIOUS_NUM;i++){
        		double minAtIdx = getDataMIN(field.getName(), i);
        		double maxAtIdx = getDataMAX(field.getName(), i);
            	fieldOffsetSteps[i] = (maxAtIdx-minAtIdx)/(double)(offset_steps-1);
            	offsetSteps[field.getIdx()] = fieldOffsetSteps;
        	}
    	}
    }
    
    
    public static void prepareOffsetInitPoint(String dataType){
    	double[] initPoint = new double[PREVIOUS_NUM];
    	offsetInitPoint.put(dataType, initPoint);
    	double[] dataMin = dataMIN.get(dataType);
    	double[] dataMax = dataMAX.get(dataType);
    	for (int i=0;i<PREVIOUS_NUM;i++){
    		initPoint[i]=(dataMax[i]+dataMin[i])/2.0f;
    	}
    }
    
    private static boolean offsetScaleInMAXAndMIN(double offset, double scale, String dataType, int index){
    	return offset-scale< getDataMAX(dataType, index) && offset+scale > getDataMIN(dataType, index);
    }
    
    private static double getDataMIN(String dataType, int index){
    	double MIN = 1000000f;
    	double[] dataMin;
    	if (!dataMIN.containsKey(dataType)){
    		dataMin = new double[PREVIOUS_NUM];
    		for (int i=0;i<PREVIOUS_NUM;i++){
    			dataMin[i] = MIN;
    		}
    		dataMIN.put(dataType, dataMin);
    	}else{
    		dataMin = dataMIN.get(dataType);
    	}
    	return dataMin[index];
    }
    
    private static void setDataMIN(String dataType, int index, double value){
    	double MIN = 1000000f;
    	double[] dataMin;
    	if (!dataMIN.containsKey(dataType)){
    		dataMin = new double[PREVIOUS_NUM];
    		for (int i=0;i<PREVIOUS_NUM;i++){
    			dataMin[i] = MIN;
    		}
    		dataMIN.put(dataType, dataMin);
    	}else{
    		dataMin = dataMIN.get(dataType);
    	}
    	dataMin[index] = value;
    }
    
    private static double getDataMAX(String dataType, int index){
    	double MAX = -1000000f;
    	double[] dataMax;
    	if (!dataMAX.containsKey(dataType)){
    		dataMax = new double[PREVIOUS_NUM];
    		for (int i=0;i<PREVIOUS_NUM;i++){
    			dataMax[i] = MAX;
    		}
    		dataMAX.put(dataType, dataMax);
    	}else{
    		dataMax = dataMAX.get(dataType);
    	}
    	return dataMax[index];
    }
    
    private static void setDataMAX(String dataType, int index, double value){
    	double MAX = -1000000f;
    	double[] dataMax;
    	if (!dataMAX.containsKey(dataType)){
    		dataMax = new double[PREVIOUS_NUM];
    		for (int i=0;i<PREVIOUS_NUM;i++){
    			dataMax[i] = MAX;
    		}
    		dataMAX.put(dataType, dataMax);
    	}else{
    		dataMax = dataMAX.get(dataType);
    	}
    	dataMax[index] = value;
    }
    
    public static void transeferToLog(){
    	for (StockGainData data:allData){
    		data.setGain(Math.log10(data.getGain()));
    		for (PreviousData pData: data.getPreviousData()){
    			for (Field param: PARAMS){
    				double value = pData.getValue(param);
    				double logValue = Math.log10(value);
					pData.setValue(param, logValue);
					if (logValue < getDataMIN(param.getName(), pData.getOffset())){
        				setDataMIN(param.getName(), pData.getOffset(), logValue);
        			}
        			if (logValue>getDataMAX(param.getName(), pData.getOffset())){
        				setDataMAX(param.getName(), pData.getOffset(), logValue);
        			}
    			}
    		}
    	}
    }
    
    public static void copyArray(double[] src, double[] dst){
    	if (dst==null || dst.length!=src.length){
    		dst = new double[src.length];
    	}
    	for (int i=0;i<src.length;i++){
    		dst[i] = src[i];
    		
    	}
    }

}
