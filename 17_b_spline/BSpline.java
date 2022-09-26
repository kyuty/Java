import java.util.*;

public class BSpline {

    static class Point {
        float x;
        float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "x = " + x + " y = " + y;
        }
    }

    public static void main(String args[]){
        // touchSpecialPoint 为关键的点，即拐点
        // （对应你那边的需求：就是touch点，touch的拐点，请你先做第一步的优化判断，即找到 需要 touch 点当中的拐点）
        ArrayList<Point> touchSpecialPoint = new ArrayList<>();
        // 计算出来的 b样曲线 数据
        // key 表示第几条线段, value 表示线段上的点
        HashMap<Integer, ArrayList<Point>> genBSplinePoint = new HashMap<>();
        touchSpecialPoint.add(new Point(100.0f, 100.0f));
        touchSpecialPoint.add(new Point(200.0f, 200.0f));
        touchSpecialPoint.add(new Point(300.0f, 100.0f));
        touchSpecialPoint.add(new Point(400.0f, 200.0f));
        touchSpecialPoint.add(new Point(500.0f, 100.0f));

        // 一次性计算，5个拐点
        genBSplinePoint(touchSpecialPoint, -1, genBSplinePoint);
        System.out.println("\ngenBSplinePoint = " + genBSplinePoint);


        // 在上次 5 个拐点的基础上，增加 1 个拐点，变成 6 个拐点
        // 我已经做了优化，不会重复计算之前不需要变化的线段
        touchSpecialPoint.add(new Point(600.0f, 200.0f));
        genBSplinePoint(touchSpecialPoint, 1, genBSplinePoint);
        System.out.println("\ngenBSplinePoint = " + genBSplinePoint);

        // 在上次 6 个拐点的基础上，增加 2 个拐点，变成 8 个拐点
        // 我已经做了优化，不会重复计算之前不需要变化的线段
        touchSpecialPoint.add(new Point(700.0f, 100.0f));
        touchSpecialPoint.add(new Point(800.0f, 200.0f));
        genBSplinePoint(touchSpecialPoint, 2, genBSplinePoint);
        System.out.println("\ngenBSplinePoint = " + genBSplinePoint);


        // TODO:
        // b样曲线的一个缺点是末端没有相接，需要增加一个 end point 才能相接
        // 绘制完 genBSplinePoint 之后，把 genBSplinePoint 的最后一个点 与 touchSpecialPoint 的最后一个点 简单相连绘制出来即可

        // check
        //genBSplinePoint.clear();
        //genBSplinePoint(touchSpecialPoint, -1, genBSplinePoint);
        //System.out.println("\n check genBSplinePoint = " + genBSplinePoint);

    }

    public static ArrayList<Integer> tArrayList = new ArrayList<>();
    public static final int ORDER = 3;
    public static float step = 0.1f;

    public static void genBSplinePoint(ArrayList<Point> touchSpecialPoint, int appendPoint, HashMap<Integer, ArrayList<Point>> out) {
        tArrayList = genTArray(touchSpecialPoint.size());
        int maxT = tArrayList.get(tArrayList.size() - 1);
        int startT;
        if (appendPoint == -1) {
            startT = 0;
        } else {
            // 3 哪里来的，即往前找 3 个点即可，只需要更新前 3 个点对应的线段
            startT = Math.max(0, tArrayList.get(tArrayList.size() - 1 - appendPoint) - 3);
        }
        int count = startT + 1;
        ArrayList<Point> points = out.get(count);
        if (points != null) {
            points.clear();
        }
        //System.out.println("maxT = " + maxT + " size = " + touchSpecialPoint.size());
        //System.out.println("update [" + (count -1)  + "-" + count + "]");
        //System.out.print("\t");
        for (float t = startT; t <= maxT; t += step) {
            Point res = cal(t, touchSpecialPoint);
            System.out.print(" t = " + t + " res = " + res);
            ArrayList<Point> pointsTemp = out.get(count);
            if (pointsTemp == null) {
                pointsTemp = new ArrayList<>();
                out.put(count, pointsTemp);
            }
            pointsTemp.add(res);
            if (t > count) {
                count++;
                ArrayList<Point> pointNeedClear = out.get(count);
                if (pointNeedClear != null) {
                    pointNeedClear.clear();
                }
                //System.out.println("");
                //System.out.println("update [" + (count -1)  + "-" + count + "]");
                //System.out.print("\t");
            }
        }
    }

    public static Point cal(float t, ArrayList<Point> data) {
        Point point = new Point(0, 0);
        int maxT = tArrayList.get(tArrayList.size() - 1);
        if (t < 0 || t > maxT) {
            return point;
        }
        for (int index = 0; index < data.size(); index++) {
            float ratio = getRatio(t, index, ORDER);
            point.x += data.get(index).x * ratio;
            point.y += data.get(index).y * ratio;
        }
        return point;
    }

    public static ArrayList<Integer> genTArray(int size) {
        tArrayList.clear();
        int length = size + ORDER;
        for (int i = 0; i < length; i++) {
            if (i < ORDER) {
                tArrayList.add(0);
            } else if (i <= size) {
                tArrayList.add(i - ORDER + 1);
            } else {
                tArrayList.add(size - ORDER + 1);
            }
        }
        return tArrayList;
    }

    public static float getRatio(float t, int index, int order) {
        if (order == 1) {
            if (t >= tArrayList.get(index) && t < tArrayList.get(index + 1)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            float m0 = t - tArrayList.get(index);
            float m1 = tArrayList.get(index + order - 1) - tArrayList.get(index);
            float n0 = tArrayList.get(index + order) - t;
            float n1 = tArrayList.get(index + order) - tArrayList.get(index + 1);
            float m = getDividedValue(m0, m1);
            float n = getDividedValue(n0, n1);
            return m * getRatio(t, index, order - 1) + n * getRatio(t, index + 1, order - 1);
        }
    }

    private static float getDividedValue(float a, float b) {
        if (b == 0) {
            return 0;
        } else {
            return a / b;
        }
    }

}
