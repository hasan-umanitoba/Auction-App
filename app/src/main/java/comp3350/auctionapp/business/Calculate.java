package comp3350.auctionapp.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comp3350.auctionapp.objects.Rating;

public class Calculate {
    //Used when calculating rating averages
    private enum RatingValueType {
        NEGATIVE, NEUTRAL, POSITIVE
    }

    // Returns how long until startTime reaches endTime.
    // Used for calculating time remaining not time passed
    public static String formatTimeDifference(Calendar startTime, Calendar endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Current time and end time can't be null!");
        } else if (startTime.compareTo(endTime) > 0) {
            throw new IllegalArgumentException("Start must come before end");
        }

        if (startTime.equals(endTime)) {
            return "DONE";
        }

        long diffInMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();

        int days = convertFromMillis(TimeUnit.DAYS, diffInMillis);
        int hours = convertFromMillis(TimeUnit.HOURS, diffInMillis);
        int minutes = convertFromMillis(TimeUnit.MINUTES, diffInMillis);
        int seconds = convertFromMillis(TimeUnit.SECONDS, diffInMillis);

        String output = "";

        //Future iteration may implement months and years
        if (days > 365) {
            throw new IllegalArgumentException("Too many days!");
        }

        if (days != 0) {
            output += days + "d";
        } else if (hours != 0) {
            output += hours + "h";
        } else if (minutes != 0) {
            output += "0h " + minutes + "m";
        } else {
            output += "0m " + seconds + "s";
        }

        return output;
    }

    public static double ratingAverage(List<Rating> ratings) {
        double result = 0.0;

        if (ratings != null && ratings.size() > 0) {
            double sum = 0;

            for (Rating rating : ratings) {
                sum += rating.getRatingValue();
            }

            result = sum / (double) ratings.size();

            BigDecimal bigDecimal = new BigDecimal(result).setScale(1, RoundingMode.HALF_UP);
            result = bigDecimal.doubleValue();
        }

        return result;
    }

    public static int positiveRatingsAsPercent(List<Rating> ratings) {
        return getRatingPercentage(ratings, RatingValueType.POSITIVE);
    }

    public static int negativeRatingsAsPercent(List<Rating> ratings) {
        return getRatingPercentage(ratings, RatingValueType.NEGATIVE);
    }

    public static int neutralRatingsAsPercent(List<Rating> ratings) {
        return getRatingPercentage(ratings, RatingValueType.NEUTRAL);
    }

    private static int convertFromMillis(TimeUnit timeUnit, long timeInMillis) {
        return (int) timeUnit.convert(timeInMillis, TimeUnit.MILLISECONDS);
    }

    private static int getRatingPercentage(List<Rating> ratings, RatingValueType ratingType) {
        int result = 0;

        if (ratings != null && ratings.size() > 0) {
            double sum = 0;

            for (Rating rating : ratings) {
                //Counts rating depending on what we're looking for
                if (ratingType == RatingValueType.NEGATIVE && rating.isNegativeRating()) {
                    sum++;
                } else if (ratingType == RatingValueType.NEUTRAL && rating.isNeutralRating()) {
                    sum++;
                } else if (ratingType == RatingValueType.POSITIVE && rating.isPositiveRating()) {
                    sum++;
                }
            }
            double percent = sum / (double) ratings.size() * 100;
            result = (int) Math.round(percent);
        }

        return result;
    }
}
