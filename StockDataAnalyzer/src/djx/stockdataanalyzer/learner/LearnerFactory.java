package djx.stockdataanalyzer.learner;

import djx.stockdataanalyzer.data.ModelWithStatistic;
import djx.stockdataanalyzer.data.ResultModel;
import djx.stockdataanalyzer.data.StockDataModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by dave on 2016/5/1.
 */
public class LearnerFactory <T extends ILeaner> {
    private Class<T> clazz;
    private LearnerFactory supplier;

    public static <T extends ILeaner> LearnerFactory get(Class<T> clazz) {
        return new LearnerFactory(clazz);
    }

    public static <T extends ILeaner> LearnerFactory get(Class<T> clazz, LearnerFactory supplier) {
        return new LearnerFactory(clazz, supplier);
    }

    public static <T extends ILeaner> LearnerFactoryHelper prepare(Class<T> clazz) {
        return new LearnerFactoryHelper(clazz);
    }

    private LearnerFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    private LearnerFactory(Class<T> clazz, LearnerFactory supplier) {
        this.clazz = clazz;
        this.supplier = supplier;
    }

    public ILeaner buildLearner(Object... args) {
        try {
            Constructor<ILeaner> constructor = findConstructor(clazz.getConstructors(), args);

            if (constructor == null) {
                System.err.println("failed to find constructor for learner " + clazz.getSimpleName());
                System.exit(1);
            }

            ILeaner leaner = constructor.newInstance(args);
            if (leaner instanceof HierarchyLearner) {
                ((HierarchyLearner)leaner).factory = this.supplier;
            }
            return leaner;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor findConstructor(Constructor[] constructors, Object...args) {
        for(Constructor constructor: constructors) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            if (paramTypes.length == args.length) {
                boolean allParamsCompatible = true;
                for (int i=0;i< args.length; i++) {
                    Class<?> paramType = paramTypes[i];
                    Object obj = args[i];
                    if (!paramType.isAssignableFrom(obj.getClass())) {
                        allParamsCompatible = false;
                    }
                }
                if (allParamsCompatible) {
                    return constructor;
                }
            }
        }
        return null;
    }

    public static class LearnerFactoryHelper {
        LearnerFactoryHelper head;
        LearnerFactoryHelper pre;
        Class<?> c;
        LearnerFactory factory;

        private <T extends ILeaner> LearnerFactoryHelper(Class<T> clazz) {
            this(null, null, clazz);
        }

        private <T extends ILeaner> LearnerFactoryHelper(LearnerFactoryHelper head, LearnerFactoryHelper pre, Class<T> clazz) {
            this.head = head == null ? this : head;
            this.pre = pre;
            this.factory = new LearnerFactory(clazz);
            this.c = clazz;
            if (pre != null) {
                if (HierarchyLearner.class.isAssignableFrom(pre.c)) {
                    pre.factory.supplier = this.factory;
                }
            }
        }

        public <T extends ILeaner> LearnerFactoryHelper append(Class<T> clazz) {
            return new LearnerFactoryHelper(this.head, this, clazz);
        }

        public LearnerFactory get() {
            return this.head.factory;
        }
    }
}
