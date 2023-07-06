package com.atharvakale.facerecognition;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReminderFSD extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "ABC ReminderFSD";
    protected TextView nRTimeTvw;
    protected EditText nRTitleTIEditT;
    protected ImageButton actionBarCloseButton;
    protected TextView titleTvw;
    private int setHour = 0;
    private int setMinute = 0;
    protected Calendar setCalendar;

    protected TimePickerDialog nRTimePDialog;

    protected ReminderInterface reminderInterface;

    protected int reminderId;
    protected String reminderTitle;
    protected String reminderDOF;
    protected String reminderTOF;
    protected long reminderTIM;
    protected int reminderPosition;
    protected boolean isEditMode;

    public static ReminderFSD newInstance(int reminderId, String reminderTitle, String reminderDOF, String reminderTOF, long reminderTIM, int reminderPosition, boolean isEditMode) {
        ReminderFSD newReminderFSD = new ReminderFSD();
        Bundle params = new Bundle();

        params.putInt("reminderId", reminderId);
        params.putString("reminderTitle", reminderTitle);
        params.putString("reminderDOF", reminderDOF);
        params.putString("reminderTOF", reminderTOF);
        params.putLong("reminderTIM", reminderTIM);
        params.putInt("reminderPosition", reminderPosition);
        params.putBoolean("isEditMode", isEditMode);

        newReminderFSD.setArguments(params);
        return newReminderFSD;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");

        reminderId = getArguments().getInt("reminderId");
        reminderTitle = getArguments().getString("reminderTitle");
        reminderDOF = getArguments().getString("reminderDOF");
        reminderTOF = getArguments().getString("reminderTOF");
        reminderTIM = getArguments().getLong("reminderTIM");
        reminderPosition = getArguments().getInt("reminderPosition");
        isEditMode = getArguments().getBoolean("isEditMode");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_rfsd, container, false);

        actionBarCloseButton = rootView.findViewById(R.id.action_bar_close_button);
        actionBarCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(rootView);
                dismiss();
            }
        });

//        nRDL = rootView.findViewById(R.id.new_reminder_date_layout);
//        nRDateTvw =rootView.findViewById(R.id.nrdl_date_value_tvw);
//        nRTL = rootView.findViewById(R.id.new_reminder_time_layout);
        nRTimeTvw = rootView.findViewById(R.id.nrtl_time_value_tvw);

        titleTvw = rootView.findViewById(R.id.action_bar_title_tvw);

        if (isEditMode) {
            titleTvw.setText("Edit com.atharvakale.facerecognition.Reminder");
        } else {
            titleTvw.setText("New com.atharvakale.facerecognition.Reminder");
        }

        reminderInterface = (ReminderInterface) getActivity();

        String nowDTS = getDTS(getNowTIM());
//        String nowDS = nowDTS.substring(7,17);
        String nowTS = nowDTS.substring(0, 5);

//        nRDateTvw.setText(nowDS);
        nRTimeTvw.setText(nowTS);

        setCalendar = Calendar.getInstance();

//        nRTitleTILayout = rootView.findViewById(R.id.nr_title_til);
        nRTitleTIEditT = rootView.findViewById(R.id.nr_title_tiet);
//        nRTitleTIEditT.setEnabled(false);
        /* TODO: CLICK LISTENER
        nRTitleTIEditT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                ArrayList<com.atharvakale.facerecognition.Reminder> reminderArrayList =
                        Stash.getArrayList(Constants.STOCKS_LIST, com.atharvakale.facerecognition.Reminder.class);
                ArrayList<String> list = new ArrayList<>();
                for (com.atharvakale.facerecognition.Reminder reminder : reminderArrayList) {
                    list.add(reminder.getReminderTitle());
                }

                String[] items = new String[reminderArrayList.size()];
                items = list.toArray(items);

                String[] finalItems = items;
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        nRTitleTIEditT.setText(finalItems[position]);
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });*/
        Button actionBarSButton = rootView.findViewById(R.id.action_bar_save_button);
        actionBarSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nReminderTitle = nRTitleTIEditT.getText().toString();
                long nReminderTIM = setCalendar.getTimeInMillis();

                String nReminderDTS = getDTS(nReminderTIM);

                if (!dialogTextIsSpaces(nReminderTitle)) {

                    if (!isEditMode) {

                        reminderInterface.addReminder(nReminderTitle, nReminderDTS, nReminderTIM, "");
                    } else {
                        reminderInterface.updateReminder(nReminderTitle, nReminderDTS, nReminderTIM, reminderId, reminderPosition, "");
                    }

                    hideKeyboard(rootView);
                    dismiss();
                } else {
                    nRTitleTIEditT.setError(getResources().getString(R.string.teit_error_text));

                   /* nRTitleTIEditT.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            String editTextText = nRTitleTIEditT.getText().toString();
                            boolean textIsSpaces = dialogTextIsSpaces(editTextText);

                            if(textIsSpaces){
                                nRTitleTIEditT.setError(getResources().getString(R.string.teit_error_text));
                            }
//                            else {
//                                nRTitleTIEditT.setErrorEnabled(false);
//                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });*/
                }
            }
        });

        initializeDateAndTimePickerDialogs();

