package android.text;

//This class is to avoid having to launch an entire virtual android machine each time small tests are being run
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}