package com.hyrt.cei.ui.personcenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.common.LoginActivity;
import com.hyrt.cei.ui.common.RegistActivity;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;

public class ChangePassword extends Fragment {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private EditText oldpassword, password1, password2, et_email, et_card_kind,
			et_card_num, et_phone_num;
	private String strid, stroldpassword, strpassword, str_email,
			str_card_kind, str_card_num, str_phone_num;
	private int code;
	private final String ID_TYPES = "身份证学生证工作证士兵证军官证护照";
	private final String[] types = { "身份证", "学生证", "工作证", "士兵证", "军官证", "护照" };
	private RadioOnClick OnClick = new RadioOnClick(1);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.changepassword);
		View view = inflater.inflate(R.layout.changepassword,container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		oldpassword = (EditText)view. findViewById(R.id.changepassword_passwordold);
		password1 = (EditText)view. findViewById(R.id.changepassword_password1);
		password2 = (EditText)view. findViewById(R.id.changepassword_password2);
		et_email = (EditText)view. findViewById(R.id.et_change_email);
		et_card_kind = (EditText)view. findViewById(R.id.et_card_kind);
		et_card_num = (EditText) view.findViewById(R.id.et_card_num);
		et_phone_num = (EditText) view.findViewById(R.id.et_phone_num);
		et_card_kind.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				AlertDialog ad = new AlertDialog.Builder(ChangePassword.this.getActivity())
						.setTitle("选择证件类型")
						.setSingleChoiceItems(types, OnClick.getIndex(),
								OnClick).create();
				ad.show();

			}
		});
		et_card_kind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog ad = new AlertDialog.Builder(ChangePassword.this.getActivity())
						.setTitle("选择证件类型")
						.setSingleChoiceItems(types, OnClick.getIndex(),
								OnClick).create();
				ad.show();

			}
		});

		refreshListData(view);
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		switch (event.getKeyCode()) {
//		case KeyEvent.KEYCODE_DPAD_CENTER:
//		case KeyEvent.KEYCODE_ENTER:
//			return true;
//		}
//		return super.dispatchKeyEvent(event);
//	}

	private void refreshListData(View view) {
		view.findViewById(R.id.changepassword_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (!password1.getText().toString().trim()
								.equals(password2.getText().toString().trim())) {
							Toast.makeText(ChangePassword.this.getActivity(),
									"两次输入的新密码必须一致!", Toast.LENGTH_SHORT).show();
							return;
						} else if (oldpassword.getText().toString().trim()
								.equals("")
						/*
						 * || password1.getText().toString().trim() .equals("")
						 * || password2.getText().toString().trim() .equals("")
						 */) {
							Toast.makeText(ChangePassword.this.getActivity(), "原密码不能为空!",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (!et_email.getText().toString().trim()
								.equals("")
								&& !et_email.getText().toString().trim()
										.contains("@")) {
							// 邮箱格式不对
							Toast.makeText(ChangePassword.this.getActivity(), "邮箱格式不对",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (!et_card_kind.getText().toString().trim()
								.equals("")
								&& !ID_TYPES.contains(et_card_kind.getText()
										.toString().trim())) {
							// 判断身份证类型
							Toast.makeText(ChangePassword.this.getActivity(), "身份证类型不对",
									Toast.LENGTH_SHORT).show();
							return;
						}

						strid = ((CeiApplication) getActivity().getApplication()).columnEntry
								.getUserId();
						stroldpassword = oldpassword.getText().toString()
								.trim();
						strpassword = password1.getText().toString().trim();
						str_email = et_email.getText().toString().trim();
						str_card_kind = et_card_kind.getText().toString()
								.trim();
						str_card_num = et_card_num.getText().toString().trim();
						str_phone_num = et_phone_num.getText().toString()
								.trim();
						executorService.submit(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								String rs = "";
								if (password1.getText().toString().trim()
										.equals("")
										&& password2.getText().toString()
												.trim().equals("")) {
									strpassword = stroldpassword;
								}
								rs = Service.updatePassWord(strid,
										stroldpassword, strpassword, str_email,
										str_card_kind, str_card_num,
										str_phone_num);
								code = Integer.parseInt(XmlUtil
										.parseReturnCode(rs).equals("") ? "0"
										: XmlUtil.parseReturnCode(rs));
								Message msg = newsHandler.obtainMessage();
								newsHandler.sendMessage(msg);
							}
						});
					}
				});
	}

	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void onClick(DialogInterface dialog, int whichButton) {
			setIndex(whichButton);
			et_card_kind.setText(types[whichButton]);
			dialog.dismiss();
		}
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (code == 1) {
				Toast.makeText(ChangePassword.this.getActivity(), "修改成功!", Toast.LENGTH_SHORT)
						.show();
			} else if (code == -2) {
				Toast.makeText(ChangePassword.this.getActivity(), "原密码错误!",
						Toast.LENGTH_SHORT).show();
			} else if (code == 0) {
				Toast.makeText(ChangePassword.this.getActivity(), "系统错误!", Toast.LENGTH_SHORT)
						.show();
			} else if (code == -1) {
				Toast.makeText(ChangePassword.this.getActivity(), "网络错误!", Toast.LENGTH_SHORT)
						.show();
			} else if (code == 2) {
				Toast.makeText(ChangePassword.this.getActivity(), "修改的邮箱名重复，请换一个吧!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
}
