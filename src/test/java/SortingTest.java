import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SortingTest {

    private final List<Entity> setEntity = new ArrayList<>();
    private final List<Entity> emptyList = new ArrayList<>();

    private Entity[] checkSetEntitySort1, checkSetEntitySort2,checkSetEntityTwoSort1,checkSetEntityTwoSort2, checkSetEmptyList;


    @Before
    public void createEntity() {

        Entity apple = new Entity("яблоко", "З");
        Entity cucumber = new Entity("огурец", "З");
        Entity plum = new Entity("слива", "С");
        Entity tomato = new Entity("помидор", "К");
        checkSetEntitySort1 = new Entity[] {apple, cucumber, apple, plum, plum, plum, tomato, tomato, tomato, tomato};
        checkSetEntitySort2 = new Entity[] {plum, plum, plum, tomato, tomato, tomato, tomato, cucumber, apple, apple};

        checkSetEntityTwoSort1 = new Entity[] {tomato, tomato, tomato, tomato, apple, cucumber, apple};
        checkSetEntityTwoSort2 = new Entity[] {apple, cucumber, apple, plum, plum, plum};

        checkSetEmptyList = new Entity[0];

        setEntity.add(apple);
        setEntity.add(tomato);
        setEntity.add(plum);
        setEntity.add(tomato);
        setEntity.add(tomato);
        setEntity.add(cucumber);
        setEntity.add(apple);
        setEntity.add(plum);
        setEntity.add(plum);
        setEntity.add(tomato);
    }
    @Test
    public void sort1() {
        System.out.println("Тест 1\n");
        String conditionSort1 = "З < С < К";
        Main.sortingObjects(setEntity, conditionSort1, true);
        Assert.assertArrayEquals("Тест 1. Массивы не совпадают",setEntity.toArray(), checkSetEntitySort1);
        System.out.println("Тест 1 завершен");
    }

    @Test
    public void sort2() {
        System.out.println("Тест 2\n");
        String conditionSort1 = "З > К > С";
        Main.sortingObjects(setEntity, conditionSort1,false);
        Assert.assertArrayEquals("Тест 2. Массивы не совпадают", setEntity.toArray(), checkSetEntitySort2);
        System.out.println("Тест 2 завершен\n");
    }

    @Test
    public void NoCorrectCondition() {
        System.out.println("Тест 3\n");
        String conditionSort1 = "З > К < С";
        Object[] tempMass = setEntity.toArray();
        Main.sortingObjects(setEntity, conditionSort1, true);
        Assert.assertArrayEquals("Тест 3. Массивы не совпадают", setEntity.toArray(), tempMass);
        System.out.println("Тест 3 завершен\n");
    }

    @Test
    public void NoCorrectSort2() {
        System.out.println("Тест 4\n");
        String conditionSort1 = "З < К > С";
        Object[] tempMass = setEntity.toArray();
        Main.sortingObjects(setEntity, conditionSort1,false);
        Assert.assertArrayEquals("Тест 4. Массивы не совпадают", setEntity.toArray(), tempMass);
        System.out.println("Тест 4 завершен\n");
    }

    @Test
    public void InsertionSortingTwoObjects() {
        System.out.println("Тест 5\n");
        String conditionSort1 = "З > К";
        Main.sortingObjects(setEntity, conditionSort1, true);
        Assert.assertArrayEquals("Тест 5. Массивы не совпадают", setEntity.toArray(), checkSetEntityTwoSort1);
        System.out.println("Тест 5 завершен\n");
    }

    @Test
    public void QuickSortingTwoObjects() {
        System.out.println("Тест 6\n");
        String conditionSort1 = "З < С";
        Main.sortingObjects(setEntity, conditionSort1,false);
        Assert.assertArrayEquals("Тест 6. Массивы не совпадают", setEntity.toArray(), checkSetEntityTwoSort2);
        System.out.println("Тест 6 завершен\n");
    }

    @Test
    public void NoData() {
        System.out.println("Тест 7\n");
        String conditionSort1 = "З < С";
        Main.sortingObjects(emptyList, conditionSort1,false);
        Assert.assertArrayEquals("Тест 7. Массивы не совпадают", emptyList.toArray(), checkSetEmptyList);
        System.out.println("Тест 7 завершен\n");
    }

    // подаются объекты отличные от условия
    @Test
    public void NoData1() {
        System.out.println("Тест 8\n");
        String conditionSort1 = "З < С";
        Main.sortingObjects(emptyList, conditionSort1,false);
        Assert.assertArrayEquals("Тест 8. Массивы не совпадают", emptyList.toArray(), checkSetEmptyList);
        System.out.println("Тест 8 завершен\n");
    }

    @Test
    public void NoCorrectCondition2() {
        System.out.println("Тест 9\n");
        String conditionSort1 = " > К < С";
        Object[] tempMass = setEntity.toArray();
        Main.sortingObjects(setEntity, conditionSort1, true);
        Assert.assertArrayEquals("Тест 9. Массивы не совпадают", setEntity.toArray(), tempMass);
        System.out.println("Тест 9 завершен\n");
    }

    @Test
    public void NoCorrectCondition3() {
        System.out.println("Тест 10\n");
        String conditionSort1 = "Зеленый < Красный < Синий";
        Object[] tempMass = setEntity.toArray();
        Main.sortingObjects(setEntity, conditionSort1, true);
        Assert.assertArrayEquals("Тест 10. Массивы не совпадают", setEntity.toArray(), tempMass);
        System.out.println("Тест 10 завершен\n");
    }

    @Test
    public void NoCorrectCondition4() {
        System.out.println("Тест 11\n");
        String conditionSort1 = "З C";
        Object[] tempMass = setEntity.toArray();
        Main.sortingObjects(setEntity, conditionSort1, true);
        Assert.assertArrayEquals("Тест 11. Массивы не совпадают", setEntity.toArray(), tempMass);
        System.out.println("Тест 11 завершен\n");
    }
}