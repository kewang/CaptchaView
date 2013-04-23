package tw.kewang.ui.captcha;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
	private CaptchaView captcha;
	private EditText edtCheck;
	private Button btnRefresh;
	private Button btnCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		findView();
		setView();
		setListener();
	}

	private void findView() {
		captcha = (CaptchaView) findViewById(R.id.captcha);
		edtCheck = (EditText) findViewById(R.id.edit_check);
		btnRefresh = (Button) findViewById(R.id.button_refresh);
		btnCheck = (Button) findViewById(R.id.button_check);
	}

	private void setView() {
	}

	private void setListener() {
		btnRefresh.setOnClickListener(this);
		btnCheck.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_refresh:
			captcha.refresh();

			break;
		case R.id.button_check:
			if (captcha.getCaptcha().equals(edtCheck.getText().toString())) {
				Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
			}

			break;
		}
	}
}