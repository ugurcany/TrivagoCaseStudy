package com.ugurcanyildirim.trivagocasestudy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ugurc on 12.08.2016.
 */
public class Movie {

    @SerializedName("title")
    public String title;

    @SerializedName("year")
    public String year;

    @SerializedName("overview")
    public String overview;

    @SerializedName("images")
    public Images images;

    public class Images {

        @SerializedName("poster")
        public Poster poster;

    }

    public class Poster {

        @SerializedName("thumb")
        public String thumb;

    }

}
