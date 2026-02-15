public class Main {
    public static void main(String[] args) {
        Rect rect1 = new Rect(0, 0, 10, 10);
        Rect rect2 = new Rect(5, 0, 10, 10);
        System.out.println(hitTest(rect1, rect2));
    }
    static class Rect {
        int x;
        int y;
        int w;
        int h;

        public Rect(int x, int y, int w, int h){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
    public static boolean hitTest(Rect l, Rect r) {
        boolean xTest = l.x + l.w > r.x && l.x < r.x + r.w;
        boolean yTest = l.y + l.h > r.y && l.y < r.y + r.h;
        return xTest && yTest;
    }
}