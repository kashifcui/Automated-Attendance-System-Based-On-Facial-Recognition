// Generated by view binder compiler. Do not edit!
package com.atharvakale.facerecognition.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.atharvakale.facerecognition.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCoursesBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton addnewCourse;

  @NonNull
  public final ConstraintLayout constraintLayout2;

  @NonNull
  public final RecyclerView courseRecyclerView;

  private ActivityCoursesBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton addnewCourse, @NonNull ConstraintLayout constraintLayout2,
      @NonNull RecyclerView courseRecyclerView) {
    this.rootView = rootView;
    this.addnewCourse = addnewCourse;
    this.constraintLayout2 = constraintLayout2;
    this.courseRecyclerView = courseRecyclerView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCoursesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCoursesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_courses, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCoursesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addnewCourse;
      FloatingActionButton addnewCourse = ViewBindings.findChildViewById(rootView, id);
      if (addnewCourse == null) {
        break missingId;
      }

      id = R.id.constraintLayout2;
      ConstraintLayout constraintLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout2 == null) {
        break missingId;
      }

      id = R.id.courseRecyclerView;
      RecyclerView courseRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (courseRecyclerView == null) {
        break missingId;
      }

      return new ActivityCoursesBinding((ConstraintLayout) rootView, addnewCourse,
          constraintLayout2, courseRecyclerView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
