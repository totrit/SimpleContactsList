package com.example.alvin.simplecontactlist.model;

/**
 * Created by maruilin on 15/9/7.
 */
public class PerContactInfo {
    public final int id;
    public final String name;
    public final String username;
    public final String email;
    public final Address address;
    public final String phone;
    public final String website;
    public final Company company;

    public PerContactInfo(int id,
                          String name,
                          String username,
                          String email,
                          Address address,
                          String phone,
                          String website,
                          Company company) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.company = company;
    }

    public String getAddress() {
        return address.toString();
    }

    static class Address {
        public final String street;
        public final String suite;
        public final String city;
        public final String zipcode;
        public final Geo geo;

        public Address(String street,
                       String suite,
                       String city,
                       String zipcode,
                       Geo geo) {
            this.street = street;
            this.suite = suite;
            this.city = city;
            this.zipcode = zipcode;
            this.geo = geo;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append(suite).append(", ").append(street).append(", ").append(city).append(", ").append(zipcode);
            return sb.toString();
        }

        static class Geo {
            public final String lat;
            public final String lng;

            public Geo(String lat,
                       String lng) {
                this.lat = lat;
                this.lng = lng;
            }
        }

    }

    public static class Company {
        public final String name;
        public final String catchPhrase;
        public final String bs;

        public Company(String name,
                       String catchPhrase,
                       String bs) {
            this.name = name;
            this.catchPhrase = catchPhrase;
            this.bs = bs;
        }
    }
}
