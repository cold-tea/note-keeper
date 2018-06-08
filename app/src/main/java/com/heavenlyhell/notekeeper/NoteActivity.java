package com.heavenlyhell.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import static com.heavenlyhell.notekeeper.NoteListActivity.NOTE_POSITION;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = "NoteActivity";
    public static final int NEXT_INDEX = 1;
    public static final int POSITION_NOT_SET = -1;

    private NoteInfo noteInfo;
    private Spinner spinnerCourses;
    private boolean isNewNote;
    private EditText etTitle;
    private EditText etDescription;
    private boolean isCancelling;
    private int newNoteIndex;
    private boolean isDisableSaving;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        isCancelling = false;

        setUpSpinner();
        readDisplayStateValues();
        if (isNewNote) {
            createNewNote();
        } else setUpFields();

    }

    private void createNewNote() {
        newNoteIndex = DataManager.getInstance().createNewNote();
        noteInfo = DataManager.getInstance().getNotes().get(newNoteIndex);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isCancelling) {
            if (isNewNote) {
                DataManager.getInstance().removeNote(newNoteIndex);
            }
        } else {
            if (!isDisableSaving)
                saveNote();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDisableSaving = false;
    }

    private void saveNote() {
        if (isNewNote && TextUtils.isEmpty(etTitle.getText().toString())) {
            DataManager.getInstance().removeNote(newNoteIndex);
            return;
        } else if (!isNewNote && TextUtils.isEmpty(etTitle.getText().toString())) {
            return;
        }
        noteInfo.setTitle(etTitle.getText().toString());
        noteInfo.setText(etDescription.getText().toString());
        noteInfo.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
    }

    private void readDisplayStateValues() {
        position = getIntent().getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = position == POSITION_NOT_SET;
        if (!isNewNote)
            noteInfo = DataManager.getInstance().getNotes().get(position);
    }

    private void setUpFields() {
        CourseInfo courseInfo = noteInfo.getCourse();
        int coursePosition = DataManager.getInstance().getCourses().indexOf(courseInfo);
        spinnerCourses.setSelection(coursePosition);
        etTitle.setText(noteInfo.getTitle());
        etDescription.setText(noteInfo.getText());
    }

    private void setUpSpinner() {
        spinnerCourses = findViewById(R.id.spinnerCourses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapter = new ArrayAdapter<>(NoteActivity.this,
                android.R.layout.simple_spinner_dropdown_item, courses);
        spinnerCourses.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        if (isNewNote)
            menu.getItem(NEXT_INDEX).setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isNewNote || position == DataManager.getInstance().getNotes().size() - 1)
            menu.getItem(NEXT_INDEX).setVisible(false);
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send_mail) {
            isDisableSaving = true;
            sendMail();
            return true;
        } else if (id == R.id.action_cancel) {
            isCancelling = true;
            finish();
            return true;
        } else if (id == R.id.action_next) {
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    private void moveNext() {
        saveNote();
        ++position;
        invalidateOptionsMenu();
        noteInfo = DataManager.getInstance().getNotes().get(position);
        setUpFields();
    }

    private void sendMail() {
        CourseInfo courseInfo = (CourseInfo) spinnerCourses.getSelectedItem();
        String textBody = "What I have learnt through this course is " +
            courseInfo.getTitle() + " and text is " + etDescription.getText().toString();
        Intent intentSendMail = new Intent(Intent.ACTION_SEND);
        intentSendMail.setType("message/rfc822");
        intentSendMail.putExtra(Intent.EXTRA_SUBJECT, etTitle.getText().toString());
        intentSendMail.putExtra(Intent.EXTRA_TEXT   , textBody);
        startActivity(intentSendMail);
    }
}
