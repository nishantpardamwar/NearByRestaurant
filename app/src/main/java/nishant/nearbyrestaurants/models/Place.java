package nishant.nearbyrestaurants.models;

/**
 * Created by nishant pardamwar on 31/8/17.
 */

public class Place {
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
