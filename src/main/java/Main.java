import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        List<Entity> setEntity = new ArrayList<>();
        boolean isInsertion = false;
        boolean isTypeSorting = true;
        String conditions;
        File file = new File ("Objects.txt");

        //проверка наличия файла Objects.txt и на пустоту
        if(!file.isFile() || file.length() == 0) {
            System.out.println("Файл Objects.txt отсутствует или пустой ");
            return;
        }

        System.out.println("Введите количество объектов, которые нужно считать со файла");
        int amountObjects = scanner.nextInt();
        scanner.nextLine();
        System.out.println();

        //Выбор сортировки
        while(isTypeSorting) {
            System.out.println("Введите тип сортировки insertion (сортировка вставками) или quick (быстрая сортировка)");
            String typeSorting = scanner.nextLine();
            switch (typeSorting) {
                case ("insertion"):
                    isInsertion = true;
                    isTypeSorting = false;
                    break;
                case ("quick"):
                    isTypeSorting = false;
                    break;
            }
        }
        System.out.println();


        /*
        Считывание объектов с файла Objects.txt
        Объект не будет записан если первая строка будет пустая
        или во второй будет написано что-то кроме букв К, З, С обозначающих цвет
        */
        try (
        FileReader fileReader1 = new FileReader("Objects.txt");
        BufferedReader br1 = new BufferedReader(fileReader1)
        ){
            String line1 = br1.readLine();
            String line2 = br1.readLine();
            for (int i = 1; i <= amountObjects; i++) {

                if ((line1 == null || line2 == null) && br1.readLine() == null) {
                    break;
                }
                if (!line1.isBlank() && line2.matches("[КЗС]")) {
                    setEntity.add(new Entity(line1, line2));
                }
                br1.readLine();
                line1 = br1.readLine();

                line2 = br1.readLine();
            }
        }

        if (checkSizeSet(setEntity)) {
            System.out.println("Не было добавлено объектов\n");
            return;
        }

        do {
            System.out.println("Введите условия отношения,отделяя буквы и знаки пробелом (Пример: К < З < С)");
            conditions = scanner.nextLine();
        } while(!checkConditions(Arrays.stream(conditions.split(" ")).collect(Collectors.toList())));
        System.out.println();
/*
        //вывод списка элементов считанных из файла
        System.out.println("Вывод списка элементов:\n");
        for (Entity temp : setEntity) {
            System.out.println (temp.getName() + " " + temp.getColor());
        }

        System.out.println("\n");
*/
        //Сортировка
        sortingObjects(setEntity,conditions, isInsertion);

        //Запись сортированных объектов в файл
        try (
                FileWriter writer = new FileWriter("out.txt")
        ) {
            for (Entity temp : setEntity) {
                writer.write (temp.getName() + " " + temp.getColor() + "\n");
            }
        }
