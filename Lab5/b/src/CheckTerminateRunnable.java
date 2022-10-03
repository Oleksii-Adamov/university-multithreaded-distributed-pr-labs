import java.util.HashMap;

public class CheckTerminateRunnable implements Runnable{

    private StringBuffer[] stringArr;

    public boolean isToTermianate() {
        return toTermianate;
    }

    private boolean toTermianate = false;


    public CheckTerminateRunnable(StringBuffer[] stringArr) {
        this.stringArr = stringArr;
    }

    @Override
    public void run() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < stringArr.length; i++) {
            int count = 0;
            for (int j = 0; j < stringArr[i].length(); j++) {
                if (stringArr[i].charAt(j) == 'A' || stringArr[i].charAt(j) == 'B')
                    count++;
            }
            if (map.containsKey(count)) {
                map.replace(count, map.get(count) + 1);
            }
            else {
                map.put(count, 1);
            }
        }
        for (Integer i: map.values()) {
            if (i >= 3) {
                toTermianate = true;
                break;
            }
        }
    }
}
