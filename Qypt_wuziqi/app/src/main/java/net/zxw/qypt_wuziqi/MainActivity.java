package net.zxw.qypt_wuziqi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private WuziqiPanel wuziqiPanel;
    private Button btn_orgin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wuziqiPanel = (WuziqiPanel) findViewById(R.id.id_wuziqi);
        btn_orgin = (Button) findViewById(R.id.btn_orgin);

        btn_orgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wuziqiPanel.start();
            }
        });
    }
}