/*
        //вывод списка элементов после сортировки
        System.out.println("Вывод списка элементов после сортировки:\n");
        for (Entity temp : setEntity) {
            System.out.println (temp.getName() + " " + temp.getColor());
        }

        System.out.println("\n");
*/
    }

    /**
     * Сортировка листа с объектами
     * @param setEntity лист объектов
     * @param conditions строка с отношением порядка цветов (использовать только буквы и знаки < или >)
     */
    public static void sortingObjects(List<Entity> setEntity, String conditions, boolean isInsertion) {
        List<String> sortingConditions = new ArrayList<>();

        boolean isOver;
        List<Integer> indexColors = new ArrayList<>();
        List<String> colorsObjects = new ArrayList<>();
        int tempIndex;

        //Перевод условий из строки в лист, для удобства обработки.
        Collections.addAll(sortingConditions,conditions.split(" "));

        //Удаление пустых элементов
        sortingConditions.removeIf(String::isEmpty);

        //Проверка условий, при неверных условиях возвращает первоначальный список.
        if (!checkConditions(sortingConditions)) {
            System.out.println("Будет возвращен первоначальный список\n");
            return;
        }

        //создается лист с набором цветов в соответствии с местом нахождения объекта
        for (Entity entity : setEntity) {
            colorsObjects.add(entity.getColor());
        }

        //определяется направление отношения порядка цветов
        isOver = sortingConditions.get(1).equals(">");

        //удаление из условий знаков больше/меньше
        sortingConditions.removeIf(i ->i.matches("([<>])"));



        //в зависимости от направления создается лист с индексами для дальнейшей сортировки
        if (isOver) {
            for (String color : colorsObjects) {
                indexColors.add(sortingConditions.size() - 1 - sortingConditions.indexOf(color));
            }
        } else {
            for (String color : colorsObjects) {
                indexColors.add(sortingConditions.indexOf(color));
            }
        }

        //Удаление объектов не участвующих в сортировке
        while (true) {
            tempIndex = isOver?indexColors.indexOf(sortingConditions.size()):indexColors.indexOf(-1);
            if (tempIndex < 0) {
                break;
            }
            indexColors.remove(tempIndex);
            setEntity.remove(tempIndex);
        }

        if (checkSizeSet(setEntity)) {
            System.out.println("Нет данных для сортировки\n");
            return;
        }

        if (isInsertion) {
            bubbleSort(indexColors, setEntity);
        } else {
            quickSort(indexColors, 0, indexColors.size() - 1,setEntity);
        }
    }

    /**
     * Проверки: количество элементов в условии(минимум 3), чередования знаков больше/меньше со словами, на совпадение знаков при наличии 3 цветов
     * @param sortingConditions лист с отношением порядка цветов
     * @return результат проверки
     */
    private static boolean checkConditions(List<String> sortingConditions) {

        if (sortingConditions.size() < 3 || sortingConditions.size() > 5) {
            System.out.println("Неверное количество элементов в условии\n");
            return false;
        }

        for (int i = 0; i < sortingConditions.size(); i++) {
            if (i % 2 == 0) {
                if (!sortingConditions.get(i).matches("[КЗС]")) {
                    System.out.println("Возможно пропущены буква или присутствует некорректное обозначение цвета (допускаются только буквы)\n");
                    sortingConditions.set(i,sortingConditions.get(i).replaceAll("[КЗС]", ""));
                    return false;
                }
                for(int j = i + 1; j < sortingConditions.size(); j++) {
                    if (sortingConditions.get(i).equals(sortingConditions.get(j))) {
                        System.out.println("В условии две буквы \"" + sortingConditions.get(i)+"\"");
                        return false;
                    }
                }
            } else {
                if (!sortingConditions.get(i).matches("([<>])")) {
                    System.out.println("Возможно некорректно веден знак больше/меньше\n");
                    sortingConditions.set(i, sortingConditions.get(i).replaceAll("([<>])",""));
                    return false;
                }
            }
            if (sortingConditions.size() == 5 && !Objects.equals(sortingConditions.get(1), sortingConditions.get(3))) {
                System.out.println("Знаки больше/меньше различаются. Невозможно определить отношение порядка.\n");
                return false;
            }

        }
        return true;
    }

    /**
     * @param indexColors цвета объектов переведенные в индексы для сортировки
     * @param low         нижняя граница сортируемого участка
     * @param high        верхняя граница сортируемого участка
     * @param setEntity   - лист с объектами
     */
    public static void quickSort(List<Integer> indexColors, int low, int high, List<Entity> setEntity) {

        int middle = low + (high - low) / 2;
        int border = indexColors.get(middle);

        int k = low, j = high;
        while (k <= j) {
            while (indexColors.get(k) < border) k++;
            while (indexColors.get(j) > border) j--;
            if (k <= j) {
                int swap = indexColors.get(k);
                indexColors.set(k, indexColors.get(j));
                Collections.swap(setEntity, k, j);
                indexColors.set(j, swap);
                k++;
                j--;
            }
            if (low < j) quickSort(indexColors, low, j, setEntity);
            if (high > k) quickSort(indexColors, k, high, setEntity);
        }
    }

    /**
     * @param indexColors цвета объектов переведенные в индексы для сортировки
     * @param setEntity   лист с объектами
     */
    public static void bubbleSort(List<Integer> indexColors, List<Entity> setEntity) {
        for (int i = 1; i < indexColors.size(); ++i) {
            int x = indexColors.get(i);
            int j = i;
            while (j > 0 && indexColors.get(j - 1) > x) {
                Collections.swap(indexColors,j,j-1);
                Collections.swap(setEntity,j,j-1);
                --j;
            }
        }
    }

    public static boolean checkSizeSet (List <Entity> setEntity) {
        return setEntity.size() < 1;
    }

}
