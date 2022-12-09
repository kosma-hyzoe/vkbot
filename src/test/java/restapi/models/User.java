package restapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class User {
    @JsonProperty
    @Getter
    private int id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String username;
    @JsonProperty
    private String email;
    @JsonProperty
    private Address address;
    @JsonProperty
    private String phone;
    @JsonProperty
    private String website;
    @JsonProperty
    private Company company;

    private static class Address {
        @JsonProperty
        private String street;
        @JsonProperty
        private String suite;
        @JsonProperty
        private String city;
        @JsonProperty
        private String zipcode;
        @JsonProperty
        private Geo geo;

        private static class Geo {
            @JsonProperty
            private String lat;
            @JsonProperty
            private String lng;
        }
    }

    private static class Company {
        @JsonProperty
        private String name;
        @JsonProperty
        private String catchPhrase;
        @JsonProperty
        private String bs;
    }
}
