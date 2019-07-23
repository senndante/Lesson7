import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

public class TestClass {

    public static void start(Class c) {
        getClassInfo(c);

        Method[] methods = c.getDeclaredMethods();
        ArrayList<Method> al = new ArrayList<>();

        //Найдем все методы с аннотацией @Test
        for(Method o : methods) {
            if (o.isAnnotationPresent(Test.class)) {
                al.add(o);
            }
        }

        //Отсортируем ArrayList в порядке уменьшения приоритета (1 - наивысший приоритет, 10 - самый низкий)
        al.sort(new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getAnnotation(Test.class).prioritet() - o2.getAnnotation(Test.class).prioritet();
            }
        });

        //Найдем и добавим методы с аннотациями @BeforeSuite и @AfterSuite на нулевое и последнее места в ArrayList
        //Если найдем больше одного таких методов, выбросим исключение
        for(Method o : methods) {
            if (o.isAnnotationPresent(BeforeSuite.class)) {
                if (!al.get(0).isAnnotationPresent(BeforeSuite.class)) {
                    al.add(0, o);
                }
                else {
                    throw new RuntimeException("Аннотация @BeforeSuite должна быть единственной");
                }
            }
        }

        for(Method o : methods) {
            if (o.isAnnotationPresent(AfterSuite.class)) {
                if (!al.get(al.size() - 1).isAnnotationPresent(BeforeSuite.class)) {
                    al.add(al.size() - 1, o);
                }
                else {
                    throw new RuntimeException("Аннотация @AfterSuite должна быть единственной");
                }
            }
        }

        //Создаем объект тестируемого класса и выполняем методы из подготовленного ArrayList
        try {
            Object object = c.newInstance();
            for(Method m : al) {
                m.invoke(object, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getClassInfo(Class c) {
        System.out.println("Сведения о классе:");
        System.out.println("Имя класса: " + c.getSimpleName());
        Constructor[] cons = c.getConstructors();
        System.out.println("Количество конструкторов: " + cons.length);
        int i = 1;
        for (Constructor v : cons) {
            System.out.println("Конструктор " + i + ": ");
            System.out.println("  Количество параметров: " + v.getParameterCount());
            System.out.print("    Тип параметров: ");
            Class[] ts = v.getParameterTypes();
            for (Class e : ts) {
                System.out.print(e.getSimpleName() + " ");
            }
            System.out.println();
            i++;
        }

        Method[] met = c.getDeclaredMethods();
        System.out.println("Количество методов: " + met.length);
        i = 1;
        for (Method v : met) {
            System.out.print("  " + v.getName() + "(");
            Class[] ts = v.getParameterTypes();
            for (Class e : ts) {
                System.out.print(e.getSimpleName());
            }
            System.out.println(")");
            i++;
        }
        Field[] f = c.getFields();
        System.out.println("Полей: " + f.length);
        for (Field v : f) {
            System.out.println("  " + v.getName() + " " + v.getAnnotatedType().getType().getTypeName());
        }
    }
}