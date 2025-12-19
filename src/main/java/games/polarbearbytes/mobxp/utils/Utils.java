package games.polarbearbytes.mobxp.utils;
import org.jetbrains.annotations.Nullable;

public class Utils {
    @Nullable
    public static Integer tryParse(String text, Integer defaultValue){
        try{
            return Integer.parseInt(text);
        } catch(Exception ignored){
            return defaultValue;
        }
    }
    @Nullable
    public static Double tryParse(String text, Double defaultValue){
        try{
            return Double.parseDouble(text);
        } catch(Exception ignored){
            return defaultValue;
        }
    }
    @Nullable
    public static Float tryParse(String text, Float defaultValue){
        try{
            return Float.parseFloat(text);
        } catch(Exception ignored){
            return defaultValue;
        }
    }
}
