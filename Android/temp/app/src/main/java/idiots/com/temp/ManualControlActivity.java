package idiots.com.temp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ManualControlActivity extends AppCompatActivity {

    private EditText editText;
    private Button sendBtn;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);

        editText = findViewById(R.id.manual_edit_text);
        sendBtn = findViewById(R.id.manual_send_btn);
        listView = findViewById(R.id.manual_list_view);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                //write(msg);//TODO
                adapter.add(msg);
                editText.setText("");
            }
        });

        listView.setAdapter(adapter);

    }
}
