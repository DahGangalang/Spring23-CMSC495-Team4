/*
 * Toolbox for some minor functions used throughout the recipe repoistory 
 */


public class Recipe_Tools {
    
    //For stripping out whitespace surrounding a string
    public static String stripSurroundingWhiteSpace(String input) {

        if(input == null) {
            return "";
        }

        int start = 0;
        int end = input.length() - 1;
        while(start < end && Character.isWhitespace(input.charAt(start))) {
            start++;
        }

        while(end > start && Character.isWhitespace(input.charAt(end))) {
            end--;
        }

        return input.substring(start, end + 1);
    }
}
