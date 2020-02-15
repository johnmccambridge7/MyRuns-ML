package com.example.myruns;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

public class ImageDialog extends DialogFragment {

    // Different dialog IDs
    public static final int DIALOG_ID_ERROR = -1;
    public static final int DIALOG_ID_PHOTO_PICKER = 1;

    // For photo picker selection:
    public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;
    public static final int ID_PHOTO_PICKER_FROM_GALLERY = 1;

    private static final String DIALOG_ID_KEY = "dialog_id";

    public static ImageDialog newInstance(int dialog_id) {
        ImageDialog frag = new ImageDialog();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ID_KEY, dialog_id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int dialog_id = getArguments().getInt(DIALOG_ID_KEY);

        final Activity parent = getActivity();

        switch (dialog_id) {
            case DIALOG_ID_PHOTO_PICKER:
                AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                builder.setTitle(R.string.ui_profile_photo_picker_title);

                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ((EditProfileActivity) parent).dialogSelection(item);
                    }
                };

                builder.setItems(R.array.ui_profile_photo_picker_items, listener);
                return builder.create();
            default:
                return null;
        }
    }
}