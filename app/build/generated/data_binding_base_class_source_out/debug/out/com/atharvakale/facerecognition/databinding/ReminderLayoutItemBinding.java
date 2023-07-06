// Generated by view binder compiler. Do not edit!
package com.atharvakale.facerecognition.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.atharvakale.facerecognition.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ReminderLayoutItemBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout createActivity;

  @NonNull
  public final ImageView deleteReminder;

  @NonNull
  public final ImageView editReminder;

  @NonNull
  public final TextView reminderTitleTvw;

  @NonNull
  public final TextView reminderTofTvw;

  private ReminderLayoutItemBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout createActivity, @NonNull ImageView deleteReminder,
      @NonNull ImageView editReminder, @NonNull TextView reminderTitleTvw,
      @NonNull TextView reminderTofTvw) {
    this.rootView = rootView;
    this.createActivity = createActivity;
    this.deleteReminder = deleteReminder;
    this.editReminder = editReminder;
    this.reminderTitleTvw = reminderTitleTvw;
    this.reminderTofTvw = reminderTofTvw;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ReminderLayoutItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ReminderLayoutItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.reminder_layout_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ReminderLayoutItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.create_activity;
      ConstraintLayout createActivity = ViewBindings.findChildViewById(rootView, id);
      if (createActivity == null) {
        break missingId;
      }

      id = R.id.delete_reminder;
      ImageView deleteReminder = ViewBindings.findChildViewById(rootView, id);
      if (deleteReminder == null) {
        break missingId;
      }

      id = R.id.edit_reminder;
      ImageView editReminder = ViewBindings.findChildViewById(rootView, id);
      if (editReminder == null) {
        break missingId;
      }

      id = R.id.reminder_title_tvw;
      TextView reminderTitleTvw = ViewBindings.findChildViewById(rootView, id);
      if (reminderTitleTvw == null) {
        break missingId;
      }

      id = R.id.reminder_tof_tvw;
      TextView reminderTofTvw = ViewBindings.findChildViewById(rootView, id);
      if (reminderTofTvw == null) {
        break missingId;
      }

      return new ReminderLayoutItemBinding((ConstraintLayout) rootView, createActivity,
          deleteReminder, editReminder, reminderTitleTvw, reminderTofTvw);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}