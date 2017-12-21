import java.util.NoSuchElementException;
import java.util.Objects;


public class Main {
    private static boolean persistentArrayTest() {
        int initialCapacity = 10;

        PersistentArray<Integer> array = new PersistentArray<>(5);
        array.set(3, 40);
        array.set(3, 42);
        array.set(3, 36);
        array.add(1);
        array.remove();
        array.remove();
        PersistentArray<String> arrayWithDefaultSize = new PersistentArray<>();
        arrayWithDefaultSize.add("Hello World!");
        PersistentArray<Object> arrayWithSizeOne = new PersistentArray<>(1);
        arrayWithSizeOne.remove();
        boolean checkRemoveNothing = false;
        try {
            arrayWithSizeOne.remove();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            checkRemoveNothing = true;
        }
        boolean checkGetNonExistingElement = false;
        try {
            array.get(10);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            checkGetNonExistingElement = true;
        }
        boolean checkGetElementFromNonExistingVersion = false;
        try {
            array.get(1, 25);
        }
        catch (NoSuchElementException e) {
            checkGetElementFromNonExistingVersion = true;
        }
        boolean checkSetNonExistingElement = false;
        try {
            array.set(10, 10);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            checkSetNonExistingElement = true;
        }

        return ((Objects.equals(array.get(3), array.get(3, 3)))
                && (array.getLength(0) == 5)
                && (array.getLength(4) == 6)
                && (array.getLength() == 4)
                && (arrayWithDefaultSize.getLength() == initialCapacity + 1)
                && checkRemoveNothing && checkGetNonExistingElement && checkGetElementFromNonExistingVersion && checkSetNonExistingElement);

    }

    public static void main(String[] args) {
        System.out.print("Persistent array test " + (persistentArrayTest() ? "passed" : "failed"));
    }
}
