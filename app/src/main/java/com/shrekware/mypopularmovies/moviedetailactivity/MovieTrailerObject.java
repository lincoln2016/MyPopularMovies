/*
 * PROJECT LICENSE
 * This project was submitted by Henry Ayers as part of the Nanodegree At Udacity.
 * As part of Udacity Honor code, your submissions must be your own work,
 * hence submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account. Me, the author of the project,
 * allow you to check the code as a reference,
 * but if you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2018 Henry Ayers
 *
 * Besides the above notice, the following license applies and
 * this license notice must be included in all works derived from this project.
 * MIT License Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.shrekware.mypopularmovies.moviedetailactivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*
* the movie trailer object returned from theMovieDB.org call for
*   https://api.themoviedb.org/3/movie/{id}/videos?
*   used to populate the movie trailers recyclerView
*/
    public class MovieTrailerObject {
         //obscure id, not sure what its for
        @SerializedName("id")
        @Expose
        private String id;
        // 2 digit language code/ default is en (english)
        @SerializedName("iso_639_1")
        @Expose
        private String iso6391;
        // 2 digit country code / default is US
        @SerializedName("iso_3166_1")
        @Expose
        private String iso31661;
        // key is the id of the trailer to fetch
        @SerializedName("key")
        @Expose
        private String key;
        // name of movie trailer
        @SerializedName("name")
        @Expose
        private String name;
        // site that hosts the trailer
        @SerializedName("site")
        @Expose
        private String site;
        // the density of the trailer
        // 4 values allowed 360/480/720/1080
        @SerializedName("size")
        @Expose
        private Integer size;
        // type of trailer
        //4 values allowed
        // Trailer/Teaser/Clip/Featurette
        @SerializedName("type")
        @Expose
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public String getIso31661() {
            return iso31661;
        }

        public void setIso31661(String iso31661) {
            this.iso31661 = iso31661;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }























