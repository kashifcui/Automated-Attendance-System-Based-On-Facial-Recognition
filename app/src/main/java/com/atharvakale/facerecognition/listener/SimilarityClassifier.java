/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.atharvakale.facerecognition.listener;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public interface SimilarityClassifier {


  class Recognition implements Serializable, Parcelable {
    public String id;
    private String title;
    private String distance;
    private String extra;
    private String rollno;
    private String className;

    public Recognition() {
    }

    protected Recognition(Parcel in) {
      id = in.readString();
      title = in.readString();
      distance = in.readString();
      extra = in.readString();
      rollno = in.readString();
      className = in.readString();
    }

    public static final Creator<Recognition> CREATOR = new Creator<Recognition>() {
      @Override
      public Recognition createFromParcel(Parcel in) {
        return new Recognition(in);
      }

      @Override
      public Recognition[] newArray(int size) {
        return new Recognition[size];
      }
    };

    public String getDistance() {
      return distance;
    }

    public void setDistance(String distance) {
      this.distance = distance;
    }

    public String getRollNo() {
      return rollno;
    }

    public void setRollNo(String rollno) {
      this.rollno = rollno;
    }

    public String getClassName() {
      return className;
    }

    public void setClassName(String className) {
      this.className = className;
    }

    public Recognition(String id, String title, String distance, String extra, String rollno, String className) {
      this.id = id;
      this.title = title;
      this.distance = distance;
      this.extra = extra;
      this.rollno = rollno;
      this.className = className;
    }

    public String getExtra() {
      return extra;
    }

    public void setExtra(String extra) {
      this.extra = extra;
    }

    public Recognition(String id, String title, String distance, String extra) {
      this.id = id;
      this.title = title;
      this.distance = distance;
      this.extra = extra;
    }

    public void setId(String id) {
      this.id = id;
    }

    public void setTitle(String title) {
      this.title = title;
    }


    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }


    @Override
    public String toString() {
      String resultString = "";
      if (id != null) {
        resultString += "[" + id + "] ";
      }

      if (title != null) {
        resultString += title + " ";
      }

      if (distance != null) {
        resultString += String.format("(%.1f%%) ", Float.valueOf(distance) * 100.0f);
      }

      return resultString.trim();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
      dest.writeString(id);
      dest.writeString(title);
      dest.writeString(distance);
      dest.writeString(extra);
      dest.writeString(rollno);
      dest.writeString(className);
    }
  }
}