//        nRDateTvw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                nRDatePDialog.show();
//            }
//        });

        nRTimeTvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int sec = mcurrentTime.get(Calendar.SECOND);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        setCalendar.set(Calendar.MINUTE, selectedMinute);
                        Log.d("setNextReminderAlarm", "beforeTime: " + setCalendar.getTimeInMillis());
                        setCalendar.set(Calendar.SECOND, 0);//TODO: ADDED
                        Log.d("setNextReminderAlarm", "AfterTime: " + setCalendar.getTimeInMillis());

                        String dateTimeString = getDTS(setCalendar.getTimeInMillis());
                        nRTimeTvw.setText(dateTimeString);
//                        nRTimeTvw.setText(dateTimeString.substring(0, 5));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
//                nRTimePDialog.show();
            }
        });

        if (isEditMode) {
            setEditMode();
        }

//        reminderInterface.hideActionBar();

        return rootView;
    }

    protected boolean dialogTextIsSpaces(String dialogText) {
        int i = 0;
        int noOfSpaces = 0;
        int dialogTextLength = dialogText.length();

        while (i >= 0 && i <= (dialogTextLength - 1)) {
            String dialogTextCharacter = String.valueOf(dialogText.charAt(i));

            if (dialogTextCharacter.equals(" ")) {
                noOfSpaces = noOfSpaces + 1;
            }

            ++i;
        }

        if (dialogTextLength == noOfSpaces) {
            return true;
        } else {
            return false;
        }

    }

    private void setEditMode() {
        nRTitleTIEditT.setText(String.valueOf(reminderTitle));
//        nRDateTvw.setText(reminderDOF);
        nRTimeTvw.setText(reminderTOF);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        reminderInterface.showActionBar();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog reminderFSD = super.onCreateDialog(savedInstanceState);
        reminderFSD.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return reminderFSD;
    }

    private long getNowTIM() {
        Date nowDate = new Date();
        long nowTIM = nowDate.getTime();
        return nowTIM;
    }

    private String getDTS(long timeInMills) {
        Date date = new Date(timeInMills);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String dateTimeString = dateFormat.format(date);
        return dateTimeString;
    }

    private void initializeDateAndTimePickerDialogs() {
        if (isEditMode) {
            setCalendar.setTimeInMillis(reminderTIM);
        } else {
            setCalendar.setTimeInMillis(getNowTIM());
        }

        int nCYear = setCalendar.get(Calendar.YEAR);
        int nCMonth = setCalendar.get(Calendar.MONTH);
        int nCDay = setCalendar.get(Calendar.DAY_OF_MONTH);
        int nCHour = setCalendar.get(Calendar.HOUR_OF_DAY);
        int nCMinute = setCalendar.get(Calendar.MINUTE);

//        setYear = nCYear;
//        setMonth = nCMonth;
//        setDay = nCDay;
        setHour = nCHour;
        setMinute = nCMinute;

//        nRDatePDialog=new DatePickerDialog(getContext(), this, nCYear, nCMonth, nCDay);
        nRTimePDialog = new TimePickerDialog(getContext(), this, nCHour, nCMinute, true);

//        nRDatePDialog.setCanceledOnTouchOutside(true);
        nRTimePDialog.setCanceledOnTouchOutside(true);

//        nRDatePDialog.setTitle("Set com.atharvakale.facerecognition.Reminder Date");
        nRTimePDialog.setTitle("Set com.atharvakale.facerecognition.Reminder Time");
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    private void updateNRDTvw() {
////        setCalendar.set(setYear,setMonth,setDay,setHour,setMinute);
//        setCalendar.set(setHour,setMinute);
//        String dateTimeString = getDTS(setCalendar.getTimeInMillis());
//        nRDateTvw.setText(dateTimeString.substring(7,17));
//    }

    private void updateNRTTvw() {
//        setCalendar.set(setYear,setMonth,setDay,setHour,setMinute);
        setCalendar.set(setHour, setMinute);
        String dateTimeString = getDTS(setCalendar.getTimeInMillis());
        nRTimeTvw.setText(dateTimeString.substring(0, 5));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//        setYear =i;
//        setMonth = i1;
//        setDay =i2;
//        updateNRDTvw();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        setHour = i;
        setMinute = i1;
        updateNRTTvw();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
