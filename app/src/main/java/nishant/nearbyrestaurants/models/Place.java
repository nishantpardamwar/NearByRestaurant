package nishant.nearbyrestaurants.models;

import java.util.Comparator;

/**
 * Created by nishant pardamwar on 31/8/17.
 */

public class Place {
    public static final Comparator<Place> SORT_BY_DISTANCE = (o1, o2) -> {
        if (o1.getDistance() > o2.getDistance())
            return 1;
        else if (o1.getDistance() < o2.getDistance())
            return -1;
        else
            return 0;
    };
    public static final Comparator<Place> SORT_BY_RATING = (o1, o2) -> {
        Integer r1 = null, r2 = null;
        try {
            r1 = Integer.valueOf(o1.getRating());
        } catch (Exception e) {
        }
        try {
            r2 = Integer.valueOf(o2.getRating());
        } catch (Exception e) {
        }

        if (r1 != null && r2 != null) {
            if (r1 < r2) return 1;
            else if (r1 > r2) return -1;
            else return 0;
        } else if (r1 == null && r2 == null) return 0;
        else if (r1 == null) return 1;
        else return -1;
    };
    private String name;
    private String rating;
    private String vicinity;
    private String iconUrl;
    private double distance;

    private Place(Builder builder) {
        name = builder.name;
        rating = builder.rating;
        vicinity = builder.vicinity;
        iconUrl = builder.iconUrl;
        distance = builder.distance;
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getIconUrl() {
        return iconUrl;
    }


    public static final class Builder {
        private String name;
        private String rating;
        private String vicinity;
        private String iconUrl;
        private double distance;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder rating(String val) {
            rating = val;
            return this;
        }

        public Builder vicinity(String val) {
            vicinity = val;
            return this;
        }

        public Builder iconUrl(String val) {
            iconUrl = val;
            return this;
        }

        public Builder distance(double val) {
            distance = val;
            return this;
        }

        public Place build() {
            return new Place(this);
        }
    }
}
