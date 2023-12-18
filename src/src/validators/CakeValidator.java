package validators;

import domain.Cake;

public class CakeValidator {

    public static void validateCake(Cake cake) {
        if (cake == null) {
            throw new IllegalArgumentException("Cake cannot be null.");
        }

        validateFlavour(cake.getFlavour());
        validateSize(cake.getSize());
        validateCakeID(cake.getId());
    }

    private static void validateFlavour(String flavour) {
        if (flavour == null || flavour.trim().isEmpty()) {
            throw new IllegalArgumentException("Flavour cannot be null or empty.");
        }

    }

    private static void validateSize(String size) {
        // Check that the size is valid (e.g., "small", "medium", "large")
        if (!size.equals("small") && !size.equals("medium") && !size.equals("large")&&!size.equals("Small") && !size.equals("Medium") && !size.equals("Large")) {
            throw new IllegalArgumentException("Invalid size for the cake.");
        }
    }

    private static void validateCakeID(Number cakeID) {
        //parse cake id to int
        int id = cakeID.intValue();
        if (cakeID == null || id <= 0) {
            throw new IllegalArgumentException("CakeID must be a positive non-null value.");
        }
    }
}
