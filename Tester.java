class Tester {
    public static void main(String[] args) {
        sort();
    }

    public static void sort() {
        int[] array = new int[]{1, 50, 3, 4, 0};

        // find min and max:
        int max = -10000, min = 10000;
        for (var num : array) {
            if (num > max) max = num;
            if (num < min) min = num;
        }

        // create a map:
        boolean[] map = new boolean[max - min + 1];
        for (var num : array) {
            map[num - min] = true;
        }

        // get the sorted array:
        int index = 0;
        int[] sorted = new int[array.length];
        for (int i = 0; i < map.length; i++) {
            if (map[i]) {
                sorted[index] = i + min;
                index++;
            }
        }

        System.out.println("Expecting [0, 1, 3, 4, 50]");

        for (var num : sorted) {
            System.out.println(num);
        }
    }
}
