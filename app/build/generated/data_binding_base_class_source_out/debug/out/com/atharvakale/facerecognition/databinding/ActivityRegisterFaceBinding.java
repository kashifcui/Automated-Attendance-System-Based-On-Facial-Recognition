// Generated by view binder compiler. Do not edit!
package com.atharvakale.facerecognition.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.atharvakale.facerecognition.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterFaceBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton btnAddFace;

  @NonNull
  public final AppCompatButton cameraSwitch;

  @NonNull
  public final FrameLayout container;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final PreviewView previewView;

  @NonNull
  public final RecyclerView registerFacesRecycler;

  private ActivityRegisterFaceBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton btnAddFace, @NonNull AppCompatButton cameraSwitch,
      @NonNull FrameLayout container, @NonNull ImageView imageView,
      @NonNull PreviewView previewView, @NonNull RecyclerView registerFacesRecycler) {
    this.rootView = rootView;
    this.btnAddFace = btnAddFace;
    this.cameraSwitch = cameraSwitch;
    this.container = container;
    this.imageView = imageView;
    this.previewView = previewView;
    this.registerFacesRecycler = registerFacesRecycler;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterFaceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterFaceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register_face, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterFaceBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAddFace;
      AppCompatButton btnAddFace = ViewBindings.findChildViewById(rootView, id);
      if (btnAddFace == null) {
        break missingId;
      }

      id = R.id.cameraSwitch;
      AppCompatButton cameraSwitch = ViewBindings.findChildViewById(rootView, id);
      if (cameraSwitch == null) {
        break missingId;
      }

      id = R.id.container;
      FrameLayout container = ViewBindings.findChildViewById(rootView, id);
      if (container == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.previewView;
      PreviewView previewView = ViewBindings.findChildViewById(rootView, id);
      if (previewView == null) {
        break missingId;
      }

      id = R.id.register_faces_recycler;
      RecyclerView registerFacesRecycler = ViewBindings.findChildViewById(rootView, id);
      if (registerFacesRecycler == null) {
        break missingId;
      }

      return new ActivityRegisterFaceBinding((ConstraintLayout) rootView, btnAddFace, cameraSwitch,
          container, imageView, previewView, registerFacesRecycler);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
