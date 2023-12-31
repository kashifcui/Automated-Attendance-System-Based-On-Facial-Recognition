// Generated by view binder compiler. Do not edit!
package com.atharvakale.facerecognition.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.atharvakale.facerecognition.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginScreenBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton btnlogin;

  @NonNull
  public final TextView createNewAccount;

  @NonNull
  public final EditText emailLoginEdit;

  @NonNull
  public final ImageView imageView2;

  @NonNull
  public final EditText passwordLoginEdit;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView4;

  private ActivityLoginScreenBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton btnlogin, @NonNull TextView createNewAccount,
      @NonNull EditText emailLoginEdit, @NonNull ImageView imageView2,
      @NonNull EditText passwordLoginEdit, @NonNull TextView textView3,
      @NonNull TextView textView4) {
    this.rootView = rootView;
    this.btnlogin = btnlogin;
    this.createNewAccount = createNewAccount;
    this.emailLoginEdit = emailLoginEdit;
    this.imageView2 = imageView2;
    this.passwordLoginEdit = passwordLoginEdit;
    this.textView3 = textView3;
    this.textView4 = textView4;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginScreenBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginScreenBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login_screen, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginScreenBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnlogin;
      AppCompatButton btnlogin = ViewBindings.findChildViewById(rootView, id);
      if (btnlogin == null) {
        break missingId;
      }

      id = R.id.createNewAccount;
      TextView createNewAccount = ViewBindings.findChildViewById(rootView, id);
      if (createNewAccount == null) {
        break missingId;
      }

      id = R.id.email_login_edit;
      EditText emailLoginEdit = ViewBindings.findChildViewById(rootView, id);
      if (emailLoginEdit == null) {
        break missingId;
      }

      id = R.id.imageView2;
      ImageView imageView2 = ViewBindings.findChildViewById(rootView, id);
      if (imageView2 == null) {
        break missingId;
      }

      id = R.id.password_login_edit;
      EditText passwordLoginEdit = ViewBindings.findChildViewById(rootView, id);
      if (passwordLoginEdit == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.textView4;
      TextView textView4 = ViewBindings.findChildViewById(rootView, id);
      if (textView4 == null) {
        break missingId;
      }

      return new ActivityLoginScreenBinding((ConstraintLayout) rootView, btnlogin, createNewAccount,
          emailLoginEdit, imageView2, passwordLoginEdit, textView3, textView4);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
