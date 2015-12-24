package com.example.uiwidgettest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	private Button button;
	private EditText editText;
	private ImageView imageView;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button_1);
		editText = (EditText) findViewById(R.id.edit_text);
		imageView = (ImageView) findViewById(R.id.image_view);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		button.setOnClickListener(this);
//		button.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				String inputText = editText.getText().toString();
//				Toast.makeText(MainActivity.this, inputText, Toast.LENGTH_SHORT).show();
//			}
//		});
	}
	
	@Override
	public void onClick (View v) {
		switch (v.getId()) {
		case R.id.button_1:
			
			//显示提示信息
//			String inputText = editText.getText().toString();
//			Toast.makeText(MainActivity.this, inputText, Toast.LENGTH_SHORT).show();
			
			// 更改图片
//			imageView.setImageResource(R.drawable.ic_launcher);  
			
			//进度条
			int progress = progressBar.getProgress();
			progress = progress + 10;
			progressBar.setProgress(progress);
//			if (progressBar.getVisibility() == View.GONE){
//				progressBar.setVisibility(View.VISIBLE);
//			}else {
//				progressBar.setVisibility(View.GONE);
//			}
			
			//警告信息对话框
			AlertDialog.Builder dialog = new AlertDialog.Builder (MainActivity.this);
			dialog.setTitle("This is a dialog");
			dialog.setMessage("Something important");
			dialog.setCancelable(false);//设置为true，点击(back键)屏幕其它地方会消失
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			dialog.show();
			
			//进度对话框
			ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setTitle("This is a progressDialog");
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(true);//设置为true，点击(back键)屏幕其它地方会消失,false不会退出，要加dismiss方法控制
			progressDialog.show();
			break;
			
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
