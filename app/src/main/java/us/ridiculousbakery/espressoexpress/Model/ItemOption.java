package us.ridiculousbakery.espressoexpress.Model;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by bkuo on 6/3/15.
 */
public class ItemOption {

    public enum Options {
        SIZE_OPTION("Size"),
        MILK_OPTION("Cream"),
        SUGAR_OPTION("Sugar")
        ;

        private final String text;

        Options(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

//    public ItemOption(List<String> sizes, List<String> milkAmounts, List<String> sugarAmounts) {
//        options = new TreeMap<>();
//        options.put(Options.SIZE_OPTION, sizes);
//        options.put(Options.MILK_OPTION, milkAmounts);
//        options.put(Options.SUGAR_OPTION, sugarAmounts);
//    }
//
//    public ItemOption(List<String> sizes, List<String> milkAmounts, List<String> sugarAmounts, boolean fauxData) {
//        options = new TreeMap<>();
//        options.put(Options.SIZE_OPTION, Arrays.asList("Small", "Medium", "Large"));
//        options.put(Options.MILK_OPTION, Arrays.asList("None", "Little", "Medium", "A lot"));
//        options.put(Options.SUGAR_OPTION, Arrays.asList("Small", "Little", "Medium", "A lot"));
//    }

    private TreeMap<Options, List<String>> options;

    public TreeMap<Options, List<String>> getOptions() {
        return options;
    }



}
