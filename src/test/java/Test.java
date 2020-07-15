public class Test {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("012345");
        System.out.println(sb.delete(sb.length() - 3, sb.length()));
        System.out.println(sb.reverse());
        System.out.println(sb);
    }
}
