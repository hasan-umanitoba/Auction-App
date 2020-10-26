package comp3350.auctionapp.objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.requireNonNull;

/**
 * Dollar
 * Stores information about a single dollar amount. Currency is CAD
 */
public class Dollar implements Serializable {
    private static final char CURRENCY = '$';
    private static final int DECIMAL_PLACES = 2;
    private BigDecimal amount;

    public Dollar(String amount) {
        requireNonNull(amount, "Amount can't be null!");

        try {
            this.amount = new BigDecimal(amount).setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    @Override
    public String toString() {
        if (isNegative()) {
            return "-" + CURRENCY + (amount.abs()).toString();
        }
        return CURRENCY + amount.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dollar) {
            return this.amount.equals(((Dollar) obj).amount);
        }
        return false;
    }

    public int compareTo(Dollar otherDollar) {
        requireNonNull(otherDollar, "Comparing against null!");

        return this.amount.compareTo(otherDollar.amount);
    }

    public boolean isNegative() {
        return amount.signum() == -1;
    }

    public boolean isZero() {
        return amount.equals(new BigDecimal("0.00"));
    }

    public String getBidAmount() {
        return amount.toString();
    }
}
