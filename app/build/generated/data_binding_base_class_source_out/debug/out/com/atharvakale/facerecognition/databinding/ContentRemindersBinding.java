// Generated by view binder compiler. Do not edit!
package com.atharvakale.facerecognition.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.atharvakale.facerecognition.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ContentRemindersBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final ImageView imageViewMenu;

  @NonNull
  public final LinearLayout raEmptyStateLinearLayout;

  @NonNull
  public final TextView raEmptyStateTextView;

  @NonNull
  public final TextView raEmptyStateTitleTextView;

  @NonNull
  public final RecyclerView remindersRecyclerView;

  @NonNull
  public final TextView textViewMain;

  private ContentRemindersBinding(@NonNull LinearLayout rootView,
      @NonNull ConstraintLayout constraintLayout, @NonNull ImageView imageViewMenu,
      @NonNull LinearLayout raEmptyStateLinearLayout, @NonNull TextView raEmptyStateTextView,
      @NonNull TextView raEmptyStateTitleTextView, @NonNull RecyclerView remindersRecyclerView,
      @NonNull TextView textViewMain) {
    this.rootView = rootView;
    this.constraintLayout = constraintLayout;
    this.imageViewMenu = imageViewMenu;
    this.raEmptyStateLinearLayout = raEmptyStateLinearLayout;
    this.raEmptyStateTextView = raEmptyStateTextView;
    this.raEmptyStateTitleTextView = raEmptyStateTitleTextView;
    this.remindersRecyclerView = remindersRecyclerView;
    this.textViewMain = textViewMain;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ContentRemindersBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ContentRemindersBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.content_reminders, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ContentRemindersBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.imageView_menu;
      ImageView imageViewMenu = ViewBindings.findChildViewById(rootView, id);
      if (imageViewMenu == null) {
        break missingId;
      }

      id = R.id.ra_empty_state_linear_layout;
      LinearLayout raEmptyStateLinearLayout = ViewBindings.findChildViewById(rootView, id);
      if (raEmptyStateLinearLayout == null) {
        break missingId;
      }

      id = R.id.ra_empty_state_text_view;
      TextView raEmptyStateTextView = ViewBindings.findChildViewById(rootView, id);
      if (raEmptyStateTextView == null) {
        break missingId;
      }

      id = R.id.ra_empty_state_title_text_view;
      TextView raEmptyStateTitleTextView = ViewBindings.findChildViewById(rootView, id);
      if (raEmptyStateTitleTextView == null) {
        break missingId;
      }

      id = R.id.reminders_recycler_view;
      RecyclerView remindersRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (remindersRecyclerView == null) {
        break missingId;
      }

      id = R.id.textView_main;
      TextView textViewMain = ViewBindings.findChildViewById(rootView, id);
      if (textViewMain == null) {
        break missingId;
      }

      return new ContentRemindersBinding((LinearLayout) rootView, constraintLayout, imageViewMenu,
          raEmptyStateLinearLayout, raEmptyStateTextView, raEmptyStateTitleTextView,
          remindersRecyclerView, textViewMain);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}