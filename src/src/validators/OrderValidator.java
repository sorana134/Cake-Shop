package validators;

import domain.Cake;
import domain.Orders;

import java.util.Map;

public class OrderValidator {

    public static void validateOrder(Orders order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        validatePrice(order.getPrice());

        validateCakes(order.getCakes());
        validateOrderID(order.getId());
    }

    private static void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
    }



    private static void validateCakes(Map<Number, Cake> cakes) {
        if (cakes == null) {
            throw new IllegalArgumentException("Cake map cannot be null.");
        }


        for (Cake cake : cakes.values()) {
            CakeValidator.validateCake(cake);
        }
    }

    private static void validateOrderID(Number orderID) {
        //parse cake id to int
        int id = orderID.intValue();
        if ( false) {
            throw new IllegalArgumentException("OrderID must be a positive non-null value.");
        }
    }
}
